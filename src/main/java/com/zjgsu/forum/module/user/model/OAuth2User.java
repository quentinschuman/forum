package com.zjgsu.forum.module.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/26
 * Time: 21:43
 */
@Entity
@Getter
@Setter
@Table(name = "forum_oauth2_user")
public class OAuth2User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String avatar;
    private String nickName;
    private Date inTime;

    private Integer userId;
    private String oauthUserId;
    private String accessToken;
    private String type;

    public enum Type{
        GITHUB
    }
}
