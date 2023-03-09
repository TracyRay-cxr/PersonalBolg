package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.RoleStatusDto;
import com.ray.domain.entity.Role;
import com.ray.domain.vo.PageVo;
import com.ray.mapper.RoleMapper;
import com.ray.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-02-12 14:46:17
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleKeyById(Long id) {
        //判断是否为管理员，如果是就只需返回admin
        if (id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public List<Role> listAllRole() {
        LambdaQueryWrapper<Role> lambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getStatus,0);
        List<Role> list = list(lambdaQueryWrapper);
        return list;
    }

    @Override
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(status),Role::getStatus,status);
        lambdaQueryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        Page page = new Page(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);

    }

    @Override
    public ResponseResult deleteRole(Long id) {
        //更新status，逻辑删除
        UpdateWrapper<Role> updateWrapper =
                 new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        updateWrapper.set("del_flag",1);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(RoleStatusDto roleStatusDto) {
        //修改逻辑删除
        //更新status，逻辑删除
        UpdateWrapper<Role> updateWrapper =
                new UpdateWrapper<>();
        updateWrapper.eq("id",roleStatusDto.getRoleId());
        if (roleStatusDto.getStatus().equals("0")){
            updateWrapper.set("status",1);
        }else {
            updateWrapper.set("status",0);
        }

        update(updateWrapper);
        return ResponseResult.okResult();

    }
}

