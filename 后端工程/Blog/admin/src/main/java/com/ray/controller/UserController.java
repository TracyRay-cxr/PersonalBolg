package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.dto.UserDto;
import com.ray.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult listUser(Integer pageNum,Integer pageSize,String userName,String status,String phonenumber){
        return userService.listUser(pageNum,pageSize,userName,status,phonenumber);
    }
    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable("id")Long id){
        return userService.getUserById(id);
    }
    @PutMapping()
    public ResponseResult updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult delUser(@PathVariable("id")Long id){return userService.delUser(id);}
    @PostMapping()
    public ResponseResult addUser(@RequestBody UserDto userDto){
        return userService.addUser(userDto);
    }
}
