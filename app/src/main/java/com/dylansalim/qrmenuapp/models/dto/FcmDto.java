package com.dylansalim.qrmenuapp.models.dto;

public class FcmDto {

    private int id;
    private String fcm_token;

    public FcmDto(int id, String fcm_token) {
        this.id = id;
        this.fcm_token = fcm_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }
}
