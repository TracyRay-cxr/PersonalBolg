package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 分类表(Category)表控制层
 *
 * @author makejava
 * @since 2023-02-03 14:34:07
 */
@RestController
@RequestMapping("/category")
public class CategoryController{

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();

    }

}

