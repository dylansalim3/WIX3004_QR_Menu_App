package com.dylansalim.qrmenuapp.models.dto;

public class OverallRating {
    private double average;

    private int count;

    public OverallRating(double average, int count) {
        this.average = average;
        this.count = count;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
