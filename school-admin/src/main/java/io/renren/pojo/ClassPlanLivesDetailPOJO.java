package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by DL on 2018/3/28.
 */
public class ClassPlanLivesDetailPOJO implements Serializable {
    //	学员规划id
    private Long userPlanId;
    //排课id
    private String classPlanId;
    //课次id
    private String classPlanLiveId;
    //课次名称
    private String classPlanLiveName;
    //直播权限id
    private String liveClassTypeIds;
    //课次上课时间
    private Date dayTime;
    //班型id
    private Integer classTypeId;
    //用户id
    private Long userId;
    //课次是否异常
    private Integer classPlanLiveIsError;


    public Long getUserPlanId() {
        return userPlanId;
    }

    public void setUserPlanId(Long userPlanId) {
        this.userPlanId = userPlanId;
    }

    public String getClassPlanId() {
        return classPlanId;
    }

    public void setClassPlanId(String classPlanId) {
        this.classPlanId = classPlanId;
    }

    public String getClassPlanLiveId() {
        return classPlanLiveId;
    }

    public void setClassPlanLiveId(String classPlanLiveId) {
        this.classPlanLiveId = classPlanLiveId;
    }

    public String getClassPlanLiveName() {
        return classPlanLiveName;
    }

    public void setClassPlanLiveName(String classPlanLiveName) {
        this.classPlanLiveName = classPlanLiveName;
    }

    public String getLiveClassTypeIds() {
        return liveClassTypeIds;
    }

    public void setLiveClassTypeIds(String liveClassTypeIds) {
        this.liveClassTypeIds = liveClassTypeIds;
    }

    public Date getDayTime() {
        return dayTime;
    }

    public void setDayTime(Date dayTime) {
        this.dayTime = dayTime;
    }

    public Integer getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(Integer classTypeId) {
        this.classTypeId = classTypeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getClassPlanLiveIsError() {
        return classPlanLiveIsError;
    }

    public void setClassPlanLiveIsError(Integer classPlanLiveIsError) {
        this.classPlanLiveIsError = classPlanLiveIsError;
    }
}
