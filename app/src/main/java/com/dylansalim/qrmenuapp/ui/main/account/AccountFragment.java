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
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.ui.edit_profile.EditProfileActivity;
import com.dylansalim.qrmenuapp.ui.login_registration.LoginRegistrationActivity;
import com.dylansalim.qrmenuapp.ui.setting.SettingActivity;
import com.dylansalim.qrmenuapp.utils.SharedPrefUtil;


public class AccountFragment extends Fragment implements AccountViewInterface {

    final String TAG = "Account Fragment";

    AccountPresenterInterface presenter;
    View editProfile;
    View switchRole;
    View settings;
    View login;
    View feedback;
    View logout;

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
        logout = root.findViewById(R.id.account_logout_button);

        if (isLogin()) {
            login.setVisibility(View.GONE);
        } else {
            editProfile.setVisibility(View.GONE);
            switchRole.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
        }

        editProfile.setOnClickListener(view -> openEditProfile());
        settings.setOnClickListener(view -> openSetting());
        login.setOnClickListener(view -> login());
        feedback.setOnClickListener(view -> openMarket());
        logout.setOnClickListener(view -> logout());

        switchRole.setOnClickListener(view -> {
            UserDetailDao userDetail = SharedPrefUtil.getUserDetail(requireContext());
            String currentRole = userDetail.getRole();
            String merchant = getString(R.string.merchant);
            String customer = getString(R.string.customer);
            presenter.switchRole(userDetail.getId(), currentRole.equals(customer) ? merchant : customer);
        });

        return root;
    }

    @Override
    public void showDialog(View.OnClickListener clickListener) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void login() {
        Intent intent = new Intent(getContext(), LoginRegistrationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void removeUserToken() {
        SharedPrefUtil.removeUserDetail(getContext());
    }

    private Boolean isLogin() {
        return SharedPrefUtil.getUserDetail(requireContext()) != null;
    }

    private void logout() {
        removeUserToken();
        login();
    }

    private void openEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void openSetting() {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        startActivity(intent);
    }

    private void openMarket() {
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
    }

    private void setupMVP() {
        this.presenter = new AccountPresenter(this);
    }
}