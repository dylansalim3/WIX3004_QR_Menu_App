package com.dylansalim.qrmenuapp.ui.login_registration.login;

public interface LoginViewInterface {
    void submitLoginForm(String email, String password);

    String getEmail();

    String getPassword();

    void displayError(String s);
}
