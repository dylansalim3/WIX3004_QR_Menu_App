package com.dylansalim.qrmenuapp.models.dao;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class StoreDao {
    private int id;

    private String name;

    private String address;

    @SerializedName("postal_code")
    private int postalCode;

    private String city;

    private String country;

    @Nullable
    private Double latitude;

    @Nullable
    private Double longitude;

    @SerializedName("phone_num")
    private String phoneNum;

    @SerializedName("user_id")
    private int userId;

    public StoreDao(int id, String name, String address, int postalCode, String city, String country, @Nullable Double latitude, @Nullable Double longitude, String phoneNum, int userId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNum = phoneNum;
        this.userId = userId;
    }

    public StoreDao(String name, String address, int postalCode, String city, String country, @Nullable Double latitude, @Nullable Double longitude, String phoneNum, int userId) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNum = phoneNum;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Nullable
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@Nullable Double latitude) {
        this.latitude = latitude;
    }

    @Nullable
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@Nullable Double longitude) {
        this.longitude = longitude;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
