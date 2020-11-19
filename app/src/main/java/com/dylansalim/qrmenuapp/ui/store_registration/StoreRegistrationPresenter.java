package com.dylansalim.qrmenuapp.ui.store_registration;

import android.content.Context;

import com.dylansalim.qrmenuapp.models.dao.AddressDao;
import com.dylansalim.qrmenuapp.services.GpsTracker;

public class StoreRegistrationPresenter implements StoreRegistrationPresenterInterface {

    private StoreRegistrationViewInterface srvi;
    private GpsTracker gpsTracker;
    private double latitude;
    private double longitude;

    public StoreRegistrationPresenter(StoreRegistrationViewInterface srvi) {
        this.srvi = srvi;
    }

    @Override
    public void onRegisterButtonClicked() {

    }

    @Override
    public void getCurrentLocation(boolean useCurrentLocation, Context context) {
        if (useCurrentLocation) {
            gpsTracker = new GpsTracker(context);
            if (gpsTracker.canGetLocation()) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                //retrieve road data
                AddressDao addressDao = gpsTracker.getAddress(latitude, longitude);
                if (addressDao != null) {
                    srvi.setAddress(addressDao.getAddress());
                    srvi.setPostalCode(addressDao.getPostalCode());
                    srvi.setCity(addressDao.getCity());
                    srvi.setCountry(addressDao.getCountry());
                }
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }
}
