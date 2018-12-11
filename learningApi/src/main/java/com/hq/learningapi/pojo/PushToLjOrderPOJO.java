package com.hq.learningapi.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/9/11.
 */
public class PushToLjOrderPOJO implements Serializable {
    /**
     * 业务线
     */
    private String company_type;
    /**
     * 对应商品表的nc_id
     */
    private String nc_commodity_id;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 对应部门表的nc_id  0001A510000000000KY0
     */
    private String nc_school_pk;
    /**
     * 用户名字
     */
    private String user_name;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 对应学员表的nc_id
     */
    private String  nc_user_id;
    /**
     * 报名表号(编号)
     */
    private String code;
    /**
     * ts 订单支付时间
     */
    private Long ts;
    /**
     * nc商品类型:类型为25的说明是双师的
     */
    private Integer item_type;
    /**
     * 对应订单表的nc_id
     */
    private String nc_id;
    /**
     * 同步时间
     */
    private String syn_time;
    /**
     * 产品线
     */
    private Long product_type;
    /**
     * 省份名称
     */
    private String province_name;
    /**
     * 学校名称
     */
    private String school_name;
    /**
     *
     */
    private Integer dr;
    /**
     *
     */
    private Integer sign_status;
    /**
     * 0：删除订单 1：同步订单
     */
    private Integer spec_status;
    /**
     * 状态
     */
    private Integer status;

    //nc审核状态,-1审核关闭,1取消审核恢复
    private Integer vbill_status;
    /**
     * 创建时间
     */
    private Long create_time;
    /**
     * 判断同步订单是否来自自考用的(我也不知道是什么鬼)
     */
    private String class_type_id;
    /**
     * 对应省份表的nc_id
     */
    private String province_pk;

    /**
     * 学员报读班型科目子表
     */
    private List<Map<String,Object>> studentCourses;

    public List<Map<String, Object>> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(List<Map<String, Object>> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public String getCompany_type() {
        return company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getNc_commodity_id() {
        return nc_commodity_id;
    }

    public void setNc_commodity_id(String nc_commodity_id) {
        this.nc_commodity_id = nc_commodity_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNc_school_pk() {
        return nc_school_pk;
    }

    public void setNc_school_pk(String nc_school_pk) {
        this.nc_school_pk = nc_school_pk;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getNc_user_id() {
        return nc_user_id;
    }

    public void setNc_user_id(String nc_user_id) {
        this.nc_user_id = nc_user_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Integer getItem_type() {
        return item_type;
    }

    public void setItem_type(Integer item_type) {
        this.item_type = item_type;
    }

    public String getNc_id() {
        return nc_id;
    }

    public void setNc_id(String nc_id) {
        this.nc_id = nc_id;
    }

    public String getSyn_time() {
        return syn_time;
    }

    public void setSyn_time(String syn_time) {
        this.syn_time = syn_time;
    }

    public Long getProduct_type() {
        return product_type;
    }

    public void setProduct_type(Long product_type) {
        this.product_type = product_type;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    public Integer getSign_status() {
        return sign_status;
    }

    public void setSign_status(Integer sign_status) {
        this.sign_status = sign_status;
    }

    public Integer getSpec_status() {
        return spec_status;
    }

    public void setSpec_status(Integer spec_status) {
        this.spec_status = spec_status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVbill_status() {
        return vbill_status;
    }

    public void setVbill_status(Integer vbill_status) {
        this.vbill_status = vbill_status;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public String getClass_type_id() {
        return class_type_id;
    }

    public void setClass_type_id(String class_type_id) {
        this.class_type_id = class_type_id;
    }

    public String getProvince_pk() {
        return province_pk;
    }

    public void setProvince_pk(String province_pk) {
        this.province_pk = province_pk;
    }

    @Override
    public String toString() {
        return "PushToLjOrderPOJO{" +
                "company_type='" + company_type + '\'' +
                ", nc_commodity_id='" + nc_commodity_id + '\'' +
                ", phone='" + phone + '\'' +
                ", nc_school_pk='" + nc_school_pk + '\'' +
                ", user_name='" + user_name + '\'' +
                ", sex=" + sex +
                ", nc_user_id='" + nc_user_id + '\'' +
                ", code='" + code + '\'' +
                ", ts=" + ts +
                ", item_type=" + item_type +
                ", nc_id='" + nc_id + '\'' +
                ", syn_time='" + syn_time + '\'' +
                ", product_type=" + product_type +
                ", province_name='" + province_name + '\'' +
                ", school_name='" + school_name + '\'' +
                ", dr=" + dr +
                ", sign_status=" + sign_status +
                ", spec_status=" + spec_status +
                ", status=" + status +
                ", vbill_status=" + vbill_status +
                ", create_time=" + create_time +
                ", class_type_id='" + class_type_id + '\'' +
                ", province_pk='" + province_pk + '\'' +
                '}';
    }
}
