package com.dylansalim.qrmenuapp.ui.store_registration;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.dylansalim.qrmenuapp.models.dto.Store;

import androidx.annotation.Nullable;

public interface StoreRegistrationPresenterInterface {
    void onRetrieveStoreDetail(Store storeDetail);

    void onItemImageResult(Uri uri, ContentResolver contentResolver);

    void onRegisterButtonClicked(Activity activity);

    void getCurrentLocation(boolean useCurrentLocation, Context context);

    void onLocationSelected(@Nullable Intent intent);

    void disposeObserver();
}
