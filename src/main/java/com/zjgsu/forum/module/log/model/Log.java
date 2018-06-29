package com.zjgsu.forum.module.log.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by qianshu on 2018/6/29.
 */
@Entity
@Table(name = "forum_log")
@Getter
@Setter
public class Log implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String event;
    private String enentDescription;
    private Integer userId;
    private String target;
    private Integer targetId;
    @Column(name = "before_content",columnDefinition = "TEXT")
    private String before;
    @Column(name = "after_content",columnDefinition = "TEXT")
    private String after;
    private Date inTime;
}
