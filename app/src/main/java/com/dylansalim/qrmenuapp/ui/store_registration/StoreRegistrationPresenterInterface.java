package com.dylansalim.qrmenuapp.ui.store_registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public interface StoreRegistrationPresenterInterface {
    void onRegisterButtonClicked(Activity activity);

    void getCurrentLocation(boolean useCurrentLocation, Context context);

    void onLocationSelected(@Nullable Intent intent);

    void disposeObserver();
}
