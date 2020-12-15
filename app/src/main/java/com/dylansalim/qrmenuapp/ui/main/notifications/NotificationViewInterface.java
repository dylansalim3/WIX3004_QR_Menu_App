package com.dylansalim.qrmenuapp.ui.main.notifications;

import com.dylansalim.qrmenuapp.models.dao.NotificationDao;

import java.util.List;

public interface NotificationViewInterface {

    void populateNotifications(List<NotificationDao> notificationDaos);

    void showLoading();

    void hideLoading();

    void showError(String error);
}
