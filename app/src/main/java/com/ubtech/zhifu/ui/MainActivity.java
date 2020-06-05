package com.ubtech.zhifu.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.ubtech.zhifu.R;
import com.ubtech.zhifu.bean.OrderBean;
import com.ubtech.zhifu.bean.WxNotifyBean;
import com.ubtech.zhifu.broadcast.AlarmReceiver;
import com.ubtech.zhifu.db.DBManager;
import com.ubtech.zhifu.service.DaemonService;
import com.ubtech.zhifu.utils.L;
import com.ubtech.zhifu.utils.MD5Utils;
import com.ubtech.zhifu.utils.PayHelperUtils;
import com.ubtech.zhifu.utils.SpUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class MainActivity extends AppCompatActivity {

    public static String BILLRECEIVED_ACTION = "com.tools.payhelper.billreceived";
    public static String LOGINIDRECEIVED_ACTION = "com.tools.payhelper.loginidreceived";
    public static String MSGRECEIVED_ACTION = "com.tools.payhelper.msgreceived";
    public static String NOTIFY_ACTION = "com.tools.payhelper.notify";
    public static String QRCODERECEIVED_ACTION = "com.tools.payhelper.qrcodereceived";
    public static String TRADENORECEIVED_ACTION = "com.tools.payhelper.tradenoreceived";
    public static TextView console;
    public static EditText et;
    public static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String txt = msg.getData().getString("log");
            if (MainActivity.console != null) {
                if (MainActivity.console.getText() == null) {
                    MainActivity.console.setText(txt);
                } else if (MainActivity.console.getText().toString().length() > 5000) {
                    MainActivity.console.setText("日志定时清理完成...\n\n" + txt);
                } else {
                    MainActivity.console.setText(MainActivity.console.getText().toString() + "\n\n" + txt);
                }
                MainActivity.scrollView.post(new Runnable() {
                    public void run() {
                        MainActivity.scrollView.fullScroll(130);
                    }
                });
            }
            super.handleMessage(msg);
        }
    };
    /* access modifiers changed from: private */
    public static ScrollView scrollView;
    private AlarmReceiver alarmReceiver;
    private BillReceived billReceived;
    private Button btnBD;
    /* access modifiers changed from: private */
    public String currentAlipay = "";
    /* access modifiers changed from: private */
    public DBManager dbManager;
    /* access modifiers changed from: private */
    public FlowableEmitter flowableEmitter;
    private Context mContext;
    /* access modifiers changed from: private */
    public Subscription mSubscription;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        this.mContext = this;
        setContentView((int) R.layout.activity_main_news);
        console = (TextView) findViewById(R.id.console);
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        et = (EditText) findViewById(R.id.et);
        this.dbManager = new DBManager(this);
        this.btnBD = (Button) findViewById(R.id.start_alipay);
        this.btnBD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.sendmsg("正在补单...");
                for (OrderBean next : MainActivity.this.dbManager.FindAllOrders()) {
                }
            }
        });
        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
        this.billReceived = new BillReceived();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BILLRECEIVED_ACTION);
        intentFilter.addAction(MSGRECEIVED_ACTION);
        intentFilter.addAction(QRCODERECEIVED_ACTION);
        intentFilter.addAction(TRADENORECEIVED_ACTION);
        intentFilter.addAction(LOGINIDRECEIVED_ACTION);
        intentFilter.addAction(PayHelperUtils.Action_SaveSpParams);
        intentFilter.addAction(PayHelperUtils.GET_PERSON_MOENY_NOTICE);
        registerReceiver(this.billReceived, intentFilter);
        this.alarmReceiver = new AlarmReceiver();
        IntentFilter alarmIntentFilter = new IntentFilter();
        alarmIntentFilter.addAction(NOTIFY_ACTION);
        registerReceiver(this.alarmReceiver, alarmIntentFilter);
        startService(new Intent(this.mContext, DaemonService.class));
        L.d("####->" + MD5Utils.encode("alipay20884127626959721.062019062422001495970523146092收钱码收款qwe1233g8vpez274fd4v22e6yocobf7bwc2p1s"));
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.alarmReceiver);
        unregisterReceiver(this.billReceived);
        if (this.mSubscription != null) {
            this.mSubscription.cancel();
        }
        super.onDestroy();
    }

    public static void sendmsg(String txt) {
        Message msg = new Message();
        msg.what = 1;
        Bundle data = new Bundle();
        data.putString("log", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())) + ":  结果:" + txt);
        msg.setData(data);
        try {
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == 4) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    class BillReceived extends BroadcastReceiver {
        BillReceived() {
        }

        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().contentEquals(MainActivity.BILLRECEIVED_ACTION)) {
                    String orderNo = intent.getStringExtra("bill_no");
                    String money = intent.getStringExtra("bill_money");
                    String type = intent.getStringExtra("bill_type");
                    String payUserId = intent.getStringExtra("bill_pay_user_id");
                    String remark = intent.getStringExtra("bill_remark");
                    String typestr = "";
                    if (type.equals("alipay")) {
                        typestr = "支付宝";
                    }
                    long time = System.currentTimeMillis();
                    if (!MainActivity.this.dbManager.isExistAliBOrder(orderNo)) {
                        MainActivity.sendmsg("收到" + typestr + "订单,订单号：" + orderNo + "金额：" + money + "付款人:" + payUserId + "备注:" + remark);
                        MainActivity.this.dbManager.addOrder(new OrderBean(money, orderNo, "", payUserId));
                        MainActivity.this.notifyapi(type, orderNo, money, payUserId, remark, time, 0);
                    }
                } else if (intent.getAction().contentEquals(MainActivity.MSGRECEIVED_ACTION)) {
                    MainActivity.sendmsg(intent.getStringExtra(NotificationCompat.CATEGORY_MESSAGE));
                } else if (intent.getAction().contentEquals(MainActivity.LOGINIDRECEIVED_ACTION)) {
                    String loginid = intent.getStringExtra("loginid");
                    String loginAccount = intent.getStringExtra("loginAccount");
                    String type2 = intent.getStringExtra("type");
                    if (!TextUtils.isEmpty(loginAccount) && type2.equals("alipay") && !loginAccount.equals(MainActivity.this.currentAlipay)) {
                        MainActivity.sendmsg("当前登录支付宝账号userId：" + loginid + "--" + loginAccount);
                        String unused = MainActivity.this.currentAlipay = loginAccount;
                        SpUtils.putString(MainActivity.this.getApplicationContext(), type2, loginid);
                        SpUtils.putString(MainActivity.this.getApplicationContext(), "alipayAccount", loginAccount);
                    }
                }
            } catch (Exception e) {
                PayHelperUtils.sendmsg(context, "BillReceived异常" + e.getMessage());
            }
        }
    }

    public void notifyapi(String type, String orderNo, String money, String payUserId, String remark, long time, int count) {
        try {
            String no = SpUtils.getString(getApplicationContext(), "no");
            String key = SpUtils.getString(getApplicationContext(), CacheEntity.KEY);
            String string = SpUtils.getString(getApplicationContext(), "alipay");
            String notifyurl = SpUtils.getString(getApplicationContext(), "notifyurl");
            if (TextUtils.isEmpty(no) || TextUtils.isEmpty(key)) {
                sendmsg("请先配置参数");
                return;
            }
            String str = money;
            String str2 = orderNo;
            long j = time;
            String encode = MD5Utils.encode(payUserId + money + orderNo + time + no + key);
            final String str3 = money;
            final String str4 = payUserId;
            final String str5 = orderNo;
            final String str6 = remark;
            final int i = count;
            final String str7 = type;
            final long j2 = time;
            ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) OkGo.post(notifyurl).params("userId", payUserId, new boolean[0])).params("money", str, new boolean[0])).params("orderNo", str2, new boolean[0])).params("no", no, new boolean[0])).params("time", j, new boolean[0])).params("sign", encode, new boolean[0])).execute(new StringCallback() {
                public void onSuccess(Response<String> response) {
                    L.d("##########--->异步通知成功");
                    MainActivity.sendmsg("支付宝回调->" + response.body().toString() + "\n金额:" + str3 + "\n支付人:" + str4 + "\n流水号:" + str5 + "\n备注:" + str6);
                    if (response.body().contains("success")) {
                        MainActivity.this.dbManager.updateOrder(str5, "success");
                    }
                }

                public void onError(Response<String> response) {
                    super.onError(response);
                    if (i == 0) {
                        MainActivity.this.notifyapi(str7, str5, str3, str4, str6, j2, 1);
                        return;
                    }
                    L.d("##########--->异步通知错误");
                    MainActivity.sendmsg("支付宝回调失败->\n金额:" + str3 + "\n支付人:" + str4 + "\n流水号:" + str5 + "\n备注:" + str6);
                }
            });
        } catch (Exception e) {
            sendmsg("notifyapi异常" + e.getMessage());
        }
    }

    @SuppressLint("WrongConstant")
    private void moveToFont() {
        Intent start = new Intent(getApplicationContext(), MainActivity.class);
        start.setFlags(268566528);
        startActivity(start);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initQRCodeQueue() {
        Flowable.create(new FlowableOnSubscribe<WxNotifyBean>() {
            public void subscribe(FlowableEmitter<WxNotifyBean> emitter) throws Exception {
                FlowableEmitter unused = MainActivity.this.flowableEmitter = emitter;
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<WxNotifyBean>() {
            public void onSubscribe(Subscription s) {
                Subscription unused = MainActivity.this.mSubscription = s;
                s.request(1);
            }

            public void onNext(WxNotifyBean bean) {
                if (bean != null) {
                }
            }

            public void onError(Throwable t) {
            }

            public void onComplete() {
            }
        });
    }

}
