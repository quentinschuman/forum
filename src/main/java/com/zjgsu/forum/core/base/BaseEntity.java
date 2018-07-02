package com.zjgsu.forum.core.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.jsoup.nodes.Document;

import java.util.Date;

/**
 * Created by qianshu on 2018/6/26.
 */
@Component
@Slf4j
public class BaseEntity {

    private static final long MINUTE = 60 * 1000;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long MONTH = 31 * DAY;
    private static final long YEAR = 12 * MONTH;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    public String formatDate(Date date){
        if (date == null)
            return "";
        long offset = System.currentTimeMillis()-date.getTime();
        if (offset > YEAR){
            return (offset/YEAR) + "年前";
        } else if (offset > MONTH) {
            return (offset / MONTH) + "个月前";
        } else if (offset > WEEK) {
            return (offset / WEEK) + "周前";
        } else if (offset > DAY) {
            return (offset / DAY) + "天前";
        } else if (offset > HOUR) {
            return (offset / HOUR) + "小时前";
        } else if (offset > MINUTE) {
            return (offset / MINUTE) + "分钟前";
        } else {
            return "刚刚";
        }
    }

    public boolean isEmpty(String text){
        return StringUtils.isEmpty(text);
    }

    public String formatContent(String content){
        Document parse = Jsoup.parse(content);
        Elements tableElements = parse.select("table");
        tableElements.forEach(element -> element.addClass("table table-bordered"));
        Elements aElements = parse.select("p");
    }

}
