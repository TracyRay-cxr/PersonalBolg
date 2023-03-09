package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-02-12 14:36:25
 */
public interface MenuService extends IService<Menu> {


    List<String> selectPermById(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult addMenu(Menu menu);

    ResponseResult updateMenu(Menu menu);

    ResponseResult delMenu(Long id);
}

