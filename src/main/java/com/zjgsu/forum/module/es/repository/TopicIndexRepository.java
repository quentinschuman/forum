package com.zjgsu.forum.module.es.repository;

import com.zjgsu.forum.module.es.model.TopicIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/28
 * Time: 21:21
 */
@Repository
public interface TopicIndexRepository extends ElasticsearchRepository<TopicIndex,Integer> {
}
