package com.dylansalim.qrmenuapp.ui.main.account;

import android.view.View;

public interface AccountViewInterface {

    void login();

    void removeUserToken();

    void showDialog(View.OnClickListener clickListener);

    void showLoading();

    void hideLoading();
}
