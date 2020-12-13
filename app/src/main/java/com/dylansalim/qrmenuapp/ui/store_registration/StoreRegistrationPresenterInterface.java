package com.dylansalim.qrmenuapp.ui.store_registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.dylansalim.qrmenuapp.models.dao.StoreDao;

import androidx.annotation.Nullable;

public interface StoreRegistrationPresenterInterface {
    void onRetrieveStoreDetail(StoreDao storeDetail);

    void onRegisterButtonClicked(Activity activity);

    void getCurrentLocation(boolean useCurrentLocation, Context context);

    void onLocationSelected(@Nullable Intent intent);

    void disposeObserver();
}
