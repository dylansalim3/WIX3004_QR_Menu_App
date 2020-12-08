package com.dylansalim.qrmenuapp.ui.merchant_info;

import android.util.Log;

import com.dylansalim.qrmenuapp.models.dao.StoreDao;

import java.util.List;

import androidx.fragment.app.Fragment;

public class MerchantInfoPresenter implements MerchantInfoPresenterInterface {

    private MerchantInfoViewInterface mivi;
    private StoreDao storeDetail;
    private boolean isStoreAdmin;
    private static final String TAG = "mipi";

    public MerchantInfoPresenter(MerchantInfoViewInterface mivi) {
        this.mivi = mivi;
    }


    @Override
    public void setStoreInfo(StoreDao storeDetail, boolean isStoreAdmin) {
        this.storeDetail = storeDetail;
        mivi.setupToolbar(storeDetail.getName());
        this.isStoreAdmin = isStoreAdmin;
    }

    @Override
    public void setupFragments(List<Fragment> fragments) {
        if(storeDetail!=null){
            mivi.populateView(fragments,storeDetail);
        }
    }
}
