package com.getui.checkwork.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wang on 16/7/11.
 */
public class SharePreUtils {

    private static String filename = "check";
    private static String KEY_EARLY = "early";
    private static String KEY_LAST = "last";

    public static void saveTime(Context context, String key, String time) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, time);
        editor.commit();
    }

    public static String getTime(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void saveEarliestTime(Context context, String time) {
        saveTime(context, KEY_EARLY, time);
    }

    public static void saveLastestTime(Context context, String time) {
        saveTime(context, KEY_LAST, time);
    }

    public static String getEarlyTime(Context context) {
        return getTime(context, KEY_EARLY);
    }

    public static String getLastTime(Context context) {
        return getTime(context, KEY_LAST);
    }
}
