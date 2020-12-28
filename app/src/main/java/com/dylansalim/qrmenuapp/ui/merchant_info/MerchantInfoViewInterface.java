package com.dylansalim.qrmenuapp.ui.merchant_info;

import com.dylansalim.qrmenuapp.models.dto.Store;

import java.util.List;

import androidx.fragment.app.Fragment;

public interface MerchantInfoViewInterface {
    void displayToast(String s);

    void showProgressBar();

    void hideProgressBar();

    void setupToolbar(String title);

    void populateView(List<Fragment> fragments, Store sotreDetail);

    void navigateToStoreQRActivity(Store storeDetail);

    void navigateToEditStoreActivity(Store storeDetail);

    void navigateToReportActivity(Store storeDetail);

    void setProfileImg(String profileImg);
}
