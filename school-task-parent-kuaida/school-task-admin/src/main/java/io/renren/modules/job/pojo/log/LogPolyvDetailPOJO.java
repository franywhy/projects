package io.renren.modules.job.pojo.log;

import io.renren.modules.job.entity.LogPolyvDetailEntity;
import io.renren.modules.job.utils.DateUtils;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;


/**
 * Created by DL on 2018/10/16.
 */
public class LogPolyvDetailPOJO implements Serializable {

    //学员id
    private String param1;
    //录播课次id
    private String param2;
    //课程id
    private Long courseId = -1L;
    //录播视频id
    private String videoId;
    //观看录播视频的客户端
    private String operatingSystem;
    //用户观看视频时长/s
    private Long playDuration;
    //录播视频总时长/s
    private Long duration;
    //保利威视平台产生日志时间
    private Long createdTime;


    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public Long getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(Long playDuration) {
        this.playDuration = playDuration;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "LogPolyvDetailPOJO{" +
                "parm2=" + param2 +
                ", courseId=" + courseId +
                ", parm1=" + param1 +
                ", videoId='" + videoId + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", playDuration=" + playDuration +
                ", duration=" + duration +
                ", createdTime=" + createdTime +
                '}';
    }

    public static LogPolyvDetailEntity getEntity(LogPolyvDetailPOJO polyvDetailPOJO){
        LogPolyvDetailEntity entity = new LogPolyvDetailEntity();
        if (polyvDetailPOJO != null) {
            entity.setRecordId(polyvDetailPOJO.getParam2());
            entity.setUserId(polyvDetailPOJO.getParam1());
            entity.setCourseId(polyvDetailPOJO.getCourseId());
            entity.setVid(polyvDetailPOJO.getVideoId());
            entity.setClient(polyvDetailPOJO.getOperatingSystem());
            //保利威视返回的时长单位是秒
            entity.setPlayDuration(polyvDetailPOJO.getPlayDuration()*1000);
            entity.setDuration(polyvDetailPOJO.getDuration()*1000);
            entity.setPolyvCreateTime(DateUtils.getDate(polyvDetailPOJO.getCreatedTime(), 1));
        }
        return entity;
    }
}
