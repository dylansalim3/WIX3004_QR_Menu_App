package com.dylansalim.qrmenuapp.ui.merchant_info.about;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.dylansalim.qrmenuapp.models.dto.Store;
import com.google.android.gms.maps.GoogleMap;

public interface AboutFragmentPresenterInterface {
    void setStoreDetail(Store storeDetail);

    void onMapReady(GoogleMap googleMap, Context context);

    void initStoreDetailView(Activity activity);

    Uri retrieveLocationUri();
}
