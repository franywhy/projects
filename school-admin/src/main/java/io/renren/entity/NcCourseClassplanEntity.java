package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-21 11:20:03
 */
public class NcCourseClassplanEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//学员手机
	private String mobile;
	//学员姓名
	private String userName;
	//nc系统的学员id
	private String ncUserId;
	//nc系统的报名表id
	private String ncId;
	//nc系统的班型id,在蓝鲸就是商品id
	private String ncCommodityId;
	//nc系统报名表号
	private String ncCode;
	//蓝鲸排课id
	private String classplanId;
	//蓝鲸的课程代码
	private String courseCode;
	//是否开通学习权限:1开通,0关闭
	private Integer openFlag;
	//创建时间
	private Date createTime;
	//是否已经生成学员规划
    private  Integer isSuccess;
    //nc系统修改学员排课时间
    private String ncModifiedTime;

    public String getNcModifiedTime() {
        return ncModifiedTime;
    }

    public void setNcModifiedTime(String ncModifiedTime) {
        this.ncModifiedTime = ncModifiedTime;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：学员手机
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：学员手机
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：学员姓名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：学员姓名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：nc系统的学员id
	 */
	public void setNcUserId(String ncUserId) {
		this.ncUserId = ncUserId;
	}
	/**
	 * 获取：nc系统的学员id
	 */
	public String getNcUserId() {
		return ncUserId;
	}
	/**
	 * 设置：nc系统的报名表id
	 */
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	/**
	 * 获取：nc系统的报名表id
	 */
	public String getNcId() {
		return ncId;
	}
	/**
	 * 设置：nc系统的班型id,在蓝鲸就是商品id
	 */
	public void setNcCommodityId(String ncCommodityId) {
		this.ncCommodityId = ncCommodityId;
	}
	/**
	 * 获取：nc系统的班型id,在蓝鲸就是商品id
	 */
	public String getNcCommodityId() {
		return ncCommodityId;
	}
	/**
	 * 设置：nc系统报名表号
	 */
	public void setNcCode(String ncCode) {
		this.ncCode = ncCode;
	}
	/**
	 * 获取：nc系统报名表号
	 */
	public String getNcCode() {
		return ncCode;
	}
	/**
	 * 设置：蓝鲸排课id
	 */
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	/**
	 * 获取：蓝鲸排课id
	 */
	public String getClassplanId() {
		return classplanId;
	}
	/**
	 * 设置：蓝鲸的课程代码
	 */
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	/**
	 * 获取：蓝鲸的课程代码
	 */
	public String getCourseCode() {
		return courseCode;
	}
	/**
	 * 设置：是否开通学习权限:1开通,0关闭
	 */
	public void setOpenFlag(Integer openFlag) {
		this.openFlag = openFlag;
	}
	/**
	 * 获取：是否开通学习权限:1开通,0关闭
	 */
	public Integer getOpenFlag() {
		return openFlag;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

}
