package com.dylansalim.qrmenuapp.ui.login_registration;

import com.dylansalim.qrmenuapp.models.dao.RoleDao;
import com.dylansalim.qrmenuapp.models.dao.TokenDao;

import java.util.List;

public interface LoginRegistrationViewInterface {
    void showProgressBar();

    void hideProgressBar();

    void displayError(String error);

    void navigateToRegistrationFragment(List<RoleDao> roleDaoList);

    void navigateToNextActivity(TokenDao tokenDao);
}
