package com.fxa.image.common.message;

import java.io.Serializable;

public class SortedContent implements Serializable {

    protected long id;

    protected long creteTime;
    protected int score;

    protected int status;//0 默认待审核状态 1自动审核通过 2 自动审核不通过 3 人工审核通过 4 人工审核不通过

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreteTime() {
        return creteTime;
    }

    public void setCreteTime(long creteTime) {
        this.creteTime = creteTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
