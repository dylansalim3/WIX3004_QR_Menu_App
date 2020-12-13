package com.dylansalim.qrmenuapp.ui.store_registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.util.Log;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.AddressDao;
import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.StoreRegistrationNetworkInterface;
import com.dylansalim.qrmenuapp.services.GpsTracker;
import com.dylansalim.qrmenuapp.ui.component.CustomPhoneInputLayout;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.dylansalim.qrmenuapp.utils.ValidationUtils;
import com.google.gson.Gson;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class StoreRegistrationPresenter implements StoreRegistrationPresenterInterface {

    private StoreRegistrationViewInterface srvi;
    private GpsTracker gpsTracker;
    private double latitude;
    private double longitude;
    private List<DisposableObserver<?>> disposableObservers = new ArrayList<>();

    private static final String TAG = "SRPresenter";

    public StoreRegistrationPresenter(StoreRegistrationViewInterface srvi) {
        this.srvi = srvi;
    }

    @Override
    public void onRegisterButtonClicked(Activity activity) {
        srvi.showProgressBar();

        int userId = getUserId(activity);
        StoreDao storeDao = validateStoreRegistrationForm(userId);

        if (null != storeDao) {
            Log.d(TAG, "Form submitted");
            disposableObservers.add(getStoreRegistrationNetworkClient().createStore(storeDao)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getRegistrationFormObserver()));
        }else{
            srvi.hideProgressBar();
        }
    }

    private StoreDao validateStoreRegistrationForm(int userId) {
        String storeName = srvi.getStoreName();
//        String phoneNumber = srvi.getPhoneNumber();
        String address = srvi.getAddress();
        String postalCodeString = srvi.getPostalCode();
        String city = srvi.getCity();
        String country = srvi.getCountry();
        String openingHour = srvi.getOpeningHour();
        String closingHour = srvi.getClosingHour();
        String specialNote = srvi.getSpecialOpeningNote();
        CustomPhoneInputLayout phoneInputLayout = srvi.getCustomPhoneInputLayout();

        String[] nonNullFieldStrings = new String[]{
                storeName, address, postalCodeString,
                city, country, openingHour, closingHour, specialNote};

        for (String nonNullFieldString : nonNullFieldStrings) {
            if (!ValidationUtils.isNonNullFieldValid(nonNullFieldString)) {
                srvi.displayErrorMessage("Cannot leave fields empty");
                return null;
            }
        }

        if (userId == -1) {
            srvi.displayErrorMessage("Role is empty");
        }

        // checks if the field is valid
        if (phoneInputLayout.isValid()) {
            phoneInputLayout.setError(null);
        } else {
            // set error message
            srvi.displayErrorMessage("Phone number is not in correct format");
            return null;
        }

        // Return the phone number as follows


        try {
            String phoneNumber = phoneInputLayout.getPhoneNumber();
            int postalCode = Integer.parseInt(postalCodeString);
            return new StoreDao(storeName, address, postalCode, city, country, latitude, longitude, phoneNumber, userId, openingHour, closingHour, specialNote);
        } catch (Exception e) {
            e.printStackTrace();
            srvi.displayErrorMessage("Invalid fields");
            return null;
        }
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
                srvi.displayErrorMessage(e.toString());
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

    @Override
    public void onLocationSelected(@Nullable Intent data) {
        AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
        assert addressData != null;
        Log.d("TAG", addressData.getAddressList().toString());
        if (null != addressData.getAddressList() && addressData.getAddressList().size() > 0) {
            Address address = addressData.getAddressList().get(0);
            String addressString = address.getAddressLine(0);
            String postalCode = address.getPostalCode();
            String city = address.getLocality();
            String country = address.getCountryName();
            latitude = address.getLatitude();
            longitude = address.getLongitude();

            srvi.setAddress(addressString);
            srvi.setPostalCode(postalCode);
            srvi.setCity(city);
            srvi.setCountry(country);
        }
    }

    @Override
    public void disposeObserver() {
        for (DisposableObserver<?> disposableObserver : disposableObservers) {
            disposableObserver.dispose();
        }
    }
}
