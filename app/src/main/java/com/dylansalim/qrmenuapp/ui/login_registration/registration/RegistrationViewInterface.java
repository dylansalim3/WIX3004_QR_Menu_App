package com.dylansalim.qrmenuapp.ui.login_registration.registration;

import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;

public interface RegistrationViewInterface {
    String getEmail();

    String getPassword();

    String getRepeatPassword();

    String getFirstName();

    String getLastName();

    String getPhoneNumber();

    String getAddress();

    void submitRegistrationForm(RegistrationDto registrationDto);

    void displayErrorMessage(String s);
}
