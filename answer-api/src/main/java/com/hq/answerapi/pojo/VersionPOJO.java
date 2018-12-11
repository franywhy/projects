package com.hq.answerapi.pojo;

/**
 * Created by Administrator on 2018/9/25 0025.
 */
public class VersionPOJO {

    private String _id;

    private String type;

    private Integer versionCode;

    private String versionName;

    private String must_up;

    private String shuould_up;

    private String download;

    private String file_name;

    private String file_name_new;

    private String update_detail;

    private String md5;

    private Integer is_grey_up;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getMust_up() {
        return must_up;
    }

    public void setMust_up(String must_up) {
        this.must_up = must_up;
    }

    public String getShuould_up() {
        return shuould_up;
    }

    public void setShuould_up(String shuould_up) {
        this.shuould_up = shuould_up;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_name_new() {
        return file_name_new;
    }

    public void setFile_name_new(String file_name_new) {
        this.file_name_new = file_name_new;
    }

    public String getUpdate_detail() {
        return update_detail;
    }

    public void setUpdate_detail(String update_detail) {
        this.update_detail = update_detail;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getIs_grey_up() {
        return is_grey_up;
    }

    public void setIs_grey_up(Integer is_grey_up) {
        this.is_grey_up = is_grey_up;
    }
}
