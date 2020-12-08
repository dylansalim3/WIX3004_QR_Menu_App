package com.dylansalim.qrmenuapp.ui.merchant_info;

import com.dylansalim.qrmenuapp.models.dao.StoreDao;

import java.util.List;

import androidx.fragment.app.Fragment;

public interface MerchantInfoViewInterface {
    void displayError(String s);

    void showProgressBar();

    void hideProgressBar();

    void setupToolbar(String title);

    void populateView(List<Fragment> fragments, StoreDao sotreDetail);
}
