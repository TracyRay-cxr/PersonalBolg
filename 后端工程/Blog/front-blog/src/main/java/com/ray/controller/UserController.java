package com.ray.controller;

import com.ray.annotation.SystemLog;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.User;
import com.ray.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 前端访问个人中心时并未访问该路径，而是采用直接拿到login时存在本地的用户信息渲染
     * 因此这里的方法还未被调用
     * @return
     */
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){

        return userService.userInfo();
    }
    @SystemLog(BusinessName="更新用户信息")
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
