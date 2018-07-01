package com.zjgsu.forum.module.notification.service;

import com.zjgsu.forum.module.notification.model.Notification;
import com.zjgsu.forum.module.notification.model.NotificationEnum;
import com.zjgsu.forum.module.notification.repository.NotificationRepository;
import com.zjgsu.forum.module.topic.model.Topic;
import com.zjgsu.forum.module.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/26
 * Time: 22:54
 */
@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void save(Notification notification){
        notificationRepository.save(notification);
    }

    public void sendNotification(Integer userId, Integer targetUserId, NotificationEnum action,Integer topicId,String content){
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTargetUserId(targetUserId);
        notification.setInTime(new Date());
        notification.setTopicId(topicId);
        notification.setAction(action.name());
        notification.setContent(content);
        notification.setIsRead(false);
        save(notification);
    }

    public Page<Map> findByTargetUserAndIsRead(int p, int size, User targetUser,Boolean isRead){
        Sort sort = new Sort(Sort.Direction.ASC,"isRead").and(new Sort(Sort.Direction.DESC,"inTime"));
        Pageable pageable = PageRequest.of(p-1,size,sort);
        if (isRead == null){
            return notificationRepository.findByTargetUserId(targetUser.getId(),pageable);
        }
        return notificationRepository.findByTargetUserIdAndIsRead(targetUser.getId(),isRead,pageable);
    }

    public long countByTargetUserAndIsRead(User targetUser,boolean isRead){
        return notificationRepository.countByTargetUserIdAndIsRead(targetUser.getId(),isRead);
    }

    public List<Notification> findByTargetUserAndIsRead(User targetUser,boolean isRead){
        return notificationRepository.findByTargetUserIdAndIsRead(targetUser.getId(),isRead);
    }

    public void updateByIsRead(User targetUser){
        notificationRepository.updateByIsRead(targetUser.getId());
    }

    public void deleteByTargetUser(Integer userId) {
        notificationRepository.deleteByTargetUserId(userId);
    }

    public void deleteByTopic(Topic topic){
        notificationRepository.deleteByTopicId(topic.getId());
    }
}
