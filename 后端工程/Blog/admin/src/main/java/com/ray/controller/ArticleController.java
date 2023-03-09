package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.dto.ArticleDto;
import com.ray.domain.entity.Article;
import com.ray.service.ArticleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult addArticle(@RequestBody ArticleDto article){
        return articleService.addArticle(article);
    }
    @GetMapping("/list")
    public ResponseResult pageList(Integer pageNum,Integer pageSize,String title,String summary){
        return articleService.articleList_admin(pageNum, pageSize,title,summary);
    }
    @GetMapping("/{id}")
    public ResponseResult selectArticle(@PathVariable("id")Long id){
        return articleService.selectArticle(id);
    }
    @PutMapping
    public ResponseResult updateArticle(@RequestBody Article article){
        return articleService.updateArticle(article);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id")Long id){
        return articleService.deleteArticle(id);
    }
}
