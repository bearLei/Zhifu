package com.ubtech.zhifu.bean;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class WxNotifyBean {
    private String money;
    private String shopName;
    private String time;
    private String timeStr;

    public WxNotifyBean(String shopName2, String money2, String time2, String timeStr2) {
        this.shopName = shopName2;
        this.money = money2;
        this.time = time2;
        this.timeStr = timeStr2;
    }

    public String getShopName() {
        return this.shopName == null ? "" : this.shopName;
    }

    public void setShopName(String shopName2) {
        this.shopName = shopName2;
    }

    public String getMoney() {
        return this.money == null ? "" : this.money;
    }

    public void setMoney(String money2) {
        this.money = money2;
    }

    public String getTime() {
        return this.time == null ? "" : this.time;
    }

    public void setTime(String time2) {
        this.time = time2;
    }

    public String getTimeStr() {
        return this.timeStr == null ? "" : this.timeStr;
    }

    public void setTimeStr(String timeStr2) {
        this.timeStr = timeStr2;
    }

}
