package com.zjgsu.forum.module.tag.service;

import com.zjgsu.forum.module.tag.model.Tag;
import com.zjgsu.forum.module.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by qianshu on 2018/6/29.
 */
@Service
@Transactional
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    public Tag findById(Integer id){
        return tagRepository.findById(id).get();
    }
    public Tag save(Tag tag){
        return tagRepository.save(tag);
    }
    public List<Tag> save(List<Tag> tags){
        return tagRepository.saveAll(tags);
    }
    public Tag findByName(String name){
        return tagRepository.findByName(name);
    }

}
