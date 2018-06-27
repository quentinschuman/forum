package com.zjgsu.forum.module.collect.repository;

import com.zjgsu.forum.module.collect.model.Collect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/27
 * Time: 21:19
 */
@Repository
public interface CollectRepository extends JpaRepository<Collect,Integer> {

    @Query(value = "select c as collect,t as topic,u as user from Collect c,Topic t,User u where t.id = c.topicId = u.id and c.userId = ?1",countQuery = "select count(1) from Collect c,Topic t,User u where t.id = c.topicId and c.userId = u.id and c.userId = ?1")
    Page<Map> findByUserId(Integer userId, Pageable pageable);

    long countByUserId(Integer userId);
    long countByTopicId(Integer topicId);
    Collect findByUserIdAndTopicId(Integer userId,Integer topicId);
    void deleteByUserId(Integer userId);
    void deleteByTopicId(Integer topicId);
}
