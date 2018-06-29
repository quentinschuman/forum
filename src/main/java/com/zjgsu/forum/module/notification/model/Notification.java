package com.zjgsu.forum.module.notification.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zjgsu.forum.core.util.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by qianshu on 2018/6/29.
 */
@Entity
@Table(name = "forum_notification")
@Getter
@Setter
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_read")
    private Boolean isRead;
    private Integer userId;
    private Integer targetUserId;

    @Column(name = "in_time")
    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    private Date inTime;
    private String action;
    private Integer topicId;
    @Column(columnDefinition = "text")
    private String content;
}
