package com.example.imagemanager.admin.controller;

import com.example.imagemanager.admin.model.*;
import com.example.imagemanager.admin.service.SysUserPermissionService;
import com.example.imagemanager.admin.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import java.util.List;

@RestController()
@RequestMapping("/admin")
public class HomePageController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserPermissionService sysUserPermissionService;


    @PostMapping("/getUserRole")
    public ModelAndView getUserRole(@RequestBody UserRoleGrantRequest userRoleGrantRequest){
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject(sysUserPermissionService.getSysRoleByUid(userRoleGrantRequest.getUid()));
        return modelAndView;
    }

    @PostMapping("/updateUserRole")
    public String updateUserRole(@RequestBody UserRoleGrantRequest userRoleGrantRequest){
        sysUserPermissionService.updateUserRole(userRoleGrantRequest);
        return "success";
    }

    @PostMapping("/updateRole")
    public String updateRole(@RequestBody SysRole sysRole){
        sysUserPermissionService.updateRole(sysRole);
        return "success";
    }

    @PostMapping("/updateRolePerm")
    public String updateRolePerm(@RequestBody RolePermRequest rolePermRequest){
        sysUserPermissionService.updateRolePerm(rolePermRequest);
        return "success";
    }

    @GetMapping("/test_login")
//    @PreAuthorize("hasAuthority('normal')")
    public String test() {
        UserPermissionResult userPermissionResult = sysUserPermissionService.getPermissionByUid("e6bf6251-e39f-11ee-af56-b42e99590e8c");
        System.out.println("userPermissionResult:" + userPermissionResult);
        return "test";
    }

    @GetMapping("/users")
    public ModelAndView getUsers() {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        List<SysUserModel> list = sysUserService.getSysUsers();
        modelAndView.addObject(list);
        return modelAndView;
    }

    @GetMapping("/roles")
    public ModelAndView getRoles() {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        List<SysRole> list = sysUserPermissionService.getRoles();
        modelAndView.addObject(list);
        return modelAndView;
    }

    @PostMapping("/addRole")
    public String addRole(@RequestBody SysRole role) {
        sysUserPermissionService.addRole(role.getName(), role.getDescription());
        return "success";
    }


    @PostMapping("/addPermission")
    public String addPermission(@RequestBody SysPermission permission) {
        sysUserPermissionService.addPermission(permission);
        return "success";
    }

    @PostMapping("/updatePermission")
    public String updatePermission(@RequestBody SysPermission permission) {
        sysUserPermissionService.updatePermission(permission);
        return "success";
    }

    @DeleteMapping("/deletePermission/{id}")
    public String deletePermission(@PathVariable int id) {
        sysUserPermissionService.deletePermission(id);
        return "success";
    }

    @GetMapping("/permissions")
    public ModelAndView permissions() {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        List<SysPermission> list = sysUserPermissionService.loadPermission();
        modelAndView.addObject(list);
        return modelAndView;
    }

    @DeleteMapping("/deleteRole")
    public String deleteRole(@RequestParam("id") int id) {
        sysUserPermissionService.deleteRole(id);
        return "success";
    }


    @GetMapping("/permissions/{uid}")
    public ModelAndView getUserPermission(@PathVariable String uid) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        UserPermissionResult userPermissionResult = sysUserPermissionService.getPermissionByUid(uid);
        modelAndView.addObject(userPermissionResult);
        return modelAndView;
    }

    @GetMapping("/rolePermissions/{role_id}")
    public ModelAndView getRolePermission(@PathVariable int role_id) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject(sysUserPermissionService.getRolePermission(role_id));
        return modelAndView;
    }


    @Deprecated
    @PostMapping("/login")
    public ModelAndView login(@RequestBody LoginData loginData) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        String username = loginData.getUsername();
        String password = loginData.getPassword();
        try {
            modelAndView.addObject(sysUserService.login(username, password));
        } catch (Exception ignored) {
        }
        return modelAndView;
    }
}
