package com.ubtech.zhifu.hook;

import android.util.Log;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class AlipayHookHelper {
    public static void rpcInvoke(Class<?> rpcClass, final AlipayRpcRunnable runnable, final String methodName, final Object... args) {
        try {
            ClassLoader classLoader = AlipayHook.mClassLoader;
            final Object rpcServiceObject = XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.framework.AlipayApplication", classLoader), "getInstance", new Object[0]), "getMicroApplicationContext", new Object[0]), "findServiceByInterface", new Object[]{XposedHelpers.findClass("com.alipay.mobile.framework.service.common.RpcService", classLoader).getName()}), "getRpcProxy", new Object[]{rpcClass});
            new Thread(new Runnable() {
                public void run() {
                    runnable.rpcResult(XposedHelpers.callMethod(rpcServiceObject, methodName, args));
                }
            }).start();
        } catch (Exception ex) {
            Log.i("testBroadReceived:", "rpcInvoke Err:" + ex);
            ex.printStackTrace();
        }
    }

    public static void getBillList(final AlipayRpcRunnable runnable) {
        try {
            ClassLoader classLoader = AlipayHook.mClassLoader;
            Class<?> rpcClass = XposedHelpers.findClass("com.alipay.mobilebill.biz.rpc.bill.v9.pb.BillListPBRPCService", classLoader);
            Log.i("testBroadReceived:", "rpcClass:" + rpcClass);
            Class<?> reqClass = XposedHelpers.findClass("com.alipay.mobilebill.common.service.model.pb.QueryListReq", classLoader);
            Log.i("testBroadReceived:", "reqClass:" + reqClass);
            Object singleCreateReq = reqClass.newInstance();
            rpcInvoke(rpcClass, new AlipayRpcRunnable() {
                public void rpcResult(Object obj) {
                    runnable.rpcResult(obj);
                }
            }, "query", singleCreateReq);
        } catch (Exception ex) {
            Log.i("testBroadReceived:", "getBillList Err:" + ex);
            ex.printStackTrace();
        }
    }

    public static void getSubBillList(final AlipayRpcRunnable runnable) {
        try {
            ClassLoader classLoader = AlipayHook.mClassLoader;
            Class<?> rpcClass = XposedHelpers.findClass("com.alipay.mobilebill.biz.rpc.bill.v9.BillListRPCService", classLoader);
            Log.i("testBroadReceived:", "rpcClass:" + rpcClass);
            Class<?> reqClass = XposedHelpers.findClass("com.alipay.mobilebill.common.service.model.req.QueryListReq", classLoader);
            Log.i("testBroadReceived:", "reqClass:" + reqClass);
            Object singleCreateReq = reqClass.newInstance();
            rpcInvoke(rpcClass, new AlipayRpcRunnable() {
                public void rpcResult(Object obj) {
                    runnable.rpcResult(obj);
                }
            }, "query", singleCreateReq);
        } catch (Exception ex) {
            Log.i("testBroadReceived:", "getSubBillList Err:" + ex);
            ex.printStackTrace();
        }
    }

    public static void getBillDetail(String tradeNo, String bizType, final AlipayRpcRunnable runnable) {
        try {
            ClassLoader classLoader = AlipayHook.mClassLoader;
            Class<?> rpcClass = XposedHelpers.findClass("com.alipay.mobilebill.biz.rpc.bill.v9.BillDetailRPCService", classLoader);
            Class<?> reqClass = XposedHelpers.findClass("com.alipay.mobilebill.core.model.billdetail.QueryDetailReq", classLoader);
            Object reqObject = reqClass.newInstance();
            reqClass.getField("tradeNo").set(reqObject, tradeNo);
            reqClass.getField("bizType").set(reqObject, bizType);
            rpcInvoke(rpcClass, new AlipayRpcRunnable() {
                public void rpcResult(Object obj) {
                    runnable.rpcResult(obj);
                }
            }, "query", reqObject);
        } catch (Exception ex) {
            Log.i("testBroadReceived:", "getBillDetail Err:" + ex);
            ex.printStackTrace();
        }
    }

}
