package com.zjgsu.forum.web.front;

import com.zjgsu.forum.core.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by qianshu on 2018/7/6.
 */
@Controller
@RequestMapping("/notification")
public class NotificationController extends BaseController {

    @GetMapping("/list")
    public String list(Integer p, Model model){
        model.addAttribute("p",p);
        return "/front/notification/list";
    }
}
