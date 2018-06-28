package com.zjgsu.forum.module.es.service;

import com.zjgsu.forum.module.es.model.TagIndex;
import com.zjgsu.forum.module.es.repository.TagIndexRepository;
import com.zjgsu.forum.module.tag.model.Tag;
import com.zjgsu.forum.module.tag.repository.TagRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
 * Time: 21:23
 */
@Service
public class TagSearchService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagIndexRepository tagIndexRepository;

    public void indexedAll(){
        List<TagIndex> tagIndices = new ArrayList<>();
        List<Tag> tags = tagRepository.findAll();
        tags.forEach(tag -> {
            TagIndex tagIndex = new TagIndex();
            BeanUtils.copyProperties(tag,tagIndex);
            tagIndices.add(tagIndex);
        });
        this.clearAll();
        tagIndexRepository.saveAll(tagIndices);
    }

    public void indexed(Tag tag){
        TagIndex tagIndex = new TagIndex();
        BeanUtils.copyProperties(tag,tagIndex);
        tagIndexRepository.save(tagIndex);
    }

    public void deleteById(Integer id){
        tagIndexRepository.deleteById(id);
    }

    public List<TagIndex> query(String keyword,Integer limit){
        Pageable pageable = PageRequest.of(0,limit);
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword,"name");
        SearchQuery query = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(queryBuilder).build();
        return tagIndexRepository.search(query).getContent();
    }

    private void clearAll() {
        tagIndexRepository.deleteAll();
    }
}
