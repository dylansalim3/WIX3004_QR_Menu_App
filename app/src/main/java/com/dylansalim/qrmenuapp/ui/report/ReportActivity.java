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
import com.dylansalim.qrmenuapp.utils.TokenData;
import com.google.gson.Gson;

/**
 * Start activity with intent as below
 *   - "store_id"   -> int
 *   - "store_name" -> String
 */
public class ReportActivity extends AppCompatActivity implements ReportViewInterface {

    static final String TAG = "Report Activity";

    ReportPresenterInterface reportPresenter;
    ActivityReportBinding binding;

    int storeId;
    String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report);
        setupMVP();
        UserDetailDao userDetail = TokenData.getUserDetailFromToken(this);

        storeId = getIntent().getIntExtra("store_id", 0);
        storeName = getIntent().getStringExtra("store_name");
        binding.reportStoreNameInput.setText(getIntent().getStringExtra("store_name"));

        binding.reportBackButton.setOnClickListener(view -> finish());
        binding.reportSubmitButton.setOnClickListener(view -> {
            String desc = binding.reportReasonInput.getText().toString();
            reportPresenter.sendReport(userDetail.getId(), storeId, userDetail.getEmail(), storeName, desc);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reportPresenter.disposeObserver();
    }

    @Override
    public void showLoading() {
        //TODO: fang -> loading
    }

    @Override
    public void hideLoading() {
        //TODO: fang -> hide loading
    }

    @Override
    public void showFinish() {
        //TODO: fang -> show success dialog
        Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(String error) {
        //TODO: fang -> show error dialog
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    private void setupMVP() {
        reportPresenter = new ReportPresenter(this);
    }
}