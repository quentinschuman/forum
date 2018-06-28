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
 * Time: 21:11
 */
@Data
@Document(indexName = "topic_index",type = "topic",refreshInterval = "-1")
public class TagIndex implements Serializable {

    @Id
    private Integer id;
    private String logo;
    private String name;
    private String intro;
    private Date inTime;
    private Integer topicCount;
}
