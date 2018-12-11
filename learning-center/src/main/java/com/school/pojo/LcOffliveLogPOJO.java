package com.school.pojo;

/**
 * Created by DL on 2018/4/18.
 */
public class LcOffliveLogPOJO {

    //学员id
    private Long userId;
    //学员手机号码
    private String userMobile;
    //视频id
    private String videoId;
    //开始观看时间
    private Long lookStartTime;
    //结束观看时间
    private Long lookEndTime;
    //是否在线:0离线1在线
    private Integer isOfflive;
    //视频进度开始时间
    private Long videoStartTime;
    //视频进度结束时间
    private Long videoEndTime;
    //视频总时长
    private Long videoTotalTime;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Long getLookStartTime() {
        return lookStartTime;
    }

    public void setLookStartTime(Long lookStartTime) {
        this.lookStartTime = lookStartTime;
    }

    public Long getLookEndTime() {
        return lookEndTime;
    }

    public void setLookEndTime(Long lookEndTime) {
        this.lookEndTime = lookEndTime;
    }

    public Integer getIsOfflive() {
        return isOfflive;
    }

    public void setIsOfflive(Integer isOfflive) {
        this.isOfflive = isOfflive;
    }

    public Long getVideoStartTime() {
        return videoStartTime;
    }

    public void setVideoStartTime(Long videoStartTime) {
        this.videoStartTime = videoStartTime;
    }

    public Long getVideoEndTime() {
        return videoEndTime;
    }

    public void setVideoEndTime(Long videoEndTime) {
        this.videoEndTime = videoEndTime;
    }

    public Long getVideoTotalTime() {
        return videoTotalTime;
    }

    public void setVideoTotalTime(Long videoTotalTime) {
        this.videoTotalTime = videoTotalTime;
    }
}
