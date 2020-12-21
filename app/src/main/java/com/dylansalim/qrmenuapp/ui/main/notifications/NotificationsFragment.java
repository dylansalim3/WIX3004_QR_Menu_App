package com.dylansalim.qrmenuapp.ui.main.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.dylansalim.qrmenuapp.ui.merchant.MerchantActivity;
import com.dylansalim.qrmenuapp.utils.SharedPrefUtil;

import java.util.List;


public class NotificationsFragment extends Fragment implements NotificationViewInterface {

    final String TAG = "Notification Fragment";

    NotificationPresenterInterface notificationPresenter;
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    View progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        setupMVP();

        recyclerView = root.findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationPresenter.getNotifications(SharedPrefUtil.getUserToken(getContext()), false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        notificationPresenter.getNotifications(SharedPrefUtil.getUserToken(getContext()), false);
    }

    @Override
    public void populateNotifications(List<NotificationDao> notificationDaos) {
        adapter = new NotificationAdapter(notificationDaos, (view, position) -> { //click listener
            NotificationDao dao = notificationDaos.get(position);
            notificationPresenter.readNotification(dao.getId(), SharedPrefUtil.getUserToken(getContext()));
            view.setBackgroundResource(R.drawable.dark_grey_rounded);

            if (dao.getActivity() != null) {
                Intent intent = new Intent(getContext(), MerchantActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(getResources().getString(R.string.store_id), dao.getData());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
        if (progressBar == null) {
            progressBar = getLayoutInflater().inflate(R.layout.progressbar_layout, null);
            ((ViewGroup) getActivity().findViewById(android.R.id.content).getRootView()).addView(progressBar);
        }
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) {
            ((ViewGroup) progressBar.getParent()).removeView(progressBar);
            progressBar = null;
        }
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
}