package com.ubtech.zhifu.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class Utils {
    private static final String TAG = "[^][ Utils ]";

    public static String getSubUtilSimple(String str, String str2) {
        Matcher matcher = Pattern.compile(str2).matcher(str);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static String getNowDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getNowDate(long j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date date = new Date();
        date.setTime(j);
        return simpleDateFormat.format(date);
    }

    public static void xposedLog(String str) {
        XposedBridge.log(str);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            StringBuilder stringBuilder = new StringBuilder(simpleDateFormat.format(date));
            stringBuilder.append(" ");
            stringBuilder.append(str);
            stringBuilder.append("\n");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(Environment.getExternalStorageDirectory().getPath() + "/WX_PAY_" + simpleDateFormat2.format(date) + "_log.txt")));
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            XposedBridge.log("[ Utils ] error 写日志文件异常, " + e.getMessage());
        }
    }

    public static void textLog(String str) {
        Log.i(TAG, str);
    }

}
