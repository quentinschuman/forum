package com.zjgsu.forum.core.base;

import com.zjgsu.forum.config.SiteConfig;
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

}
