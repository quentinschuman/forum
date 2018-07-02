package com.zjgsu.forum.config;

import com.zjgsu.forum.core.base.BaseEntity;
import com.zjgsu.forum.web.tag.*;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityConfig;
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

    @Autowired
    private UserTopicDirective userTopicDirective;

    @Autowired
    private UserCommentDirective userCommentDirective;

    @Autowired
    private UserCollectDirective userCollectDirective;

    @Autowired
    private TopicsDirective topicsDirective;

    @Autowired
    private UserDirective userDirective;

    @Autowired
    private CurrentUserDirective currentUserDirective;

    @Autowired
    private NotificationsDirective notificationsDirective;

    @Autowired
    private CommentsDirective commentsDirective;

    @Autowired
    private ReputationDirective reputationDirective;

    @Autowired
    private BaseEntity baseEntity;

    @Autowired
    private SecurityConfig securityConfig;

    public void setShareVariable()throws TemplateModelException{
        configuration.setSharedVariable("site",siteConfig);
        configuration.setSharedVariable("model",baseEntity);
        configuration.setSharedVariable("sec",securityConfig);
        configuration.setSharedVariable("user_topics_tag",userTopicDirective);
        configuration.setSharedVariable("user_comment_tag",userCommentDirective);
        configuration.setSharedVariable("user_collect_tag",userCollectDirective);
        configuration.setSharedVariable("topic_tag",topicsDirective);
        configuration.setSharedVariable("user_tag",userDirective);
        configuration.setSharedVariable("current_user_tag",currentUserDirective);
        configuration.setSharedVariable("notifications_tag",notificationsDirective);
        configuration.setSharedVariable("comments_tag",commentsDirective);
        configuration.setSharedVariable("reputation_tag",reputationDirective);
        log.info("init freemarker sharedVariables {site} success...");
    }
}
