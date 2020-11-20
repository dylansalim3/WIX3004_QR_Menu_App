package com.dylansalim.qrmenuapp.ui.store_registration;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.AddressDao;
import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.StoreRegistrationNetworkInterface;
import com.dylansalim.qrmenuapp.services.GpsTracker;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.dylansalim.qrmenuapp.utils.ValidationUtils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class StoreRegistrationPresenter implements StoreRegistrationPresenterInterface {

    private StoreRegistrationViewInterface srvi;
    private GpsTracker gpsTracker;
    private double latitude;
    private double longitude;

    private static final String TAG = "SRPresenter";

    public StoreRegistrationPresenter(StoreRegistrationViewInterface srvi) {
        this.srvi = srvi;
    }

    @Override
    public void onRegisterButtonClicked(Activity activity) {
        srvi.showProgressBar();
        String storeName = srvi.getStoreName();
        String phoneNumber = srvi.getPhoneNumber();
        String address = srvi.getAddress();
        String postalCodeString = srvi.getPostalCode();
        String city = srvi.getCity();
        String country = srvi.getCountry();
        int userId = getUserId(activity);
        int postalCode = -1;
        if (postalCodeString.length() > 0) {
            postalCode = Integer.parseInt(postalCodeString);
        }
        StoreDao storeDao = new StoreDao(storeName, address, postalCode, city, country, latitude, longitude, phoneNumber, userId);

        boolean isValidForm = validateStoreRegistrationForm(storeDao);
        Log.d(TAG,Boolean.toString(isValidForm));
        Log.d(TAG,"Validated");


        if (isValidForm) {
            Log.d(TAG,"Form submitted");
            getStoreRegistrationNetworkClient().createStore(storeDao)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getRegistrationFormObserver());
        }

    }

    private boolean validateStoreRegistrationForm(StoreDao storeDao) {
        String storeName = storeDao.getName();
        String phoneNumber = storeDao.getPhoneNum();
        String address = storeDao.getAddress();
        String postalCode = Integer.toString(storeDao.getPostalCode());
        String city = storeDao.getCity();
        String country = storeDao.getCountry();
        int userId = storeDao.getUserId();

        String[] nonNullFieldStrings = new String[]{
                storeName, phoneNumber, address, postalCode,
                city, country};

        for (String nonNullFieldString : nonNullFieldStrings) {
            if (!ValidationUtils.isNonNullFieldValid(nonNullFieldString)) {
                srvi.displayErrorMessage("Cannot leave fields empty");
                return false;
            }
        }

        if (userId == -1) {
            srvi.displayErrorMessage("Role is empty");
        }

        if (!ValidationUtils.isValidPhoneNumber(phoneNumber)) {
            srvi.displayErrorMessage("Phone number is not in correct format");
            return false;
        }

        return true;

    }

    private int getUserId(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(activity.getString(R.string.token), "");
        try {
            String dataString = JWTUtils.getDataString(token);
            UserDetailDao userDetailDao = new Gson().fromJson(dataString, UserDetailDao.class);
            Log.d(TAG, userDetailDao.toString());
            return userDetailDao.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private StoreRegistrationNetworkInterface getStoreRegistrationNetworkClient() {
        return NetworkClient.getNetworkClient().create(StoreRegistrationNetworkInterface.class);
    }

    public DisposableObserver<Result<String>> getRegistrationFormObserver() {
        return new DisposableObserver<Result<String>>() {

            @Override
            public void onNext(Result<String> result) {
                srvi.navigateToNextScreen();
            }

            @Override
            public void onError(Throwable e) {
                srvi.displayErrorMessage("Error occurred");
                srvi.hideProgressBar();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                srvi.hideProgressBar();
            }
        };
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
