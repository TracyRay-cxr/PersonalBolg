package com.ray.service.impl;

import com.ray.domain.ResponseResult;
import com.ray.domain.entity.LoginUser;
import com.ray.domain.entity.User;
import com.ray.exception.SystemException;
import com.ray.service.LoginService;
import com.ray.utils.JwtUtil;
import com.ray.utils.RedisCache;
import com.ray.utils.SecurityUtils;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseResult login(User user) {
        //认证登录
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authentication)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userID
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //jwt对userid进行加密生产token
        String jwt = JwtUtil.createJWT(userId);
        //存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //封装数据并响应返回
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //获取当前登录的用户id
        Long userId= SecurityUtils.getUserId();
        //删除redis中对应的值
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }
}
