package com.fxa.image.client;

public class HttpResponse<T> {

    public static final int SUCCESS = 0;
    public static final int FAILED = -1;

    public static final int ERR_LOGIN_NONE = 10000000;
    public static final int ERR_LOGIN_USERNAME_EMPTY = 10000001;
    public static final int ERR_LOGIN_PASSWORD_ERR = 10000002;
    public static final int ERR_LOGIN_USER_NOT_FOUND= 10000003;
    public static final int ERR_LOGIN_TOKEN_CREATE_ERR= 10000004;
    //登录已过期
    public static final int ERR_LOGIN_TOKEN_EXPIRY= 10000005;

    public static final int ERR_LOGIN_ERR_SECURITY= 10000006;
    //接口响应结果
    public int ret = SUCCESS;

    private String msg;

    private T data;

    //错误代码 可以定义一堆

    private int err;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
