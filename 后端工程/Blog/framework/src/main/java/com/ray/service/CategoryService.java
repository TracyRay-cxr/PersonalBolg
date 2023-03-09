package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.CategoryDto;
import com.ray.domain.dto.UpdateCategoryDto;
import com.ray.domain.entity.Category;
import com.ray.domain.vo.CategoryAdminVo;
import com.ray.domain.vo.CategoryVo;
import com.ray.domain.vo.PageVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-02-03 13:55:16
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult<List<CategoryVo>> listAllCategory();

    PageVo listCategoryAdmin(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(CategoryDto categoryDto);

    CategoryAdminVo getCategory(Long id);

    ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto);

    ResponseResult deleteCategory(Long id);
}

