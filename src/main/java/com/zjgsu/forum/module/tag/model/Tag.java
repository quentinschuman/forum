package com.zjgsu.forum.module.tag.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/28
 * Time: 21:33
 */
@Entity
@Table(name = "forum_tag")
@Getter
@Setter
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    private String intro;
    private Date inTime;
    private String logo;
    private Integer topicCount;
}
