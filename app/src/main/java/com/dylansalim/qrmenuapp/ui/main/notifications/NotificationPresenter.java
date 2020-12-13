package com.dylansalim.qrmenuapp.ui.main.notifications;

import android.util.Log;

import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.NotificationNetworkInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationPresenter implements NotificationPresenterInterface {

    static final String TAG = "Notification Presenter";

    NotificationViewInterface notificationView;
    Disposable disposable;

    public NotificationPresenter(NotificationViewInterface notificationView) {
        this.notificationView = notificationView;
    }

    @Override
    public void getNotifications(int userId) {
        notificationView.showLoading();

        disposable = NetworkClient.getNetworkClient().create(NotificationNetworkInterface.class)
                .getNotifications(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notificationDaos -> {
                    Log.d(TAG, "get notifications success");
                    notificationView.hideLoading();
                    notificationView.populateNotifications(notificationDaos);
                }, error -> {
                    Log.e(TAG, "get notifications failed -> " + error);
                    notificationView.hideLoading();
                    notificationView.showError("Get notifications failed");
                });
    }

    @Override
    public void disposeObserver() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
