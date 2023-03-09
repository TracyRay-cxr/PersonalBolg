package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.UserDto;
import com.ray.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-02-06 12:57:18
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String status,String number);

    ResponseResult addUser(UserDto userDto);

    ResponseResult getUserById(Long id);

    ResponseResult updateUser(UserDto userDto);

    ResponseResult delUser(Long id);
}

