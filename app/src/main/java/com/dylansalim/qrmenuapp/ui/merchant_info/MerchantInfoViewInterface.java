package com.dylansalim.qrmenuapp.ui.merchant_info;

import com.dylansalim.qrmenuapp.models.dao.StoreDao;

import java.util.List;

import androidx.fragment.app.Fragment;

public interface MerchantInfoViewInterface {
    void displayToast(String s);

    void showProgressBar();

    void hideProgressBar();

    void setupToolbar(String title);

    void populateView(List<Fragment> fragments, StoreDao sotreDetail);

    void navigateToStoreQRActivity(StoreDao storeDetail);

    void navigateToEditStoreActivity(StoreDao storeDetail);

    void navigateToReportActivity(StoreDao storeDetail);

    void setProfileImg(String profileImg);
}
