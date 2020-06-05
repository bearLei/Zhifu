package com.ubtech.zhifu.bean;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class SmsBean {
    private String content;
    private String phone;
    private long time;
    private String uri;

    public SmsBean(String phone2, String content2, long time2, String uri2) {
        this.phone = phone2;
        this.content = content2;
        this.time = time2;
        this.uri = uri2;
    }

    public String getPhone() {
        return this.phone == null ? "" : this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getContent() {
        return this.content == null ? "" : this.content;
    }

    public void setContent(String content2) {
        this.content = content2;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time2) {
        this.time = time2;
    }

    public String getUri() {
        return this.uri == null ? "" : this.uri;
    }

    public void setUri(String uri2) {
        this.uri = uri2;
    }

}
