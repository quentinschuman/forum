package com.zjgsu.forum.web.api;

import com.zjgsu.forum.config.SiteConfig;
import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.core.exception.ApiAssert;
import com.zjgsu.forum.core.util.CookieHelper;
import com.zjgsu.forum.core.util.EnumUtil;
import com.zjgsu.forum.core.util.StrUtil;
import com.zjgsu.forum.core.util.identicon.Identicon;
import com.zjgsu.forum.core.util.security.Base64Helper;
import com.zjgsu.forum.core.util.security.crypto.BCryptPasswordEncoder;
import com.zjgsu.forum.module.code.model.CodeEnum;
import com.zjgsu.forum.module.code.service.CodeService;
import com.zjgsu.forum.module.es.model.TopicIndex;
import com.zjgsu.forum.module.es.service.TopicSearchService;
import com.zjgsu.forum.module.tag.service.TagService;
import com.zjgsu.forum.module.topic.model.TopicTab;
import com.zjgsu.forum.module.topic.service.TopicService;
import com.zjgsu.forum.module.user.model.User;
import com.zjgsu.forum.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by qianshu on 2018/7/7.
 */
@RestController
@RequestMapping("/api")
public class IndexApiController extends BaseController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private Identicon identicon;
    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private TopicSearchService topicSearchService;

    @GetMapping("/")
    public Result index(@RequestParam(defaultValue = "1") Integer pageNo,@RequestParam(defaultValue = "") String tab){
        if(!StringUtils.isEmpty(tab)) {
            ApiAssert.isTrue(EnumUtil.isDefined(TopicTab.values(), tab), "参数错误");
        }
        Page<Map> page = topicService.page(pageNo, siteConfig.getPageSize(), tab);
        return Result.success(page);
    }

    @GetMapping("/search")
    public Result search(String keyword, @RequestParam(defaultValue = "1") Integer pageNo) {
        Page<TopicIndex> page = topicSearchService.query(keyword, pageNo, siteConfig.getPageSize());
        return Result.success(page);
    }

    @GetMapping("/tags")
    public Result tags(@RequestParam(defaultValue = "1") Integer pageNo) {
        return Result.success(tagService.page(pageNo, siteConfig.getPageSize()));
    }

    @GetMapping("/top100")
    public Result top100() {
        return Result.success(userService.findByReputation(1, 100));
    }

    @PostMapping("/login")
    public Result login(String username, String password, HttpServletResponse response) {
        ApiAssert.notEmpty(username, "用户名不能为空");
        ApiAssert.notEmpty(password, "密码不能为空");

        User user = userService.findByUsername(username);
        ApiAssert.notNull(user, "用户不存在");
        ApiAssert.notTrue(user.getBlock(), "用户已被禁");

        ApiAssert.isTrue(new BCryptPasswordEncoder().matches(password, user.getPassword()), "密码不正确");

        // 把用户信息写入cookie
        CookieHelper.addCookie(
                response,
                siteConfig.getCookie().getDomain(),
                "/",
                siteConfig.getCookie().getUserName(),
                Base64Helper.encode(user.getToken().getBytes()),
                siteConfig.getCookie().getUserMaxAge() * 24 * 60 * 60,
                true,
                false
        );

        return Result.success();
    }

    @PostMapping("/register")
    public Result register(String username, String password, String email, String emailCode, String code, HttpSession session) {
        String genCaptcha = (String) session.getAttribute("index_code");
        ApiAssert.notEmpty(code, "验证码不能为空");
        ApiAssert.notEmpty(username, "用户名不能为空");
        ApiAssert.notEmpty(password, "密码不能为空");
        ApiAssert.notEmpty(email, "邮箱不能为空");

        ApiAssert.isTrue(genCaptcha.toLowerCase().equals(code.toLowerCase()), "验证码错误");
        ApiAssert.isTrue(StrUtil.check(username, StrUtil.userNameCheck), "用户名不合法");

        User user = userService.findByUsername(username);
        ApiAssert.isNull(user, "用户名已经被注册");

        User user_email = userService.findByEmail(email);
        ApiAssert.isNull(user_email, "邮箱已经被使用");

        int validateResult = codeService.validateCode(email, emailCode, CodeEnum.EMAIL);
        ApiAssert.notTrue(validateResult == 1, "邮箱验证码不正确");
        ApiAssert.notTrue(validateResult == 2, "邮箱验证码已过期");
        ApiAssert.notTrue(validateResult == 3, "邮箱验证码已经被使用");

        // generator avatar
        String avatar = identicon.generator(username);

        // 创建用户
        userService.createUser(username, password, email, avatar, null, null);

        return Result.success();
    }

}
