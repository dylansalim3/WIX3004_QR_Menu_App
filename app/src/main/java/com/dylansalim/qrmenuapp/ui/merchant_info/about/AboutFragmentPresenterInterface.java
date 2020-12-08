package com.dylansalim.qrmenuapp.ui.merchant_info.about;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.dylansalim.qrmenuapp.models.dao.StoreDao;
import com.google.android.gms.maps.GoogleMap;

public interface AboutFragmentPresenterInterface {
    void setStoreDetail(StoreDao storeDetail);

    void onMapReady(GoogleMap googleMap, Context context);

    void initStoreDetailView(Activity activity);

    Uri retrieveLocationUri();
}
