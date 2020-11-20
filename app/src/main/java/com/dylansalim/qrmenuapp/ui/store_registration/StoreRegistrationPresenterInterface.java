package com.dylansalim.qrmenuapp.ui.store_registration;

import android.app.Activity;
import android.content.Context;

public interface StoreRegistrationPresenterInterface {
    void onRegisterButtonClicked(Activity activity);

    void getCurrentLocation(boolean useCurrentLocation, Context context);
}
