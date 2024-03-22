package com.example.imagemanager.cache;

import com.example.imagemanager.admin.model.Ticket;
import com.example.imagemanager.admin.model.UserPermissionResult;

import java.io.Serializable;

public class SysUserCache implements Serializable {
    private String uid;

    //登录信息
    private Ticket ticket;

    //权限信息
    private UserPermissionResult userPermissionResult;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public UserPermissionResult getUserPermissionResult() {
        return userPermissionResult;
    }

    public void setUserPermissionResult(UserPermissionResult userPermissionResult) {
        this.userPermissionResult = userPermissionResult;
    }
}
