package com.example.imagemanager.admin.model;

import java.util.List;

public class RolePermRequest {

    private int roleId;
    private List<Integer> perms;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public List<Integer> getPerms() {
        return perms;
    }

    public void setPerms(List<Integer> perms) {
        this.perms = perms;
    }
}
