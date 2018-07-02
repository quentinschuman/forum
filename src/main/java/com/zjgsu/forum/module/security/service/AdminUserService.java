package com.zjgsu.forum.module.security.service;

import com.zjgsu.forum.module.security.model.AdminUser;
import com.zjgsu.forum.module.security.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/29
 * Time: 22:32
 */
@Service
@Transactional
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Page<AdminUser> page(Integer pageNo,Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo-1,pageSize,new Sort(Sort.Direction.DESC,"inTime"));
        return adminUserRepository.findAll(pageable);
    }

    public AdminUser findByToken(String token) {
        return adminUserRepository.findByToken(token);
    }
    public AdminUser findOne(Integer id){
        return adminUserRepository.findById(id).get();
    }

    public AdminUser save(AdminUser adminUser){
        adminUser = adminUserRepository.save(adminUser);
        this.deleteAllRedisAdminUser();
        return adminUser;
    }

    public AdminUser findByUsername(String username){
        return adminUserRepository.findByUsername(username);
    }

    public void deleteById(Integer id){
        AdminUser adminUser = this.findOne(id);
        stringRedisTemplate.delete("admin_" + adminUser.getToken());
        adminUserRepository.delete(adminUser);
    }

    public void deleteAllRedisAdminUser(){
        List<AdminUser> adminUsers = adminUserRepository.findAll();
        adminUsers.forEach(adminUser -> stringRedisTemplate.delete("admin_" + adminUser.getToken()));
    }
}
