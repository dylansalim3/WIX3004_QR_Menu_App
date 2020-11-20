package com.dylansalim.qrmenuapp.models.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenDao {
    @Expose
    @SerializedName("token")
    public String token;

    public TokenDao(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}