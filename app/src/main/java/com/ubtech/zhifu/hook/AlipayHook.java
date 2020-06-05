package com.ubtech.zhifu.hook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ubtech.zhifu.db.DBManager;
import com.ubtech.zhifu.utils.JSONParser;
import com.ubtech.zhifu.utils.L;
import com.ubtech.zhifu.utils.PayHelperUtils;
import com.ubtech.zhifu.utils.StringUtils;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class AlipayHook {
    public static String BILLRECEIVED_ACTION = "com.tools.payhelper.billreceived";
    static DBManager dbManager;
    public static ClassLoader mClassLoader;

    public void hook(final ClassLoader classLoader, final Context context) {
        securityCheckHook(classLoader);
        mClassLoader = classLoader;
        dbManager = new DBManager(context);
        try {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.alipay.android.phone.messageboxstatic.biz.dao.TradeDao", classLoader), "insertMessageInfo", new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    try {
                        L.log("######支付宝个人账号订单start########=");
                        String MessageInfo = (String) XposedHelpers.callMethod(param.args[0], "toString", new Object[0]);
                        L.log("个人账号订单---->" + MessageInfo);
                        String content = StringUtils.getTextCenter(MessageInfo, "content='", "'");
                        if (content.contains("二维码收款") || content.contains("收到一笔转账")) {
                            JSONObject jsonObject = new JSONObject(content);
                            String money = jsonObject.getString("content").replace("￥", "");
                            L.log("收到个人账号订单支付宝支付订单：" + StringUtils.getTextCenter(MessageInfo, "tradeNO=", "&") + "##" + money + "##" + jsonObject.getString("assistMsg2"));
                            AlipayHook.getBillList(context, "D_TRANSFER");
                        }
                        L.log("######支付宝个人账号订单end########=");
                    } catch (Exception e) {
                        L.log(e.getMessage());
                    }
//                    AlipayHook.super.beforeHookedMethod(param);
                }
            });
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.alipay.android.phone.messageboxstatic.biz.dao.ServiceDao", classLoader), "insertMessageInfo", new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    try {
                        L.log("######支付宝商家服务订单start########=");
                        String MessageInfo = (String) XposedHelpers.callMethod(param.args[0], "toString", new Object[0]);
                        L.log("商家服务订单-->" + MessageInfo);
                        L.log("#####----->" + StringUtils.getTextCenter(MessageInfo, "extraInfo='", "'").replace("\\", ""));
                        String moneyQr = StringUtils.getTextCenter(MessageInfo, "mainAmount\":\"", "\"");
                        L.log("####---->moneyQr:" + moneyQr);
                        if (!"error".equals(moneyQr)) {
                            AlipayHook.getBillList(context, "TRADE");
                        }
                        L.log("######支付宝商家服务订单end########=");
                    } catch (Exception e) {
                        PayHelperUtils.sendmsg(context, e.getMessage());
                    }
