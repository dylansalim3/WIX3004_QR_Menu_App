package com.dylansalim.qrmenuapp.ui.login_registration;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dylansalim.qrmenuapp.models.dao.TokenDao;
import com.dylansalim.qrmenuapp.models.dao.RoleDao;
import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;
import com.dylansalim.qrmenuapp.network.LoginRegistrationNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginRegistrationPresenter implements LoginRegistrationPresenterInterface {

    LoginRegistrationViewInterface lrvi;
    private final String TAG = "LRPresenter";

    public LoginRegistrationPresenter(LoginRegistrationViewInterface lrvi) {
        this.lrvi = lrvi;
    }

    @Override
    public void submitLoginForm(String email, String password) {
        lrvi.showProgressBar();
        getLoginRegistrationNetworkClient().submitLoginRequest(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getLoginFormObserver());
    }

    @Override
    public void getRoles() {
        lrvi.showProgressBar();
        getLoginRegistrationNetworkClient().getRoles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getRoleObserver());
    }

    @Override
    public void submitRegistrationForm(RegistrationDto registrationDto) {
        lrvi.showProgressBar();
        getLoginRegistrationNetworkClient().submitRegistrationRequest(registrationDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getRegistrationFormObserver());
    }

    private LoginRegistrationNetworkInterface getLoginRegistrationNetworkClient() {
        return NetworkClient.getNetworkClient().create(LoginRegistrationNetworkInterface.class);
    }

    public DisposableObserver<List<RoleDao>> getRoleObserver(){
        return new DisposableObserver<List<RoleDao>>(){

            @Override
            public void onNext(List<RoleDao> roleDaoList) {
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

    public DisposableObserver<TokenDao> getLoginFormObserver() {
        return new DisposableObserver<TokenDao>() {
            @Override
            public void onNext(@NonNull TokenDao tokenDao) {
                Log.d(TAG, "OnNext" + tokenDao.getToken());
                lrvi.navigateToNextActivity(tokenDao);
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

    public DisposableObserver<TokenDao> getRegistrationFormObserver() {
        return new DisposableObserver<TokenDao>() {
            @Override
            public void onNext(@NonNull TokenDao tokenDao) {
                Log.d(TAG, "getRegistrationFormObserver" + tokenDao);
                lrvi.navigateToNextActivity(tokenDao);
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

}
