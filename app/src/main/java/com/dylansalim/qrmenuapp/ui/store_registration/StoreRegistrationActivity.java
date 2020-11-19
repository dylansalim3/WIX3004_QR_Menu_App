package com.dylansalim.qrmenuapp.ui.store_registration;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dylansalim.qrmenuapp.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class StoreRegistrationActivity extends AppCompatActivity implements StoreRegistrationViewInterface {

    private StoreRegistrationPresenter storeRegistrationPresenter;

    private TextInputLayout mStoreName, mPhoneNumber, mAddress, mPostalCode, mCity, mCountry;
    private SwitchMaterial mUseCurrentLocation;

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

        storeRegistrationPresenter = new StoreRegistrationPresenter(this);

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
}