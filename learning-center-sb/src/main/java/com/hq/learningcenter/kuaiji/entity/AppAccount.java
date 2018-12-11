package com.hq.learningcenter.kuaiji.entity;

import java.io.Serializable;
import java.util.Date;

public class AppAccount implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bd_app_account.accountid
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    private Integer accountid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bd_app_account.appid
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    private Integer appid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bd_app_account.code
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    private String code;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bd_app_account.username
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    private String username;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bd_app_account.userpass
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    private String userpass;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bd_app_account.createtime
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bd_app_account.dr
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    private Integer dr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bd_app_account.is_teacher
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    private Integer isTeacher;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table bd_app_account
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bd_app_account.accountid
     *
     * @return the value of bd_app_account.accountid
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public Integer getAccountid() {
        return accountid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bd_app_account.accountid
     *
     * @param accountid the value for bd_app_account.accountid
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bd_app_account.appid
     *
     * @return the value of bd_app_account.appid
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public Integer getAppid() {
        return appid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bd_app_account.appid
     *
     * @param appid the value for bd_app_account.appid
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bd_app_account.code
     *
     * @return the value of bd_app_account.code
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public String getCode() {
        return code;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bd_app_account.code
     *
     * @param code the value for bd_app_account.code
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bd_app_account.username
     *
     * @return the value of bd_app_account.username
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bd_app_account.username
     *
     * @param username the value for bd_app_account.username
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bd_app_account.userpass
     *
     * @return the value of bd_app_account.userpass
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public String getUserpass() {
        return userpass;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bd_app_account.userpass
     *
     * @param userpass the value for bd_app_account.userpass
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public void setUserpass(String userpass) {
        this.userpass = userpass == null ? null : userpass.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bd_app_account.createtime
     *
     * @return the value of bd_app_account.createtime
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bd_app_account.createtime
     *
     * @param createtime the value for bd_app_account.createtime
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bd_app_account.dr
     *
     * @return the value of bd_app_account.dr
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public Integer getDr() {
        return dr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bd_app_account.dr
     *
     * @param dr the value for bd_app_account.dr
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public void setDr(Integer dr) {
        this.dr = dr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bd_app_account.is_teacher
     *
     * @return the value of bd_app_account.is_teacher
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public Integer getIsTeacher() {
        return isTeacher;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bd_app_account.is_teacher
     *
     * @param isTeacher the value for bd_app_account.is_teacher
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    public void setIsTeacher(Integer isTeacher) {
        this.isTeacher = isTeacher;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_account
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", accountid=").append(accountid);
        sb.append(", appid=").append(appid);
        sb.append(", code=").append(code);
        sb.append(", username=").append(username);
        sb.append(", userpass=").append(userpass);
        sb.append(", createtime=").append(createtime);
        sb.append(", dr=").append(dr);
        sb.append(", isTeacher=").append(isTeacher);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_account
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
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
        AppAccount other = (AppAccount) that;
        return (this.getAccountid() == null ? other.getAccountid() == null : this.getAccountid().equals(other.getAccountid()))
            && (this.getAppid() == null ? other.getAppid() == null : this.getAppid().equals(other.getAppid()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getUserpass() == null ? other.getUserpass() == null : this.getUserpass().equals(other.getUserpass()))
            && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
            && (this.getDr() == null ? other.getDr() == null : this.getDr().equals(other.getDr()))
            && (this.getIsTeacher() == null ? other.getIsTeacher() == null : this.getIsTeacher().equals(other.getIsTeacher()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_account
     *
     * @mbggenerated Thu Sep 28 09:56:30 CST 2017
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAccountid() == null) ? 0 : getAccountid().hashCode());
        result = prime * result + ((getAppid() == null) ? 0 : getAppid().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getUserpass() == null) ? 0 : getUserpass().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getDr() == null) ? 0 : getDr().hashCode());
        result = prime * result + ((getIsTeacher() == null) ? 0 : getIsTeacher().hashCode());
        return result;
    }
}