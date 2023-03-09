package com.ray.controller;


import com.ray.domain.ResponseResult;
import com.ray.domain.entity.LoginUser;
import com.ray.domain.entity.Menu;
import com.ray.domain.entity.User;
import com.ray.domain.vo.AdminUserInfoVo;
import com.ray.domain.vo.RoutersVo;
import com.ray.domain.vo.UserInfoVo;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.service.LoginService;
import com.ray.service.MenuService;
import com.ray.service.RoleService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.SecurityUtils;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;


    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        //对用户名判空
        if (!StringUtils.hasText(user.getUserName())){
            //提示必须填写用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
       return loginService.login(user);
    }
    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        //获取userid和用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //通过id获取权限列表
        List<String> premissions = menuService.selectPermById(loginUser.getUser().getId());
        //通过id获取角色列表
        List<String> roles = roleService.selectRoleKeyById(loginUser.getUser().getId());
        //封装用户信息
        UserInfoVo user = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        //将数据封装到AdminUserInfo
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(premissions, roles, user);
        return ResponseResult.okResult(adminUserInfoVo);

    }
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree类型
        List<Menu> menus =
                menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

}
