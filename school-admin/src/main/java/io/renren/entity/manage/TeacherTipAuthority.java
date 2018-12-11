package io.renren.entity.manage;

import java.util.List;

/**
 * Created by Administrator on 2018/5/24 0024.
 * @author Evan
 */
public class TeacherTipAuthority {

    private String tuid;

    private String realName;

    private String mobile;

    private String schoolName;

    private String parentTipName;

    private int isHq;

    private int priv;

    private int answerPermission;

    private List<Long> parentTipIdList;

    private List<Long> tipIdList;

    private String businessId;

    public String getTuid() {
        return tuid;
    }

    public void setTuid(String tuid) {
        this.tuid = tuid;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getParentTipName() {
        return parentTipName;
    }

    public void setParentTipName(String parentTipName) {
        this.parentTipName = parentTipName;
    }

    public int getAnswerPermission() {
        return answerPermission;
    }

    public void setAnswerPermission(int answerPermission) {
        this.answerPermission = answerPermission;
    }

    public List<Long> getParentTipIdList() {
        return parentTipIdList;
    }

    public void setParentTipIdList(List<Long> parentTipIdList) {
        this.parentTipIdList = parentTipIdList;
    }

    public List<Long> getTipIdList() {
        return tipIdList;
    }

    public void setTipIdList(List<Long> tipIdList) {
        this.tipIdList = tipIdList;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public int getPriv() {
        return priv;
    }

    public void setPriv(int priv) {
        this.priv = priv;
    }

    public int getIsHq() {
        return isHq;
    }

    public void setIsHq(int isHq) {
        this.isHq = isHq;
    }
}
