package com.zjgsu.forum.module.security.repository;

import com.zjgsu.forum.module.security.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/29
 * Time: 22:03
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission,Integer> {

    @Query(value = "select p from Permission p,RolePermission rp,Role r,AdminUser au where " + "p.id = rp.permissionId and rp.roleId = r.id and r.id = au.id = ?1")
    List<Permission> findByUserId(Integer userId);

    List<Permission> findByPid(Integer pid);
}
