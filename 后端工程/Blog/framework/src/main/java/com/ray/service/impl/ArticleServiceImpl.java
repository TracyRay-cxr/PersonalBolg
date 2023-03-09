package com.ray.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.constants.SystemConstants;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.ArticleDto;
import com.ray.domain.entity.Article;
import com.ray.domain.entity.ArticleTag;
import com.ray.domain.entity.Category;
import com.ray.domain.vo.*;
import com.ray.mapper.ArticleMapper;
import com.ray.service.ArticleService;
import com.ray.service.ArticleTagService;
import com.ray.service.CategoryService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.RedisCache;
import org.apache.poi.hssf.record.DVALRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Lazy
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装到ResponseResult
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        //1.必须正式文章，草稿不行
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //2.按照浏览量进行降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //3.最多十条数据，采用分页
        Page<Article> page=new Page<>(1,10);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        articles.stream()
                .map(article -> {
                    //从redis中获取viewCount
                    Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT_KEY, article.getId().toString());
                    //防止redis挂掉
                    if (Objects.nonNull(viewCount)){
                        article.setViewCount(viewCount.longValue());
                    }
                    return article;
                })
                .collect(Collectors.toList());
        //4.封装返回数据,bean拷贝
//        List<HotArticleVo> articleVos=new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo=new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            articleVos.add(vo);
//        }
        //优化封装后
        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList_admin(Integer pageNum, Integer pageSize,String title,String summary) {
        //查询条件，模糊查询
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.like(StringUtils.hasText(title),Article::getTitle,title);
            lambdaQueryWrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        //TODO 分页查询所有文章
        Page<Article> page = new Page(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<Article> articleList = page.getRecords();
        //封装数据，返回结果
        List<ArticleListVo_admin> articleListVo_admins = BeanCopyUtils.copyBeanList(articleList, ArticleListVo_admin.class);
        PageVo pageVo = new PageVo(articleListVo_admins,page.getTotal());
        return ResponseResult.okResult(pageVo);

    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //1.categoryId判断是否为空,不为空时要判断相同否
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //2.状态是否正常分布
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //3.置顶需求：对isTop进行降序排序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page=new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        //通过categoryId查询categoryName
        List<Article> articles = page.getRecords();
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }
        //使用Stream流操作
          articles.stream()
                .map(article -> {
                    //从redis中获取viewCount
                    Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT_KEY, article.getId().toString());
                    //防止redis挂掉
                    if (Objects.nonNull(viewCount)){
                        article.setViewCount(viewCount.longValue());
                    }
                    return article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
                })
                .collect(Collectors.toList());
        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());

        return ResponseResult.okResult(pageVo);//

    }

    @Override
    public ResponseResult selectArticle(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT_KEY, id.toString());
        //防止redis挂掉
        if (Objects.nonNull(viewCount)){
            article.setViewCount(viewCount.longValue());
        }
        //查询文章相关的tags
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper =
                 new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> list = articleTagService.list(lambdaQueryWrapper);
        List<Long> tags = new ArrayList<>();
        for (ArticleTag articleTag : list) {
            tags.add(articleTag.getTagId());
        }
        article.setTags(tags);
        //封装响应返回
        return ResponseResult.okResult(article);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT_KEY, id.toString());
        //防止redis挂掉
        if (Objects.nonNull(viewCount)){
            article.setViewCount(viewCount.longValue());
        }
        //封装Vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Category category = categoryService.getById(articleDetailVo.getCategoryId());
        if (category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中的对应文章的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT_KEY, id.toString(),1);
        return ResponseResult.okResult();
    }
    @Override
    @Transactional
    public ResponseResult addArticle(ArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateArticle(Article article) {
        //添加 博客
        updateById(article);

        List<ArticleTag> articleTags = article.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        //TODO 更新关系表
        UpdateWrapper<ArticleTag> updateWrapper =
                new UpdateWrapper<>();
        updateWrapper.eq("article_id",article.getId());
        for (ArticleTag articleTag : articleTags) {
            articleTagService.saveOrUpdate(articleTag,updateWrapper);
        }
//        articleTagService.saveOrUpdateBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {

       UpdateWrapper<Article> updateWrapper =
               new UpdateWrapper<>();
       updateWrapper.eq("id",id);
       updateWrapper.set("del_flag",SystemConstants.DEL_FLAG);
       update(updateWrapper);
       return ResponseResult.okResult();
    }
}
