package com.hq.learningapi.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

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

    private Date pushTime;

    private Long pkSupport;

    private Long pkOppose;

    private Integer pkSupportIsLike;

    private Integer pkOpposeIsLike;

    //封面类型:1大图2小图
    private Integer cardBannerType;

    //资源总时长
    private String contentTotalTime;


    //参与Pk总人数
    private Long pkTotalNumber;

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

    public Long getPkTotalNumber() {
        return pkTotalNumber;
    }

    public void setPkTotalNumber(Long pkTotalNumber) {
        this.pkTotalNumber = pkTotalNumber;
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

    @DateTimeFormat(pattern = "MM-dd HH:mm")
    @JsonFormat(pattern = "MM-dd HH:mm",timezone = "GMT+8")
    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Long getPkSupport() {
        return pkSupport;
    }

    public void setPkSupport(Long pkSupport) {
        this.pkSupport = pkSupport;
    }

    public Long getPkOppose() {
        return pkOppose;
    }

    public void setPkOppose(Long pkOppose) {
        this.pkOppose = pkOppose;
    }

    public Integer getPkSupportIsLike() {
        return pkSupportIsLike;
    }

    public void setPkSupportIsLike(Integer pkSupportIsLike) {
        this.pkSupportIsLike = pkSupportIsLike;
    }

    public Integer getPkOpposeIsLike() {
        return pkOpposeIsLike;
    }

    public void setPkOpposeIsLike(Integer pkOpposeIsLike) {
        this.pkOpposeIsLike = pkOpposeIsLike;
    }

    public String getVoiceTitle() {
        return voiceTitle;
    }

    public void setVoiceTitle(String voiceTitle) {
        this.voiceTitle = voiceTitle;
    }
}