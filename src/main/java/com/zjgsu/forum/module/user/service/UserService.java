package com.zjgsu.forum.module.user.service;

import com.zjgsu.forum.core.util.JsonUtil;
import com.zjgsu.forum.core.util.security.crypto.BCryptPasswordEncoder;
import com.zjgsu.forum.module.collect.service.CollectService;
import com.zjgsu.forum.module.comment.service.CommentService;
import com.zjgsu.forum.module.log.service.LogService;
import com.zjgsu.forum.module.notification.service.NotificationService;
import com.zjgsu.forum.module.topic.service.TopicService;
import com.zjgsu.forum.module.user.model.User;
import com.zjgsu.forum.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

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
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setInTime(new Date());
        user.setBlock(false);
        user.setToken(UUID.randomUUID().toString());
        user.setAvatar(avatar);
        user.setUrl(url);
        user.setBio(bio);
        user.setReputation(0);
        user.setCommentEmail(true);
        user.setReplyEmail(true);
        return this.save(user);
    }

    public Page<User> findByReputation(int p,int size){
        Sort sort = new Sort(Sort.Direction.DESC,"reputation");
        Pageable pageable = PageRequest.of(p-1,size,sort);
        return userRepository.findAll(pageable);
    }

    public User findById(int id){
        return userRepository.findById(id);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    private User save(User user) {

        user = userRepository.save(user);
        //redis
        ValueOperations<String,String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set(user.getToken(), JsonUtil.objectToJson(user));
        return user;
    }
}
