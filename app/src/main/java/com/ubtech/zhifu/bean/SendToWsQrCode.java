package com.ubtech.zhifu.bean;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class SendToWsQrCode {
    private String qrCode;
    private String remark;

    public SendToWsQrCode(String remark2, String qrCode2) {
        this.remark = remark2;
        this.qrCode = qrCode2;
    }

    public String getRemark() {
        return this.remark == null ? "" : this.remark;
    }

    public void setRemark(String remark2) {
        this.remark = remark2;
    }

    public String getQrCode() {
        return this.qrCode == null ? "" : this.qrCode;
    }

    public void setQrCode(String qrCode2) {
        this.qrCode = qrCode2;
    }

}
