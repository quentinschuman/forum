package com.zjgsu.forum.module.es.repository;

import com.zjgsu.forum.module.es.model.TagIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/28
 * Time: 21:19
 */
@Repository
public interface TagIndexRepository extends ElasticsearchRepository<TagIndex,Integer> {
}
