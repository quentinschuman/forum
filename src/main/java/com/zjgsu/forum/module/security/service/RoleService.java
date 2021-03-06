package com.zjgsu.forum.module.security.service;

import com.zjgsu.forum.module.security.model.Role;
import com.zjgsu.forum.module.security.model.RolePermission;
import com.zjgsu.forum.module.security.repository.AdminUserRepository;
import com.zjgsu.forum.module.security.repository.RolePermissionRepository;
import com.zjgsu.forum.module.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/29
 * Time: 22:18
 */
@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private AdminUserService adminUserService;

    public Role findById(Integer id){
        return roleRepository.findById(id).get();
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public void save(Integer id,String name,Integer[] permissionIds){
        Role role = new Role();
        if (id != null){
            role = findById(id);
        }
        role.setName(name);
        role = roleRepository.save(role);
        rolePermissionService.deleteRoleId(role.getId());
        if (permissionIds.length > 0){
            List<RolePermission> list = new ArrayList<>();
            for (Integer permissionId : permissionIds){
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermissionId(permissionId);
                list.add(rolePermission);
            }
            rolePermissionService.save(list);
        }
        adminUserService.deleteAllRedisAdminUser();
    }

    public void delete(Integer id){
        rolePermissionService.deleteRoleId(id);
        roleRepository.deleteById(id);
        adminUserService.deleteAllRedisAdminUser();
    }
}
