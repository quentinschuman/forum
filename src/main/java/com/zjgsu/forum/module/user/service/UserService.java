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
import java.util.List;
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

    public User createUser(String username, String password, String email, String avatar, String url, String bio) {
        if(!StringUtils.isEmpty(email) && email.equals("null")) email = null;
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
        // 默认邮件打开
        user.setCommentEmail(true);
        user.setReplyEmail(true);
        return this.save(user);
    }

    /**
     * search user by log desc
     *
     * @param p
     * @param size
     * @return
     */
    public Page<User> findByReputation(int p, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "reputation");
        Pageable pageable = PageRequest.of(p - 1, size, sort);
        return userRepository.findAll(pageable);
    }

    public User findById(int id) {
        return userRepository.findById(id);
    }

    /**
     * 根据用户名判断是否存在
     *
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        user = userRepository.save(user);
        // 更新redis里的数据
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set(user.getToken(), JsonUtil.objectToJson(user));
        return user;
    }

    /**
     * 分页查询用户列表
     *
     * @param p
     * @param size
     * @return
     */
    public Page<User> pageUser(int p, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "inTime");
        Pageable pageable = PageRequest.of(p - 1, size, sort);
        return userRepository.findAll(pageable);
    }

    /**
     * 禁用用户
     *
     * @param id
     */
    public void blockUser(Integer id) {
        User user = findById(id);
        user.setBlock(true);
        save(user);
    }

    /**
     * 用户解禁
     *
     * @param id
     */
    public void unBlockUser(Integer id) {
        User user = findById(id);
        user.setBlock(false);
        save(user);
    }

    public User refreshToken(User user) {
        user.setToken(UUID.randomUUID().toString());
        return this.save(user);
    }

    /**
     * 根据令牌查询用户
     *
     * @param token
     * @return
     */
    public User findByToken(String token) {
        return userRepository.findByToken(token);
    }

    public void deleteById(Integer id) {
        // 删除用户的日志
        logService.deleteByUserId(id);
        // 删除用户的通知
        notificationService.deleteByTargetUser(id);
        // 删除用户的收藏
        collectService.deleteByUserId(id);
        // 删除用户的评论
        commentService.deleteByUserId(id);
        // 删除用户的话题
        topicService.deleteByUserId(id);
        // 删除用户
        userRepository.deleteById(id);
    }

    // 删除所有后台用户存在redis里的数据
    public void deleteAllRedisUser() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> stringRedisTemplate.delete(user.getToken()));
    }
}
