package com.zjgsu.forum.web.api;

import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.core.exception.ApiAssert;
import com.zjgsu.forum.core.util.EnumUtil;
import com.zjgsu.forum.module.comment.model.Comment;
import com.zjgsu.forum.module.comment.service.CommentService;
import com.zjgsu.forum.module.topic.model.Topic;
import com.zjgsu.forum.module.topic.model.VoteAction;
import com.zjgsu.forum.module.topic.service.TopicService;
import com.zjgsu.forum.module.user.model.ReputationPermission;
import com.zjgsu.forum.module.user.model.User;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/7/6
 * Time: 22:47
 */
@RestController
@RequestMapping("/api/comment")
public class CommentApiController extends BaseController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    public Result save(Integer topicId,Integer commentId,String content){
        User user = getApiUser();
        ApiAssert.notTrue(user.getBlock(),"你的账户已经被禁用，不能进行此项操作");
        ApiAssert.notEmpty(content,"评论内容不能为空");
        ApiAssert.notNull(topicId,"话题ID不存在");
        Topic topic = topicService.findById(topicId);
        ApiAssert.notNull(topic,"回复的话题不存在");
        Comment comment = commentService.createComment(user.getId(),topic,commentId,content);
        return Result.success(comment);
    }

    @PostMapping("/edit")
    public Result edit(Integer id,String content){
        User user = getApiUser();
        ApiAssert.isTrue(user.getReputation() >= ReputationPermission.VOTE_COMMENT.getReputation(),"声望太低，不能进行这项操作");
        ApiAssert.notEmpty(content,"评论内容不能为空");
        Comment comment = commentService.findById(id);
        Comment oldComment = comment;
        comment.setContent(Jsoup.clean(content, Whitelist.relaxed()));
        commentService.save(comment);
        Topic topic = topicService.findById(comment.getTopicId());
        comment = commentService.update(topic,oldComment,comment,user.getId());
        return Result.success(comment);
    }

    @GetMapping("/{id}/vote")
    public Result vote(@PathVariable Integer id,String action){
        User user = getApiUser();
        ApiAssert.isTrue(user.getReputation() >= ReputationPermission.VOTE_COMMENT.getReputation(),"声望太低，不能进行这项操作");
        Comment comment = commentService.findById(id);
        ApiAssert.notNull(comment,"评论不存在");
        ApiAssert.notTrue(user.getId().equals(comment.getUserId()),"不能给自己的评论投票");
        ApiAssert.isTrue(EnumUtil.isDefined(VoteAction.values(),action),"参数错误");
        Map<String,Object> map = commentService.vote(user.getId(),comment,action);
        return Result.success(map);
    }
}
