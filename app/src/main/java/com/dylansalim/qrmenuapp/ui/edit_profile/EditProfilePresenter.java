package com.dylansalim.qrmenuapp.ui.edit_profile;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.dylansalim.qrmenuapp.BuildConfig;
import com.dylansalim.qrmenuapp.models.dto.Address;
import com.dylansalim.qrmenuapp.network.AccountNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.services.GpsTracker;
import com.dylansalim.qrmenuapp.utils.ValidationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfilePresenter implements EditProfilePresenterInterface {

    final String TAG = "Edit Profile Presenter";

    EditProfileViewInterface view;
    Disposable disposable;
    List<Disposable> disposables = new ArrayList<>();

    public EditProfilePresenter(EditProfileViewInterface view) {
        this.view = view;
    }

    @Override
    public void getProfileImage(int userId) {
        disposable = NetworkClient.getNetworkClient().create(AccountNetworkInterface.class)
                .getImageUrl(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageUrlDao -> {
                    Log.d(TAG, "get profile image success");
                    view.loadImage(BuildConfig.SERVER_API_URL + '/' + imageUrlDao.getUrl());
                }, error -> {
                    Log.e(TAG, "get profile image failed");
                });
        disposables.add(disposable);
    }

    @Override
    public void savePicture(int userId, Uri imageUri, String token) {
        File imageFile = getImage(imageUri);
        MultipartBody.Part imgBody = MultipartBody.Part.createFormData(
                "img",
                userId + ".jpeg",
                RequestBody.create(MediaType.parse("image/jpeg"), imageFile)
        );

        view.showLoading();
        disposable = NetworkClient.getNetworkClient(token).create(AccountNetworkInterface.class)
                .updatePicture(imgBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, "profile picture updated");
                    view.hideLoading();
                    view.loadImage(imageFile);
                }, error -> {
                    Log.e(TAG, "update picture failed ->" + error);
                    view.hideLoading();
                    view.showError(EditProfileViewInterface.ErrorType.REQUEST_FAILED);
                });
        disposables.add(disposable);
    }

    @Override
    public void saveProfile(String firstName, String lastName, String phoneNum, String address, String token) {
        view.showLoading();

        Boolean valid = validateInput(firstName, lastName, phoneNum, address);
        if (!valid) {
            view.hideLoading();
            return;
        }

        disposable = NetworkClient.getNetworkClient(token).create(AccountNetworkInterface.class)
                .updateProfile(firstName, lastName, phoneNum, address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenDao -> {
                    Log.d(TAG, "profile updated successfully");
                    view.saveToken(tokenDao);
                    view.hideLoading();
                    view.showSuccess();
                }, error -> {
                    Log.e(TAG, "profile updated failed");
                    view.hideLoading();
                    view.showError(EditProfileViewInterface.ErrorType.REQUEST_FAILED);
                });
        disposables.add(disposable);
    }

    public void getLocation(Context context) {
        GpsTracker gpsTracker = new GpsTracker(context);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            //retrieve road data
            Address address = gpsTracker.getAddress(latitude, longitude);
            if (address != null) {
                view.setAddress(address.getAddress());
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private File getImage(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = view.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imageUrl = cursor.getString(columnIndex);
        cursor.close();
        return new File(imageUrl);
    }

    private Boolean validateInput(String firstName, String lastName, String phoneNum, String address) {
        if (!ValidationUtils.isNonNullFieldValid(firstName)) {
            view.showError(EditProfileViewInterface.ErrorType.EMPTY_FIRST_NAME);
            return false;
        }
        if (!ValidationUtils.isNonNullFieldValid(lastName)) {
            view.showError(EditProfileViewInterface.ErrorType.EMPTY_LAST_NAME);
            return false;
        }
        if (!ValidationUtils.isNonNullFieldValid(phoneNum)) {
            view.showError(EditProfileViewInterface.ErrorType.EMPTY_PHONE_NUM);
            return false;
        }
        if (!ValidationUtils.isNonNullFieldValid(address)) {
            view.showError(EditProfileViewInterface.ErrorType.EMPTY_ADDRESS);
            return false;
        }
        if (!ValidationUtils.isValidPhoneNumber(phoneNum)) {
            view.showError(EditProfileViewInterface.ErrorType.INVALID_PHONE_NUMBER);
            return false;
        }
        return true;
    }

    @Override
    public void disposeObserver() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }
}
