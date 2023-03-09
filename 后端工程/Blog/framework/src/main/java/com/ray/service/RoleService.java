package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.RoleStatusDto;
import com.ray.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-02-12 14:46:17
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyById(Long id);

    List<Role> listAllRole();

    ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult deleteRole(Long id);

    ResponseResult changeStatus(RoleStatusDto roleStatusDto);
}

