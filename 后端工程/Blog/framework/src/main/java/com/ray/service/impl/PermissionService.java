package com.ray.service.impl;

import com.ray.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    /**
     * 自定义判断参数权限是否有效
     * @param permission 需要判断的权限
     * @return
     */
    public boolean hasPermission(String permission){
        //判断是否是超级管理员
        if (SecurityUtils.isAdmin()){
            return true;
        }
        //查询当前登录用户的信息，判断是否拥有参数的权限
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }

}
