package io.renren.pojo.log;

/**
 * 查询学员在startTime和endTime有排课的数据 考勤统计报表用
 * 学员规划ID1,学员规划子表ID11,排课ID1
 * 学员规划ID1,学员规划子表ID12,排课ID2
 * 学员规划ID1,学员规划子表ID13,排课ID3
 * 学员规划ID2,学员规划子表ID21,排课ID4
 * @class io.renren.pojo.log.LogAttendanceClassTeacherPOJO.java
 * @Description:
 * @author shihongjie
 * @dete 2017年8月25日
 */
public class LogAttendanceClassTeacherPOJO {
	
	private Long userplanId;
	private Long userplanDetailId;
	private String classplanId;
	public Long getUserplanId() {
		return userplanId;
	}
	public void setUserplanId(Long userplanId) {
		this.userplanId = userplanId;
	}
	public Long getUserplanDetailId() {
		return userplanDetailId;
	}
	public void setUserplanDetailId(Long userplanDetailId) {
		this.userplanDetailId = userplanDetailId;
	}
	public String getClassplanId() {
		return classplanId;
	}
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	@Override
	public String toString() {
		return "LogAttendanceClassTeacherPOJO [userplanId=" + userplanId + ", userplanDetailId=" + userplanDetailId + ", classplanId=" + classplanId
				+ "]";
	}
	
	
}
