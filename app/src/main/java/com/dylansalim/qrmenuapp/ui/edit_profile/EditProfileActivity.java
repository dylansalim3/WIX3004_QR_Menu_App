package com.dylansalim.qrmenuapp.ui.edit_profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.utils.TokenData;

// TODO: fang -> address editText multiline (refer report reason)

public class EditProfileActivity extends AppCompatActivity implements EditProfileViewInterface {

    final String TAG = "Edit Profile Activity";

    EditProfilePresenterInterface presenter;
    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText address;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setupMVP();

        firstName = findViewById(R.id.profile_firstname);
        lastName = findViewById(R.id.profile_lastname);
        phoneNumber = findViewById(R.id.profile_phonenumber);
        address = findViewById(R.id.profile_address);
        saveButton = findViewById(R.id.profile_save_button);

        UserDetailDao userDetailDao = TokenData.getUserDetailFromToken(this);

        if (userDetailDao == null) {
            Log.e(TAG, "user detail token missing");
            return;
        }

        firstName.setText(userDetailDao.getFirstName());
        lastName.setText(userDetailDao.getLastName());
        phoneNumber.setText(userDetailDao.getPhoneNum());

        saveButton.setOnClickListener(view -> {
            presenter.saveProfile(
                    userDetailDao.getId(),
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    phoneNumber.getText().toString(),
                    address.getText().toString()
            );
        });

        findViewById(R.id.profile_back_button).setOnClickListener(view -> finish());
    }

    @Override
    public void showLoading() {
        //TODO: fang -> loading
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.disposeObserver();
    }

    private void setupMVP() {
        presenter = new EditProfilePresenter(this);
    }
}