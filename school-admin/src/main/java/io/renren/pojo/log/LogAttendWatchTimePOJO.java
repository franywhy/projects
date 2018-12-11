package io.renren.pojo.log;

import java.io.Serializable;
/**
 * 教务报表-出勤时间
 */
public class LogAttendWatchTimePOJO implements Serializable {

	private static final long serialVersionUID = -2185238409177387325L;

	private Long watchTime;//实际出勤时长
	
	private Long fullTime;//应出勤时长

	public Long getWatchTime() {
		return watchTime;
	}

	public void setWatchTime(Long watchTime) {
		this.watchTime = watchTime;
	}

	public Long getFullTime() {
		return fullTime;
	}

	public void setFullTime(Long fullTime) {
		this.fullTime = fullTime;
	}
}
