package com.dylansalim.qrmenuapp.ui.main.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;


public class AccountFragment extends Fragment implements AccountViewInterface {

    AccountPresenterInterface presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        setupMVP();

        root.findViewById(R.id.account_edit_profile_button).setOnClickListener(view -> {
            // TODO: fang -> navigate to edit profile activity
        });
        root.findViewById(R.id.account_switch_role_button).setOnClickListener(view -> {
            presenter.switchRole("a");
        });
        root.findViewById(R.id.account_settings_button).setOnClickListener(view -> {
            Toast.makeText(getContext(), "setting", Toast.LENGTH_LONG).show();
        });
        root.findViewById(R.id.account_feedback_button).setOnClickListener(view -> {
            // TODO: fang -> navigate to playstore
        });

        return root;
    }

    private void setupMVP() {
        this.presenter = new AccountPresenter(this);
    }

    // TODO: fang -> extract into util
    private TokenData getDataFromToken() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(
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
            tokenData.role = userDetailDao.getRole();
        }
        return tokenData;
    }

    private static class TokenData {
        String email;
        int userId;
        String role;
    }
}