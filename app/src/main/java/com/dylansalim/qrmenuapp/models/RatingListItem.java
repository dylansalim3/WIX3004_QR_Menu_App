package com.dylansalim.qrmenuapp.models;

import java.util.Date;

public class RatingListItem {
    private String reviewerName;
    private Date date;
    private String desc;
    private float rating;

    public RatingListItem(String reviewerName, Date date, String desc, float rating) {
        this.reviewerName = reviewerName;
        this.date = date;
        this.desc = desc;
        this.rating = rating;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
