package com.elise.singlesignoncenter.entity;

import java.util.Date;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

public class LogModifyUserInfoEntity {

    private Long id;
    private Integer userId;
    private Integer modifyUser;
    private Date modifyTime;
    private String detail;
    private String loginIp;
    private String businessId;
    private String path;
    private String method;

    public LogModifyUserInfoEntity(Integer userId, Integer modifyUser, Date modifyTime, String detail, String loginIp, String businessId) {
        this.userId = userId;
        this.modifyUser = modifyUser;
        this.modifyTime = modifyTime;
        this.detail = detail;
        this.loginIp = loginIp;
        this.businessId = businessId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getModifyUser() {
        return modifyUser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public String getDetail() {
        return detail;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


}
