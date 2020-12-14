package com.dylansalim.qrmenuapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.google.gson.Gson;

public class TokenData {

    public static UserDetailDao getUserDetailFromToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(context.getString(R.string.token), "");
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
}
