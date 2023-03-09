package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ray.constants.SystemConstants;
import com.ray.domain.entity.LoginUser;
import com.ray.domain.entity.User;
import com.ray.mapper.MenuMapper;
import com.ray.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 自定义用户校验过程中的一个返回类型，重写loadUserByUsername方法
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,username);
        User user=userMapper.selectOne(lambdaQueryWrapper);
        //判断是否查到用户，为空抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //存在用户，包装返回
        //TODO 查询权限信息封装
        if (user.getType().equals(SystemConstants.ADMAIN)){
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,list);
        }
        //普通用户直接返回
        return new LoginUser(user,null);
    }
}
