package com.dylansalim.qrmenuapp.ui.edit_profile;

import com.dylansalim.qrmenuapp.models.dao.TokenDao;

public interface EditProfileViewInterface {

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

    void saveToken(TokenDao tokenDao);
}
