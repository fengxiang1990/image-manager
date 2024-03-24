package com.fxa.image.client.model;

import java.io.Serializable;

//登录信息
public class LoginResult implements Serializable {

    public String uid;

    public String username;

    public Ticket ticket;

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
