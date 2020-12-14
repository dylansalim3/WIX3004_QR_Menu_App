package com.dylansalim.qrmenuapp.ui.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;

import com.dylansalim.qrmenuapp.R;

public class SettingActivity extends AppCompatActivity implements SettingViewInterface {

    final String TAG = "Setting Activity";

    SettingPresenterInterface presenter;
    CheckBox notificationCheckbox;
    CheckBox locationCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setupMVP();

        notificationCheckbox = findViewById(R.id.setting_notification);
        locationCheckbox = findViewById(R.id.setting_location);

        notificationCheckbox.setChecked(presenter.getNotificationStatus());
        notificationCheckbox.setOnClickListener(checkbox -> {
            presenter.setNotificationStatus(notificationCheckbox.isChecked());
        });

        locationCheckbox.setChecked(presenter.getLocationStatus());
        locationCheckbox.setOnClickListener(checkbox -> {
            presenter.setLocationStatus(locationCheckbox.isChecked());
        });

        findViewById(R.id.setting_back_button).setOnClickListener(view -> finish());
    }

    private void setupMVP() {
        presenter = new SettingPresenter(this);
    }
}