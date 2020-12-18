package com.dylansalim.qrmenuapp.ui.edit_profile;

import android.util.Log;

import com.dylansalim.qrmenuapp.network.LoginRegistrationNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.utils.ValidationUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditProfilePresenter implements EditProfilePresenterInterface {

    final String TAG = "Edit Profile Presenter";

    EditProfileViewInterface view;
    Disposable disposable;

    public EditProfilePresenter(EditProfileViewInterface view) {
        this.view = view;
    }

    @Override
    public void saveProfile(int userId, String firstName, String lastName, String phoneNum, String address) {
        view.showLoading();

        Boolean valid = validateInput(firstName, lastName, phoneNum, address);
        if (!valid) {
            view.hideLoading();
            return;
        }

        disposable = NetworkClient.getNetworkClient().create(LoginRegistrationNetworkInterface.class)
                .updateProfile(userId, firstName, lastName, phoneNum, address)
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
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
