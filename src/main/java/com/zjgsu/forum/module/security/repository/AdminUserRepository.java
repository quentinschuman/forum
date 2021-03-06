package com.zjgsu.forum.module.security.repository;

import com.zjgsu.forum.module.security.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/29
 * Time: 22:08
 */
@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser,Integer> {

    AdminUser findByToken(String token);
    AdminUser findByUsername(String username);
}
