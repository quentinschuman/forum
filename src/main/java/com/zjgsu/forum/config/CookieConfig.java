package com.zjgsu.forum.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/25
 * Time: 22:23
 */
@Getter
@Setter
public class CookieConfig {

    private String domain;
    private String userName;
    private Integer userMaxAge;

    private String adminUserName;
    private Integer adminUserMaxAge;
}
