package com.ubtech.zhifu.bean;

import java.io.Serializable;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class QrCodeBean implements Serializable {
    private static final long serialVersionUID = 8988815091574805671L;
    private String dt;
    private String mark;
    private String money;
    private String payurl;
    private String type;

    public QrCodeBean(String money2, String mark2, String type2, String payurl2, String dt2) {
        this.money = money2;
        this.mark = mark2;
        this.type = type2;
        this.payurl = payurl2;
        this.dt = dt2;
    }

    public QrCodeBean() {
    }

    public String getMoney() {
        return this.money;
    }

    public void setMoney(String money2) {
        this.money = money2;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String mark2) {
        this.mark = mark2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getPayurl() {
        return this.payurl;
    }

    public void setPayurl(String payurl2) {
        this.payurl = payurl2;
    }

    public String getDt() {
        return this.dt;
    }

    public void setDt(String dt2) {
        this.dt = dt2;
    }

}
