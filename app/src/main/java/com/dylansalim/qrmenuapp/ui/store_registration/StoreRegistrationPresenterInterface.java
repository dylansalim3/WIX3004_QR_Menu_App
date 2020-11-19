package com.dylansalim.qrmenuapp.ui.store_registration;

import android.content.Context;

public interface StoreRegistrationPresenterInterface {
    void onRegisterButtonClicked();

    void getCurrentLocation(boolean useCurrentLocation, Context context);
}
