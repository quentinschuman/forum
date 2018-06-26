package com.zjgsu.forum.module.user.service;

import com.zjgsu.forum.module.collect.service.CollectService;
import com.zjgsu.forum.module.comment.service.CommentService;
import com.zjgsu.forum.module.log.service.LogService;
import com.zjgsu.forum.module.notification.service.NotificationService;
import com.zjgsu.forum.module.topic.service.TopicService;
import com.zjgsu.forum.module.user.model.User;
import com.zjgsu.forum.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/26
 * Time: 22:48
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogService logService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public User createUser(String username,String password,String email,String avatar,String url,String bio){
        if (!StringUtils.isEmpty(email) && email.equals("null"))
            email = null;
        User user = new User();
        user.setEmail(email);
    }
}
