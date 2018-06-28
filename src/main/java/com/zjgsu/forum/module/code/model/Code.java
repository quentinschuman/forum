package com.zjgsu.forum.module.code.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by qianshu on 2018/6/28.
 */
@Entity
@Table(name = "forum_code")
@Getter
@Setter
public class Code implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String code;

    @Column(name = "expire_time")
    private Date expireTime;
    private String type;
    @Column(name = "is_used")
    private Boolean isUsed;
    private String email;
    private String mobile;
}
