package io.renren.pojo;

import java.util.Date;


/**
 * 特殊情况-转省份
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-09 18:00:13
 */
public class MallAdjustProfessionPOJO {

	//主键
	private Long id;
	//学员ID
	private Long userId;
	//学员姓名
	private String userName;
	//原来专业
	private String lastProfessionName;
	//现在专业
	private String professionName;
	//创建时间
	private Date createTime;
	//是否存档相关申请,0:否,1:是
	private Integer applystatus;

	//是否存档相关申请
	private String applystatusStr;

	//班主任
	private String teacherName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastProfessionName() {
		return lastProfessionName;
	}

	public void setLastProfessionName(String lastProfessionName) {
		this.lastProfessionName = lastProfessionName;
	}

	public String getProfessionName() {
		return professionName;
	}

	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getApplystatus() {
		return applystatus;
	}

	public void setApplystatus(Integer applystatus) {
		this.applystatus = applystatus;
	}

	public String getApplystatusStr() {
		applystatusStr = "是";
		if(applystatus.equals(0)){
			applystatusStr = "否";
		}
		return applystatusStr;
	}

	public void setApplystatusStr(String applystatusStr) {
		this.applystatusStr = applystatusStr;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	@Override
	public String toString() {
		return "MallAdjustProfessionPOJO{" +
				"id=" + id +
				", userId=" + userId +
				", userName='" + userName + '\'' +
				", lastProfessionName='" + lastProfessionName + '\'' +
				", professionName='" + professionName + '\'' +
				", createTime=" + createTime +
				", applystatus=" + applystatus +
				", applystatusStr='" + applystatusStr + '\'' +
				", teacherName='" + teacherName + '\'' +
				'}';
	}
}
