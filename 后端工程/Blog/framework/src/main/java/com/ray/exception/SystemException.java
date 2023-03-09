package com.ray.exception;

import com.ray.enums.AppHttpCodeEnum;
import lombok.Data;

@Data
public class SystemException extends RuntimeException{

    private int code;

    private String msg;

    //自定义子类独有的构造方法，自由赋值给自定义的属性
    public SystemException(AppHttpCodeEnum httpCodeEnum){
        //调用父类的有参构造，赋值父类的属性
        super(httpCodeEnum.getMsg());
        this.code=httpCodeEnum.getCode();
        this.msg=httpCodeEnum.getMsg();
    }
}
