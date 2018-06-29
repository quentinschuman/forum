package com.zjgsu.forum.module.security.repository;

import com.zjgsu.forum.module.security.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/29
 * Time: 22:02
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission,Integer> {
    void deleteRoleId(Integer id);
}
