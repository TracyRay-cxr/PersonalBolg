package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.UserDto;
import com.ray.domain.entity.LoginUser;
import com.ray.domain.entity.Role;
import com.ray.domain.entity.User;
import com.ray.domain.entity.UserRole;
import com.ray.domain.vo.PageVo;
import com.ray.domain.vo.UserInfoVo;
import com.ray.domain.vo.UserRoleAndIdsVo;
import com.ray.domain.vo.UserVo;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.mapper.RoleMapper;
import com.ray.mapper.UserMapper;
import com.ray.mapper.UserRoleMapper;
import com.ray.service.RoleService;
import com.ray.service.UserRoleService;
import com.ray.service.UserService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.JwtUtil;
import com.ray.utils.RedisCache;
import com.ray.utils.SecurityUtils;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-02-06 12:58:23
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
//    @Autowired
//    private RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserMapper userMapper;


    @Override
    public ResponseResult userInfo() {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //查询用户信息
        User user = getById(userId);
        //封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        //TODO 判断是否是伪造的请求例如postman直接发送的参数
        //更新数据库的值
        updateById(user);
        //更新redis里的用户信息,
        //但登录之后（登录认证过程中会给redis赋值）,
        //这里redis里的值只在过滤器(JWT...Filter)里赋值给SecurityContextHolder中,
        //而SecurityContextHolder也只是用了redis里的userId并未在登录中使用其他属性,
        //所以在一次登录状态中不会影响，登出后下次登录也会重载redis。
//        Authentication authenticate = SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
//        String userId = loginUser.getUser().getId().toString();
//        User uploaduser = getById(userId);
//        loginUser.setUser(uploaduser);
//        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行判空
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.MAIL_NOT_NULL);
        }
        //对数据进行重复判断，防止多次注册相同用户名，昵称
        if (userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //密码明文加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String status,String number) {
        //模糊查询
        LambdaQueryWrapper<User> lambdaQueryWrapper =
                 new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        lambdaQueryWrapper.eq(StringUtils.hasText(number),User::getPhonenumber,number);
        lambdaQueryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        //分页
        Page<User> page=new Page(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        //封装返回
        PageVo pageVo = new PageVo( page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo) ;
    }

    @Override
    public ResponseResult addUser(UserDto userDto) {
        //对数据进行判空
        if (!StringUtils.hasText(userDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!StringUtils.hasText(userDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(userDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(userDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.MAIL_NOT_NULL);
        }
        //对数据进行重复判断，防止多次注册相同用户名，昵称
        if (userNameExist(userDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(userDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(userDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //密码明文加密
        String encodePassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodePassword);
        //存数据库
        List<Long> roleIds = userDto.getRoleIds();
        //获取插入后的自增id
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        Long userId = Long.valueOf(userMapper.insert(user));
        //存入关系表
        List<UserRole> userRoleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            userRoleList.add(new UserRole(userId,roleId));
        }
        userRoleService.saveBatch(userRoleList);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserById(Long id) {
        //查询用户
        User user = getById(id);
        //根据用户id查询角色id
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper =
                 new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoleList = userRoleService.list(lambdaQueryWrapper);
        List<Long> roleIds = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            roleIds.add(userRole.getRoleId());
        }
        //封装返回
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
        //查询所有的角色
        List<Role> roles = roleService.listAllRole();
        UserRoleAndIdsVo userRoleAndIdsVo = new UserRoleAndIdsVo(roleIds, roles, userVo);
        return ResponseResult.okResult(userRoleAndIdsVo);
    }

    @Override
    public ResponseResult updateUser(UserDto userDto) {
        //拷贝到user实体类
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        //更新用户信息
        updateById(user);
        //更新用户角色关系
//        List<Long> roleIds = user.getRoleIds();
//        for (Long roleId : roleIds) {
//            userRoleService.saveOrUpdate(new UserRole(user.getId(),roleId));
//        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delUser(Long id) {
        //更新逻辑删除
        UpdateWrapper<User> userUpdateWrapper =
                new UpdateWrapper<>();
        userUpdateWrapper.eq("id",id);
        userUpdateWrapper.set("del_flag",1);
        update(userUpdateWrapper);
        return ResponseResult.okResult();
    }


    private boolean nickNameExist(String nickname) {
        //查询数据库判断重复
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(User::getNickName,nickname);
        return count(lambdaQueryWrapper)>0;
    }
    private boolean emailExist(String email) {
        //查询数据库判断重复
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(User::getEmail,email);
        return count(lambdaQueryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        //查询数据库判断重复
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(User::getUserName,userName);
        return count(lambdaQueryWrapper)>0;
    }
}

