package com.dylansalim.qrmenuapp.ui.login_registration.login;

import com.dylansalim.qrmenuapp.utils.ValidationUtils;

public class LoginPresenter implements LoginPresenterInterface {
    private LoginViewInterface lvi;

    public LoginPresenter(LoginViewInterface lvi){
        this.lvi = lvi;
    }

    @Override
    public void onLoginButtonClicked() {
        String email = lvi.getEmail();
        String password = lvi.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            lvi.displayError("Please don't leave the field blank");
        } else if (!ValidationUtils.isEmailValid(email)) {
            lvi.displayError("Invalid Email");
        } else if (!ValidationUtils.isPasswordValid(password)) {
            lvi.displayError("Invalid Password Format. The password should consist of at least 5 characters.");
        }

        lvi.submitLoginForm(email,password);
    }
}
