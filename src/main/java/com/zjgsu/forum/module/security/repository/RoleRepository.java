package com.zjgsu.forum.module.security.repository;

import com.zjgsu.forum.module.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/29
 * Time: 22:01
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
}
