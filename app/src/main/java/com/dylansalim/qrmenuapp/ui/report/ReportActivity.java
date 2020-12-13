package com.dylansalim.qrmenuapp.ui.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.databinding.ActivityReportBinding;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;

public class ReportActivity extends AppCompatActivity implements ReportViewInterface {

    static final String TAG = "Report Activity";

    ReportPresenterInterface reportPresenter;
    ActivityReportBinding binding;

    TokenData tokenData;
    int storeId;
    String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report);
        setupMVP();
        tokenData = getDataFromToken();

        storeId = getIntent().getIntExtra("store_id", 0);
        storeName = getIntent().getStringExtra("store_name");
        binding.reportStoreNameInput.setText(getIntent().getStringExtra("store_name"));

        binding.reportBackButton.setOnClickListener(view -> finish());
        binding.reportSubmitButton.setOnClickListener(view -> {
            String desc = binding.reportReasonInput.getText().toString();
            reportPresenter.sendReport(tokenData.userId, storeId, tokenData.email, storeName, desc);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reportPresenter.disposeObserver();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showFinish() {
        Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    private void setupMVP() {
        reportPresenter = new ReportPresenter(this);
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