package com.zjgsu.forum.web.admin;

import com.zjgsu.forum.config.SiteConfig;
import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.module.topic.model.Topic;
import com.zjgsu.forum.module.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by qianshu on 2018/7/9.
 */
@Controller
@RequestMapping("/admin/topic")
public class TopicAdminController extends BaseController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private SiteConfig siteConfig;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer p, Model model) {
        model.addAttribute("page", topicService.findAllForAdmin(p, siteConfig.getPageSize()));
        return "admin/topic/list";
    }

    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        Topic topic = topicService.findById(id);
        Assert.notNull(topic, "话题不存在");

        model.addAttribute("topic", topic);
        return "admin/topic/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public Result update(Integer id, String title, String content) {
        Topic topic = topicService.findById(id);
        topic.setTitle(title);
        topic.setContent(content);
        topic.setModifyTime(new Date());
        topicService.save(topic);

        return Result.success();
    }

    @GetMapping("/delete")
    @ResponseBody
    public Result delete(Integer id) {
        // delete topic
        topicService.deleteById(id, getAdminUser().getId());
        return Result.success();
    }

    @GetMapping("/good")
    @ResponseBody
    public Result good(Integer id) {
        Topic topic = topicService.findById(id);
        topic.setGood(!topic.getGood());
        topicService.save(topic);
        return Result.success();
    }

    @GetMapping("/top")
    @ResponseBody
    public Result top(Integer id) {
        Topic topic = topicService.findById(id);
        topic.setTop(!topic.getTop());
        topicService.save(topic);
        return Result.success();
    }
}
