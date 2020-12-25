package com.dylansalim.qrmenuapp.ui.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.util.Log;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.utils.SharedPrefUtil;


public class SettingActivity extends AppCompatActivity {

    final String TAG = "Setting Activity";

    SwitchCompat notificationCheckbox;
    SwitchCompat notificationSoundCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        notificationCheckbox = findViewById(R.id.setting_notification);
        notificationSoundCheckbox = findViewById(R.id.setting_notification_sound);

        notificationCheckbox.setChecked(SharedPrefUtil.getNotificationPref(this));
        notificationCheckbox.setOnCheckedChangeListener((view, isCheck) -> {
            SharedPrefUtil.setNotificationPref(this, isCheck);
            Log.d(TAG, "Notification set to " + isCheck);
        });

        notificationSoundCheckbox.setChecked(SharedPrefUtil.getNotificationSoundPref(this));
        notificationSoundCheckbox.setOnCheckedChangeListener((view, isCheck) -> {
            SharedPrefUtil.setNotificationSoundPref(this, isCheck);
            Log.d(TAG, "Notification sound set to " + isCheck);
        });

        findViewById(R.id.setting_back_button).setOnClickListener(view -> finish());
    }
}