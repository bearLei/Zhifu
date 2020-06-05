package com.ubtech.zhifu.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lzy.okgo.cache.CacheEntity;
import com.ubtech.zhifu.R;
import com.ubtech.zhifu.utils.SpUtils;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class SettingActivity extends Activity implements View.OnClickListener {
    private Button bt_back;
    private Button bt_save;
    private EditText et_key;
    private EditText et_no;
    private EditText et_notifyurl;
    private RelativeLayout rl_back;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.activity_setting);
        this.et_notifyurl = (EditText) findViewById(R.id.et_notifyurl);
        if (!TextUtils.isEmpty(SpUtils.getString(getApplicationContext(), "notifyurl"))) {
            this.et_notifyurl.setText(SpUtils.getString(getApplicationContext(), "notifyurl"));
        }
        this.et_no = (EditText) findViewById(R.id.et_no);
        if (!TextUtils.isEmpty(SpUtils.getString(getApplicationContext(), "no"))) {
            this.et_no.setText(SpUtils.getString(getApplicationContext(), "no"));
        }
        this.et_key = (EditText) findViewById(R.id.et_key);
        if (!TextUtils.isEmpty(SpUtils.getString(getApplicationContext(), CacheEntity.KEY))) {
            this.et_key.setText(SpUtils.getString(getApplicationContext(), CacheEntity.KEY));
        }
        this.bt_save = (Button) findViewById(R.id.save);
        this.bt_back = (Button) findViewById(R.id.back);
        this.rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        this.bt_back.setOnClickListener(this);
        this.bt_save.setOnClickListener(this);
        this.rl_back.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                return;
            case R.id.rl_back:
                finish();
                return;
            case R.id.save:
                String notifyurl = this.et_notifyurl.getText().toString();
                String key = this.et_key.getText().toString();
                String no = this.et_no.getText().toString();
                if (TextUtils.isEmpty(notifyurl)) {
                    Toast.makeText(getApplicationContext(), "异步通知地址不能为空！", 1).show();
                    return;
                }
                SpUtils.putString(getApplicationContext(), "notifyurl", notifyurl);
                if (TextUtils.isEmpty(no)) {
                    Toast.makeText(getApplicationContext(), "设备号不能为空！", 1).show();
                    return;
                }
                SpUtils.putString(getApplicationContext(), "no", no);
                if (TextUtils.isEmpty(key)) {
                    Toast.makeText(getApplicationContext(), "signKey不能为空！", 1).show();
                    return;
                }
                SpUtils.putString(getApplicationContext(), CacheEntity.KEY, key);
                Toast.makeText(getApplicationContext(), "保存成功", 1).show();
                finish();
                return;
            default:
                return;
        }
    }

}
