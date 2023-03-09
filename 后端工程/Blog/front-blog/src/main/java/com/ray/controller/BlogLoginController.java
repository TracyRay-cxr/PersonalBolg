package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.entity.User;

import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {


    @Autowired
    private BlogLoginService blogLoginService;
    /**
     * 登录接口
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        //前端校验后还要后端校验，以防有人绕过前端伪造请求
        if (!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            //throw new RuntimeException();
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
