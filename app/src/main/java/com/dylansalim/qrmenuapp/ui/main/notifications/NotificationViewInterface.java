package com.dylansalim.qrmenuapp.ui.main.notifications;

import com.dylansalim.qrmenuapp.models.dto.Notification;

import java.util.List;

public interface NotificationViewInterface {

    void populateNotifications(List<Notification> notifications);

    void showLoading();

    void hideLoading();

    void showError(String error);
}
