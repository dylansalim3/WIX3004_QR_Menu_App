package com.dylansalim.qrmenuapp.ui.merchant_info.review;

import com.dylansalim.qrmenuapp.models.dao.StoreDao;

public class MerchantReviewPresenter implements MerchantReviewPresenterInterface {

    private MerchantReviewViewInterface mrvi;
    private StoreDao storeDetail;

    public MerchantReviewPresenter(MerchantReviewViewInterface mrvi){
        this.mrvi = mrvi;
    }

    @Override
    public void setStoreDetail(StoreDao storeDetail) {
        this.storeDetail = storeDetail;
    }

}
