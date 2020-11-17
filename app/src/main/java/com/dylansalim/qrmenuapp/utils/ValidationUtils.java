package com.dylansalim.qrmenuapp.utils;

import android.util.Patterns;

public class ValidationUtils {
    public static boolean isEmailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password){
        return password != null && password.trim().length() > 5;
    }
}
