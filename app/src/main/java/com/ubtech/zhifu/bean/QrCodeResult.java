package com.ubtech.zhifu.bean;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class QrCodeResult {
    private String agencyUid;
    private String orderId;
    private String price;
    private String remark;

    public String getAgencyUid() {
        return this.agencyUid == null ? "" : this.agencyUid;
    }

    public void setAgencyUid(String agencyUid2) {
        this.agencyUid = agencyUid2;
    }

    public String getOrderId() {
        return this.orderId == null ? "" : this.orderId;
    }

    public void setOrderId(String orderId2) {
        this.orderId = orderId2;
    }

    public String getPrice() {
        return this.price == null ? "" : this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getRemark() {
        return this.remark == null ? "" : this.remark;
    }

    public void setRemark(String remark2) {
        this.remark = remark2;
    }

}
