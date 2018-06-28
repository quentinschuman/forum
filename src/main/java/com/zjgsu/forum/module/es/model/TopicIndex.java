package com.zjgsu.forum.module.es.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/28
 * Time: 21:13
 */
@Data
@Document(indexName = "topic_index",type = "topic",refreshInterval = "-1")
public class TopicIndex implements Serializable {

    @Id
    private Integer id;
    private String title;
    private String content;
    private String username;
    private String tag;
    private Date inTime;
    private Double weight;

    public TopicIndex(){

    }

    public TopicIndex(Integer id,String title,String content,String username,String tag,Date inTime,Double weight){
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.tag = tag;
        this.inTime = inTime;
        this.weight = weight;
    }
}
