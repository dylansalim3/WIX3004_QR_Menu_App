package com.dylansalim.qrmenuapp.ui.login_registration;

import android.util.Log;

import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;
import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Role;
import com.dylansalim.qrmenuapp.models.dto.Token;
import com.dylansalim.qrmenuapp.network.LoginRegistrationNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginRegistrationPresenter implements LoginRegistrationPresenterInterface {

    LoginRegistrationViewInterface lrvi;
    private final String TAG = "LRPresenter";
    private List<Role> roles;
    private List<DisposableObserver<?>> disposableObservers = new ArrayList<>();

    public LoginRegistrationPresenter(LoginRegistrationViewInterface lrvi) {
        this.lrvi = lrvi;
    }

    @Override
    public void submitLoginForm(String email, String password) {
        lrvi.showProgressBar();
        disposableObservers.add(getLoginRegistrationNetworkClient().submitLoginRequest(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getLoginFormObserver()));
    }

    @Override
    public List<Role> getRoles() {
        return roles;
    }


    @Override
    public void retrieveRolesAvailable(@Nullable List<Role> roles) {
        if (roles != null) {
            Log.d(TAG, roles.toString());
            this.roles = roles;
            lrvi.navigateToRegistrationFragment(roles);
        } else if (this.roles != null) {
            lrvi.navigateToRegistrationFragment(this.roles);
        } else {
            lrvi.showProgressBar();
            disposableObservers.add(getLoginRegistrationNetworkClient().getRoles()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getRoleObserver()));
        }
    }

    @Override
    public void submitRegistrationForm(RegistrationDto registrationDto) {
        lrvi.showProgressBar();
        disposableObservers.add(getLoginRegistrationNetworkClient().submitRegistrationRequest(registrationDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getRegistrationFormObserver()));
    }

    @Override
    public void disposeObserver() {
        for (DisposableObserver<?> disposableObserver : disposableObservers) {
            disposableObserver.dispose();
        }
    }

    private LoginRegistrationNetworkInterface getLoginRegistrationNetworkClient() {
        return NetworkClient.getNetworkClient().create(LoginRegistrationNetworkInterface.class);
    }

    public DisposableObserver<List<Role>> getRoleObserver() {
        return new DisposableObserver<List<Role>>() {

            @Override
            public void onNext(List<Role> roleList) {
                roles = roleList;
                lrvi.navigateToRegistrationFragment(roleList);
            }

            @Override
            public void onError(Throwable e) {
                lrvi.displayError("Error Occurred. Please try again later.");
                lrvi.hideProgressBar();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                lrvi.hideProgressBar();
            }
        };
    }

    public DisposableObserver<Result<Token>> getLoginFormObserver() {
        return new DisposableObserver<Result<Token>>() {
            @Override
            public void onNext(@NonNull Result<Token> tokenDao) {
                Log.d(TAG, "OnNext " + tokenDao);
                if (tokenDao.getData() != null) {
                    setupFCM();
                    lrvi.navigateToNextActivity(tokenDao.getData());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
                lrvi.displayError("Error Occurred. Please try again later.");
                lrvi.hideProgressBar();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                lrvi.hideProgressBar();
            }
        };
    }

    public DisposableObserver<Result<Token>> getRegistrationFormObserver() {
        return new DisposableObserver<Result<Token>>() {
            @Override
            public void onNext(@NonNull Result<Token> tokenDao) {
                Log.d(TAG, "getRegistrationFormObserver" + tokenDao);
                setupFCM();
                lrvi.navigateToNextActivity(tokenDao.getData());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
                lrvi.displayError("Error Occurred. Please try again later.");
                lrvi.hideProgressBar();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                lrvi.hideProgressBar();
            }
        };
    }

    private void setupFCM() {
        Log.d(TAG, "FCM init");
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }
}
