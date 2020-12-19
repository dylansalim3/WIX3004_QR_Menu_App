package com.dylansalim.qrmenuapp.ui.main.notifications;

public interface NotificationPresenterInterface {

    void getNotifications(String token);

    void readNotification(int notificationId, String token);

    void disposeObserver();
}
