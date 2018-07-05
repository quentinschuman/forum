package com.zjgsu.forum.web.handle;

import com.zjgsu.forum.config.SiteConfig;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.core.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by qianshu on 2018/7/5.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private SiteConfig siteConfig;

    private HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request,Exception e) throws Exception{
        e.printStackTrace();
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception",e);
        mav.addObject("errorCode",getStatus(request));
        mav.setViewName("front/error");
        return mav;
    }

    @ExceptionHandler(value = ApiException.class)
    public Result jsonErrorHandler(ApiException e) throws Exception{
        Result result = new Result();
        result.setCode(e.getCode());
        result.setDescription(e.getMessage());
        return result;
    }
}
