package com.ray.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.CategoryDto;
import com.ray.domain.dto.UpdateCategoryDto;
import com.ray.domain.entity.Category;
import com.ray.domain.vo.CategoryVo;
import com.ray.domain.vo.ExcelCategoryVo;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.service.CategoryService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult<List<CategoryVo>> listAllCategory(){
        return categoryService.listAllCategory();
    }
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void exprot(HttpServletResponse response){

        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入Excel中
            EasyExcel.write(response.getOutputStream(),ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //异常响应给json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("/list")
    public ResponseResult listCategoryAdmin(Integer pageNum,Integer pageSize,String name,String status){
        return ResponseResult.okResult(categoryService.listCategoryAdmin(pageNum,pageSize,name,status));
    }
    @PostMapping()
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getCategory(@PathVariable("id") Long id){
        return ResponseResult.okResult(categoryService.getCategory(id));
    }
    @PutMapping()
    public ResponseResult updateCategory(@RequestBody UpdateCategoryDto updateCategoryDto){
        return categoryService.updateCategory(updateCategoryDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id){
        return categoryService.deleteCategory(id);
    }
}

