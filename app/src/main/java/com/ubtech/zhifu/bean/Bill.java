package com.ubtech.zhifu.bean;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class Bill {
    private ActionParamBean actionParam;
    private String bizInNo;
    private String bizSubType;
    private String bizType;
    private boolean canDelete;
    private String categoryName;
    private String consumeFee;
    private String consumeStatus;
    private String consumeTitle;
    private int contentRender;
    private String createDesc;
    private String createTime;
    private long gmtCreate;
    private boolean isAggregatedRec;
    private String oppositeLogo;
    private String recordType;
    private int tagStatus;

    public ActionParamBean getActionParam() {
        return this.actionParam;
    }

    public void setActionParam(ActionParamBean actionParam2) {
        this.actionParam = actionParam2;
    }

    public String getBizInNo() {
        return this.bizInNo;
    }

    public void setBizInNo(String bizInNo2) {
        this.bizInNo = bizInNo2;
    }

    public String getBizSubType() {
        return this.bizSubType;
    }

    public void setBizSubType(String bizSubType2) {
        this.bizSubType = bizSubType2;
    }

    public String getBizType() {
        return this.bizType;
    }

    public void setBizType(String bizType2) {
        this.bizType = bizType2;
    }

    public boolean isCanDelete() {
        return this.canDelete;
    }

    public void setCanDelete(boolean canDelete2) {
        this.canDelete = canDelete2;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName2) {
        this.categoryName = categoryName2;
    }

    public String getConsumeFee() {
        return this.consumeFee;
    }

    public void setConsumeFee(String consumeFee2) {
        this.consumeFee = consumeFee2;
    }

    public String getConsumeStatus() {
        return this.consumeStatus;
    }

    public void setConsumeStatus(String consumeStatus2) {
        this.consumeStatus = consumeStatus2;
    }

    public String getConsumeTitle() {
        return this.consumeTitle;
    }

    public void setConsumeTitle(String consumeTitle2) {
        this.consumeTitle = consumeTitle2;
    }

    public int getContentRender() {
        return this.contentRender;
    }

    public void setContentRender(int contentRender2) {
        this.contentRender = contentRender2;
    }

    public String getCreateDesc() {
        return this.createDesc;
    }

    public void setCreateDesc(String createDesc2) {
        this.createDesc = createDesc2;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime2) {
        this.createTime = createTime2;
    }

    public long getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(long gmtCreate2) {
        this.gmtCreate = gmtCreate2;
    }

    public boolean isIsAggregatedRec() {
        return this.isAggregatedRec;
    }

    public void setIsAggregatedRec(boolean isAggregatedRec2) {
        this.isAggregatedRec = isAggregatedRec2;
    }

    public String getOppositeLogo() {
        return this.oppositeLogo;
    }

    public void setOppositeLogo(String oppositeLogo2) {
        this.oppositeLogo = oppositeLogo2;
    }

    public String getRecordType() {
        return this.recordType;
    }

    public void setRecordType(String recordType2) {
        this.recordType = recordType2;
    }

    public int getTagStatus() {
        return this.tagStatus;
    }

    public void setTagStatus(int tagStatus2) {
        this.tagStatus = tagStatus2;
    }

    public static class ActionParamBean {
        private String autoJumpUrl;
        private String type;

        public String getAutoJumpUrl() {
            return this.autoJumpUrl;
        }

        public void setAutoJumpUrl(String autoJumpUrl2) {
            this.autoJumpUrl = autoJumpUrl2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }
    }

}
