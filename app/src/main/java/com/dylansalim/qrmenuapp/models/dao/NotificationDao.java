package com.dylansalim.qrmenuapp.models.dao;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class NotificationDao {

    private String title;

    private String body;

    private String activity;

    private int data;

    @SerializedName("is_read")
    private boolean isRead;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
