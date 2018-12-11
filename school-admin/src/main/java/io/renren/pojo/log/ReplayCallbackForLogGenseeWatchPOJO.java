package io.renren.pojo.log;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by DL on 2018/4/21.
 */
public class ReplayCallbackForLogGenseeWatchPOJO implements Serializable {

    //主键ID
    private Long replayCallbackDetailId;
    //回放视频id
    private String videoId;
    //学员id
    private Long userId;
    //观看直播回放-加入时间
    private Long joinTime;
    //观看直播回放-离开时间
    private Long leaveTime;
    //日记同步时间
    private Date createTime;
    //视频进度开始时间
    private Long videoStartTime;
    //视频进度结束时间
    private Long videoEndTime;
    //视频总时长
    private Long videoTotalTime;
    //是否离线(缓存):0离线1回放回调
    private Integer isOfflive;
    //课次id
    private String classPlanLiveId;
    //课次开始时间
    private Date startTime;
    //课次结束时间
    private Date endTime;
    //产品线
    private Long productId;
    //系数
    private Float coefficient;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Float coefficient) {
        this.coefficient = coefficient;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getReplayCallbackDetailId() {
        return replayCallbackDetailId;
    }

    public void setReplayCallbackDetailId(Long replayCallbackDetailId) {
        this.replayCallbackDetailId = replayCallbackDetailId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Long joinTime) {
        this.joinTime = joinTime;
    }

    public Long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Long leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Integer getIsOfflive() {
        return isOfflive;
    }

    public void setIsOfflive(Integer isOfflive) {
        this.isOfflive = isOfflive;
    }

    public String getClassPlanLiveId() {
        return classPlanLiveId;
    }

    public void setClassPlanLiveId(String classPlanLiveId) {
        this.classPlanLiveId = classPlanLiveId;
    }
}
