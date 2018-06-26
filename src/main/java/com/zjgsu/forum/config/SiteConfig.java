package com.zjgsu.forum.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by qianshu on 2018/6/26.
 */
@Configuration
@ConfigurationProperties(prefix = "site")
@Getter
@Setter
public class SiteConfig {

    private String name;
    private String intro;
    private String baseUrl;
    private String staticUrl;
    private Integer pageSize;
    private String uploadPath;
    private String theme;
    private boolean search;
    private String GA;
    private String googleZZ;
    private String baiduTJ;
    private String baiduZZ;
    private String uploadType;
    private CookieConfig cookie;
    private OAuth2Config oauth2;
    private UploadConfig upload;
}
