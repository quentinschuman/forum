package com.zjgsu.forum.module.collect.model;

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
 * Time: 21:20
 */
@Entity
@Table(name = "forum_collect")
@Getter
@Setter
public class Collect implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer topicId;
    private Integer userId;

    @Column
    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    private Date inTime;
}
