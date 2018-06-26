package com.zjgsu.forum.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by qianshu on 2018/6/26.
 */
@Component
@Slf4j
public class FreemarkerConfig {

    @Autowired
    private freemarker.template.Configuration configuration;

    @Autowired
    private SiteConfig siteConfig;

}
