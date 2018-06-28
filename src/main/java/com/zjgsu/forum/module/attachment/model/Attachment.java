package com.zjgsu.forum.module.attachment.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by qianshu on 2018/6/28.
 */
@Entity
@Table(name = "forum_attachment")
@Getter
@Setter
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String localPath;
    private String fileName;
    private Date inTime;
    private String requestUrl;
    private String type;
    private Integer width;
    private Integer height;
    private String size;
    private String suffix;
    @Column(unique = true)
    private String md5;
}
