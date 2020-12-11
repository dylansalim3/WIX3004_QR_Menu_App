package com.dylansalim.qrmenuapp.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;

public class SessionService {
    public static UserDetailDao getUserDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(context.getString(R.string.token), "");


        if (!token.equals("")) {
            String dataString = null;
            try {
                dataString = JWTUtils.getDataString(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Gson().fromJson(dataString, UserDetailDao.class);
        }
        return null;
    }
}
