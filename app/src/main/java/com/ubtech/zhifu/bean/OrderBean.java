package com.ubtech.zhifu.bean;

import java.io.Serializable;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class OrderBean implements Serializable {
    private static final long serialVersionUID = 8988815091574805671L;
    private String money;
    private String no;
    private String payuser;
    private String result;

    public OrderBean() {
    }

    public OrderBean(String money2, String no2, String result2, String payuser2) {
        this.money = money2;
        this.no = no2;
        this.result = result2;
        this.payuser = payuser2;
    }

    public String getMoney() {
        return this.money == null ? "" : this.money;
    }

    public void setMoney(String money2) {
        this.money = money2;
    }

    public String getNo() {
        return this.no == null ? "" : this.no;
    }

    public void setNo(String no2) {
        this.no = no2;
    }

    public String getResult() {
        return this.result == null ? "" : this.result;
    }

    public void setResult(String result2) {
        this.result = result2;
    }

    public String getPayuser() {
        return this.payuser == null ? "" : this.payuser;
    }

    public void setPayuser(String payuser2) {
        this.payuser = payuser2;
    }

}
