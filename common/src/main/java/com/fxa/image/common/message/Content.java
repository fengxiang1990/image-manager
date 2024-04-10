package com.fxa.image.common.message;

import java.io.Serial;
import java.util.ArrayList;

public class Content extends SortedContent {
    @Serial
    private static final long serialVersionUID = -8247528687520982804L;
    public Content(){

    }

    private String uid;
    private String title;
    private String content;
    private String url;



    private long views;
    private long like;

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




    @Override
    public String toString() {
        return "Content{" +
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
