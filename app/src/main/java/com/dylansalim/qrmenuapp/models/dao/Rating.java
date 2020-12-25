package com.dylansalim.qrmenuapp.models.dao;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import androidx.annotation.Nullable;

public class Rating {
    @SerializedName("store_id")
    private int storeId;

    @SerializedName("user_id")
    private int userId;

    @Nullable
    private String username;

    private float rating;

    private String desc;

    private Date created;

    public Rating(int storeId, int userId, float rating, String desc) {
        this.storeId = storeId;
        this.userId = userId;
        this.rating = rating;
        this.desc = desc;
    }

    public Rating(int storeId, float rating, String desc) {
        this.storeId = storeId;
        this.rating = rating;
        this.desc = desc;
    }

    public Rating(int storeId, float rating, String desc, Date created) {
        this.storeId = storeId;
        this.rating = rating;
        this.desc = desc;
        this.created = created;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }
}
