package com.dylansalim.qrmenuapp.ui.main.account;

import android.view.View;

import com.dylansalim.qrmenuapp.models.dao.TokenDao;

public interface AccountViewInterface {

    void saveUserToken(TokenDao tokenDao);

    void removeUserToken();

    void reopenApp();

    void showDialog(View.OnClickListener clickListener);

    void showLoading();

    void hideLoading();
}
