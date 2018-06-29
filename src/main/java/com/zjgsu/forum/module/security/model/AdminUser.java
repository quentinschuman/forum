package com.zjgsu.forum.module.security.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/29
 * Time: 21:49
 */
@Entity
@Table(name = "forum_admin_user")
@Getter
@Setter
public class AdminUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private Date inTime;
    private String token;
    private Integer roleId;
    @Transient
    private Role role;
    @Transient
    private List<Permission> permissions;
}
