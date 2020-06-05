package com.ubtech.zhifu.bean;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class LoginResult {
    private int code;
    private String message;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public String getMessage() {
        return this.message == null ? "" : this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

}
