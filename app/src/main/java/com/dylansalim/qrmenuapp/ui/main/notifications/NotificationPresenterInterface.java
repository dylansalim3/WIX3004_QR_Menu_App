package com.dylansalim.qrmenuapp.ui.main.notifications;

public interface NotificationPresenterInterface {

    void getNotifications(int userId);

    void disposeObserver();
}
