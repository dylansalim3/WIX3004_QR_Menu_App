package com.dylansalim.qrmenuapp.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.ui.login_registration.LoginRegistrationActivity;
import com.dylansalim.qrmenuapp.ui.qr_scan.QRScanActivity;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        navigateToLogin();
    }

    public void navigateToLogin(){
        Intent intent = new Intent(OnboardingActivity.this, LoginRegistrationActivity.class);
        startActivity(intent);
        finish();
    }

}