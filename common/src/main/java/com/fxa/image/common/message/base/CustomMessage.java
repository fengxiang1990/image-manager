package com.fxa.image.common.message.base;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * 用户产的消息 需要插入rabbit mq中
 * @param <T>
 */


@JsonDeserialize(using = BaseMessageDeserializer.class)
public class CustomMessage<T> implements Serializable {

    public interface DataType{
        int CONTENT = 4;
        int TESTMSG = 5;
        int STRING = 0;
        int INTEGER = 1;
        int LONG = 2;
        int DOUBLE = 3;
    }

    public static final int reg = 1; //注册消息
    public static final int content_publish = 2;

    public static final int content_comment = 3;

    private int dataType;
    private int code;
    private String msg;

    private T data;

    public CustomMessage() {

    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
    public CustomMessage(int dataType) {
        this.dataType = dataType;
    }

    public CustomMessage(int dataType, int code) {
        this.dataType = dataType;
        this.code = code;
    }

    public CustomMessage(int dataType, int code, String msg) {
        this.dataType = dataType;
        this.code = code;
        this.msg = msg;
    }

    public CustomMessage(int dataType, int code, String msg, T data) {
        this.dataType = dataType;
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

    @Override
    public String toString() {
        return "CustomMessage{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