//                    AlipayHook.super.beforeHookedMethod(param);
                }
            });
            XposedHelpers.findAndHookMethod("com.alipay.mobile.quinox.LauncherActivity", classLoader, "onResume", new Object[]{new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    String loginid = PayHelperUtils.getAlipayLoginId(classLoader);
                    String loginAccount = PayHelperUtils.getAlipayLoginAccount(classLoader);
                    L.log("支付宝的登陆" + loginid + "---" + loginAccount);
                    PayHelperUtils.sendLoginId(loginid, loginAccount, "alipay", context);
                }
            }});
        } catch (Error | Exception e) {
            PayHelperUtils.sendmsg(context, e.getMessage());
        }
    }

    static void replaceDetailPage(Context context) {
        XposedHelpers.findAndHookMethod("com.alipay.mobile.common.rpc.util.RpcInvokerUtil", context.getClassLoader(), "getOperationTypeValue", new Object[]{Method.class, Object[].class, new XC_MethodHook() {
            /* access modifiers changed from: protected */
            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                if (param.getResult() != null) {
                    try {
                        if ("alipay.mobile.bill.QuerySingleBillDetail".equals((String) param.getResult())) {
                            param.setResult("alipay.mobile.bill.QuerySingleBillDetailForH5");
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }});
    }

    static void billDetail(final Context context, final String bizInNo, String bizType, final String money, final String remark) {
        AlipayHookHelper.getBillDetail(bizInNo, bizType, new AlipayRpcRunnable() {
            public void rpcResult(Object obj) {
                try {
                    Log.i("#####testBroadReceived", "rpcResult:" + JSON.toJSONString(obj));
                    String mdata = (String) ((Map) obj.getClass().getDeclaredField("extension").get(obj)).get("mdata");
                    Log.i("####testBroadReceived", "mdata:" + mdata);
                    if (mdata != null) {
                        String conbiz_opp_uid = new JSONObject(mdata).getString("conbiz_opp_uid");
                        Log.i("####testBroadReceived:", "conbiz_opp_uid:" + conbiz_opp_uid);
                        Intent broadCastIntent = new Intent();
                        broadCastIntent.putExtra("bill_no", bizInNo);
                        broadCastIntent.putExtra("bill_money", money);
                        broadCastIntent.putExtra("bill_remark", remark);
                        broadCastIntent.putExtra("bill_type", "alipay");
                        broadCastIntent.putExtra("bill_pay_user_id", conbiz_opp_uid);
                        broadCastIntent.setAction(AlipayHook.BILLRECEIVED_ACTION);
                        context.sendBroadcast(broadCastIntent);
                    }
                } catch (Exception ex) {
                    Log.i("####testBroadReceived:", "getBillDetail Err:" + ex);
                    ex.printStackTrace();
                }
            }
        });
    }

    static void getBillList(final Context context, final String type) {
        L.d("获取账单");
        ClassLoader loader = context.getClassLoader();
        Class ServiceUtil = XposedHelpers.findClass("com.alipay.mobile.beehive.util.ServiceUtil", loader);
        Class RpcService = XposedHelpers.findClass("com.alipay.mobile.framework.service.common.RpcService", loader);
        Class BillListPBRPCService = XposedHelpers.findClass("com.alipay.mobilebill.biz.rpc.bill.v9.pb.BillListPBRPCService", loader);
        final Object RpcProxy = XposedHelpers.callMethod(XposedHelpers.callStaticMethod(ServiceUtil, "getServiceByInterface", new Object[]{RpcService}), "getRpcProxy", new Object[]{BillListPBRPCService});
        final Object QueryListReq = XposedHelpers.newInstance(XposedHelpers.findClass("com.alipay.mobilebill.common.service.model.pb.QueryListReq", loader), new Object[0]);
        XposedHelpers.setObjectField(QueryListReq, "category", "ALL");
        XposedHelpers.setObjectField(QueryListReq, "pageType", "WaitPayConsumeQuery");
        XposedHelpers.setObjectField(QueryListReq, "paging", XposedHelpers.newInstance(XposedHelpers.findClass("com.alipay.mobilebill.common.service.model.pb.PagingCondition", loader), new Object[0]));
        XposedHelpers.setObjectField(QueryListReq, "needMonthSeparator", Boolean.FALSE);
        XposedHelpers.setObjectField(QueryListReq, "scene", "BILL_LIST");
        long currentTimeMillis = System.currentTimeMillis();
        XposedHelpers.setObjectField(QueryListReq, "startTime", Long.valueOf(currentTimeMillis - 180000));
        XposedHelpers.setObjectField(QueryListReq, "endTime", Long.valueOf(currentTimeMillis));
        XposedHelpers.setObjectField(QueryListReq, "dateType", "day");
        new Thread() {
            @SuppressLint({"CheckResult"})
            public void run() {
                List billListItems = (List) XposedHelpers.getObjectField(XposedHelpers.callMethod(RpcProxy, "query", new Object[]{QueryListReq}), "billListItems");
                L.d("抓取到:" + billListItems.size() + "条账单");
                String data = JSONParser.toString(billListItems);
                L.d(data + "-------");
                JSONArray array = JSON.parseArray(data);
                for (int i = 0; i < array.size(); i++) {
                    com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) array.get(i);
                    String orderNo = object.getString("bizInNo");
                    String consumeFee = object.getString("consumeFee").replace("+", "").replace(",", "");
                    String remark = object.getString("consumeTitle");
                    String remark2 = remark.substring(0, remark.indexOf("-"));
                    L.d("订单号:" + orderNo + "金额" + consumeFee);
                    L.d("当前订单号:" + orderNo);
                    AlipayHook.replaceDetailPage(context);
                    AlipayHook.billDetail(context, orderNo, type, consumeFee, remark2);
                }
            }
        }.start();
    }

    private void securityCheckHook(ClassLoader classLoader) {
        try {
            Class<?> securityCheckClazz = XposedHelpers.findClass("com.alipay.mobile.base.security.CI", classLoader);
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", new Object[]{String.class, String.class, String.class, new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Object object = param.getResult();
                    XposedHelpers.setBooleanField(object, "a", false);
                    param.setResult(object);
//                    AlipayHook.super.afterHookedMethod(param);
                }
            }});
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", new Object[]{Class.class, String.class, String.class, new XC_MethodReplacement() {
                /* access modifiers changed from: protected */
                public Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    return (byte) 1;
                }
            }});
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", new Object[]{ClassLoader.class, String.class, new XC_MethodReplacement() {
                /* access modifiers changed from: protected */
                public Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    return (byte) 1;
                }
            }});
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", new Object[]{new XC_MethodReplacement() {
                /* access modifiers changed from: protected */
                public Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    return false;
                }
            }});
        } catch (Error | Exception e) {
            e.printStackTrace();
        }
    }

}
