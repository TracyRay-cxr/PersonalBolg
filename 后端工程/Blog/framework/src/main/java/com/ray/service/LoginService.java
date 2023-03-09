package com.ray.service;

import com.ray.domain.ResponseResult;
import com.ray.domain.entity.User;


public interface LoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}
