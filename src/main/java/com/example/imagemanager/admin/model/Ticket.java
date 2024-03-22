package com.example.imagemanager.admin.model;

/**
 * 登录成功的token 等信息
 */
public class Ticket {

    //默认过期时间先设置为7天
    public static final long DEFAULT_EXPIRY_DURATION = 7 * 24 * 60 * 1000;
    //过期时间为登录时间 + 过期间隔
    public long expiry;

    //登录成功后为当前用户生成一个字符串
    //规则
    //时间戳+uid+username+随机数
    public String token;

    //登录时间
    public long loginTime;

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
}
