package com.dylansalim.qrmenuapp.ui.edit_profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.TokenDao;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.ui.component.ConfirmDialog;
import com.dylansalim.qrmenuapp.utils.SharedPrefUtil;


public class EditProfileActivity extends AppCompatActivity implements EditProfileViewInterface {

    final String TAG = "Edit Profile Activity";

    EditProfilePresenterInterface presenter;
    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText address;
    Button saveButton;
    View progressBar;

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
        SharedPrefUtil.setUserToken(this, tokenDao);
        Log.d(TAG, "new token saved");
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
    public void showSuccess() {
        ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.setDialogText("Your profile is updated");
        dialog.setListener(v -> finish());
        dialog.show();
    }

    @Override
    public void showError(ErrorType type) {
        Log.e(TAG, "error -> " + type);
        ConfirmDialog dialog = new ConfirmDialog(this);
        switch (type) {
            case EMPTY_FIRST_NAME:
                dialog.setDialogText("First name cannot be empty");
                break;
            case EMPTY_LAST_NAME:
                dialog.setDialogText("Last name cannot be empty");
                break;
            case EMPTY_PHONE_NUM:
                dialog.setDialogText("Phone number cannot be empty");
                break;
            case INVALID_PHONE_NUMBER:
                dialog.setDialogText("Phone number is not valid");
                break;
            case EMPTY_ADDRESS:
                dialog.setDialogText("Address cannot be empty");
                break;
            case REQUEST_FAILED:
                dialog.setDialogText("Error occurred. Please try again");
                break;
        }
        dialog.show();
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