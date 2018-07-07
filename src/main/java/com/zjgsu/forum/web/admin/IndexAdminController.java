package com.zjgsu.forum.web.admin;

import com.zjgsu.forum.config.SiteConfig;
import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.core.exception.ApiAssert;
import com.zjgsu.forum.core.util.CookieHelper;
import com.zjgsu.forum.core.util.security.Base64Helper;
import com.zjgsu.forum.core.util.security.crypto.BCryptPasswordEncoder;
import com.zjgsu.forum.module.es.service.TagSearchService;
import com.zjgsu.forum.module.es.service.TopicSearchService;
import com.zjgsu.forum.module.security.model.AdminUser;
import com.zjgsu.forum.module.security.model.Permission;
import com.zjgsu.forum.module.security.model.Role;
import com.zjgsu.forum.module.security.service.AdminUserService;
import com.zjgsu.forum.module.security.service.PermissionService;
import com.zjgsu.forum.module.security.service.RoleService;
import com.zjgsu.forum.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by qianshu on 2018/7/7.
 */
@Controller
@RequestMapping("/admin")
public class IndexAdminController extends BaseController {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private TopicSearchService topicSearchService;
    @Autowired
    private TagSearchService tagSearchService;

    @GetMapping("/login")
    public String login() {
        if (getAdminUser() != null)
            return redirect("/admin/index");
        return "admin/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(String username, String password, String code, HttpServletResponse response, HttpSession session) {
        ApiAssert.notEmpty(username, "用户名不能为空");
        ApiAssert.notEmpty(password, "密码不能为空");
        ApiAssert.notEmpty(code, "验证码不能为空");

        String index_code = (String) session.getAttribute("index_code");
        ApiAssert.isTrue(code.equalsIgnoreCase(index_code), "验证码不正确");

        AdminUser adminUser = adminUserService.findByUsername(username);
        ApiAssert.notNull(adminUser, "用户不存在");
        ApiAssert.isTrue(new BCryptPasswordEncoder().matches(password, adminUser.getPassword()), "密码不正确");

        // 查询用户的角色权限封装进adminuser里
        Role role = roleService.findById(adminUser.getRoleId());
        List<Permission> permissions = permissionService.findByUserId(adminUser.getId());
        adminUser.setRole(role);
        adminUser.setPermissions(permissions);
        session.setAttribute("admin_user", adminUser);
        CookieHelper.addCookie(
                response,
                siteConfig.getCookie().getDomain(),
                "/admin/",
                siteConfig.getCookie().getAdminUserName(),
                Base64Helper.encode(adminUser.getToken().getBytes()),
                siteConfig.getCookie().getAdminUserMaxAge() * 24 * 60 * 60,
                true,
                false
        );
        return Result.success();
    }
}