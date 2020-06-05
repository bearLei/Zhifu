package com.ubtech.zhifu.utils;

import android.util.Log;

import com.lzy.okgo.BuildConfig;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class L {
    public static void log(String msg) {
        XposedBridge.log("####---->" + msg);
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d("####---->", msg);
        }
    }

}
