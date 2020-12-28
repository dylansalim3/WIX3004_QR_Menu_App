package com.dylansalim.qrmenuapp.ui.merchant_info;

import android.content.Context;

import com.dylansalim.qrmenuapp.models.dto.Store;

import java.util.List;

import androidx.fragment.app.Fragment;

public interface MerchantInfoPresenterInterface {

    void setStoreInfo(Store storeDetail, boolean isStoreAdmin);

    void setupFragments(List<Fragment> fragments);

    void onQRActionBtnClick();

    void onEditBtnClick();

    void onReportBtnClick();

    void onReviewSubmit(String text, float rating, Context context);

    void disposeObserver();
}
