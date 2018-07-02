package com.zjgsu.forum.module.log.service;

import com.google.common.collect.Maps;
import com.zjgsu.forum.config.LogEventConfig;
import com.zjgsu.forum.core.util.FreemarkerUtil;
import com.zjgsu.forum.module.log.model.Log;
import com.zjgsu.forum.module.log.model.LogEventEnum;
import com.zjgsu.forum.module.log.repository.LogRepository;
import com.zjgsu.forum.module.topic.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/26
 * Time: 22:50
 */
@Service
public class LogService {

    @Autowired
    private FreemarkerUtil freemarkerUtil;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private LogEventConfig logEventConfig;

    public Log save(Log log){
        return logRepository.save(log);
    }

    public Page<Log> findByUserId(Integer p,int size,Integer userId){
        Sort sort = new Sort(Sort.Direction.DESC,"inTime");
        Pageable pageable = PageRequest.of(p-1,size,sort);
        return logRepository.findByUserId(userId,pageable);
    }

    public void deleteByUserId(Integer userId){
        logRepository.deleteByUserId(userId);
    }

    public Page<Map> findAllForAdmin(Integer pageNo,Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo-1,pageSize,new Sort(Sort.Direction.DESC,"inTime"));
        return logRepository.findAllForAdmin(pageable);
    }

    public Log save(LogEventEnum event, Integer userId, String target, Integer targetId, String before, String after, Topic topic){
        Map<String,Object> params = Maps.newHashMap();
        String desc = freemarkerUtil.format(logEventConfig.getTemplate().get(event.getName()), params);
        Log log = new Log();
        log.setEvent(event.getEvent());
        log.setEnentDescription(desc);
        log.setUserId(userId);
        log.setTarget(target);
        log.setTargetId(targetId);
        log.setBefore(before);
        log.setAfter(after);
        log.setInTime(new Date());
        return save(log);
    }
}
