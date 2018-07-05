package com.zjgsu.forum.web.front;

import com.zjgsu.forum.config.LogEventConfig;
import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.exception.ApiAssert;
import com.zjgsu.forum.core.util.FreemarkerUtil;
import com.zjgsu.forum.module.comment.model.Comment;
import com.zjgsu.forum.module.comment.service.CommentService;
import com.zjgsu.forum.module.log.service.LogService;
import com.zjgsu.forum.module.topic.service.TopicService;
import com.zjgsu.forum.module.user.model.ReputationPermission;
import com.zjgsu.forum.module.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by qianshu on 2018/7/5.
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private CommentService commentService;
    @Autowired
    FreemarkerUtil freemarkerUtil;
    @Autowired
    LogEventConfig logEventConfig;
    @Autowired
    LogService logService;

    @GetMapping("/edit")
    public String edit(Integer id, Model model){
        User user = getUser();
        ApiAssert.isTrue(user.getReputation() >= ReputationPermission.EDIT_COMMENT.getReputation(),"声望太低，不能进行这项操作");
        Comment comment = commentService.findById(id);
        model.addAttribute("topic",topicService.findById(comment.getTopicId()));
        model.addAttribute("comment",comment);
        return "front/comment/edit";
    }

    @GetMapping("/delete")
    public String delete(Integer id){
        User user = getUser();
        ApiAssert.isTrue(user.getReputation() >= ReputationPermission.DELETE_COMMENT.getReputation(),"声望太低，不能进行这项操作");
        Comment comment = commentService.findById(id);
        Assert.notNull(comment, "评论不存在");
        Integer topicId = comment.getTopicId();
        commentService.delete(id, getUser().getId());
        return redirect("/topic/" + topicId);
    }

}
