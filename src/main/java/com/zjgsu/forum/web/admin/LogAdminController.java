package com.zjgsu.forum.web.admin;

import com.zjgsu.forum.config.SiteConfig;
import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.module.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by qianshu on 2018/7/9.
 */
@Controller
@RequestMapping("/admin/log")
public class LogAdminController extends BaseController {

    @Autowired
    private LogService logService;
    @Autowired
    private SiteConfig siteConfig;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer p, Model model){
        model.addAttribute("page",logService.findAllForAdmin(p,siteConfig.getPageSize()));
        return "admin/log/list";
    }

}
