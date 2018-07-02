package com.zjgsu.forum.config;

import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.module.security.model.AdminUser;
import com.zjgsu.forum.module.security.model.Permission;
import org.springframework.stereotype.Component;

/**
 * Created by qianshu on 2018/7/2.
 */
@Component
public class SecurityConfig extends BaseController {

    public boolean hasRole(String role){
        AdminUser adminUser = getAdminUser();
        return adminUser != null && adminUser.getRole().getName().equalsIgnoreCase(role);
    }

    public boolean hasPermission(String permission){
        AdminUser adminUser = getAdminUser();
        if (adminUser == null || adminUser.getPermissions() == null)
            return false;
        for (Permission permission1 : adminUser.getPermissions()){
            if (permission1.getValue().equalsIgnoreCase(permission))
                return true;
        }
        return false;
    }
}
