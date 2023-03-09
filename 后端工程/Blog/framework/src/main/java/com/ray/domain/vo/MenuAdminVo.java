package com.ray.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ray.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuAdminVo {
    //菜单ID
    private Long id;
    //菜单名称
    private String label;
    //父菜单ID
    private Long parentId;
    //子菜单列表
    private List<Menu> children;

}
