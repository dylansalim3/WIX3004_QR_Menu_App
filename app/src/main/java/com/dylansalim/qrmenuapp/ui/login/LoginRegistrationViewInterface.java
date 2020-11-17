package com.dylansalim.qrmenuapp.ui.login;

import com.dylansalim.qrmenuapp.models.dao.RoleDao;

import java.util.List;

public interface LoginRegistrationViewInterface {
    void showProgressBar();
    void hideProgressBar();
    void displayError(String error);
    void navigateToRegistrationFragment(List<RoleDao> roleDaoList);
}
