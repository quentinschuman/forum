package com.zjgsu.forum.web.api;

import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.base.BaseEntity;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.core.exception.ApiAssert;
import com.zjgsu.forum.core.util.EnumUtil;
import com.zjgsu.forum.module.collect.service.CollectService;
import com.zjgsu.forum.module.tag.model.Tag;
import com.zjgsu.forum.module.tag.service.TagService;
import com.zjgsu.forum.module.topic.model.Topic;
import com.zjgsu.forum.module.topic.model.VoteAction;
import com.zjgsu.forum.module.topic.service.TopicService;
import com.zjgsu.forum.module.user.model.ReputationPermission;
import com.zjgsu.forum.module.user.model.User;
import com.zjgsu.forum.module.user.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya at 2018/4/12
 */
@RestController
@RequestMapping("/api/topic")
public class TopicApiController extends BaseController {

  @Autowired
  private TopicService topicService;
  @Autowired
  private BaseEntity baseEntity;
  @Autowired
  private CollectService collectService;
  @Autowired
  private UserService userService;
  @Autowired
  private TagService tagService;

  @GetMapping("/{id}")
  public Result detail(@PathVariable Integer id) {
    Map<String, Object> map = new HashMap<>();

    Topic topic = topicService.findById(id);
    ApiAssert.notNull(topic, "话题不存在");

    // 浏览量+1
    topic.setView(topic.getView() + 1);
    topicService.save(topic);// 更新话题数据
    map.put("topic", topic);
    // 查询是否收藏过
    User user = baseEntity.getUser();
    if (user != null) {
      map.put("collect", collectService.findByUserIdAndTopicId(getUser().getId(), id));
    } else {
      map.put("collect", null);
    }
    // 查询这个话题被收藏的个数
    map.put("collectCount", collectService.countByTopicId(id));
    map.put("topicUser", userService.findById(topic.getUserId()));
    // 查询话题的标签
    List<Tag> tags = tagService.findByTopicId(id);
    map.put("tags", tags);
    return Result.success(map);
  }

  /**
   * 保存话题
   * @param title 话题标题
   * @param content 话题内容
   * @param tag 话题标签，格式是 , 隔开的字符串（英文下的逗号）
   * @return
   */
  @PostMapping("/save")
  public Result save(String title, String content, String tag) {
    User user = getUser();

    ApiAssert.notTrue(user.getBlock(), "你的帐户已经被禁用了，不能进行此项操作");

    ApiAssert.notEmpty(title, "请输入标题");
//    ApiAssert.notEmpty(content, "请输入内容");
    ApiAssert.notEmpty(tag, "标签不能为空");
    ApiAssert.notTrue(topicService.findByTitle(title) != null, "话题标题已经存在");

    Topic topic = topicService.createTopic(title, content, tag, user);

    return Result.success(topic);
  }

  /**
   * 话题编辑
   * @param id 话题ID
   * @param title 话题标题
   * @param content 话题内容
   * @param tag 话题标签，格式是 , 隔开的字符串（英文下的逗号）
   * @return
   */
  @PostMapping("/edit")
  public Result update(Integer id, String title, String content, String tag) {
    User user = getApiUser();
    ApiAssert.isTrue(user.getReputation() >= ReputationPermission.EDIT_TOPIC.getReputation(), "声望太低，不能进行这项操作");

    ApiAssert.notEmpty(title, "请输入标题");
//    ApiAssert.notEmpty(content, "请输入内容");
    ApiAssert.notEmpty(tag, "标签不能为空");

    Topic oldTopic = topicService.findById(id);
    ApiAssert.isTrue(oldTopic.getUserId().equals(user.getId()), "不能修改别人的话题");

    Topic topic = oldTopic;
    topic.setTitle(Jsoup.clean(title, Whitelist.none()));
    topic.setContent(Jsoup.clean(content, Whitelist.relaxed()));
    topic.setTag(Jsoup.clean(tag, Whitelist.none()));

    topic = topicService.updateTopic(oldTopic, topic, user);

    return Result.success(topic);
  }

  /**
   * 给话题投票
   * @param id 话题ID
   * @param action 赞成或者反对，只能填：UP, DOWN
   * @return
   */
  @GetMapping("/{id}/vote")
  public Result vote(@PathVariable Integer id, String action) {
    User user = getApiUser();

    ApiAssert.isTrue(user.getReputation() >= ReputationPermission.VOTE_TOPIC.getReputation(), "声望太低，不能进行这项操作");

    Topic topic = topicService.findById(id);

    ApiAssert.notNull(topic, "话题不存在");
    ApiAssert.notTrue(user.getId().equals(topic.getUserId()), "不能给自己的话题投票");

    ApiAssert.isTrue(EnumUtil.isDefined(VoteAction.values(), action), "参数错误");

    Map<String, Object> map = topicService.vote(user.getId(), topic, action);
    return Result.success(map);
  }
}
