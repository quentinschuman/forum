package com.zjgsu.forum.module.tag.repository;

import com.zjgsu.forum.module.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/28
 * Time: 21:31
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Tag findByName(String name);

    @Query(value = "select t from Tag t, TopicTag tt where t.id = tt.tagId and tt.topicId = ?1")
    List<Tag> findByTopicId(Integer topicId);

}
