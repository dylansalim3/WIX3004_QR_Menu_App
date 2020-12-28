package com.dylansalim.qrmenuapp.models.dao;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class UserDetailDao {
    private int id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String email;

    private String password;

    private String created;

    private boolean active;

    @SerializedName("profile_img")
    private String profileImg;

    @SerializedName("phone_num")
    private String phoneNum;

    @SerializedName("role_id")
    private int roleId;

    private String role;

    @Nullable
    @SerializedName("store_id")
    private Integer storeId;

    @Nullable
    @SerializedName("store_name")
    private String storeName;

    private String address;

    public UserDetailDao(int id, String firstName, String lastName, String email, String password, String created, boolean active, String profileImg, String phoneNum, int roleId, String role, int storeId,String storeName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.created = created;
        this.active = active;
        this.profileImg = profileImg;
        this.phoneNum = phoneNum;
        this.roleId = roleId;
        this.role = role;
        this.storeId = storeId;
        this.storeName = storeName;
    }

    public boolean isActive() {
        return active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    @Nullable
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(@Nullable String storeName) {
        this.storeName = storeName;
    }
}

//{
//        "id": 14,
//        "first_name": "dylan",
//        "last_name": "salim",
//        "email": "dylansalim015@gmail.com",
//        "password": "$2b$10$YI76tr8S5ryjAgWMWIESKObBogGnlH2EkoqeGHroRUOPJHgfqkfna",
//        "created": "2020-11-15T09:05:55.000Z",
//        "active": true,
//        "verification_hash": "",
//        "profile_img": null,
//        "address": "No. 13, Jalan Bee",
//        "phone_num": "0186650165",
//        "role_id": 7,
//        "iat": 1605756809
//        }
