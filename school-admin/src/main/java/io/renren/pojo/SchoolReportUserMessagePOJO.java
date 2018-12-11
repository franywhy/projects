package io.renren.pojo;

import java.io.Serializable;

/**
 * Created by DL on 2018/3/17.
 */
public class SchoolReportUserMessagePOJO implements Serializable {
    //订单id
    private Long orderId;
    //用户id
    private Long userId;
    //班主任id
    private Long classTeacherId;
    //班主任名称
    private  String classTeacherName;
    //班型id
    private Long classTypeId;

    public Long getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(Long classTypeId) {
        this.classTypeId = classTypeId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getClassTeacherId() {
        return classTeacherId;
    }

    public void setClassTeacherId(Long classTeacherId) {
        this.classTeacherId = classTeacherId;
    }

    public String getClassTeacherName() {
        return classTeacherName;
    }

    public void setClassTeacherName(String classTeacherName) {
        this.classTeacherName = classTeacherName;
    }
}
