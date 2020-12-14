package com.dylansalim.qrmenuapp.ui.edit_profile;

import android.util.Log;

import com.dylansalim.qrmenuapp.network.LoginRegistrationNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;

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
        disposable = NetworkClient.getNetworkClient().create(LoginRegistrationNetworkInterface.class)
                .updateProfile(userId, firstName, lastName, phoneNum, address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, "profile updated successfully");
                    view.hideLoading();
                }, error -> {
                    Log.e(TAG, "profile updated failed");
                    view.hideLoading();
                    view.showError("Profile update failed");
                });
    }

    @Override
    public void disposeObserver() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
