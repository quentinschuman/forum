package com.zjgsu.forum.core.base;

import com.zjgsu.forum.config.SiteConfig;
import com.zjgsu.forum.core.exception.ApiAssert;
import com.zjgsu.forum.module.security.model.AdminUser;
import com.zjgsu.forum.module.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by qianshu on 2018/6/26.
 */
public class BaseController {

    @Autowired
    private BaseEntity baseEntity;

    @Autowired
    private SiteConfig siteConfig;

    /**
     * 带参重定向
     * @param path
     * @return
     */
    protected String redirect(String path){
        String baseUrl = siteConfig.getBaseUrl();
        baseUrl = baseUrl.substring(0,baseUrl.length()-1);
        return "redirect:" + baseUrl + path;
    }

    protected User getUser(){
        return baseEntity.getUser();
    }

    protected AdminUser getAdminUser(){
        return baseEntity.getAdminUser();
    }

    protected User getApiUser(){
        User user = baseEntity.getUser();
        ApiAssert.notNull(user,"请先登录");
        return user;
    }

}
