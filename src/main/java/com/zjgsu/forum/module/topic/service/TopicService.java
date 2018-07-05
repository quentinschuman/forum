package com.zjgsu.forum.module.topic.service;

import com.google.common.collect.Lists;
import com.zjgsu.forum.core.util.JsonUtil;
import com.zjgsu.forum.module.collect.service.CollectService;
import com.zjgsu.forum.module.comment.model.Comment;
import com.zjgsu.forum.module.comment.service.CommentService;
import com.zjgsu.forum.module.es.service.TopicSearchService;
import com.zjgsu.forum.module.log.model.LogEventEnum;
import com.zjgsu.forum.module.log.model.LogTargetEnum;
import com.zjgsu.forum.module.log.service.LogService;
import com.zjgsu.forum.module.notification.model.NotificationEnum;
import com.zjgsu.forum.module.notification.service.NotificationService;
import com.zjgsu.forum.module.tag.model.Tag;
import com.zjgsu.forum.module.tag.service.TagService;
import com.zjgsu.forum.module.topic.model.Topic;
import com.zjgsu.forum.module.topic.model.VoteAction;
import com.zjgsu.forum.module.topic.repository.TopicRepository;
import com.zjgsu.forum.module.user.model.User;
import com.zjgsu.forum.module.user.model.UserReputation;
import com.zjgsu.forum.module.user.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/26
 * Time: 22:51
 */
