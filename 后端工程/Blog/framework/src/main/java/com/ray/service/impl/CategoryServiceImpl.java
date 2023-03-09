package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.constants.SystemConstants;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.CategoryDto;
import com.ray.domain.dto.UpdateCategoryDto;
import com.ray.domain.entity.Article;
import com.ray.domain.entity.Category;
import com.ray.domain.vo.CategoryAdminVo;
import com.ray.domain.vo.CategoryVo;
import com.ray.domain.vo.PageVo;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.mapper.CategoryMapper;
import com.ray.service.ArticleService;
import com.ray.service.CategoryService;
import com.ray.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-02-03 13:55:16
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper=new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，以及去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        List<Category> categoryList = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVoList = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return ResponseResult.okResult(categoryVoList);

    }

    @Override
    public ResponseResult<List<CategoryVo>> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public PageVo listCategoryAdmin(Integer pageNum, Integer pageSize, String name, String status) {
        //模糊查询
        LambdaQueryWrapper<Category> lambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        lambdaQueryWrapper.like(StringUtils.hasText(status),Category::getStatus,status);
        //分页
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<Category> categoryList = page.getRecords();
        //封装
        List<CategoryAdminVo>  categoryAdminVos= categoryList.stream()
                .map(category -> BeanCopyUtils.copyBean(category, CategoryAdminVo.class))
                .collect(Collectors.toList());
        return  new PageVo(categoryAdminVos,page.getTotal());
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        //判空
        if (Objects.isNull(categoryDto)){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        //封装
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        //插入
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public CategoryAdminVo getCategory(Long id) {
        //根据id查询
        Category category = getById(id);
        //封装
        return BeanCopyUtils.copyBean(category,CategoryAdminVo.class);
    }

    @Override
    public ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto) {
        //判空
        if (Objects.isNull(updateCategoryDto)){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        //封装成实体类更新到数据库
        Category category = BeanCopyUtils.copyBean(updateCategoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {
        //逻辑删除
        UpdateWrapper<Category> updateWrapper =
                new UpdateWrapper<>();
        updateWrapper
                .set("del_flag",1)
                .eq("id",id);
        boolean update = update(updateWrapper);
        //判断是否成功
        if (update){
            return ResponseResult.okResult();
        }else {
            return ResponseResult.errorResult(500,"删除操作失败");
        }

    }
}



