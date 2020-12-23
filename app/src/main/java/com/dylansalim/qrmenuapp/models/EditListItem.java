package com.dylansalim.qrmenuapp.models;

public class EditListItem extends Header {
    private String name;
    private String desc;
    private double pricing;
    private double promoPricing;
    private String currency;
    private boolean recommended;
    private int categoryId;
    private boolean isHeader;
    private boolean isAddNewBtn = false;
    private String imgUrl;

    public EditListItem() {
    }

    public EditListItem(String name, String desc, int id, double pricing, boolean recommended, double promoPricing, int categoryId, String imgUrl,String currency) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.pricing = pricing;
        this.categoryId = categoryId;
        this.imgUrl = imgUrl;
        this.recommended = recommended;
        this.promoPricing = promoPricing;
        this.currency = currency;
        isHeader = false;
    }

    public EditListItem(String header, int id) {
        this.name = header;
        this.categoryId = id;
        isHeader = true;
    }

    public EditListItem(int id) {
        this.categoryId = id;
        isAddNewBtn = true;
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

    public double getPricing() {
        return pricing;
    }

    public void setPricing(double pricing) {
        this.pricing = pricing;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isAddNewBtn() {
        return isAddNewBtn;
    }

    public void setAddNewBtn(boolean addNewBtn) {
        isAddNewBtn = addNewBtn;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getPromoPricing() {
        return promoPricing;
    }

    public void setPromoPricing(double promoPricing) {
        this.promoPricing = promoPricing;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "EditListItem{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", pricing=" + pricing +
                ", categoryId=" + categoryId +
                ", isHeader=" + isHeader +
                ", isAddNewBtn=" + isAddNewBtn +
                ", imgUrl='" + imgUrl + '\'' +
                ", id=" + id +
                '}';
    }
}