@Service
@Transactional
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TopicTagService topicTagService;
    @Autowired
    private TagService tagService;
    @Autowired
    private LogService logService;
    @Autowired
    private UserService userService;
    @Autowired
    private TopicSearchService topicSearchService;

    public Topic createTopic(String title, String content, String tag, User user) {
        Topic topic = new Topic();
        topic.setTitle(Jsoup.clean(title, Whitelist.none()));
        topic.setContent(Jsoup.clean(content, Whitelist.relaxed()));
        topic.setInTime(new Date());
        topic.setView(0);
        topic.setUserId(user.getId());
        topic.setCommentCount(0);
        topic.setGood(false);
        topic.setTop(false);
        topic.setUp(0);
        topic.setDown(0);
        topic.setUpIds("");
        topic.setDownIds("");
        topic.setTag(Jsoup.clean(tag, Whitelist.none()));
        topic.setWeight(0.0);
        this.save(topic);
        // 处理标签
        topicTagService.deleteByTopicId(topic.getId());
        List<Tag> tagList = tagService.save(tag.split(","));
        topicTagService.save(tagList, topic.getId());
        // 日志
        logService.save(LogEventEnum.CREATE_TOPIC, user.getId(), LogTargetEnum.TOPIC.name(), topic.getId(),
                null, JsonUtil.objectToJson(topic), topic);
        // 索引话题
        topicSearchService.indexed(topic, user.getUsername());
        return topic;
    }

    public Topic updateTopic(Topic oldTopic, Topic topic, User user) {
        this.save(topic);
        // 处理标签
        topicTagService.deleteByTopicId(topic.getId());
        List<Tag> tagList = tagService.save(topic.getTag().split(","));
        topicTagService.save(tagList, topic.getId());
        // 日志
        logService.save(LogEventEnum.EDIT_TOPIC, user.getId(), LogTargetEnum.TOPIC.name(), topic.getId(),
                JsonUtil.objectToJson(oldTopic), JsonUtil.objectToJson(topic), topic);
        // 索引话题
        topicSearchService.indexed(topic, user.getUsername());
        return topic;
    }

    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    public Topic findById(Integer id) {
        return topicRepository.findById(id).get();
    }

    public void deleteById(Integer id, Integer userId) {
        Topic topic = findById(id);
        if (topic != null) {
            //删除收藏这个话题的记录
            collectService.deleteByTopicId(id);
            //删除通知里提到的话题
            notificationService.deleteByTopic(topic);
            //删除话题下面的评论
            commentService.deleteByTopic(topic);
            // 添加日志
            logService.save(LogEventEnum.DELETE_TOPIC, userId, LogTargetEnum.TOPIC.name(), topic.getId(),
                    JsonUtil.objectToJson(topic), null, topic);
            //删除话题
            topicRepository.delete(topic);
            //删除索引
            topicSearchService.deleteById(id);
        }
    }

    /**
     * 删除用户的所有话题，这里不做日志记录了，
     * 这方法只会在后台被管理员删除用户时调用，
     * 同时也会删除这个用户的所有日志，所以不用做日志记录
     *
     * @param userId
     */
    public void deleteByUserId(Integer userId) {
        topicRepository.deleteByUserId(userId);
    }

    public Page<Map> page(Integer pageNo, Integer pageSize, String tab) {
        Sort sort = new Sort(Sort.Direction.DESC, "top", "weight", "inTime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        if (tab.equalsIgnoreCase("good")) {
            return topicRepository.findByGood(true, pageable);
        } else if (tab.equalsIgnoreCase("newest")) {
            sort = new Sort(Sort.Direction.DESC, "inTime", "weight");
            pageable = PageRequest.of(pageNo - 1, pageSize, sort);
            return topicRepository.findTopics(pageable);
        } else if (tab.equalsIgnoreCase("noanswer")) {
            return topicRepository.findByCommentCount(0, pageable);
        } else {
            return topicRepository.findTopics(pageable);
        }
    }

    public Page<Map> pageByTagId(Integer pageNo, Integer pageSize, Integer tagId) {
        Sort sort = new Sort(Sort.Direction.DESC, "weight", "inTime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return topicRepository.findTopicsByTagId(tagId, pageable);
    }

    /**
     * 查询用户的话题
     *
     * @param p
     * @param size
     * @param user
     * @return
     */
    public Page<Map> findByUser(int p, int size, User user) {
        Sort sort = new Sort(Sort.Direction.DESC, "inTime");
        Pageable pageable = PageRequest.of(p - 1, size, sort);
        return topicRepository.findByUserId(user.getId(), pageable);
    }

    /**
     * 根据标题查询话题（防止发布重复话题）
     *
     * @param title
     * @return
     */
    public Topic findByTitle(String title) {
        return topicRepository.findByTitle(title);
    }

    public Map<String, Object> vote(Integer userId, Topic topic, String action) {
        Map<String, Object> map = new HashMap<>();
        List<String> upIds = new ArrayList<>();
        List<String> downIds = new ArrayList<>();
        LogEventEnum logEventEnum = null;
        NotificationEnum notificationEnum = null;
        User topicUser = userService.findById(topic.getUserId());
        if (!StringUtils.isEmpty(topic.getUpIds())) {
            upIds = Lists.newArrayList(topic.getUpIds().split(","));
        }
        if (!StringUtils.isEmpty(topic.getDownIds())) {
            downIds = Lists.newArrayList(topic.getDownIds().split(","));
        }
        if (action.equals(VoteAction.UP.name())) {
            logEventEnum = LogEventEnum.UP_TOPIC;
            notificationEnum = NotificationEnum.UP_TOPIC;
            topicUser.setReputation(topicUser.getReputation() + UserReputation.UP_TOPIC.getReputation());
            // 如果点踩ID里有，就删除，并将down - 1
            if (downIds.contains(String.valueOf(userId))) {
                topic.setDown(topic.getDown() - 1);
                downIds.remove(String.valueOf(userId));
            }
            // 如果点赞ID里没有，就添加上，并将up + 1
            if (!upIds.contains(String.valueOf(userId))) {
                upIds.add(String.valueOf(userId));
                topic.setUp(topic.getUp() + 1);
                map.put("isUp", true);
                map.put("isDown", false);
            } else {
                upIds.remove(String.valueOf(userId));
                topic.setUp(topic.getUp() - 1);
                map.put("isUp", false);
                map.put("isDown", false);
            }
        } else if (action.equals(VoteAction.DOWN.name())) {
            logEventEnum = LogEventEnum.DOWN_TOPIC;
            notificationEnum = NotificationEnum.DOWN_TOPIC;
            topicUser.setReputation(topicUser.getReputation() + UserReputation.DOWN_TOPIC.getReputation());
            // 如果点赞ID里有，就删除，并将up - 1
            if (upIds.contains(String.valueOf(userId))) {
                topic.setUp(topic.getUp() - 1);
                upIds.remove(String.valueOf(userId));
            }
            // 如果点踩ID里没有，就添加上，并将down + 1
            if (!downIds.contains(String.valueOf(userId))) {
                downIds.add(String.valueOf(userId));
                topic.setDown(topic.getDown() + 1);
                map.put("isUp", false);
                map.put("isDown", true);
            } else {
                downIds.remove(String.valueOf(userId));
                topic.setDown(topic.getDown() - 1);
                map.put("isUp", false);
                map.put("isDown", false);
            }
        }
        topic.setUpIds(StringUtils.collectionToCommaDelimitedString(upIds));
        topic.setDownIds(StringUtils.collectionToCommaDelimitedString(downIds));
        topic = save(topic);
        map.put("up", topic.getUp());
        map.put("down", topic.getDown());
        // 更新用户声望
        userService.save(topicUser);
        // 计算weight
        this.weight(topic, null);
        // 发送通知
        notificationService.sendNotification(userId, topic.getUserId(), notificationEnum, topic.getId(), null);
        // 记录日志
        logService.save(logEventEnum, userId, LogTargetEnum.TOPIC.name(), topic.getId(), null, null, topic);
        return map;
    }

    public Page<Map> findAllForAdmin(Integer pageNo, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "top", "weight", "inTime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return topicRepository.findAllForAdmin(pageable);
    }

    // 计算话题的weight
    public void weight(Topic topic, List<Comment> comments) {
        if (comments == null) {
            comments = commentService.findByTopicId(topic.getId());
        }
        double Qview = Math.log10(topic.getView());
        int Qanswer = comments.size();
        int Qscore = topic.getUp() - topic.getDown();
        Optional<Integer> Ascore = Optional.of(0);
        if (Qanswer > 0) {
            Ascore = comments.stream()
                    .map(comment -> comment.getUp() - comment.getDown())
                    .reduce(Integer::sum);
        }
        long Qage = topic.getInTime().getTime();
        long Qupdated = topic.getLastCommentTime() == null ? 0 : topic.getLastCommentTime().getTime();
        double weightScore = ((Qview * 4) + (Qanswer * Qscore) / 5 + Ascore.get()) / Math.pow(((Qage + 1) - (Qage - Qupdated) / 2), 1.5);
        topic.setWeight(weightScore);
        save(topic);
    }

    /**
     * 复杂sql查询demo
     * @return
     */
    public List<Topic> findByTest(Integer userId, List<String> tags){
        Specification<Topic> specification = (Specification<Topic>) (root, query, cb) -> {
            List<Predicate> pList = new ArrayList<>();
            if(userId != null) {
                Predicate predicate = cb.equal(root.get("userId"), userId);
                pList.add(predicate);
            }
            if(tags.size() > 0) {
                Predicate predicate = cb.not(cb.in(root.get("tag")).value(tags));
                pList.add(predicate);
            }
            return cb.and(pList.toArray(new Predicate[0]));
        };
        return topicRepository.findAll(specification);
    }

}
