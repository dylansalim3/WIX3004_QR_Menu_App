package com.dylansalim.qrmenuapp.ui.login_registration.registration;

import android.widget.TextView;

import com.dylansalim.qrmenuapp.models.dao.RoleDao;
import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;
import com.dylansalim.qrmenuapp.utils.ValidationUtils;

public class RegistrationPresenter implements RegistrationPresenterInterface {
    RegistrationViewInterface rvi;
    int roleId = -1;

    public RegistrationPresenter(RegistrationViewInterface rvi) {
        this.rvi = rvi;
    }

    @Override
    public void onRegistrationButtonClicked() {
        String email = rvi.getEmail();
        String password = rvi.getPassword();
        String repeatPassword = rvi.getRepeatPassword();
        String firstName = rvi.getFirstName();
        String lastName = rvi.getLastName();
        String phoneNumber = rvi.getPhoneNumber();
        String address = rvi.getAddress();

        RegistrationDto registrationDto = new RegistrationDto(firstName, lastName, email, password, roleId, phoneNumber, address);

        if (validateForm(registrationDto, repeatPassword)) {
            rvi.submitRegistrationForm(registrationDto);
        }
    }

    private boolean validateForm(RegistrationDto registrationDto, String repeatPassword) {
        String[] nonNullFieldStrings = new String[]{
                registrationDto.getEmail(), registrationDto.getPassword(), repeatPassword, registrationDto.getPhone_num(),
                registrationDto.getFirst_name(), registrationDto.getLast_name(), registrationDto.getAddress()};

        for (String nonNullFieldString : nonNullFieldStrings) {
            if (!ValidationUtils.isNonNullFieldValid(nonNullFieldString)) {
                rvi.displayErrorMessage("Cannot leave fields empty");
                return false;
            }
        }

        if (roleId == -1) {
            rvi.displayErrorMessage("Role is empty");
        }

        if (!ValidationUtils.isEmailValid(registrationDto.getEmail())) {
            rvi.displayErrorMessage("Invalid email format");
            return false;
        }

        if (!ValidationUtils.isPasswordValid(registrationDto.getPassword())) {
            rvi.displayErrorMessage("Invalid password format");
            return false;
        }

        if (!registrationDto.getPassword().equals(repeatPassword)) {
            rvi.displayErrorMessage("Both password is not equal");
            return false;
        }


        if (!ValidationUtils.isValidPhoneNumber(registrationDto.getPhone_num())) {
            rvi.displayErrorMessage("Phone number is not in correct format");
            return false;
        }

        return true;
    }

    @Override
    public void setSelectedRole(RoleDao roleDao) {
        roleId = roleDao.getId();
    }

    @Override
    public void validateEmail(TextView textView, String text) {
        if (!ValidationUtils.isEmailValid(text)) {
            textView.setError("Email is invalid");
        }
    }

    @Override
    public void validatePassword(TextView textView, String text) {
        if (!ValidationUtils.isPasswordValid(text)) {
            textView.setError("Password is invalid");
        }
    }

    @Override
    public void validateRepeatPassword(TextView textView, String text, String text2) {
        if (!ValidationUtils.isPasswordValid(text)) {
            textView.setError("Password is invalid");
        } else if (!text.equals(text2)) {
            textView.setError("Both password is not the same");
        }
    }

    @Override
    public void validatePhoneNumber(TextView textView, String text) {
        if (!ValidationUtils.isValidPhoneNumber(text)) {
            textView.setError("Phone number invalid");
        }
    }

    @Override
    public void validateNonNullField(TextView textView, String text) {
        if (!ValidationUtils.isNonNullFieldValid(text)) {
            textView.setError("Address cannot leave blank");
        }
    }
}
