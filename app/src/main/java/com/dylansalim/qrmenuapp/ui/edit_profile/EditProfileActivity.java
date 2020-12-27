package com.dylansalim.qrmenuapp.ui.edit_profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.TokenDao;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.services.FileIOService;
import com.dylansalim.qrmenuapp.ui.component.ConfirmDialog;
import com.dylansalim.qrmenuapp.ui.component.CustomPhoneInputLayout;
import com.dylansalim.qrmenuapp.utils.SharedPrefUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FilePermission;


public class EditProfileActivity extends AppCompatActivity implements EditProfileViewInterface {

    final String TAG = "Edit Profile Activity";
    final int REQUEST = 6393;

    EditProfilePresenterInterface presenter;
    ImageView picture;
    EditText firstName;
    EditText lastName;
    CustomPhoneInputLayout phoneNumber;
    EditText address;
    SwitchCompat location;
    Button saveButton;
    View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setupMVP();

        picture = findViewById(R.id.profile_picture);
        firstName = findViewById(R.id.profile_firstname);
        lastName = findViewById(R.id.profile_lastname);
        phoneNumber = findViewById(R.id.profile_phone_number);
        address = findViewById(R.id.profile_address);
        location = findViewById(R.id.profile_location);
        saveButton = findViewById(R.id.profile_save_button);

        UserDetailDao userDetailDao = SharedPrefUtil.getUserDetail(this);
        presenter.getProfileImage(userDetailDao.getId());

        if (userDetailDao == null) {
            Log.e(TAG, "user token missing");
            return;
        }

        firstName.setText(userDetailDao.getFirstName());
        lastName.setText(userDetailDao.getLastName());
        address.setText(userDetailDao.getAddress());

        phoneNumber.setHint(R.string.phone_number);
        phoneNumber.setPhoneNumber(userDetailDao.getPhoneNum());
        phoneNumber.getEditText().setText( // remove hyphens
                phoneNumber.getEditText().getText().toString().replace("-", "")
        );
        picture.setOnClickListener(view -> {
            if (FileIOService.requestReadIOPermission(this)) {
                pickImage();
            }
        });

        location.setOnCheckedChangeListener((view, isCheck) -> {
            if (isCheck) presenter.getLocation(this);
        });

        saveButton.setOnClickListener(view -> {
            presenter.saveProfile(
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    phoneNumber.getPhoneNumber(),
                    address.getText().toString(),
                    SharedPrefUtil.getUserToken(this)
            );
        });

        findViewById(R.id.profile_back_button).setOnClickListener(view -> finish());
    }

    private void pickImage() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST);
    }

    @Override
    public void loadImage(File imageFile) {
        Log.d(TAG, "load image file");
        Picasso.get()
                .load(imageFile)
                .placeholder(R.drawable.ic_account_circle_white_24dp)
                .fit()
                .into(picture);
    }

    @Override
    public void loadImage(String imageUrl) {
        Log.d(TAG, "load image url");
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_account_circle_white_24dp)
                .fit()
                .into(picture);
    }

    public void setAddress(String address_text) {
        address.setText(address_text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST && resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "Image picked");
            Uri image = data.getData();
            presenter.savePicture(SharedPrefUtil.getUserDetail(this).getId(),
                    image, SharedPrefUtil.getUserToken(this));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && requestCode == FileIOService.PICK_IMAGE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
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
        dialog.setDialogText(getString(R.string.dialog_profile_updated));
        dialog.setListener(v -> finish());
        dialog.show();
    }

    @Override
    public void showError(ErrorType type) {
        Log.e(TAG, "error -> " + type);
        ConfirmDialog dialog = new ConfirmDialog(this);
        switch (type) {
            case EMPTY_FIRST_NAME:
                dialog.setDialogText(getString(R.string.dialog_empty_first_name));
                break;
            case EMPTY_LAST_NAME:
                dialog.setDialogText(getString(R.string.dialog_empty_last_name));
                break;
            case EMPTY_PHONE_NUM:
                dialog.setDialogText(getString(R.string.dialog_empty_phone));
                break;
            case INVALID_PHONE_NUMBER:
                dialog.setDialogText(getString(R.string.dialog_invalid_phone));
                break;
            case EMPTY_ADDRESS:
                dialog.setDialogText(getString(R.string.dialog_empty_address));
                break;
            case REQUEST_FAILED:
                dialog.setDialogText(getString(R.string.dialog_error));
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