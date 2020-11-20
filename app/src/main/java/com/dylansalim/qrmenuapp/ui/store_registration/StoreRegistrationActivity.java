package com.dylansalim.qrmenuapp.ui.store_registration;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.ui.merchant_menu.MerchantMenuActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StoreRegistrationActivity extends AppCompatActivity implements StoreRegistrationViewInterface {

    private StoreRegistrationPresenter storeRegistrationPresenter;

    private TextInputLayout mStoreName, mPhoneNumber, mAddress, mPostalCode, mCity, mCountry;
    private SwitchMaterial mUseCurrentLocation;
    ViewGroup progressView;
    protected boolean isProgressShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_registration);
        mStoreName = (TextInputLayout) findViewById(R.id.til_store_registration_name);
        mPhoneNumber = (TextInputLayout) findViewById(R.id.til_store_registration_phone_number);
        mAddress = (TextInputLayout) findViewById(R.id.til_store_registration_address);
        mPostalCode = (TextInputLayout) findViewById(R.id.til_store_registration_postal_code);
        mCity = (TextInputLayout) findViewById(R.id.til_store_registration_city);
        mCountry = (TextInputLayout) findViewById(R.id.til_store_registration_country);
        mUseCurrentLocation = (SwitchMaterial) findViewById(R.id.switch_store_registration_current_location);
        MaterialButton mSubmitBtn = (MaterialButton) findViewById(R.id.btn_store_registration_submit);


        setupMVP();

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(StoreRegistrationActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mUseCurrentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                storeRegistrationPresenter.getCurrentLocation(b, StoreRegistrationActivity.this);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeRegistrationPresenter.onRegisterButtonClicked(StoreRegistrationActivity.this);
            }
        });
    }

    private void setupMVP() {
        storeRegistrationPresenter = new StoreRegistrationPresenter(this);
    }


    @Override
    public String getStoreName() {
        return Objects.requireNonNull(mStoreName.getEditText()).getText().toString();
    }

    @Override
    public String getPhoneNumber() {
        return Objects.requireNonNull(mPhoneNumber.getEditText()).getText().toString();
    }

    @Override
    public String getAddress() {
        return Objects.requireNonNull(mAddress.getEditText()).getText().toString();
    }

    @Override
    public String getPostalCode() {
        return Objects.requireNonNull(mPostalCode.getEditText()).getText().toString();
    }

    @Override
    public String getCity() {
        return Objects.requireNonNull(mCity.getEditText()).getText().toString();
    }

    @Override
    public String getCountry() {
        return Objects.requireNonNull(mCountry.getEditText()).getText().toString();
    }

    @Override
    public void setAddress(String address) {
        Objects.requireNonNull(mAddress.getEditText()).setText(address);
    }

    @Override
    public void setPostalCode(String postalCode) {
        Objects.requireNonNull(mPostalCode.getEditText()).setText(postalCode);

    }

    @Override
    public void setCity(String city) {
        Objects.requireNonNull(mCity.getEditText()).setText(city);

    }

    @Override
    public void setCountry(String country) {
        Objects.requireNonNull(mCountry.getEditText()).setText(country);
    }

    @Override
    public void displayErrorMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
//        isProgressShowing = true;
//        progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);
//        View v = this.findViewById(android.R.id.content).getRootView();
//        ViewGroup viewGroup = (ViewGroup) v;
//        viewGroup.addView(progressView);
    }

    @Override
    public void hideProgressBar() {
//        View v = this.findViewById(android.R.id.content).getRootView();
//        ViewGroup viewGroup = (ViewGroup) v;
//        viewGroup.removeView(progressView);
//        isProgressShowing = false;
    }

    @Override
    public void navigateToNextScreen() {
        Intent intent = new Intent(this, MerchantMenuActivity.class);
        startActivity(intent);
        finish();
    }
}