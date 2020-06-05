package com.ubtech.zhifu.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.ubtech.zhifu.R;
import com.ubtech.zhifu.bean.LoginResult;
import com.ubtech.zhifu.utils.JSONParser;
import com.ubtech.zhifu.utils.SpUtils;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private Button bt_save;
    private EditText etAccount;
    private EditText etPwd;
    /* access modifiers changed from: private */
    public Context mContext;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mContext = this;
        this.etAccount = (EditText) findViewById(R.id.et_account);
        this.etPwd = (EditText) findViewById(R.id.et_password);
        this.bt_save = (Button) findViewById(R.id.save);
        this.bt_save.setOnClickListener(this);
        String name = SpUtils.getString(this.mContext, "login_name");
        String pwd = SpUtils.getString(this.mContext, "login_pwd");
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
            this.etAccount.setText(name);
            this.etPwd.setText(pwd);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                login();
                return;
            default:
                return;
        }
    }

    @SuppressLint("WrongConstant")
    private void login() {
        final String account = this.etAccount.getText().toString();
        final String pwd = this.etPwd.getText().toString();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(getApplicationContext(), "请输入用户名！", 1).show();
        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(getApplicationContext(), "请输入密码！", 1).show();
        } else {
            Toast.makeText(getApplicationContext(), "登陆中..请稍后...", 1).show();
            ((PostRequest) ((PostRequest) OkGo.post("http://yibeipay.com/auth/login").params("username", account, new boolean[0])).params("password", pwd, new boolean[0])).execute(new StringCallback() {
                public void onSuccess(Response<String> response) {
                    SpUtils.putString(LoginActivity.this.mContext, "login_name", account);
                    SpUtils.putString(LoginActivity.this.mContext, "login_pwd", pwd);
                    LoginResult result = (LoginResult) JSONParser.toObject(response.body().toString(), LoginResult.class);
                    if (result == null || result.getCode() != 1) {
                        Toast.makeText(LoginActivity.this.getApplicationContext(), result.getMessage(), 1).show();
                        return;
                    }
                    Toast.makeText(LoginActivity.this.getApplicationContext(), result.getMessage(), 1).show();
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this.mContext, MainActivity.class));
                    LoginActivity.this.finish();
                }

                public void onError(Response<String> response) {
                    super.onError(response);
                }
            });
        }
    }

}
