package com.example.imagemanager.admin.model;

public class UserPermissionResult {
    private String uid;

    private SysRole sysRole;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public SysRole getSysRole() {
        return sysRole;
    }

    public void setSysRole(SysRole sysRole) {
        this.sysRole = sysRole;
    }

    @Override
    public String toString() {
        return "UserPermissionResult{" +
                "uid='" + uid + '\'' +
                ", sysRole=" + sysRole +
                '}';
    }
}
