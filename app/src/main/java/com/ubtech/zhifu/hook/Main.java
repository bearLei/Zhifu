package com.ubtech.zhifu.hook;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ubtech.zhifu.utils.L;
import com.ubtech.zhifu.utils.PayHelperUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class Main implements IXposedHookLoadPackage {
    public static String ALIPAY_PACKAGE = "com.eg.android.AlipayGphone";
    public static boolean ALIPAY_PACKAGE_ISHOOK = false;
    public static String WECHAT_PACKAGE = "com.tencent.mm";
    public static boolean WECHAT_PACKAGE_ISHOOK = false;

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.appInfo != null && (lpparam.appInfo.flags & 129) == 0) {
            String packageName = lpparam.packageName;
            final String processName = lpparam.processName;
            if (ALIPAY_PACKAGE.equals(packageName)) {
                L.log("支付宝启动---hook成功");
                Class<Application> cls = Application.class;
                try {
                    XposedHelpers.findAndHookMethod(cls, "attach", new Object[]{Context.class, new XC_MethodHook() {
                        /* access modifiers changed from: protected */
                        public void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
//                            Main.super.afterHookedMethod(param);
                            Context context = (Context) param.args[0];
                            ClassLoader appClassLoader = context.getClassLoader();
                            if (Main.ALIPAY_PACKAGE.equals(processName) && !Main.ALIPAY_PACKAGE_ISHOOK) {
                                Main.ALIPAY_PACKAGE_ISHOOK = true;
                                StartAlipayReceived startAlipay = new StartAlipayReceived();
                                IntentFilter intentFilter = new IntentFilter();
                                intentFilter.addAction(PayHelperUtils.ALIPAYSTART_ACTION);
                                context.registerReceiver(startAlipay, intentFilter);
                                PayHelperUtils.sendmsg(context, "支付宝Hook成功，当前支付宝版本:" + PayHelperUtils.getVerName(context));
                                new AlipayHook().hook(appClassLoader, context);
                            }
                        }
                    }});
                } catch (Throwable e) {
                    L.log(e.getMessage());
                }
            }
        }
    }

    class StartAlipayReceived extends BroadcastReceiver {
        StartAlipayReceived() {
        }

        @SuppressLint({"WrongConstant"})
        public void onReceive(Context context, Intent intent) {
            L.d("收到广播--去爬账单....");
        }
    }

}
