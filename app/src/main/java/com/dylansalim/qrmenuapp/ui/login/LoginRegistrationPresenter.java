package com.dylansalim.qrmenuapp.ui.login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dylansalim.qrmenuapp.models.dao.LoginDao;
import com.dylansalim.qrmenuapp.models.dao.RoleDao;
import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;
import com.dylansalim.qrmenuapp.network.LoginRegistrationNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.utils.JWTUtils;

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

    public DisposableObserver<LoginDao> getLoginFormObserver() {
        return new DisposableObserver<LoginDao>() {
            @Override
            public void onNext(@NonNull LoginDao loginDao) {
                Log.d(TAG, "OnNext" + loginDao.getToken());
                try {
                    JWTUtils.decoded(loginDao.getToken());
                } catch (Exception e) {
                    e.printStackTrace();
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
}
