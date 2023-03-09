package com.ray.handler.security;


import com.alibaba.fastjson.JSON;
import com.ray.domain.ResponseResult;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义认证异常处理器
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //打印异常信息
        authException.printStackTrace();
        //InsufficientAuthenticationException
        //BadCredentialsException
        //判断一下是什么错误并返回对应的错误信息
        ResponseResult result=null;
        if (authException instanceof BadCredentialsException){
            result=ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),authException.getMessage());
        }else if (authException instanceof InsufficientAuthenticationException){
            result=ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else{
            //其他错误先这样处理:D
            result=ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"认证或授权失败");
        }
        //响应给前端

        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
