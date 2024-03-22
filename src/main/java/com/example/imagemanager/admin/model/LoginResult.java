package com.example.imagemanager.admin.model;

import com.example.imagemanager.model.BaseResponse;

//登录信息
public class LoginResult{


    public String uid;

    public String username;

    public Ticket ticket;

    public SysRole sysRole;


    public SysRole getSysRole() {
        return sysRole;
    }

    public void setSysRole(SysRole sysRole) {
        this.sysRole = sysRole;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
