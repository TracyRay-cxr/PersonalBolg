package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.dto.RoleStatusDto;
import com.ray.service.RoleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return ResponseResult.okResult(roleService.listAllRole());
    }
    @GetMapping("/list")
    public ResponseResult listRole(Integer pageNum, Integer pageSize,String roleName,String status){
        return roleService.listRole(pageNum,pageSize,roleName,status);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id") Long id){
        return roleService.deleteRole(id);
    }
    @PutMapping("changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleStatusDto roleStatusDto){
        return roleService.changeStatus(roleStatusDto);
    }
}
