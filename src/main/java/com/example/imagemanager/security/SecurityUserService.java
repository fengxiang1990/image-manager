package com.example.imagemanager.security;

import com.example.imagemanager.admin.model.SysUserModel;
import com.example.imagemanager.admin.model.UserPermissionResult;
import com.example.imagemanager.admin.service.SysUserPermissionService;
import com.example.imagemanager.admin.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityUserService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUserService.class);

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserPermissionService sysUserPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            SysUserModel sysUserModel = sysUserService.getUserByUserName(username);
            if(sysUserModel == null){
                throw new UsernameNotFoundException("Username: " + username + " not found");
            }
            //用户具备的权限
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            UserPermissionResult userPermissionResult = sysUserPermissionService.getPermissionByUid(sysUserModel.getId());
            if(userPermissionResult.getSysRole()!=null){
                if(userPermissionResult.getSysRole().getPermissions() != null){
                    userPermissionResult.getSysRole().getPermissions().forEach(sysPermission -> {
                        grantedAuthorities.add(new SimpleGrantedAuthority(sysPermission.getCode()+""));
                    });
                }
                grantedAuthorities.add(new SimpleGrantedAuthority(userPermissionResult.getSysRole().getName()));
            }
            SecurityUserDetails userDetail = new SecurityUserDetails(sysUserModel.getId(),username,sysUserModel.getPassword(),grantedAuthorities);
            LOGGER.info("用户信息：{}", userDetail);
            return userDetail;
        }catch (UsernameNotFoundException e){
            String msg = "Username: " + username + " not found";
            LOGGER.error(msg, e);
            throw new UsernameNotFoundException(msg);
        }
    }
}
