package com.dylansalim.qrmenuapp.models;

public class EditListItem extends Header  {
    private String name;
    private String desc;
    private double pricing;
    private int categoryId;
    private boolean isHeader;
    private boolean isAddNewBtn = false;
    private String imgUrl;

    public EditListItem() {
    }

    public EditListItem(String name, String desc, int id, double pricing, int categoryId,String imgUrl) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.pricing = pricing;
        this.categoryId = categoryId;
        this.imgUrl = imgUrl;
        isHeader = false;
    }

    public EditListItem(String header, int id) {
        this.name = header;
        this.categoryId = id;
        isHeader = true;
    }

    public EditListItem(int id){
        this.categoryId = id;
        isAddNewBtn=true;
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
