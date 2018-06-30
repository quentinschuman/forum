package com.zjgsu.forum.module.security.service;

import com.zjgsu.forum.module.security.model.RolePermission;
import com.zjgsu.forum.module.security.repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/30
 * Time: 9:26
 */
@Service
@Transactional
public class RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    public RolePermission save(RolePermission rolePermission){
        return rolePermissionRepository.save(rolePermission);
    }

    public List<RolePermission> save(List<RolePermission> rolePermissions){
        return rolePermissionRepository.saveAll(rolePermissions);
    }

    public List<RolePermission> findByRoleId(Integer roleId){
        return rolePermissionRepository.findByRoleId(roleId);
    }

    public void deleteByPermissionId(Integer id) {
        rolePermissionRepository.deleteByPermissionId(id);
    }

    public void deleteRoleId(Integer id) {
        rolePermissionRepository.deleteRoleId(id);
    }
}
