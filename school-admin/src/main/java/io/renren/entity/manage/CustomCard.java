package io.renren.entity.manage;

import java.util.Date;

public class CustomCard {
    private Long cardId;

    private String title;

    private String subtitle;

    private String cardUrl;

    private String cardBannerUrl;

    private Long productId;

    private String schoolId;

    private Integer pushStatus;

    private Date pushTime;

    private String pushMsgId;

    private Integer appStatus;

    private Integer canShare;

    private Long creator;

    private Long modifier;

    private Date creationTime;

    private Date modifiedTime;

    private Integer dr;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
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

    public String getCardUrl() {
        return cardUrl;
    }

    public void setCardUrl(String cardUrl) {
        this.cardUrl = cardUrl;
    }

    public String getCardBannerUrl() {
        return cardBannerUrl;
    }

    public void setCardBannerUrl(String cardBannerUrl) {
        this.cardBannerUrl = cardBannerUrl;
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

    public Integer getCanShare() {
        return canShare;
    }

    public void setCanShare(Integer canShare) {
        this.canShare = canShare;
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

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    public String getPushMsgId() {
        return pushMsgId;
    }

    public void setPushMsgId(String pushMsgId) {
        this.pushMsgId = pushMsgId;
    }
}