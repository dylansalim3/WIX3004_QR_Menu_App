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
}
