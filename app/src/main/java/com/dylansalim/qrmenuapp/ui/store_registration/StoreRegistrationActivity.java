package com.dylansalim.qrmenuapp.ui.store_registration;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;
import com.dylansalim.qrmenuapp.services.GpsTracker;
import com.dylansalim.qrmenuapp.ui.component.CustomPhoneInputLayout;
import com.dylansalim.qrmenuapp.ui.main.MainActivity;
import com.dylansalim.qrmenuapp.ui.merchant_info.MerchantInfoActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StoreRegistrationActivity extends AppCompatActivity implements StoreRegistrationViewInterface {

    private StoreRegistrationPresenter storeRegistrationPresenter;

    private TextInputLayout mStoreName, mAddress, mPostalCode, mCity, mCountry, mOpeningHour, mClosingHour, mSpecialOpeningNote;
    private MaterialButton mChooseLocationBtn;
    private SwitchMaterial mUseCurrentLocation;
    ViewGroup progressView;
    protected boolean isProgressShowing = false;
    TimePickerDialog picker;

    private CustomPhoneInputLayout mPhoneInputLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupMVP();
        setContentView(R.layout.activity_store_registration);

        mStoreName = (TextInputLayout) findViewById(R.id.til_store_registration_name);
        mAddress = (TextInputLayout) findViewById(R.id.til_store_registration_address);
        mPostalCode = (TextInputLayout) findViewById(R.id.til_store_registration_postal_code);
        mCity = (TextInputLayout) findViewById(R.id.til_store_registration_city);
        mCountry = (TextInputLayout) findViewById(R.id.til_store_registration_country);
        mUseCurrentLocation = (SwitchMaterial) findViewById(R.id.switch_store_registration_current_location);
        MaterialButton mSubmitBtn = (MaterialButton) findViewById(R.id.btn_store_registration_submit);
        Button mBackBtn = findViewById(R.id.btn_store_registration_back);

        mChooseLocationBtn = findViewById(R.id.btn_store_registration_choose_location);
        mOpeningHour = findViewById(R.id.til_store_registration_opening_hour);
        mClosingHour = findViewById(R.id.til_store_registration_closing_hour);
        mSpecialOpeningNote = findViewById(R.id.til_store_registration_special_hour_note);

        mPhoneInputLayout = findViewById(R.id.phone_input_layout);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle && bundle.getBoolean(getResources().getString(R.string.edit_store))) {
            StoreDao storeResult = bundle.getParcelable(getResources().getString(R.string.store_result));
            storeRegistrationPresenter.onRetrieveStoreDetail(storeResult);
            ((TextView) findViewById(R.id.tv_store_registration_title)).setText(getResources().getString(R.string.store_edit_title));
            ((TextView) findViewById(R.id.tv_store_registration_desc)).setText(getResources().getString(R.string.store_edit_desc));
            mSubmitBtn.setText(R.string.update);
        }

        mPhoneInputLayout.setDefaultCountry("MY");

        mBackBtn.setOnClickListener(view -> finish());


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(StoreRegistrationActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.tied_store_registration_opening_hour).setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            // time picker dialog
            picker = new TimePickerDialog(StoreRegistrationActivity.this,
                    (tp, sHour, sMinute) -> Objects.requireNonNull(mOpeningHour.getEditText()).setText(String.format("%02d:%02d", sHour, sMinute)), hour, minutes, false);

            picker.show();
        });

        findViewById(R.id.tied_store_registration_closing_hour).setOnClickListener(view -> {
            TimePickerDialog picker;
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            // time picker dialog
            picker = new TimePickerDialog(StoreRegistrationActivity.this,
                    (tp, sHour, sMinute) -> Objects.requireNonNull(mClosingHour.getEditText()).setText(String.format("%02d:%02d", sHour, sMinute)), hour, minutes, false);
            picker.show();
        });

        mUseCurrentLocation.setOnCheckedChangeListener((compoundButton, b) -> storeRegistrationPresenter.getCurrentLocation(b, StoreRegistrationActivity.this));

        mSubmitBtn.setOnClickListener(view -> storeRegistrationPresenter.onRegisterButtonClicked(StoreRegistrationActivity.this));

        mChooseLocationBtn.setOnClickListener(view -> {
            GpsTracker gpsTracker = new GpsTracker(this);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                Intent intent = new PlacePicker.IntentBuilder()
                        .setLatLong(latitude, longitude)  // Initial Latitude and Longitude the Map will load into
                        .showLatLong(true)  // Show Coordinates in the Activity
                        .setMapZoom(12.0f)  // Map Zoom Level. Default: 14.0
                        .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                        .setMarkerImageImageColor(R.color.colorPrimary)
                        .setFabColor(R.color.colorAccent)
                        .setPrimaryTextColor(R.color.colorTextPrimary) // Change text color of Shortened Address
                        .setSecondaryTextColor(R.color.design_default_color_secondary) // Change text color of full Address
                        .setBottomViewColor(R.color.white) // Change Address View Background Color (Default: White)
                        .setMapRawResourceStyle(R.raw.default_map_style)  //Set Map Style (https://mapstyle.withgoogle.com/)
                        .setMapType(MapType.NORMAL)
                        .setPlaceSearchBar(true, getResources().getString(R.string.map_v2_api)) //Activate GooglePlace Search Bar. Default is false/not activated. SearchBar is a chargeable feature by Google
                        .build(StoreRegistrationActivity.this);
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                storeRegistrationPresenter.onLocationSelected(data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupMVP() {
        storeRegistrationPresenter = new StoreRegistrationPresenter(this);
    }


    @Override
    public String getStoreName() {
        return Objects.requireNonNull(mStoreName.getEditText()).getText().toString();
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
    public String getOpeningHour() {
        return Objects.requireNonNull(mOpeningHour.getEditText()).getText().toString();
    }

    @Override
    public String getClosingHour() {
        return Objects.requireNonNull(mClosingHour.getEditText()).getText().toString();
    }

    @Override
    public String getSpecialOpeningNote() {
        return Objects.requireNonNull(mSpecialOpeningNote.getEditText()).getText().toString();
    }

    @Override
    public CustomPhoneInputLayout getCustomPhoneInputLayout() {
        return mPhoneInputLayout;
    }

    @Override
    public void setStoreName(String storeName) {
        Objects.requireNonNull(mStoreName.getEditText()).setText(storeName);
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
    public void setOpeningHour(String openingHour) {
        Objects.requireNonNull(mOpeningHour.getEditText()).setText(openingHour);
    }

    @Override
    public void setClosingHour(String closingHour) {
        Objects.requireNonNull(mClosingHour.getEditText()).setText(closingHour);
    }

    @Override
    public void setSpecialOpeningNote(String specialOpeningNote) {
        Objects.requireNonNull(mSpecialOpeningNote.getEditText()).setText(specialOpeningNote);
    }


    @Override
    public void displayErrorMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        isProgressShowing = true;
        progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.addView(progressView);
    }

    @Override
    public void hideProgressBar() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }

    @Override
    public void navigateToNextScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFormSubmitted(StoreDao storeDetail) {
        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.message), "Store Detail successfully updated");
        intent.putExtra(getResources().getString(R.string.store_result), storeDetail);
        setResult(MerchantInfoActivity.UPDATE_FORM_REQUEST_CODE, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storeRegistrationPresenter.disposeObserver();
    }
}