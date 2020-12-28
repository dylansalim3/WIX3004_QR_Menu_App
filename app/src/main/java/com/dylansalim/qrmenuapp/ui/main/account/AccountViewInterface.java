package com.dylansalim.qrmenuapp.ui.main.account;

import android.view.View;

import com.dylansalim.qrmenuapp.models.dto.Token;

public interface AccountViewInterface {

    void saveUserToken(Token token);

    void reopenApp();

    enum DialogType {
        SWITCH_MERCHANT, SWITCH_CUSTOMER, LOGOUT, ERROR
    }

    void showDialog(DialogType dialogType, View.OnClickListener clickListener);

    void showLoading();

    void hideLoading();
}
