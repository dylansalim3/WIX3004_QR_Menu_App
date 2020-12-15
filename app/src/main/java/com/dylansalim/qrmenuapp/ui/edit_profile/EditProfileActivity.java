package com.dylansalim.qrmenuapp.ui.edit_profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.TokenDao;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.utils.SharedPrefUtil;


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

        UserDetailDao userDetailDao = SharedPrefUtil.getUserDetail(this);

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
    public void saveToken(TokenDao tokenDao) {
        SharedPrefUtil.setUserDetail(this, tokenDao);
        Log.d(TAG, "new token saved");
    }

    @Override
    public void showLoading() {
        //TODO: fang -> loading
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(ErrorType type) {
        Log.e(TAG, "error -> " + type);
        switch (type) {
            case EMPTY_FIRST_NAME:
            case EMPTY_LAST_NAME:
            case EMPTY_PHONE_NUM:
            case EMPTY_ADDRESS:
            case REQUEST_FAILED:
            case INVALID_PHONE_NUMBER:
        }
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