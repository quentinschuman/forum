package com.zjgsu.forum.web.front;

import com.zjgsu.forum.config.SiteConfig;
import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.util.identicon.Identicon;
import com.zjgsu.forum.module.log.service.LogService;
import com.zjgsu.forum.module.user.model.User;
import com.zjgsu.forum.module.user.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qianshu on 2018/7/5.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private Identicon identicon;
    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private LogService logService;

    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Model model){
        model.addAttribute("username",username);
        return "front/user/info";
    }

    @GetMapping("/{username}/topics")
    public String topics(@PathVariable String username,Integer p,Model model){
        model.addAttribute("username",username);
        model.addAttribute("p",p);
        return "front/user/topics";
    }

    @GetMapping("/{username}/comments")
    public String comments(@PathVariable String username,Integer p,Model model){
        model.addAttribute("username",username);
        model.addAttribute("p",p);
        return "front/user/comments";
    }

    @GetMapping("/{username}/collects")
    public String collects(@PathVariable String username,Integer p,Model model){
        model.addAttribute("username",username);
        model.addAttribute("p",p);
        return "front/user/collects";
    }

    @GetMapping("/setting/profile")
    public String setting(Model model){
        model.addAttribute("user",getUser());
        return "front/user/setting/profile";
    }

    @PostMapping("/setting/profile")
    public String updateUserInfo(String email, String url, String bio, @RequestParam(defaultValue = "false") Boolean commentEmail,@RequestParam(defaultValue = "false") Boolean replyEmail) throws Exception{
        User user = getUser();
        if (user.getBlock())
            throw new Exception("你的帐户已经被禁用，不能进行此项操作");
//    user.setEmail(email); TODO 还要对邮箱进行验证 另外这个方法将被删除，提到接口Controller里处理
        if (bio != null && bio.trim().length() > 0 )
            user.setBio(Jsoup.clean(bio, Whitelist.none()));
        user.setCommentEmail(commentEmail);
        user.setReplyEmail(replyEmail);
        user.setUrl(url);
        userService.save(user);
        return redirect("/user/"+user.getUsername());
    }

    @GetMapping("/setting/changeAvatar")
    public String changeAvatar(Model model){
        model.addAttribute("user",getUser());
        return "front/user/setting/changeAvatar";
    }

    @GetMapping("/setting/changePassword")
    public String changePassword(){
        return "front/user/setting/changePassword";
    }

    @GetMapping("/setting/accessToken")
    public String accessToken(Model model) {
        model.addAttribute("user", getUser());
        return "/front/user/setting/accessToken";
    }

    @GetMapping("/setting/refreshToken")
    public String refreshToken() {
        User user = getUser();
        userService.save(user);
        return redirect("/user/setting/accessToken");
    }

    @GetMapping("/setting/log")
    public String scoreLog(@RequestParam(defaultValue = "1") Integer p,Model model){
        User user = getUser();
        model.addAttribute("page",logService.findByUserId(p,siteConfig.getPageSize(),user.getId()));
        return "front/user/setting/log";
    }

}
