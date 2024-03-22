package com.example.imagemanager.admin.model;

public class UserRoleGrantRequest {

    private String uid;
    private int roleId;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
