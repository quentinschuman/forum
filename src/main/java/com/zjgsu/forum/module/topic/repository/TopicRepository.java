package com.zjgsu.forum.module.topic.repository;

import com.zjgsu.forum.module.topic.model.Topic;

import java.util.List;

/**
 * Created by qianshu on 2018/6/28.
 */
public interface TopicRepository {
    List<Topic> findAll();
}
