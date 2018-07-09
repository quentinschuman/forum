package com.zjgsu.forum.web.admin;

import com.zjgsu.forum.config.SiteConfig;
import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by qianshu on 2018/7/9.
 */
@Controller
@RequestMapping("/admin/user")
public class UserAdminController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private SiteConfig siteConfig;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer p, Model model) {
        model.addAttribute("page", userService.pageUser(p, siteConfig.getPageSize()));
        return "admin/user/list";
    }

    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "admin/user/edit";
    }

    @GetMapping("/delete")
    @ResponseBody
    public Result delete(Integer id) {
        userService.deleteById(id);
        return Result.success();
    }
}
