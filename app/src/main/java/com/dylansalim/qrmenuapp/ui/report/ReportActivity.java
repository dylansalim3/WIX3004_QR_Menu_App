package com.dylansalim.qrmenuapp.ui.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.databinding.ActivityReportBinding;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;

public class ReportActivity extends AppCompatActivity {

    ReportViewModel viewModel;
    ActivityReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report);

        TokenData tokenData = getDataFromToken();
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        viewModel.setStoreId(getIntent().getIntExtra("store_id", 0));
        viewModel.setEmail(tokenData.email);
        viewModel.setUserId(tokenData.userId);

        binding.setReportViewModel(viewModel);
        binding.storeInput.setText(getIntent().getStringExtra("store_name"));
        binding.closeButton.setOnClickListener(view -> finish());
        binding.sendButton.setOnClickListener(view -> viewModel.onSendClicked());
    }

    private TokenData getDataFromToken() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(
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