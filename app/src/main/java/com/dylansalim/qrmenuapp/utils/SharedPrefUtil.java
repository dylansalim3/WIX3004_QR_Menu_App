package com.dylansalim.qrmenuapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.google.gson.Gson;

public class SharedPrefUtil {

    public static UserDetailDao getUserDetail(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        String token = pref.getString(context.getString(R.string.token), "");
        UserDetailDao userDetailDao = null;

        assert token != null;
        if (!token.equals("")) {
            String dataString = null;
            try {
                dataString = JWTUtils.getDataString(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            userDetailDao = new Gson().fromJson(dataString, UserDetailDao.class);
        }
        return userDetailDao;
    }

    public static void removeUserDetail(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        pref.edit().remove(context.getString(R.string.token)).apply();
    }

    public static Boolean getNotificationPref(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.notification), false);
    }

    public static void setNotificationPref(Context context, Boolean notificationPref) {
        SharedPreferences pref = getSharedPreferences(context);
        pref.edit().putBoolean(context.getString(R.string.notification), notificationPref).apply();
    }

    public static Boolean getNotificationSoundPref(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.notification_sound), false);
    }

    public static void setNotificationSoundPref(Context context, Boolean notificationPref) {
        SharedPreferences pref = getSharedPreferences(context);
        pref.edit().putBoolean(context.getString(R.string.notification_sound), notificationPref).apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }
}
