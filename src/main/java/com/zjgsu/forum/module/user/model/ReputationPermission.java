package com.zjgsu.forum.module.user.model;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/26
 * Time: 21:37
 */
public enum ReputationPermission {

    DELETE_TOPIC("删除话题",5000),
    DELETE_COMMENT("删除评论",3000),
    EDIT_TOPIC("编辑话题",200),
    EDIT_COMMENT("编辑评论",200),
    VOTE_TOPIC("给话题投票",15),
    VOTE_COMMENT("给评论投票",15),;

    private String name;
    private Integer reputation;
    ReputationPermission(String name, Integer reputation) {
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
