package com.hq.learningcenter.kuaiji.entity;

import java.io.Serializable;
import java.util.Date;

public class UserApp implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.userappid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private Integer userappid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.userid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private Long userid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.appid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private Integer appid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.code
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private String code;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.username
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private String username;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.userpass
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private String userpass;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.nc_commodity_id
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private String ncCommodityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.classtype
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private String classtype;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.school_code
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private String schoolCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.school_name
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private String schoolName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.courseid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private String courseid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.dr
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private Integer dr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.createtime
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.userappid
     *
     * @return the value of user_app.userappid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public Integer getUserappid() {
        return userappid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.userappid
     *
     * @param userappid the value for user_app.userappid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setUserappid(Integer userappid) {
        this.userappid = userappid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.userid
     *
     * @return the value of user_app.userid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.userid
     *
     * @param userid the value for user_app.userid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.appid
     *
     * @return the value of user_app.appid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public Integer getAppid() {
        return appid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.appid
     *
     * @param appid the value for user_app.appid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.code
     *
     * @return the value of user_app.code
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public String getCode() {
        return code;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.code
     *
     * @param code the value for user_app.code
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.username
     *
     * @return the value of user_app.username
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.username
     *
     * @param username the value for user_app.username
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.userpass
     *
     * @return the value of user_app.userpass
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public String getUserpass() {
        return userpass;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.userpass
     *
     * @param userpass the value for user_app.userpass
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setUserpass(String userpass) {
        this.userpass = userpass == null ? null : userpass.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.nc_commodity_id
     *
     * @return the value of user_app.nc_commodity_id
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public String getNcCommodityId() {
        return ncCommodityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.nc_commodity_id
     *
     * @param ncCommodityId the value for user_app.nc_commodity_id
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setNcCommodityId(String ncCommodityId) {
        this.ncCommodityId = ncCommodityId == null ? null : ncCommodityId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.classtype
     *
     * @return the value of user_app.classtype
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public String getClasstype() {
        return classtype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.classtype
     *
     * @param classtype the value for user_app.classtype
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setClasstype(String classtype) {
        this.classtype = classtype == null ? null : classtype.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.school_code
     *
     * @return the value of user_app.school_code
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public String getSchoolCode() {
        return schoolCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.school_code
     *
     * @param schoolCode the value for user_app.school_code
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode == null ? null : schoolCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.school_name
     *
     * @return the value of user_app.school_name
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.school_name
     *
     * @param schoolName the value for user_app.school_name
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName == null ? null : schoolName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.courseid
     *
     * @return the value of user_app.courseid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public String getCourseid() {
        return courseid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.courseid
     *
     * @param courseid the value for user_app.courseid
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setCourseid(String courseid) {
        this.courseid = courseid == null ? null : courseid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.dr
     *
     * @return the value of user_app.dr
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public Integer getDr() {
        return dr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.dr
     *
     * @param dr the value for user_app.dr
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setDr(Integer dr) {
        this.dr = dr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.createtime
     *
     * @return the value of user_app.createtime
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.createtime
     *
     * @param createtime the value for user_app.createtime
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userappid=").append(userappid);
        sb.append(", userid=").append(userid);
        sb.append(", appid=").append(appid);
        sb.append(", code=").append(code);
        sb.append(", username=").append(username);
        sb.append(", userpass=").append(userpass);
        sb.append(", ncCommodityId=").append(ncCommodityId);
        sb.append(", classtype=").append(classtype);
        sb.append(", schoolCode=").append(schoolCode);
        sb.append(", schoolName=").append(schoolName);
        sb.append(", courseid=").append(courseid);
        sb.append(", dr=").append(dr);
        sb.append(", createtime=").append(createtime);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserApp other = (UserApp) that;
        return (this.getUserappid() == null ? other.getUserappid() == null : this.getUserappid().equals(other.getUserappid()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
            && (this.getAppid() == null ? other.getAppid() == null : this.getAppid().equals(other.getAppid()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getUserpass() == null ? other.getUserpass() == null : this.getUserpass().equals(other.getUserpass()))
            && (this.getNcCommodityId() == null ? other.getNcCommodityId() == null : this.getNcCommodityId().equals(other.getNcCommodityId()))
            && (this.getClasstype() == null ? other.getClasstype() == null : this.getClasstype().equals(other.getClasstype()))
            && (this.getSchoolCode() == null ? other.getSchoolCode() == null : this.getSchoolCode().equals(other.getSchoolCode()))
            && (this.getSchoolName() == null ? other.getSchoolName() == null : this.getSchoolName().equals(other.getSchoolName()))
            && (this.getCourseid() == null ? other.getCourseid() == null : this.getCourseid().equals(other.getCourseid()))
            && (this.getDr() == null ? other.getDr() == null : this.getDr().equals(other.getDr()))
            && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserappid() == null) ? 0 : getUserappid().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getAppid() == null) ? 0 : getAppid().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getUserpass() == null) ? 0 : getUserpass().hashCode());
        result = prime * result + ((getNcCommodityId() == null) ? 0 : getNcCommodityId().hashCode());
        result = prime * result + ((getClasstype() == null) ? 0 : getClasstype().hashCode());
        result = prime * result + ((getSchoolCode() == null) ? 0 : getSchoolCode().hashCode());
        result = prime * result + ((getSchoolName() == null) ? 0 : getSchoolName().hashCode());
        result = prime * result + ((getCourseid() == null) ? 0 : getCourseid().hashCode());
        result = prime * result + ((getDr() == null) ? 0 : getDr().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        return result;
    }
}