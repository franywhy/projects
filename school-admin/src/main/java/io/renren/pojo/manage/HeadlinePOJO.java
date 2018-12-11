package io.renren.pojo.manage;

import java.util.Date;

public class HeadlinePOJO {
    private Long headlineId;

    private String title;

    private String subtitle;

    private String cardBannerUrl;

    private Integer contentType;

    private String voiceTitle;

    private String contentHtml;

    private String contentUrl;

    private String labels;

    private Integer readNumber;

    private Integer commentNumber;

    private Long productId;

    private String schoolId;

    private Integer pushStatus;

    private Date pushTime;

    private String pushFindMsgId;

    private Integer appStatus;

    private Long creator;

    private Long modifier;

    private Date creationTime;

    private Date modifiedTime;

    private String productName;

    private String creationName;

    private String modifiedName;

    //封面类型:1大图2小图
    private Integer cardBannerType;

    //资源总时长
    private String contentTotalTime;

    public Integer getCardBannerType() {
        return cardBannerType;
    }

    public void setCardBannerType(Integer cardBannerType) {
        this.cardBannerType = cardBannerType;
    }

    public String getContentTotalTime() {
        return contentTotalTime;
    }

    public void setContentTotalTime(String contentTotalTime) {
        this.contentTotalTime = contentTotalTime;
    }

    public Long getHeadlineId() {
        return headlineId;
    }

    public void setHeadlineId(Long headlineId) {
        this.headlineId = headlineId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCardBannerUrl() {
        return cardBannerUrl;
    }

    public void setCardBannerUrl(String cardBannerUrl) {
        this.cardBannerUrl = cardBannerUrl;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Integer getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(Integer readNumber) {
        this.readNumber = readNumber;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Integer pushStatus) {
        this.pushStatus = pushStatus;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Integer appStatus) {
        this.appStatus = appStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCreationName() {
        return creationName;
    }

    public void setCreationName(String creationName) {
        this.creationName = creationName;
    }

    public String getModifiedName() {
        return modifiedName;
    }

    public void setModifiedName(String modifiedName) {
        this.modifiedName = modifiedName;
    }

    public String getPushFindMsgId() {
        return pushFindMsgId;
    }

    public void setPushFindMsgId(String pushFindMsgId) {
        this.pushFindMsgId = pushFindMsgId;
    }

    public String getVoiceTitle() {
        return voiceTitle;
    }

    public void setVoiceTitle(String voiceTitle) {
        this.voiceTitle = voiceTitle;
    }
}