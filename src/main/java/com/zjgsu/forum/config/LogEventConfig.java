package com.zjgsu.forum.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/7/1
 * Time: 10:28
 */
@Configuration
@ConfigurationProperties(prefix = "log")
@Getter
@Setter
public class LogEventConfig {

    private Map<String,String> template = new HashMap<>();
}
