package com.dylansalim.qrmenuapp.ui.qr_scan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.QRResult;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.ui.login_registration.LoginRegistrationActivity;
import com.dylansalim.qrmenuapp.ui.main.MainActivity;
import com.dylansalim.qrmenuapp.ui.merchant.MerchantActivity;
import com.dylansalim.qrmenuapp.ui.store_registration.StoreRegistrationActivity;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Objects;

public class QRScanActivity extends AppCompatActivity implements QRScanViewInterface {
    private TextView closeTV, qrActivityTV;
    private Button scanButton;
    private QRScanPresenter qrScanPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        closeTV = findViewById(R.id.closeTV);
        qrActivityTV = findViewById(R.id.qrActivityTV);
        scanButton = findViewById(R.id.scanBtn);

        setupMVP();

//      will intent to zxing camera scanner api to scanner qr code
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(QRScanActivity.this);
                integrator.setPrompt("Scan a QrCode to navigate to Menu");
                integrator.setOrientationLocked(false);
                integrator.setCameraId(0);
                integrator.initiateScan();
            }
        });

        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigateToMain = new Intent(QRScanActivity.this, MainActivity.class);
                startActivity(navigateToMain);
                finish();
            }
        });

        qrScanPresenter.checkUserType(this);
        qrActivityTV.setText(qrScanPresenter.getUserName());
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

//  Scanner activity starts here and scan result storeId will save as bundle transfer to merchant activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(getBaseContext(), "Scan Cancelled", Toast.LENGTH_LONG).show();
            }else{
                try{
                    QRResult qrResult = new Gson().fromJson(result.getContents(), QRResult.class);
                    Intent navigateToMerchant = new Intent(this, MerchantActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("storeId", qrResult.getStoreId());
                    navigateToMerchant.putExtras(bundle);
                    startActivity(navigateToMerchant);
                }catch(Exception e){
                    e.printStackTrace();
                    // show invalid qr
                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
            Toast.makeText(getBaseContext(), "Failed to get Scan result. Scan the correct Qr Code", Toast.LENGTH_LONG).show();
        }
    }
}