package com.dylansalim.qrmenuapp.ui.QRScan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;

import java.util.Objects;

public class QRScanActivity extends AppCompatActivity implements QRScanViewInterface {

    private QRScanPresenter qrScanPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        setupMVP();

        String token = Objects.requireNonNull(getIntent().getExtras()).getString(getString(R.string.token));
        qrScanPresenter.setToken(token);
        qrScanPresenter.checkUserType(this);
    }

    private void setupMVP() {
        qrScanPresenter = new QRScanPresenter(this, getApplicationContext());
    }

    @Override
    public void navigateToNextScreen(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

    @Override
    public void displayError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}