package com.dylansalim.qrmenuapp.ui.store_registration;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.dylansalim.qrmenuapp.BuildConfig;
import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dto.Address;
import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Store;
import com.dylansalim.qrmenuapp.models.dto.Token;
import com.dylansalim.qrmenuapp.models.dto.UserDetail;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.StoreRegistrationNetworkInterface;
import com.dylansalim.qrmenuapp.services.GpsTracker;
import com.dylansalim.qrmenuapp.ui.component.CustomPhoneInputLayout;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.dylansalim.qrmenuapp.utils.ValidationUtils;
import com.google.gson.Gson;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class StoreRegistrationPresenter implements StoreRegistrationPresenterInterface {

    private StoreRegistrationViewInterface srvi;
    private GpsTracker gpsTracker;
    private double latitude;
    private double longitude;
    private List<DisposableObserver<?>> disposableObservers = new ArrayList<>();
    private boolean isEdit = false;
    private int editStoreId = -1;
    private String profileImg;

    private static final String TAG = "SRPresenter";

    public StoreRegistrationPresenter(StoreRegistrationViewInterface srvi) {
        this.srvi = srvi;
    }

    @Override
    public void onRetrieveStoreDetail(Store storeDetail) {
        if (null != storeDetail && null != srvi.getCustomPhoneInputLayout().getTextInputLayout().getEditText()) {
            isEdit = true;
            editStoreId = storeDetail.getId();
            srvi.getCustomPhoneInputLayout().getTextInputLayout().getEditText().setText(storeDetail.getPhoneNum().substring(2));
            srvi.setStoreName(storeDetail.getName());
            srvi.setAddress(storeDetail.getAddress());
            srvi.setCity(storeDetail.getCity());
            srvi.setPostalCode(String.valueOf(storeDetail.getPostalCode()));
            srvi.setCountry(storeDetail.getCountry());
            srvi.setOpeningHour(storeDetail.getOpenHour());
            srvi.setClosingHour(storeDetail.getClosingHour());
            if (null != storeDetail.getSpecialOpeningNote()) {
                srvi.setSpecialOpeningNote(storeDetail.getSpecialOpeningNote());
            }
            if (null != storeDetail.getProfileImg()) {
                srvi.setProfileImg(BuildConfig.SERVER_API_URL + "/" + storeDetail.getProfileImg());
            }
        }
    }

    @Override
    public void onItemImageResult(Uri uri, ContentResolver contentResolver) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String itemImgUrl = cursor.getString(columnIndex);
        cursor.close();
        srvi.setProfileImg("file://" + itemImgUrl);


        profileImg = itemImgUrl;
        Log.d(TAG, profileImg);
    }

    @Override
    public void onRegisterButtonClicked(Activity activity) {
        srvi.showProgressBar();

        int userId = getUserId(activity);
        Store store = validateStoreRegistrationForm(userId);

        if (store != null) {

            RequestBody nameBody = RequestBody.create(MediaType.parse("plain/text"), store.getName());
            RequestBody addressBody = RequestBody.create(MediaType.parse("plain/text"), store.getAddress());
            RequestBody poscodeRequestBody = RequestBody.create(MediaType.parse("plain/text"), String.valueOf(store.getPostalCode()));
            RequestBody cityRequestBody = RequestBody.create(MediaType.parse("plain/text"), store.getCity());
            RequestBody countryRequestBody = RequestBody.create(MediaType.parse("plain/text"), store.getCountry());
            RequestBody latitudeBody = RequestBody.create(MediaType.parse("plain/text"), String.valueOf(store.getLatitude()));
            RequestBody longitudeBody = RequestBody.create(MediaType.parse("plain/text"), String.valueOf(store.getLongitude()));
            RequestBody phoneNumRequestBody = RequestBody.create(MediaType.parse("plain/text"), store.getPhoneNum());
            RequestBody userIdRequestBody = RequestBody.create(MediaType.parse("plain/text"), String.valueOf(store.getUserId()));
            RequestBody openHourRequestBody = RequestBody.create(MediaType.parse("plain/text"), store.getOpenHour());
            RequestBody closingHourBody = RequestBody.create(MediaType.parse("plain/text"), store.getClosingHour());
            RequestBody specialOpeningNoteRequestBody = RequestBody.create(MediaType.parse("plain/text"), store.getSpecialOpeningNote());

            MultipartBody.Part imgBody = null;

            if (profileImg != null) {
                File file = new File(profileImg);

                RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/*"), file);

                imgBody = MultipartBody.Part.createFormData("file", "file.png", requestBody1);
            }

            if (!isEdit) {
                // Create new Store
                disposableObservers.add(getStoreRegistrationNetworkClient().createStore(imgBody, nameBody, addressBody, poscodeRequestBody, cityRequestBody, countryRequestBody, latitudeBody, longitudeBody, phoneNumRequestBody, userIdRequestBody, openHourRequestBody, closingHourBody, specialOpeningNoteRequestBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getRegistrationFormObserver()));
            } else if (isEdit && editStoreId != -1) {
                // Edit Store
                RequestBody storeIdRequestBody = RequestBody.create(MediaType.parse("plain/text"), String.valueOf(editStoreId));

                disposableObservers.add(getStoreRegistrationNetworkClient().updateStore(imgBody, storeIdRequestBody, nameBody, addressBody, poscodeRequestBody, cityRequestBody, countryRequestBody, latitudeBody, longitudeBody, phoneNumRequestBody, userIdRequestBody, openHourRequestBody, closingHourBody, specialOpeningNoteRequestBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getUpdateFormObserver()));
            } else {
                srvi.hideProgressBar();
            }
        }
    }

    private Store validateStoreRegistrationForm(int userId) {
        String storeName = srvi.getStoreName();
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
                city, country, openingHour, closingHour};

        for (String nonNullFieldString : nonNullFieldStrings) {
            if (!ValidationUtils.isNonNullFieldValid(nonNullFieldString)) {
                srvi.displayErrorMessage("Cannot leave fields empty");
                srvi.hideProgressBar();
                return null;
            }
        }

        if (userId == -1) {
            srvi.displayErrorMessage("Role is empty");
            srvi.hideProgressBar();
            return null;
        }

        // checks if the field is valid
        if (phoneInputLayout.isValid()) {
            phoneInputLayout.setError(null);
        } else {
            // set error message
            srvi.displayErrorMessage("Phone number is not in correct format");
            srvi.hideProgressBar();
            return null;
        }


        try {
            String phoneNumber = phoneInputLayout.getPhoneNumber();
            int postalCode = Integer.parseInt(postalCodeString);
            return new Store(storeName, address, postalCode, city, country, latitude, longitude, phoneNumber, userId, openingHour, closingHour, specialNote, profileImg);
        } catch (Exception e) {
            e.printStackTrace();
            srvi.displayErrorMessage("Invalid fields");
            srvi.hideProgressBar();
            return null;
        }
    }

    private int getUserId(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(activity.getString(R.string.token), "");
        try {
            String dataString = JWTUtils.getDataString(token);
            UserDetail userDetail = new Gson().fromJson(dataString, UserDetail.class);
            Log.d(TAG, userDetail.toString());
            return userDetail.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private StoreRegistrationNetworkInterface getStoreRegistrationNetworkClient() {
        return NetworkClient.getNetworkClient().create(StoreRegistrationNetworkInterface.class);
    }

    public DisposableObserver<Result<Token>> getRegistrationFormObserver() {
        return new DisposableObserver<Result<Token>>() {

            @Override
            public void onNext(Result<Token> result) {
                Log.d(TAG,result.getData().toString());

                srvi.navigateToNextScreen(result.getData());
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

    public DisposableObserver<Result<Store>> getUpdateFormObserver() {
        return new DisposableObserver<Result<Store>>() {

            @Override
            public void onNext(Result<Store> result) {
                srvi.onFormSubmitted(result.getData());
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
                Address address = gpsTracker.getAddress(latitude, longitude);
                if (address != null) {
                    srvi.setAddress(address.getAddress());
                    srvi.setPostalCode(address.getPostalCode());
                    srvi.setCity(address.getCity());
                    srvi.setCountry(address.getCountry());
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
            android.location.Address address = addressData.getAddressList().get(0);
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
