package com.zjgsu.forum.web.front;

import com.zjgsu.forum.config.SiteConfig;
import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.util.CookieHelper;
import com.zjgsu.forum.module.es.model.TopicIndex;
import com.zjgsu.forum.module.es.service.TopicSearchService;
import com.zjgsu.forum.module.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qianshu on 2018/7/5.
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private TagService tagService;
    @Autowired
    private TopicSearchService topicSearchService;

    @GetMapping("/")
    public String index(String tab, Integer p, Model model){
        model.addAttribute("p",p);
        model.addAttribute("tab",tab);
        return "front/index";
    }

    @GetMapping("/search")
    public String search(String keyword, @RequestParam(defaultValue = "1") Integer pageNo, Model model) {
        Assert.notNull(keyword, "请输入关键词");
        Page<TopicIndex> page = topicSearchService.query(keyword, pageNo, siteConfig.getPageSize());
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        return "front/search";
    }

    @GetMapping("/tags")
    public String tags(@RequestParam(defaultValue = "1") Integer p, Model model) {
        model.addAttribute("page", tagService.page(p, siteConfig.getPageSize()));
        return "front/tag/list";
    }

    @GetMapping("/top100")
    public String top100(){
        return "front/top100";
    }

    @GetMapping("/login")
    public String toLogin(String s, Model model) {
        model.addAttribute("s", s);
        return "front/login";
    }

    @GetMapping("/register")
    public String toRegister(){
        return "front/register";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        CookieHelper.clearCookieByName(request, response, siteConfig.getCookie().getUserName(),
                siteConfig.getCookie().getDomain(), "/");
        return redirect("/");
    }

}
