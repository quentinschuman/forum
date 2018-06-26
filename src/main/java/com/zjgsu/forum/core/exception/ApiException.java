package com.zjgsu.forum.core.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by qianshu on 2018/6/26.
 */
@Getter
@Setter
public class ApiException extends RuntimeException {

    private int code;
    private String message;

    public ApiException(String message){
        this.code = 201;
        this.message = message;
    }

    public ApiException(int code,String message){
        this.code = code;
        this.message = message;
    }
}
