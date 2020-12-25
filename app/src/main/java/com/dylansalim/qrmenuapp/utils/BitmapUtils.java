package com.dylansalim.qrmenuapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class BitmapUtils {
    public static Bitmap base64ToBitmapConverter(String base64) {
        String[] base64Strings = base64.split(",");
        byte[] decodedString = Base64.decode(base64Strings[1], Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
