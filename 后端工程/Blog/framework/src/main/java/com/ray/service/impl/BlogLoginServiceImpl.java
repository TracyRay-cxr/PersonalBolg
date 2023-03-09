package com.ray.service.impl;

import com.ray.domain.ResponseResult;
import com.ray.domain.entity.LoginUser;
import com.ray.domain.entity.User;
import com.ray.domain.vo.BlogUserLoginVo;
import com.ray.domain.vo.UserInfoVo;
import com.ray.service.BlogLoginService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.JwtUtil;
import com.ray.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //通过用户名和密码生产AuthenticationToken，把AuthenticationToken传入相对应方法进行验证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        /*通过主体对象获取userid生产token
        * 1.使用自定义的对象（loginUser类继承了UserDetails,并且封装了user对象)
        * 2.接收查询到的用户信息，过滤器链将返回UserDetails类型对象，这里调用自定义的UserDetailsServiceImpl类
        * 3.通过自定义的UserDetails对象LoginUser可以拿到Userid
        * 4.通过id进行加密得到token
        * */
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
            //用JWT来进行加密成token,参数给的String，上面转换一下
        String jwt = JwtUtil.createJWT(userId);
//        System.out.println("token = " + jwt);
        //把用户信息存入redis
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
        //把token和userinfo封装返回
        UserInfoVo userInfo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo=new BlogUserLoginVo(jwt,userInfo);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //获取token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //通过token和jwt获取userid
        Long userId=loginUser.getUser().getId();
        //通过userid删除redis信息
        redisCache.deleteObject("bloglogin:"+userId);
        //响应对应信息
        return ResponseResult.okResult();
    }
}
