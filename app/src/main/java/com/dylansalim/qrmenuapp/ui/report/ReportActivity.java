package com.dylansalim.qrmenuapp.ui.report;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dto.UserDetail;
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
    View progressBar;

    int storeId;
    String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setupMVP();

        UserDetail userDetail = SharedPrefUtil.getUserDetail(this);
        storeId = getIntent().getIntExtra(getString(R.string.store_id), 0);
        storeName = getIntent().getStringExtra(getString(R.string.store_name));

        ((EditText) findViewById(R.id.report_store_name_input))
                .setText(storeName);

        findViewById(R.id.report_back_button).setOnClickListener(view -> finish());

        findViewById(R.id.report_submit_button).setOnClickListener(view -> {
            String desc = ((EditText) findViewById(R.id.report_reason_input)).getText().toString();
            String token = SharedPrefUtil.getUserToken(this);
            reportPresenter.sendReport(storeId, userDetail.getEmail(), storeName, desc, token);
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
        dialog.setDialogText(getString(R.string.dialog_report));
        dialog.setListener(v -> finish());
        dialog.show();
    }

    @Override
    public void showError(String error) {
        ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.setDialogText(getString(R.string.dialog_error));
        dialog.show();
    }

    private void setupMVP() {
        reportPresenter = new ReportPresenter(this);
    }
}