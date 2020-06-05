package com.ubtech.zhifu.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.ubtech.zhifu.db.DBManager;
import com.ubtech.zhifu.ui.CustomApplcation;
import com.ubtech.zhifu.ui.MainActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class PayHelperUtils {
    public static String ALIPAYSTART_ACTION = "com.payhelper.alipay.start";
    public static final String Action_SaveSpParams = "com.yjz.save.params";
    public static String GET_PERSON_MOENY_NOTICE = "GET_PERSON_MOENY_NOTICE";
    public static String LOGINIDRECEIVED_ACTION = "com.tools.payhelper.loginidreceived";
    public static String MSGRECEIVED_ACTION = "com.tools.payhelper.msgreceived";
    public static String QQSTART_ACTION = "com.payhelper.qq.start";
    public static String TRADENORECEIVED_ACTION = "com.tools.payhelper.tradenoreceived";
    public static String WECHATSTART_ACTION = "com.payhelper.wechat.start";

    @SuppressLint("WrongConstant")
    public static void startAPP() {
        try {
            Intent intent = new Intent(CustomApplcation.getInstance().getApplicationContext(), MainActivity.class);
            intent.addFlags(268435456);
            CustomApplcation.getInstance().getApplicationContext().startActivity(intent);
        } catch (Exception e) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0058 A[SYNTHETIC, Splitter:B:25:0x0058] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String imageToBase64(java.lang.String r7) {
        /*
            boolean r5 = android.text.TextUtils.isEmpty(r7)
            if (r5 == 0) goto L_0x0008
            r4 = 0
        L_0x0007:
            return r4
        L_0x0008:
            r2 = 0
            r0 = 0
            r4 = 0
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0046 }
            r3.<init>(r7)     // Catch:{ IOException -> 0x0046 }
            int r5 = r3.available()     // Catch:{ IOException -> 0x0064, all -> 0x0061 }
            byte[] r0 = new byte[r5]     // Catch:{ IOException -> 0x0064, all -> 0x0061 }
            r3.read(r0)     // Catch:{ IOException -> 0x0064, all -> 0x0061 }
            r5 = 0
            java.lang.String r4 = android.util.Base64.encodeToString(r0, r5)     // Catch:{ IOException -> 0x0064, all -> 0x0061 }
            if (r3 == 0) goto L_0x0067
            r3.close()     // Catch:{ IOException -> 0x0040 }
            r2 = r3
        L_0x0024:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "\"data:image/gif;base64,"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r4)
            java.lang.String r6 = "\""
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r4 = r5.toString()
            goto L_0x0007
        L_0x0040:
            r1 = move-exception
            r1.printStackTrace()
            r2 = r3
            goto L_0x0024
        L_0x0046:
            r1 = move-exception
        L_0x0047:
            r1.printStackTrace()     // Catch:{ all -> 0x0055 }
            if (r2 == 0) goto L_0x0024
            r2.close()     // Catch:{ IOException -> 0x0050 }
            goto L_0x0024
        L_0x0050:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0024
        L_0x0055:
            r5 = move-exception
        L_0x0056:
            if (r2 == 0) goto L_0x005b
            r2.close()     // Catch:{ IOException -> 0x005c }
        L_0x005b:
            throw r5
        L_0x005c:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x005b
        L_0x0061:
            r5 = move-exception
            r2 = r3
            goto L_0x0056
        L_0x0064:
            r1 = move-exception
            r2 = r3
            goto L_0x0047
        L_0x0067:
            r2 = r3
            goto L_0x0024
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yjz.zpay.utils.PayHelperUtils.imageToBase64(java.lang.String):java.lang.String");
    }

    public static void sendAppMsg(String money, String mark, String type, Context context) {
        Intent broadCastIntent = new Intent();
        if (type.equals("alipay")) {
            broadCastIntent.setAction(ALIPAYSTART_ACTION);
        } else if (type.equals("wechat")) {
            broadCastIntent.setAction(WECHATSTART_ACTION);
        } else if (type.equals("qq")) {
            broadCastIntent.setAction(QQSTART_ACTION);
        }
        broadCastIntent.putExtra("mark", mark);
        broadCastIntent.putExtra("money", money);
        context.sendBroadcast(broadCastIntent);
    }

    public static String stampToDate(String s) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1000 * Long.parseLong(s)));
    }

    public static boolean isAppRunning(Context context, String packageName) {
        List<ActivityManager.RunningTaskInfo> list = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void startAPP(Context context, String appPackageName) {
        try {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(appPackageName));
        } catch (Exception e) {
            sendmsg(context, "startAPP异常" + e.getMessage());
        }
    }

    public static void notify(Context context, String type, String no, String money, String mark, String dt) {
        String notifyurl = SpUtils.getString(context, "notifyurl");
        String signkey = SpUtils.getString(context, "signkey");
        sendmsg(context, "订单" + no + "重试发送异步通知...");
        if (TextUtils.isEmpty(notifyurl) || TextUtils.isEmpty(signkey)) {
            sendmsg(context, "发送异步通知异常，异步通知地址为空");
            update(no, "异步通知地址为空");
        } else if (type.equals("alipay")) {
            String account = SpUtils.getString(context, "alipay");
        } else if (type.equals("wechat")) {
            String account2 = SpUtils.getString(context, "wechat");
        } else if (type.equals("qq")) {
            String account3 = SpUtils.getString(context, "qq");
        }
    }

    private static void update(String no, String result) {
        new DBManager(CustomApplcation.getInstance().getApplicationContext()).updateOrder(no, result);
    }

    public static String getCookieStr(ClassLoader appClassLoader) {
        XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.transportext.biz.appevent.AmnetUserInfo", appClassLoader), "getSessionid", new Object[0]);
        Context context = (Context) XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.transportext.biz.shared.ExtTransportEnv", appClassLoader), "getAppContext", new Object[0]);
        if (context == null) {
            sendmsg(context, "异常context为空");
            return "";
        } else if (XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.helper.ReadSettingServerUrl", appClassLoader), "getInstance", new Object[0]) != null) {
            return (String) XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.transport.http.GwCookieCacheHelper", appClassLoader), "getCookie", new Object[]{".alipay.com"});
        } else {
            sendmsg(context, "异常readSettingServerUrl为空");
            return "";
        }
    }

    public static void sendBroadcastAgencyQr(Context context, String money, String alipayId) {
        Intent broadCastIntent = new Intent();
        broadCastIntent.putExtra("money", money);
        broadCastIntent.putExtra("alipayId", alipayId);
        broadCastIntent.setAction(TRADENORECEIVED_ACTION);
        context.sendBroadcast(broadCastIntent);
    }

    public static void getTradeInfo(final Context context, String cookie) {
        long current = System.currentTimeMillis();
        String c = getCurrentDate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.HEAD_KEY_COOKIE, cookie);
        httpHeaders.put("Referer", "https://render.alipay.com/p/z/merchant-mgnt/simple-order.html?beginTime=" + c + "&endTime=" + c + "&fromBill=true&channelType=ALL");
        ((GetRequest) OkGo.get("https://mbillexprod.alipay.com/enterprise/simpleTradeOrderQuery.json?beginTime=" + (current - 864000000) + "&limitTime=" + current + "&pageSize=20&pageNum=1&channelType=ALL").headers(httpHeaders)).execute(new StringCallback() {
            public void onSuccess(Response<String> response) {
                String result = response.body();
                Log.d("##########--->账单接口返回-->", result);
                try {
                    new JSONObject(result).getJSONObject("result").getJSONArray("list");
                } catch (Exception e) {
                    PayHelperUtils.sendmsg(context, "getTradeInfo异常" + e.getMessage());
                }
            }
        });
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
    }

    public static void sendmsg(Context context, String msg) {
        Intent broadCastIntent = new Intent();
        broadCastIntent.putExtra(NotificationCompat.CATEGORY_MESSAGE, msg);
        broadCastIntent.setAction(MSGRECEIVED_ACTION);
        context.sendBroadcast(broadCastIntent);
    }

    public static int getVersionCode(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            sendmsg(mContext, "getVersionCode异常" + e.getMessage());
            return 0;
        }
    }

    public static String getVerName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            sendmsg(context, "getVerName异常" + e.getMessage());
            return "";
        }
    }

    public static boolean isreg(Activity activity, String name) {
        Intent intent = new Intent();
        intent.setAction(name);
        List<ResolveInfo> resolveInfos = activity.getPackageManager().queryBroadcastReceivers(intent, 0);
        if (resolveInfos == null || resolveInfos.isEmpty()) {
            return false;
        }
        return true;
    }

    public static int isActivityTop(Context context) {
        try {
            @SuppressLint("WrongConstant") ActivityManager manager = (ActivityManager) context.getSystemService("activity");
            for (ActivityManager.RunningServiceInfo next : manager.getRunningServices(100)) {
            }
            for (ActivityManager.RunningTaskInfo runningTaskInfo : manager.getRunningTasks(100)) {
                if (runningTaskInfo.topActivity.getClassName().equals("cooperation.qwallet.plugin.QWalletPluginProxyActivity")) {
                    return runningTaskInfo.numActivities;
                }
            }
            return 0;
        } catch (SecurityException e) {
            sendmsg(context, e.getMessage());
            return 0;
        }
    }

    public static String getAlipayLoginId(ClassLoader classLoader) {
        try {
            Class<?> AlipayApplication = XposedHelpers.findClass("com.alipay.mobile.framework.AlipayApplication", classLoader);
            Class<?> SocialSdkContactService = XposedHelpers.findClass("com.alipay.mobile.personalbase.service.SocialSdkContactService", classLoader);
            return XposedHelpers.getObjectField(XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers.callStaticMethod(AlipayApplication, "getInstance", new Object[0]), "getMicroApplicationContext", new Object[0]), "findServiceByInterface", new Object[]{SocialSdkContactService.getName()}), "getMyAccountInfoModelByLocal", new Object[0]), "userId").toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getAlipayLoginAccount(ClassLoader classLoader) {
        try {
            Class<?> AlipayApplication = XposedHelpers.findClass("com.alipay.mobile.framework.AlipayApplication", classLoader);
            Class<?> SocialSdkContactService = XposedHelpers.findClass("com.alipay.mobile.personalbase.service.SocialSdkContactService", classLoader);
            return XposedHelpers.getObjectField(XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers.callStaticMethod(AlipayApplication, "getInstance", new Object[0]), "getMicroApplicationContext", new Object[0]), "findServiceByInterface", new Object[]{SocialSdkContactService.getName()}), "getMyAccountInfoModelByLocal", new Object[0]), "loginId").toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static void sendLoginId(String loginid, String loginAccount, String type, Context context) {
        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction(LOGINIDRECEIVED_ACTION);
        broadCastIntent.putExtra("type", type);
        broadCastIntent.putExtra("loginid", loginid);
        broadCastIntent.putExtra("loginAccount", loginAccount);
        context.sendBroadcast(broadCastIntent);
    }

    public static void startAlipayMonitor(final Context context) {
        try {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    L.d("轮询获取订单数据...");
                    SpUtils.putString(context, "time", (System.currentTimeMillis() / 1000) + "");
                    Intent intent1 = new Intent();
                    intent1.setAction(PayHelperUtils.ALIPAYSTART_ACTION);
                    context.sendBroadcast(intent1);
                }
            }, 0, (long) 12000);
        } catch (Exception e) {
            sendmsg(context, "startAlipayMonitor->>" + e.getMessage());
        }
    }

}
