package com.hq.learningapi.pojo;

/**
 * Created by Administrator on 2017/5/4 0004.
 */
public class CheckVersionPOJO {

    private Integer versionCode;
    private String  versionName;
    private String  downloadUrl;
    private String  updateDetail;
    private String  md5;
    private Boolean forceUpdate;

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


    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpdateDetail() {
        return updateDetail;
    }

    public void setUpdateDetail(String updateDetail) {
        this.updateDetail = updateDetail;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

}
