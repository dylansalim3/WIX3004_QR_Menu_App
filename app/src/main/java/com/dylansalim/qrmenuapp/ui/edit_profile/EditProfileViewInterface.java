package com.dylansalim.qrmenuapp.ui.edit_profile;

import android.content.ContentResolver;

import com.dylansalim.qrmenuapp.models.dao.TokenDao;

import java.io.File;

public interface EditProfileViewInterface {

    ContentResolver getContentResolver();

    void loadImage(File imageFile);

    void loadImage(String imageUrl);

    void showLoading();

    void hideLoading();

    enum ErrorType {
        REQUEST_FAILED,
        INVALID_PHONE_NUMBER,
        EMPTY_FIRST_NAME,
        EMPTY_LAST_NAME,
        EMPTY_PHONE_NUM,
        EMPTY_ADDRESS
    }

    void showError(ErrorType error);

    void showSuccess();

    void saveToken(TokenDao tokenDao);
}
