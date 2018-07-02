package com.zjgsu.forum.module.user.model;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/26
 * Time: 21:30
 */
public enum UserReputation {

    UP_COMMENT("评论被点赞", 10),
    UP_TOPIC("话题被点赞", 5),

    DOWN_COMMENT("点踩话题", -2),
    DOWN_TOPIC("点踩评论", -2),;

    private String name;
    private Integer reputation;

    UserReputation(String name, Integer reputation) {
        this.name = name;
        this.reputation = reputation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public String getName() {
        return name;
    }

    public Integer getReputation() {
        return reputation;
    }
}
