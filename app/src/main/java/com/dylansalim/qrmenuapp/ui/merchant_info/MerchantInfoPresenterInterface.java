package com.dylansalim.qrmenuapp.ui.merchant_info;

import com.dylansalim.qrmenuapp.models.dao.StoreDao;

import java.util.List;

import androidx.fragment.app.Fragment;

public interface MerchantInfoPresenterInterface {

    void setStoreInfo(StoreDao storeDetail, boolean isStoreAdmin);

    void setupFragments(List<Fragment> fragments);
}
