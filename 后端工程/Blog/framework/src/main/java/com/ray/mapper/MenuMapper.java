package com.ray.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-12 14:36:24
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectAllRouterMenu();
    //TODO 需要进行动态sql的模糊查询
    List<Menu> selectAllMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    Menu selectMenuByUserId(Long id);


}
