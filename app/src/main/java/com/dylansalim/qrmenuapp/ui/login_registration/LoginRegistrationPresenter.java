package com.dylansalim.qrmenuapp.ui.login_registration;

import android.util.Log;

import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dao.RoleDao;
import com.dylansalim.qrmenuapp.models.dao.TokenDao;
import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;
import com.dylansalim.qrmenuapp.network.LoginRegistrationNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginRegistrationPresenter implements LoginRegistrationPresenterInterface {

    LoginRegistrationViewInterface lrvi;
    private final String TAG = "LRPresenter";
    private List<RoleDao> roleDaos;
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
    public void getRoles() {
        lrvi.showProgressBar();
        disposableObservers.add(getLoginRegistrationNetworkClient().getRoles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getRoleObserver()));
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

    public DisposableObserver<List<RoleDao>> getRoleObserver() {
        return new DisposableObserver<List<RoleDao>>() {

            @Override
            public void onNext(List<RoleDao> roleDaoList) {
                roleDaos = roleDaoList;
                lrvi.navigateToRegistrationFragment(roleDaoList);
            }

            @Override
            public void onError(Throwable e) {
                lrvi.displayError("Error occurred");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                lrvi.hideProgressBar();
            }
        };
    }

    public DisposableObserver<Result<TokenDao>> getLoginFormObserver() {
        return new DisposableObserver<Result<TokenDao>>() {
            @Override
            public void onNext(@NonNull Result<TokenDao> tokenDao) {
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
                lrvi.displayError("Error fetching Data");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                lrvi.hideProgressBar();
            }
        };
    }

    public DisposableObserver<Result<TokenDao>> getRegistrationFormObserver() {
        return new DisposableObserver<Result<TokenDao>>() {
            @Override
            public void onNext(@NonNull Result<TokenDao> tokenDao) {
                Log.d(TAG, "getRegistrationFormObserver" + tokenDao);
                setupFCM();
                lrvi.navigateToNextActivity(tokenDao.getData());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
                lrvi.displayError("Error fetching Data");
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
