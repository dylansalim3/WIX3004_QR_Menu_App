package com.dylansalim.qrmenuapp.models.dao;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ItemDao {
    private int id;
    @SerializedName("item_category_id")
    private int itemCategoryId;

    private String name;

    private String desc;

    private double price;

    @SerializedName("promo_price")
    private double promoPrice;

    private Date created;

    @SerializedName("item_img")
    private String itemImg;

    private boolean recommmended;

    private boolean hidden;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(int itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(double promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public boolean isRecommmended() {
        return recommmended;
    }

    public void setRecommmended(boolean recommmended) {
        this.recommmended = recommmended;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}