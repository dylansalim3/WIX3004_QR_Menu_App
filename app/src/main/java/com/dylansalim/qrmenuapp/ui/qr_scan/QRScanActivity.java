package com.dylansalim.qrmenuapp.ui.qr_scan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.ui.store_registration.StoreRegistrationActivity;

import java.util.Objects;

public class QRScanActivity extends AppCompatActivity implements QRScanViewInterface {

    private QRScanPresenter qrScanPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        setupMVP();

        qrScanPresenter.checkUserType(this);
    }

    private void setupMVP() {
        qrScanPresenter = new QRScanPresenter(this);
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

    @Override
    public void showStoreNotFoundAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Setup Store?");

        // Setting Dialog Message
        alertDialog.setMessage("You have not setup a store yet. Setup now?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Go to Registration", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                navigateToNextScreen(StoreRegistrationActivity.class);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}