package com.fxa.image.rpcreview.model;

import java.util.ArrayList;

public class ReviewContent {
    private long id;

    private String uid;
    private String title;
    private String content;
    private String url;

    private int status;//0 默认待审核状态 1自动审核通过 2 自动审核不通过 3 人工审核通过 4 人工审核不通过

    private long views;
    private long like;

    private int score;
    private ArrayList<Integer> topic;
    private ArrayList<Integer> label;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getLike() {
        return like;
    }

    public void setLike(long like) {
        this.like = like;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Integer> getTopic() {
        return topic;
    }

    public void setTopic(ArrayList<Integer> topic) {
        this.topic = topic;
    }

    public ArrayList<Integer> getLabel() {
        return label;
    }

    public void setLabel(ArrayList<Integer> label) {
        this.label = label;
    }

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


    @Override
    public String toString() {
        return "ReviewContent{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                ", views=" + views +
                ", like=" + like +
                ", score=" + score +
                ", topic=" + topic +
                ", label=" + label +
                '}';
    }
}
