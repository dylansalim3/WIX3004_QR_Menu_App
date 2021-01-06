package com.dylansalim.qrmenuapp.ui.main.account;

import android.util.Log;

import com.dylansalim.qrmenuapp.network.AccountNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AccountPresenter implements AccountPresenterInterface {

    final String TAG = "Account Presenter";

    AccountViewInterface accountView;
    List<Disposable> disposables = new ArrayList<>();
    Disposable disposable;

    public AccountPresenter(AccountViewInterface accountView) {
        this.accountView = accountView;
    }

    @Override
    public void switchRole(String role, String token) {
        accountView.showLoading();
        disposable = NetworkClient.getNetworkClient(token).create(AccountNetworkInterface.class)
                .updateRole(role)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenDao -> {
                    Log.d(TAG, "role switch success");
                    accountView.saveUserToken(tokenDao);
                    accountView.hideLoading();
                    accountView.showDialog(
                            role.equals("MERCHANT")
                                    ? AccountViewInterface.DialogType.SWITCH_MERCHANT
                                    : AccountViewInterface.DialogType.SWITCH_CUSTOMER,
                            view -> accountView.reopenApp()
                    );
                }, error -> {
                    Log.e(TAG, "role switch fail");
                    accountView.hideLoading();
                    accountView.showDialog(AccountViewInterface.DialogType.ERROR, null);
                });
        disposables.add(disposable);
    }

    @Override
    public void logout() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        disposable = Observable
                .fromCallable(() -> FirebaseMessaging.getInstance().deleteToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    Log.d(TAG, "FCM token removed");
                    accountView.hideLoading();
                    accountView.showDialog(AccountViewInterface.DialogType.LOGOUT,
                            view -> accountView.reloadPage());
                }, error -> {
                    Log.d(TAG, "remove FCM token error");
                    accountView.hideLoading();
                    accountView.showDialog(AccountViewInterface.DialogType.ERROR, null);
                });
        disposables.add(disposable);
    }

    @Override
    public void disposeObserver() {
        for(Disposable disposable: disposables) {
            disposable.dispose();
        }
    }
}
