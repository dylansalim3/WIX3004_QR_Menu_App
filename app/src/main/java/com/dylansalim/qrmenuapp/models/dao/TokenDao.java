package com.dylansalim.qrmenuapp.models.dao;

public class TokenDao {

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
