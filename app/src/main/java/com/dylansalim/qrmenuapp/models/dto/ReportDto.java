package com.dylansalim.qrmenuapp.models.dto;

public class ReportDto {

    private int store_id;
    private int user_id;
    private String title;
    private String desc;
    private String email;

    public ReportDto(int store_id, int user_id, String title, String desc, String email) {
        this.store_id = store_id;
        this.user_id = user_id;
        this.title = title;
        this.desc = desc;
        this.email = email;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
