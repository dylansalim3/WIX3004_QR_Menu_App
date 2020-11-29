package com.dylansalim.qrmenuapp.models.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllItemDao {
    private int id;
    private String name;

    @SerializedName("store_id")
    private int storeId;

    private int position;

    private List<ItemDao> items;

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

    public List<ItemDao> getItems() {
        return items;
    }

    public void setItems(List<ItemDao> items) {
        this.items = items;
    }
}
