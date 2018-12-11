package com.hqjy.pay;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 贷款 实体类
 * @author zhaozhiguang
 */
public class LoanEntity {

    private Integer id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 交易金额
     */
    private BigDecimal tradeMoney;

    /**
     * 下单时间戳
     */
    private String orderTimestamp;

    /**
     * 第三方贷款平台标识
     */
    private String terrace;

    /**
     * 加密密文
     */
    private String ciphertext;

    /**
     * 课程专业
     */
    private String courseMajor;

    /**
     * 学历层次
     */
    private String educLevel;

    /**
     * 班型名称
     */
    private String classTypeName;

    /**
     * 课程原价
     */
    private BigDecimal coursePrice;

    /**
     * 优惠折扣
     */
    private BigDecimal discount;

    /**
     * 剩余需支付
     */
    private BigDecimal overpayMoney;

    /**
     * 折后金额
     */
    private BigDecimal discountMoney;

    /**
     * 已付金额
     */
    private BigDecimal paidMoney;

    /**
     * 第三方贷款机构的参数
     */
    private Map otherData;

    private String otherDataStr;

    public String getOtherDataStr() {
        return otherDataStr;
    }

    public void setOtherDataStr(String otherDataStr) {
        this.otherDataStr = otherDataStr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public BigDecimal getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(BigDecimal tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public String getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(String orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public String getTerrace() {
        return terrace;
    }

    public void setTerrace(String terrace) {
        this.terrace = terrace;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public String getCourseMajor() {
        return courseMajor;
    }

    public void setCourseMajor(String courseMajor) {
        this.courseMajor = courseMajor;
    }

    public String getEducLevel() {
        return educLevel;
    }

    public void setEducLevel(String educLevel) {
        this.educLevel = educLevel;
    }

    public String getClassTypeName() {
        return classTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName;
    }

    public BigDecimal getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(BigDecimal coursePrice) {
        this.coursePrice = coursePrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getOverpayMoney() {
        return overpayMoney;
    }

    public void setOverpayMoney(BigDecimal overpayMoney) {
        this.overpayMoney = overpayMoney;
    }

    public BigDecimal getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(BigDecimal discountMoney) {
        this.discountMoney = discountMoney;
    }

    public BigDecimal getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(BigDecimal paidMoney) {
        this.paidMoney = paidMoney;
    }

    public Map getOtherData() {
        return otherData;
    }

    public void setOtherData(Map otherData) {
        this.otherData = otherData;
    }
}
