package com.ubtech.zhifu.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ubtech.zhifu.R;
import com.ubtech.zhifu.utils.PayHelperUtils;
import com.ubtech.zhifu.utils.SpUtils;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class DaemonService  extends Service {
    public static final int NOTICE_ID = 100;
    public static String NOTIFY_ACTION = "com.tools.payhelper.notify";

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    public void onCreate() {
        super.onCreate();
        Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= 26) {
            XposedBridge.log("=>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            NotificationChannel notificationChannel = new NotificationChannel("com.yjz.zpay.setvice.DaemonService", "PpService", 4);
            notificationChannel.enableLights(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(1);
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(notificationChannel);
            startForeground(100, builder.setChannelId("com.yjz.zpay.setvice.DaemonService").setContentTitle("支付监控").setContentText("支付监控正在运行中...").build());
        } else if (Build.VERSION.SDK_INT >= 18) {
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setAutoCancel(false);
            builder.setOngoing(true);
            startForeground(100, builder.build());
        } else {
            startForeground(100, new Notification());
        }
        PayHelperUtils.sendmsg(getApplicationContext(), "启动定时任务");
        AlarmManager manager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM);
        int time = SpUtils.getInt(getApplicationContext(), "time");
        int triggerTime = 7000;
        if (time != 0) {
            triggerTime = time * 1000;
        }
        manager.setRepeating(0, System.currentTimeMillis(), (long) triggerTime, PendingIntent.getBroadcast(this, 0, new Intent(NOTIFY_ACTION), 134217728));
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    @SuppressLint("WrongConstant")
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= 18) {
            ((NotificationManager) getSystemService("notification")).cancel(100);
        }
        startService(new Intent(getApplicationContext(), DaemonService.class));
    }

}
