package com.zjgsu.forum.web.admin;

import com.zjgsu.forum.config.SiteConfig;
import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.module.comment.model.Comment;
import com.zjgsu.forum.module.comment.service.CommentService;
import com.zjgsu.forum.module.topic.model.Topic;
import com.zjgsu.forum.module.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by qianshu on 2018/7/7.
 */
@Controller
@RequestMapping("/admin/comment")
public class CommentAdminController extends BaseController {

    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TopicService topicService;

    @GetMapping("/list")
    public String list(Integer p, Model model){
        Page<Map> page = commentService.findAllForAdmin(p == null ? 1 : p,siteConfig.getPageSize());
        model.addAttribute("page",page);
        return "admin/comment/list";
    }

    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        Comment comment = commentService.findById(id);
        Topic topic = topicService.findById(comment.getTopicId());
        model.addAttribute("comment", comment);
        model.addAttribute("topic", topic);
        return "admin/comment/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public Result update(Integer id, String content) {
        Comment comment = commentService.findById(id);
        Assert.notNull(comment, "评论不存在");

        comment.setContent(content);
        commentService.save(comment);
        return Result.success();
    }

    @GetMapping("/delete")
    @ResponseBody
    public Result delete(Integer id) {
        commentService.delete(id, getAdminUser().getId());
        return Result.success();
    }

}
