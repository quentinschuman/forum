package com.zjgsu.forum.module.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zjgsu.forum.core.util.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by qianshu on 2018/6/26.
 */
@Entity
@Getter
@Setter
@Table(name = "forum_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String avatar;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    @Column(length = 64)
    private String bio;

    private String url;

    @Column(nullable = false)
    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    private Date inTime;

    private Boolean block;

    private String token;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer reputation;

    private Boolean commentEmail;

    private Boolean replyEmail;

}
