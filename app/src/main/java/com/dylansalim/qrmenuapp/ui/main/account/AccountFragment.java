package com.dylansalim.qrmenuapp.ui.main.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.ui.edit_profile.EditProfileActivity;
import com.dylansalim.qrmenuapp.ui.login_registration.LoginRegistrationActivity;
import com.dylansalim.qrmenuapp.ui.main.MainActivity;
import com.dylansalim.qrmenuapp.ui.report.ReportActivity;
import com.dylansalim.qrmenuapp.ui.setting.SettingActivity;


public class AccountFragment extends Fragment implements AccountViewInterface {

    final String TAG = "Account Fragment";

    AccountPresenterInterface presenter;
    View editProfile;
    View switchRole;
    View settings;
    View login;
    View feedback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        setupMVP();

        editProfile = root.findViewById(R.id.account_edit_profile_button);
        switchRole = root.findViewById(R.id.account_switch_role_button);
        settings = root.findViewById(R.id.account_settings_button);
        login = root.findViewById(R.id.account_login_register_button);
        feedback = root.findViewById(R.id.account_feedback_button);

        if (isLogin()) {
            login.setVisibility(View.GONE);
        } else {
            editProfile.setVisibility(View.GONE);
            switchRole.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
        }

        editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        switchRole.setOnClickListener(view -> {
            presenter.switchRole("a");
            // TODO: fang -> show dialog 'You have change your role, please login again'
            Intent intent = new Intent(getContext(), LoginRegistrationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        settings.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), LoginRegistrationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        feedback.setOnClickListener(view -> {
            String appPackageName = requireContext().getPackageName();
            appPackageName = "com.whatsapp";
            try {
                Log.d(TAG, "Open market");
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException exception) {
                Log.e(TAG, "Fail to open market, open webpage instead");
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });

        return root;
    }

    private Boolean isLogin() {
        // TODO: fang -> check if a user has login
        return true;
    }

    private void setupMVP() {
        this.presenter = new AccountPresenter(this);
    }
}