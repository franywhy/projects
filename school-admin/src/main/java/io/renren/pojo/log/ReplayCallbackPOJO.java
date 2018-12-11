package io.renren.pojo.log;

import java.io.Serializable;
import java.util.Date;

public class ReplayCallbackPOJO implements Serializable {

	private static final long serialVersionUID = 1L;

    //回放视频id
    private String videoId;
    //学员id
    private Long userId;
    //观看直播回放-加入时间
    private Long joinTime;
    //观看直播回放-离开时间
    private Long leaveTime;
    //日记同步时间
    private Long createTime;
    //视频进度开始时间
    private Long videoStartTime;
    //视频进度结束时间
    private Long videoEndTime;
    //视频总时长
    private Long videoTotalTime;
    //是否离线(缓存):0离线1回放回调
    private Integer isOfflive;
    //类型: 0进入 1退出 2离线观看
    private int type;
    /*
    //产品线
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }*/

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
