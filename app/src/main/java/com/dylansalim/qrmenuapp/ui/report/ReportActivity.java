package com.dylansalim.qrmenuapp.ui.report;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.databinding.ActivityReportBinding;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.ui.component.ConfirmDialog;
import com.dylansalim.qrmenuapp.utils.SharedPrefUtil;

/**
 * Start activity with intent as below
 * - "store_id"   -> int
 * - "store_name" -> String
 */
public class ReportActivity extends AppCompatActivity implements ReportViewInterface {

    static final String TAG = "Report Activity";

    ReportPresenterInterface reportPresenter;
    ActivityReportBinding binding;
    View progressBar;

    int storeId;
    String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report);
        setupMVP();
        UserDetailDao userDetail = SharedPrefUtil.getUserDetail(this);

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
        if (progressBar == null) {
            progressBar = getLayoutInflater().inflate(R.layout.progressbar_layout, null);
            ((ViewGroup) this.findViewById(android.R.id.content).getRootView()).addView(progressBar);
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
    public void showFinish() {
        ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.setDialogText("Your report has been filled successfully");
        dialog.show();
    }

    @Override
    public void showError(String error) {
        ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.setDialogText("Error occurred. Please try again");
        dialog.show();
    }

    private void setupMVP() {
        reportPresenter = new ReportPresenter(this);
    }
}