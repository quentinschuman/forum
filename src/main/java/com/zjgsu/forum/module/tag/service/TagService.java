package com.zjgsu.forum.module.tag.service;

import com.zjgsu.forum.module.tag.model.Tag;
import com.zjgsu.forum.module.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
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

    public List<Tag> save(String[] tags){
        List<Tag> tagList = new ArrayList<>();
        for (String t:tags){
            Tag tag = this.findByName(t);
            if (tag == null){
                tag = new Tag();
                tag.setInTime(new Date());
                tag.setName(t);
                tag.setTopicCount(1);
            }else {
                tag.setTopicCount(tag.getTopicCount()+1);
            }
            tagList.add(tag);
        }
        return tagRepository.saveAll(tagList);
    }

    public List<Tag> findByTopicId(Integer topicId){
        return tagRepository.findByTopicId(topicId);
    }

    public Page<Tag> page(Integer pageNo,Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo-1,pageSize,new Sort(Sort.Direction.DESC,"topicCount"));
        return tagRepository.findAll(pageable);
    }

    public void delete(Tag tag){
        tagRepository.delete(tag);
    }
}
