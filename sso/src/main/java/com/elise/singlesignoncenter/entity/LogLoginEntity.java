package com.elise.singlesignoncenter.entity;

import java.util.Date;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

public class LogLoginEntity {

    private Long id;
    private Integer userId;
    private Date loginTime;
    private String loginIp;
    private String businessId;

    public LogLoginEntity(Integer userId,Date loginTime,String loginIp,String businessId){
        this.userId = userId;
        this.loginIp = loginIp;
        this.loginTime = loginTime;
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

    public Date getLoginTime() {
        return loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public String getBusinessId() {
        return businessId;
    }
}
