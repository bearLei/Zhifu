package com.ubtech.zhifu.utils;
import android.text.format.Time;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class TimesUtils {
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TimesUtils() {
        throw new AssertionError();
    }

    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static String getMonth() {
        Time time = new Time("GMT+8");
        time.setToNow();
        int month = time.month + 1;
        if (month < 10) {
            return "0" + month;
        }
        return month + "";
    }

    public static int getCurrentMonth() {
        Time time = new Time("GMT+8");
        time.setToNow();
        return time.month;
    }

    public static int getCurrentYear() {
        Time time = new Time("GMT+8");
        time.setToNow();
        return time.year;
    }

    public static int getCurrentDay() {
        Time time = new Time("GMT+8");
        time.setToNow();
        return time.monthDay;
    }

}
