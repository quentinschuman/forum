package com.zjgsu.forum.module.tag.repository;

import com.zjgsu.forum.module.tag.model.Tag;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/28
 * Time: 21:31
 */
public interface TagRepository {
    List<Tag> findAll();
}
