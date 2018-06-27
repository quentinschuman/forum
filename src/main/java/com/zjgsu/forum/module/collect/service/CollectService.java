package com.zjgsu.forum.module.collect.service;

import com.zjgsu.forum.core.util.JsonUtil;
import com.zjgsu.forum.module.collect.model.Collect;
import com.zjgsu.forum.module.collect.repository.CollectRepository;
import com.zjgsu.forum.module.log.service.LogService;
import com.zjgsu.forum.module.notification.service.NotificationService;
import com.zjgsu.forum.module.topic.model.Topic;
import com.zjgsu.forum.module.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/26
 * Time: 22:53
 */
@Service
@Transactional
public class CollectService {

    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private TopicService topicService;
    @Autowired
    private LogService logService;
    @Autowired
    private NotificationService notificationService;

    public Page<Map> findByUserId(int p,int size,Integer userId){
        Sort sort = new Sort(Sort.Direction.DESC,"inTime");
        Pageable pageable = PageRequest.of(p-1,size,sort);
        return collectRepository.findByUserId(userId,pageable);
    }

    public long countByUserId(Integer userId){
        return collectRepository.countByUserId(userId);
    }

    public long countByTopicId(Integer topicId){
        return collectRepository.countByUserId(topicId);
    }

    public Collect findByUserIdAndTopicId(Integer userId,Integer topicId){
        return collectRepository.findByUserIdAndTopicId(userId,topicId);
    }

    public Collect save(Collect collect){
        return collectRepository.save(collect);
    }

    public Collect createCollect(Topic topic, Integer userId){
        Collect collect = new Collect();
        collect.setInTime(new Date());
        collect.setTopicId(topic.getId());
        collect.setUserId(userId);
        this.save(collect);

        if (!topic.getUserId().equals(userId)) {
            notificationService.sendNotification(userId, topic.getUserId(), NotificationEnum.COLLECT, topic.getId(), null);
        }
            logService.save(LogEventEnum.COLLECT_TOPIC,collect.getUserId(),LogTargetEnum.COLLECT.name(),collect.getId(),null, JsonUtil.objectToJson(collect),topic);
            return collect;
    }

    public void deleteById(int id){
        Collect collect = collectRepository.findById(id).get();
        Topic topic = topicService.findById(collect.getTopicId());
        logService.save(LogEventEnum.DELETE_COLLECT_TOPIC,collect.getUserId(),LogTargetEnum.COLLECT.name(),collect.getId(),JsonUtil.objectToJson(collect),null,topic);
        collectRepository.deleteById(id);
    }

    public void deleteByUserId(Integer userId) {
        collectRepository.deleteByUserId(userId);
    }

    public void deleteByTopicId(Integer topicId) {
        collectRepository.deleteByTopicId(topicId);
    }
}
