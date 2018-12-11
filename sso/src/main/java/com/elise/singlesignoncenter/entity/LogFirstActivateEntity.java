package com.elise.singlesignoncenter.entity;

import java.util.Date;

/**
 * Created by Glenn on 2017/4/26 0026.
 */


public class LogFirstActivateEntity {

    private Long id;
    private Integer userId;
    private Integer createUser;
    private Date createTime;
    private String businessId;
    private Integer isActivate;

    public LogFirstActivateEntity(Integer userId,Integer createUser,Date createTime,String businessId,Integer isActivate){
         this.userId = userId;
         this.createUser = createUser;
         this.createTime = createTime;
         this.businessId = businessId;
         this.isActivate = isActivate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getBusinessId() {
        return businessId;
    }

    public Integer getIsActivate() {
        return isActivate;
    }

}
