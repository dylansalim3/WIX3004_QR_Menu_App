package com.dylansalim.qrmenuapp.ui.login_registration.registration;

import android.widget.TextView;

import com.dylansalim.qrmenuapp.models.dto.Role;

public interface RegistrationPresenterInterface {
    void onRegistrationButtonClicked();

    void setSelectedRole(Role role);

    void validateEmail(TextView textView, String text);

    void validatePassword(TextView textView, String text);

    void validateRepeatPassword(TextView textView, String text, String text2);

    void validatePhoneNumber(TextView textView, String text);

    void validateNonNullField(TextView textView, String text);
}
