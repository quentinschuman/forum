package com.zjgsu.forum.web.api;

import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.core.exception.ApiException;
import com.zjgsu.forum.module.notification.service.NotificationService;
import com.zjgsu.forum.module.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qianshu on 2018/7/7.
 */
@RestController
@RequestMapping("/api/notification")
public class NotificationApiController extends BaseController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notRead")
    public Result notRead() throws ApiException{
        User user = getApiUser();
        return Result.success(notificationService.countByTargetUserAndIsRead(user,false));
    }

}
