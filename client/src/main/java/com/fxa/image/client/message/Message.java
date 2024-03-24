package com.fxa.image.client.message;

import java.io.Serializable;

/**
 * 用户产的消息 需要插入rabbit mq中
 * @param <T>
 */
public class Message<T> implements Serializable {
    public static final int reg = 1; //注册消息
    public static final int content_publish = 2;

    public static final int content_comment = 3;

    private int code;
    private String msg;
    private T data;

    public Message(int code) {
        this.code = code;
    }

    public Message(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Message(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
