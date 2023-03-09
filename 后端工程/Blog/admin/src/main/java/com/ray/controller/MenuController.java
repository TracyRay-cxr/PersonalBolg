package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.entity.Menu;
import com.ray.mapper.MenuMapper;
import com.ray.service.MenuService;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult listMenu(Integer status, String menuName){
        return ResponseResult.okResult(menuMapper.selectAllMenu());
    }
    @GetMapping("/{id}")
    public ResponseResult selectMenuById(@PathVariable("id")Long id){
        return ResponseResult.okResult(menuMapper.selectMenuByUserId(id));
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }
    @DeleteMapping("/{menuId}")
    public ResponseResult delMenu(@PathVariable("menuId")Long id){
        return menuService.delMenu(id);
    }
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        //TODO 构建角色树
        return ResponseResult.okResult();
    }

}
