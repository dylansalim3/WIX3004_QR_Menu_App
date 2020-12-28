package com.dylansalim.qrmenuapp.ui.login_registration;

import com.dylansalim.qrmenuapp.models.dto.Role;
import com.dylansalim.qrmenuapp.models.dto.Token;

import java.util.List;

public interface LoginRegistrationViewInterface {
    void showProgressBar();

    void hideProgressBar();

    void displayError(String error);

    void navigateToRegistrationFragment(List<Role> roleList);

    void navigateToNextActivity(Token token);
}
