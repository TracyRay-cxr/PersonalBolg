package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.ArticleDto;
import com.ray.domain.entity.Article;

public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList_admin(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult selectArticle(Long id);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(ArticleDto article);

    ResponseResult updateArticle(Article article);

    ResponseResult deleteArticle(Long id);
}
