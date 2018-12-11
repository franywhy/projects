package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 学习计划排课详情
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-28 15:41:14
 */
public class CourseUserplanClassDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long userplanClassDetailId;
	//学习计划PK
	private Long userplanClassId;
	//学员规划课程子表PK
	private Long userplanDetailId;
	//排课计划PK
	private String classplanId;
	//入课时间
	private Date timestamp;
	//备注
	private String remark;
	//dr
	private Integer dr;
	//平台id
	private String schoolId;
	//排序
	private Integer orderNum;
	

	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 设置：主键
	 */
	public void setUserplanClassDetailId(Long userplanClassDetailId) {
		this.userplanClassDetailId = userplanClassDetailId;
	}
	/**
	 * 获取：主键
	 */
	public Long getUserplanClassDetailId() {
		return userplanClassDetailId;
	}
	/**
	 * 设置：学习计划PK
	 */
	public void setUserplanClassId(Long userplanClassId) {
		this.userplanClassId = userplanClassId;
	}
	/**
	 * 获取：学习计划PK
	 */
	public Long getUserplanClassId() {
		return userplanClassId;
	}
	/**
	 * 设置：学员规划课程子表PK
	 */
	public void setUserplanDetailId(Long userplanDetailId) {
		this.userplanDetailId = userplanDetailId;
	}
	/**
	 * 获取：学员规划课程子表PK
	 */
	public Long getUserplanDetailId() {
		return userplanDetailId;
	}
	/**
	 * 设置：排课计划PK
	 */
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	/**
	 * 获取：排课计划PK
	 */
	public String getClassplanId() {
		return classplanId;
	}
	/**
	 * 设置：入课时间
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * 获取：入课时间
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：dr
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：dr
	 */
	public Integer getDr() {
		return dr;
	}
}
