package com.dylansalim.qrmenuapp.ui.main.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.NotificationDao;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.ui.report.ReportActivity;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//TODO: fang -> max title length and max body length
//TODO: fang -> the whole row item should be clickable not just the small image button
//TODO: fang -> set notification as read after click

public class NotificationsFragment extends Fragment implements NotificationViewInterface {

    NotificationPresenterInterface notificationPresenter;
    RecyclerView recyclerView;
    NotificationAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        setupMVP();

        recyclerView = root.findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationPresenter.getNotifications(getDataFromToken().userId);
        return root;
    }

    @Override
    public void populateNotifications(List<NotificationDao> notificationDaos) {
        adapter = new NotificationAdapter(notificationDaos, (view, position) -> {
            // click listener
            NotificationDao dao = notificationDaos.get(position);
            switch (dao.getActivity()) {
                case "store":
                    int storeId = dao.getData();
                    // TODO: navigate to store activity
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    public void setupMVP() {
        notificationPresenter = new NotificationPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        notificationPresenter.disposeObserver();
        notificationPresenter = null;
    }

    private TokenData getDataFromToken() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(
                this.getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(this.getString(R.string.token), "");
        TokenData tokenData = new TokenData();

        assert token != null;
        if (!token.equals("")) {
            String dataString = null;
            try {
                dataString = JWTUtils.getDataString(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserDetailDao userDetailDao = new Gson().fromJson(dataString, UserDetailDao.class);
            tokenData.email = userDetailDao.getEmail();
            tokenData.userId = userDetailDao.getId();
        }
        return tokenData;
    }

    private static class TokenData {
        String email;
        int userId;
    }
}