package com.hq.answerapi.entity;

/**
 * Created by Glenn on 2017/5/3 0003.
 */
public class VersionEntity {

    private Integer versionCode;
    private String  versionName;
    private Integer updateStrategy;
    private String  downloadUrl;
    private String  updateDetail;
    private String  md5;
    private Integer isGreyUpdate;
    private String  updateUserList;

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getUpdateStrategy() {
        return updateStrategy;
    }

    public void setUpdateStrategy(Integer updateStrategy) {
        this.updateStrategy = updateStrategy;
    }

    public String getUpdateDetail() {
        return updateDetail;
    }

    public void setUpdateDetail(String updateDetail) {
        this.updateDetail = updateDetail;
    }


    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }


    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getIsGreyUpdate() {
        return isGreyUpdate;
    }

    public void setIsGreyUpdate(Integer isGreyUpdate) {
        this.isGreyUpdate = isGreyUpdate;
    }

    public String getUpdateUserList() {
        return updateUserList;
    }

    public void setUpdateUserList(String updateUserList) {
        this.updateUserList = updateUserList;
    }
}
