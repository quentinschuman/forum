package com.zjgsu.forum.module.es.service;

import com.zjgsu.forum.module.es.model.TopicIndex;
import com.zjgsu.forum.module.es.repository.TopicIndexRepository;
import com.zjgsu.forum.module.topic.model.Topic;
import com.zjgsu.forum.module.topic.repository.TopicRepository;
import com.zjgsu.forum.module.user.model.User;
import com.zjgsu.forum.module.user.service.UserService;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/28
 * Time: 22:15
 */
@Service
public class TopicSearchService {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TopicIndexRepository topicIndexRepository;

    public void indexedAll(){
        List<TopicIndex> topicIndices = new ArrayList<>();
        List<Topic> topics = topicRepository.findAll();
        topics.forEach(topic -> {
            TopicIndex topicIndex = new TopicIndex();
            topicIndex.setId(topic.getId());
            topicIndex.setTitle(topic.getTitle());
            topicIndex.setTag(topic.getTag());
            topicIndex.setContent(topic.getContent());
            topicIndex.setInTime(topic.getInTime());

            User user = userService.findById(topic.getUserId());
            topicIndex.setUsername(user.getUsername());
            topicIndices.add(topicIndex);
        });
        this.clearAll();
        topicIndexRepository.saveAll(topicIndices);
    }

    public void indexed(Topic topic,String username){
        TopicIndex topicIndex = new TopicIndex();
        topicIndex.setId(topic.getId());
        topicIndex.setTitle(topic.getTitle());
        topicIndex.setTag(topic.getTag());
        topicIndex.setContent(topic.getContent());
        topicIndex.setInTime(topic.getInTime());
        topicIndex.setUsername(username);
        topicIndexRepository.save(topicIndex);
    }

    public void deleteById(Integer id){
        topicIndexRepository.deleteById(id);
    }

    public Page<TopicIndex> query(String keyword,Integer pageNo,Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword,"title","content");
        SearchQuery query = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(queryBuilder).build();
        return topicIndexRepository.search(query);
    }

    private void clearAll() {
        topicIndexRepository.deleteAll();
    }
}
