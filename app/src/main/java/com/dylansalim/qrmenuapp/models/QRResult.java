package com.dylansalim.qrmenuapp.models;

public class QRResult {
    private int storeId;

    public QRResult(int storeId) {
        this.storeId = storeId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
