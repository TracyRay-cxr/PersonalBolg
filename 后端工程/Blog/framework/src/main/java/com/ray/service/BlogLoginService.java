package com.ray.service;

import com.ray.domain.ResponseResult;
import com.ray.domain.entity.User;


/**
 * 登录接口
 */

public interface BlogLoginService {
    /**
     * 登录方法
     * @param user
     * @return
     */
    ResponseResult login(User user);

    ResponseResult logout();
}
