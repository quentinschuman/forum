package com.zjgsu.forum.module.topic.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zjgsu.forum.core.util.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/27
 * Time: 21:41
 */
@Entity
@Table(name = "forum_topic")
@Getter
@Setter
public class Topic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    private Date inTime;

    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    @Column(name = "modify_time")
    private Date modifyTime;

    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    @Column(name = "last_comment_time")
    private Date lastCommentTime;

    private Boolean top;
    private Boolean good;

    @Column(nullable = false)
    private Integer view;

    private Integer userId;

    @Column(name = "comment_count")
    private Integer commentCount;

    private Integer up;
    private Integer down;

    @Column(columnDefinition = "text")
    private String upIds;

    @Column(columnDefinition = "text")
    private String downIds;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double weight;

    private String tag;
}
