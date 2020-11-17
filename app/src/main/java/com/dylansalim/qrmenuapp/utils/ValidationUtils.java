package com.dylansalim.qrmenuapp.utils;

import android.util.Patterns;

public class ValidationUtils {
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public static boolean isNonNullFieldValid(String field) {
        return field != null && field.length() > 0;
    }

    public static boolean isValidPhoneNumber(String phoneNum) {
        return phoneNum.length() > 10 && phoneNum.charAt(0) == '+';
    }
}
