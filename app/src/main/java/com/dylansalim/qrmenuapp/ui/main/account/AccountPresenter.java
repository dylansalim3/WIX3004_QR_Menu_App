package com.dylansalim.qrmenuapp.ui.main.account;

import android.util.Log;

import com.dylansalim.qrmenuapp.network.AccountNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AccountPresenter implements AccountPresenterInterface {

    final String TAG = "Account Presenter";

    AccountViewInterface accountView;
    Disposable disposable;

    public AccountPresenter(AccountViewInterface accountView) {
        this.accountView = accountView;
    }

    @Override
    public void switchRole(int userId, String role) {
        accountView.showLoading();
        disposable = NetworkClient.getNetworkClient().create(AccountNetworkInterface.class)
                .updateRole(userId, role)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, "role switch success");
                    accountView.hideLoading();
                    accountView.removeUserToken();
                    accountView.showDialog(view -> accountView.login());
                }, error -> {
                    accountView.hideLoading();
                    Log.e(TAG, "role switch fail");
                });
    }
}
