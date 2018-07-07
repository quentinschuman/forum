package com.zjgsu.forum.web.api;

import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.core.exception.ApiAssert;
import com.zjgsu.forum.module.collect.model.Collect;
import com.zjgsu.forum.module.collect.service.CollectService;
import com.zjgsu.forum.module.topic.model.Topic;
import com.zjgsu.forum.module.topic.service.TopicService;
import com.zjgsu.forum.module.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/7/6
 * Time: 22:37
 */
@RestController
@RequestMapping("/api/collect")
public class CollectApiController extends BaseController {

    @Autowired
    private CollectService collectService;
    @Autowired
    private TopicService topicService;

    @GetMapping("/add")
    public Result add(Integer topicId){
        User user = getApiUser();
        Topic topic = topicService.findById(topicId);
        ApiAssert.notNull(topic,"话题不存在");
        Collect collect = collectService.findByUserIdAndTopicId(getUser().getId(),topicId);
        ApiAssert.isNull(collect,"你已经收藏了这个话题");
        collectService.createCollect(topic,user.getId());
        return Result.success(collectService.countByTopicId(topicId));
    }

    @GetMapping("/delete")
    public Result delete(Integer topicId){
        User user = getApiUser();
        Collect collect = collectService.findByUserIdAndTopicId(getUser().getId(),topicId);
        ApiAssert.isNull(collect,"你还没收藏这个话题");
        collectService.deleteById(collect.getId());
        return Result.success(collectService.countByTopicId(topicId));
    }
}
