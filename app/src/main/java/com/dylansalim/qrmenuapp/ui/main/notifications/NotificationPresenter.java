package com.dylansalim.qrmenuapp.ui.main.notifications;

import android.util.Log;

import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.NotificationNetworkInterface;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationPresenter implements NotificationPresenterInterface {

    static final String TAG = "Notification Presenter";

    NotificationViewInterface notificationView;
    List<Disposable> disposables = new ArrayList<>();
    Disposable disposable;

    public NotificationPresenter(NotificationViewInterface notificationView) {
        this.notificationView = notificationView;
    }

    @Override
    public void getNotifications(String token) {
        notificationView.showLoading();

        disposable = NetworkClient.getNetworkClient(token).create(NotificationNetworkInterface.class)
                .getNotifications()
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
        disposables.add(disposable);
    }

    @Override
    public void readNotification(int notificationId, String token) {
        disposable = NetworkClient.getNetworkClient(token).create(NotificationNetworkInterface.class)
                .readNotification(notificationId)
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    Log.d(TAG, "read notifications success");
                }, error -> {
                    Log.e(TAG, "read notifications failed -> " + error);
                });
        disposables.add(disposable);
    }

    @Override
    public void disposeObserver() {
        Log.d(TAG, "dispose observers");
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }
}
