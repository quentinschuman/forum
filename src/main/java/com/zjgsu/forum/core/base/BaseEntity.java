package com.zjgsu.forum.core.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.jsoup.nodes.Document;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public String formatContent(String content) {
        Document parse = Jsoup.parse(content);
        Elements tableElements = parse.select("table");
        tableElements.forEach(element -> element.addClass("table table-bordered"));
        Elements aElements = parse.select("p");
        if (aElements != null && aElements.size() > 0) {
            aElements.forEach(element -> {
                try {
                    String href = element.text();
                    if (href.contains("https://www.youtube.com/watch")) {
                        URL aUrl = new URL(href);
                        String query = aUrl.getQuery();
                        Map<String, Object> querys = StrUtil.formatParams(query);
                        element.text("");
                        element.addClass("embed-responsive embed-responsive-16by9");
                        element.append("<iframe class='embedded_video' src='https://www.youtube.com/embed/" + querys.get("v") + "' frameborder='0' allowfullscreen></iframe>");
                    } else if(href.contains("http://v.youku.com/v_show/")) {
                        element.text("");
                        URL aUrl = new URL(href);
                        String _href = "http://player.youku.com/embed/" + aUrl.getPath().replace("/v_show/id_", "").replace(".html", "");
                        element.addClass("embedded_video_wrapper");
                        element.append("<iframe class='embedded_video' src='" + _href + "' frameborder='0' allowfullscreen></iframe>");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            });
        }
        return parse.outerHtml();
    }

    public User getUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String token = CookieHelper.getValue(request, siteConfig.getCookie().getUserName());
        if (org.springframework.util.StringUtils.isEmpty(token)) return null;
        // token不为空，查redis
        try {
            token = new String(Base64Helper.decode(token));
            ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
            String redisUser = stringStringValueOperations.get(token);
            if (!org.springframework.util.StringUtils.isEmpty(redisUser)) return JsonUtil.jsonToObject(redisUser, User.class);

            User user = userService.findByToken(token);
            if (user == null) {
                CookieHelper.clearCookieByName(response, siteConfig.getCookie().getUserName());
            } else {
                stringStringValueOperations.set(token, JsonUtil.objectToJson(user));
                return user;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            CookieHelper.clearCookieByName(response, siteConfig.getCookie().getUserName());
        }
        return null;
    }

    public AdminUser getAdminUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String token = CookieHelper.getValue(request, siteConfig.getCookie().getAdminUserName());
        if (org.springframework.util.StringUtils.isEmpty(token)) return null;
        // token不为空，查redis
        try {
            token = new String(Base64Helper.decode(token));
            ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
            String redisAdminUser = stringStringValueOperations.get("admin_" + token);
            if (!org.springframework.util.StringUtils.isEmpty(redisAdminUser)) return JsonUtil.jsonToObject(redisAdminUser, AdminUser.class);

            AdminUser adminUser = adminUserService.findByToken(token);
            if (adminUser == null) {
                CookieHelper.clearCookieByName(request, response, siteConfig.getCookie().getAdminUserName(),
                        siteConfig.getCookie().getDomain(), "/admin/");
            } else {
                Role role = roleService.findById(adminUser.getRoleId());
                List<Permission> permissions = permissionService.findByUserId(adminUser.getId());
                adminUser.setRole(role);
                adminUser.setPermissions(permissions);
                stringStringValueOperations.set("admin_" + token, JsonUtil.objectToJson(adminUser));
                return adminUser;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            CookieHelper.clearCookieByName(request, response, siteConfig.getCookie().getAdminUserName(),
                    siteConfig.getCookie().getDomain(), "/admin/");
        }
        return null;
    }

}
