package com.example.imagemanager.admin.service;

import com.example.imagemanager.admin.dao.UserPermissionMapper;
import com.example.imagemanager.admin.model.*;
import com.example.imagemanager.cache.SysUserCache;
import com.example.imagemanager.utils.JsonUtils;
import com.sun.source.doctree.SeeTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SysUserPermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserPermissionService.class);

    @Autowired
    UserPermissionMapper userPermissionMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public SysRole getSysRoleByUid(String uid){
        return userPermissionMapper.getSysRoleByUid(uid);
    }

    public UserPermissionResult getPermissionByUid(String uid) {
        UserPermissionResult userPermissionResult;
        SysUserCache sysUserCache;
        try {
//            if (Boolean.TRUE.equals(redisTemplate.hasKey(uid))) {
//                sysUserCache = JsonUtils.fromJson((String) redisTemplate.opsForValue().get(uid), SysUserCache.class);
//            } else {
//                sysUserCache = new SysUserCache();
//                redisTemplate.opsForValue().set(uid, JsonUtils.toJson(sysUserCache));
//            }
//            if (sysUserCache.getUserPermissionResult() != null) {
//                LOGGER.info("load permission from redis for uid:" + uid);
//                return sysUserCache.getUserPermissionResult();
//            }
            userPermissionResult = userPermissionMapper.getPermission(uid);
//            sysUserCache.setUserPermissionResult(userPermissionResult);
            LOGGER.info("save permission to redis for uid:" + uid);
//            redisTemplate.opsForValue().set(uid, JsonUtils.toJson(sysUserCache));
        } catch (Exception e) {
            userPermissionResult = userPermissionMapper.getPermission(uid);
        }
        return userPermissionResult;
    }


    public SysRole getSysRole(int roleId) {
        return userPermissionMapper.getSysRole(roleId);
    }

    public List<SysRole> getRoles() {
        return userPermissionMapper.getSysRoles();
    }


    public List<SysPermission> getSysPermission(int roleId) {
        return userPermissionMapper.getRolePermissions(roleId);
    }

    public void addRole(String name, String description) {
        userPermissionMapper.addRole(name, description);
    }

    public void deleteRole(int id) {
        userPermissionMapper.deleteRole(id);
    }

    public List<SysPermission> loadPermission() {
        return userPermissionMapper.getAllPermission();
    }

    public void addPermission(SysPermission permission) {
        userPermissionMapper.addPermission(permission.getCode(), permission.getName()
                , permission.getDescription(), permission.getParent(), permission.getLevel(), permission.getUrl());
    }

    public void deletePermission(int id) {
        userPermissionMapper.deletePermission(id);
    }

    public void updatePermission(SysPermission permission) {
        userPermissionMapper.updatePermission(permission.getId(), permission.getCode(), permission.getName(),
                permission.getDescription(), permission.getParent(), permission.getLevel(), permission.getUrl());
    }

    public List<SysPermission> getRolePermission(int roleId) {
        return userPermissionMapper.getRolePermissions(roleId);
    }

    public void updateRolePerm(RolePermRequest rolePermRequest) {
        //对比传进来的
        userPermissionMapper.deleteRolePerm(rolePermRequest.getRoleId());
        for(int pid : rolePermRequest.getPerms()){
           userPermissionMapper.updateRolePerm(rolePermRequest.getRoleId(),pid);
        }
    }

    public void updateUserRole(UserRoleGrantRequest userRoleGrantRequest) {
        userPermissionMapper.updateUserRole(userRoleGrantRequest.getUid(),userRoleGrantRequest.getRoleId());
    }

    public void updateRole(SysRole sysRole) {
        userPermissionMapper.updateRole(sysRole.getId(),sysRole.getName(),sysRole.getDescription());
    }
}
