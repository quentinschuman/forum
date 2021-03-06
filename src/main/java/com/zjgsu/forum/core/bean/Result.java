package com.zjgsu.forum.core.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by qianshu on 2018/6/26.
 */
@Getter
@Setter
public class Result {

    private int code;
    private String description;
    private Object detail;

    public static Result success() {
        return success(null);
    }

    public static Result success(Object detail){
        Result result = new Result();
        result.setCode(200);
        result.setDescription("success");
        result.setDetail(detail);
        return result;
    }

    public static Result error(){
        return error(null);
    }

    public static Result error(String description){
        return error(201,description);
    }

    public static Result error(int code,String description){
        Result result = new Result();
        result.setCode(code);
        result.setDescription(description);
        result.setDetail(null);
        return result;
    }
}
