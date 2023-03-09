package com.ray.filter;

import com.alibaba.fastjson.JSON;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.LoginUser;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.utils.JwtUtil;
import com.ray.utils.RedisCache;
import com.ray.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 自定义一次拦截器继承OncePreRequestFilter,并放入SecurityConfig中生效
 * 验证是否为登录状态
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)){
            //直接放行让后面的过滤器链判断是否需要登录
            filterChain.doFilter(request,response);
            return;
        }
        //解析token获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时 或者 token非法
            //响应告诉前端重新登录：约定code==401
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        String userId=claims.getSubject();
        //从redis判断获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);
        if (Objects.isNull(loginUser)){
            //用户信息为空说明登录过期,要重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //存入SecurityContextHolder中方便以后使用
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
