package com.zjgsu.forum.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by qianshu on 2018/7/2.
 */
@Configuration
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class MailTemplateConfig {

    Map<String,Object> register;
    Map<String,Object> commentTopic;
    Map<String,Object> replyComment;
}
