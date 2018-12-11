package io.renren.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-17 16:09:03
 */
public class SchoolReportDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//班主任pk
	private Long classTeacherId;
	//班主任姓名
	private String classTeacherName;
	//用户pk
	private Long userId;
	//订单PK
	private Long orderId;
	//学习状态：-1.其他，0.在读，1.休学，2.失联，3.弃考，4.免考
	private Integer learningStatus;
	//实际出勤时长（毫秒）
	private Long watchDuration;
	//应出勤时长（毫秒）
	private Long fullDuration;
	//统计开始日期
	private Date startDate;
	//统计结束日期
	private Date endDate;
	//创建时间
	private Date createTime;
	//软删除：0.有效，1.删除
	private Integer dr;

	//报表类型:0月报,1周报
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
	 * 设置：班主任pk
	 */
	public void setClassTeacherId(Long classTeacherId) {
		this.classTeacherId = classTeacherId;
	}
	/**
	 * 获取：班主任pk
	 */
	public Long getClassTeacherId() {
		return classTeacherId;
	}
	/**
	 * 设置：班主任姓名
	 */
	public void setClassTeacherName(String classTeacherName) {
		this.classTeacherName = classTeacherName;
	}
	/**
	 * 获取：班主任姓名
	 */
	public String getClassTeacherName() {
		return classTeacherName;
	}
	/**
	 * 设置：用户pk
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户pk
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：订单PK
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：订单PK
	 */
	public Long getOrderId() {
		return orderId;
	}
	/**
	 * 设置：学习状态：-1.其他，0.在读，1.休学，2.失联，3.弃考，4.免考
	 */
	public void setLearningStatus(Integer learningStatus) {
		this.learningStatus = learningStatus;
	}
	/**
	 * 获取：学习状态：-1.其他，0.在读，1.休学，2.失联，3.弃考，4.免考
	 */
	public Integer getLearningStatus() {
		return learningStatus;
	}
	/**
	 * 设置：实际出勤时长（毫秒）
	 */
	public void setWatchDuration(Long watchDuration) {
		this.watchDuration = watchDuration;
	}
	/**
	 * 获取：实际出勤时长（毫秒）
	 */
	public Long getWatchDuration() {
		return watchDuration;
	}
	/**
	 * 设置：应出勤时长（毫秒）
	 */
	public void setFullDuration(Long fullDuration) {
		this.fullDuration = fullDuration;
	}
	/**
	 * 获取：应出勤时长（毫秒）
	 */
	public Long getFullDuration() {
		return fullDuration;
	}
	/**
	 * 设置：统计开始日期
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * 获取：统计开始日期
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * 设置：统计结束日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * 获取：统计结束日期
	 */
	public Date getEndDate() {
		return endDate;
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
	/**
	 * 设置：软删除：0.有效，1.删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：软删除：0.有效，1.删除
	 */
	public Integer getDr() {
		return dr;
	}
}
