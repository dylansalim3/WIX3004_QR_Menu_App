package com.dylansalim.qrmenuapp.models.dto;

import com.google.gson.annotations.SerializedName;

public class ItemCategory {
    private int id;
    private String name;

    @SerializedName("store_id")
    private int storeId;

    private int position;

    public ItemCategory(String name, int storeId) {
        this.name = name;
        this.storeId = storeId;
    }

    public ItemCategory(int id, String name, int storeId, int position) {
        this.name = name;
        this.storeId = storeId;
        this.position = position;
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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
