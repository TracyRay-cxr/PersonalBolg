package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Update;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.constants.SystemConstants;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.Menu;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.mapper.MenuMapper;
import com.ray.service.MenuService;
import com.ray.utils.SecurityUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-02-12 14:36:26
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermById(Long id) {
        //如果是管理员，返回所有的权限
        if ((id==1L)){
            LambdaQueryWrapper<Menu> lambdaQueryWrapper =
                    new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            lambdaQueryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(lambdaQueryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则 获取当前用户所具有的Menu
           menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单 然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        if (Objects.isNull(menu)){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (Objects.nonNull(menu)&&menu.getParentId()!=menu.getId()){
            updateById(menu);
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(500,"修改菜单'写博文'失败，上级菜单不能选择自己");

    }

    @Override
    public ResponseResult delMenu(Long id) {
        //查看是否存在的子菜单
        LambdaQueryWrapper<Menu> lambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId,id);
        List<Menu> list = list(lambdaQueryWrapper);
        if (list.size()>0||list==null){
            throw new SystemException(AppHttpCodeEnum.NOT_DEL);
        }
        //更新删除状态，逻辑删除
        UpdateWrapper<Menu> updateWrapper =
                new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        updateWrapper.set("del_flag",1);
        update(updateWrapper);
        return ResponseResult.okResult();
    }


    private List<Menu> builderMenuTree(List<Menu> menus,Long parentId){
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(MenuServiceImpl.this.getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu,List<Menu> menus){
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}


