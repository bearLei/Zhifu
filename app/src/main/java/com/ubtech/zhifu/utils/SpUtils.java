package com.ubtech.zhifu.utils;
import android.content.Context;
import android.content.SharedPreferences;
import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class SpUtils {
    private static final String SHARED_PATH = "com.yjz.zpay";
    private static final String SHARED_PATH_XP = "com.yjz.zpay_preferences";

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PATH, 0);
    }

    public static SharedPreferences getXpSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PATH_XP, 0);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor edit = getDefaultSharedPreferences(context).edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(Context context, String key) {
        return getDefaultSharedPreferences(context).getInt(key, 0);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor edit = getDefaultSharedPreferences(context).edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context, String key) {
        return getDefaultSharedPreferences(context).getString(key, (String) null);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor edit = getDefaultSharedPreferences(context).edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getDefaultSharedPreferences(context).getBoolean(key, defValue);
    }

    public static String getStringXp(String key, String def) {
        return new XSharedPreferences(SHARED_PATH).getString(key, def);
    }

    public static void putStringXp(Context context, String key, String value) {
        SharedPreferences.Editor edit = getXpSharedPreferences(context).edit();
        edit.putString(key, value);
        edit.commit();
    }

}
