package com.ray.handler.exception;

import com.ray.domain.ResponseResult;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义异常处理器-->springMVC的controller
 * mvc优先级低于过滤器异常处理器-->authenticationFilter
 */
//@ControllerAdvice
//@ResponseBody
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
        @ExceptionHandler(SystemException.class)
        public ResponseResult systemExceptionHandler(SystemException e){
            //用slf4j打印异常信息
            log.error("出现了异常！{}",e.getMsg());
            //从异常对象中获取提示信息，封装返回
            return ResponseResult.errorResult(e.getCode(),e.getMsg());
        }
        //其他所有异常先统一处理
        @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
            log.error("出现了异常! {}",e.getMessage());
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
        }
}
