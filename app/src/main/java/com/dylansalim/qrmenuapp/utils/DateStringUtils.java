package com.dylansalim.qrmenuapp.utils;

public class DateStringUtils {
    public static String convert24HourTo12Hour(String time) {
        String[] timeArr = time.split(":");
        if (timeArr.length > 2) {
            String hour = timeArr[0];
            String min = timeArr[1];


            return String.format("%d:%s %s", Integer.parseInt(hour) % 12, min, Integer.parseInt(hour) / 12 == 1 ? "pm" : "am");
        }
        return "";
    }
}
