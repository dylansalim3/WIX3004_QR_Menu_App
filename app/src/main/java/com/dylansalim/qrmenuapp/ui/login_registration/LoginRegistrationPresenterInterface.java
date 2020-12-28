package com.dylansalim.qrmenuapp.ui.login_registration;

import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;
import com.dylansalim.qrmenuapp.models.dto.Role;

import java.util.List;

import androidx.annotation.Nullable;

public interface LoginRegistrationPresenterInterface {
    void submitLoginForm(String email, String password);

    List<Role> getRoles();

    void retrieveRolesAvailable(@Nullable List<Role> roles);

    void submitRegistrationForm(RegistrationDto registrationDto);

    void disposeObserver();
}
