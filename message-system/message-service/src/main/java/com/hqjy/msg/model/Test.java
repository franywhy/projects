package com.hqjy.msg.model;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
@Table(name = "test")
public class Test  {
    private Integer id;   //主键
    private String name;  //名称
    private Date createTime;  //创建时间
    private String remark;    //描述

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
