package com.dylansalim.qrmenuapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dto.Token;
import com.dylansalim.qrmenuapp.models.dto.UserDetail;
import com.google.gson.Gson;

public class SharedPrefUtil {

    public static UserDetail getUserDetail(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        String token = pref.getString(context.getString(R.string.token), "");
        UserDetail userDetail = null;

        assert token != null;
        if (!token.equals("")) {
            String dataString = null;
            try {
                dataString = JWTUtils.getDataString(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            userDetail = new Gson().fromJson(dataString, UserDetail.class);
        }
        return userDetail;
    }

    public static String getUserToken(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        return pref.getString(context.getString(R.string.token), "");
    }

    public static void setUserToken(Context context, Token token) {
        getSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.token), token.getToken())
                .apply();
    }

    public static void removeUserToken(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        pref.edit().remove(context.getString(R.string.token)).apply();
    }

    public static Boolean getNotificationPref(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.notification), true);
    }

    public static void setNotificationPref(Context context, Boolean notificationPref) {
        SharedPreferences pref = getSharedPreferences(context);
        pref.edit().putBoolean(context.getString(R.string.notification), notificationPref).apply();
    }

    public static Boolean getNotificationSoundPref(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.notification_sound), true);
    }

    public static void setNotificationSoundPref(Context context, Boolean notificationPref) {
        SharedPreferences pref = getSharedPreferences(context);
        pref.edit().putBoolean(context.getString(R.string.notification_sound), notificationPref).apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }
}
