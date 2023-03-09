package com.ray.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-05 15:58:56
 */
//启动类加入包扫描，爆红不影响
//@Mapper
public interface UserMapper extends BaseMapper<User> {

}
