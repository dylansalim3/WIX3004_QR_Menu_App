package com.dylansalim.qrmenuapp.ui.login_registration;

import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;

public interface LoginRegistrationPresenterInterface {
    void submitLoginForm(String email, String password);

    void getRoles();

    void submitRegistrationForm(RegistrationDto registrationDto);

    void disposeObserver();
}
