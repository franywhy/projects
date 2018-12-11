package io.renren.pojo.log;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询学员在startTime和endTime有排课的数据 考勤统计报表用
 * @class io.renren.pojo.log.LogAttendanceClassTeacherPOJO.java
 * @Description:
 * @author shihongjie
 * @dete 2017年8月25日
 */
public class LogAttendanceClassTeacherFormatPOJO {
	
	private Long userplanId;
	
	private List<LogAttendanceClassTeacherPOJO> userplanDetail;
	

	/**
	 * 获取排课数组
	 * @return
	 */
	public List<String> getClassplanIdList(){
		List<String> classplanIds = null;
		if(null != userplanDetail && !userplanDetail.isEmpty()){
			classplanIds = new ArrayList<>();
			for (LogAttendanceClassTeacherPOJO pojo : userplanDetail) {
				classplanIds.add(pojo.getClassplanId());
			}
		}
		return classplanIds;
	}
	
	public LogAttendanceClassTeacherFormatPOJO(Long userplanId) {
		super();
		this.userplanId = userplanId;
		this.userplanDetail = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "LogAttendanceClassTeacherFormatPOJO [userplanId=" + userplanId + ", userplanDetail=" + userplanDetail + "]";
	}

	public List<LogAttendanceClassTeacherPOJO> getUserplanDetail() {
		return userplanDetail;
	}

	public void setUserplanDetail(List<LogAttendanceClassTeacherPOJO> userplanDetail) {
		this.userplanDetail = userplanDetail;
	}

	public Long getUserplanId() {
		return userplanId;
	}

	public void setUserplanId(Long userplanId) {
		this.userplanId = userplanId;
	}

}

