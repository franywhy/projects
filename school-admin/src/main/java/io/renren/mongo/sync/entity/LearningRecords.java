package io.renren.mongo.sync.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "learning_records")
public class LearningRecords {
	private String _id;
	/**
	 * 直播ID
	 */
	private String live_id;
	/**
	 * type = 0 直播 type = 1 录播
	 */
	private Integer type;
	/**
	 * 用户ID
	 */
	private Integer user_id;
	private String record_time;
	private String period_id;
	private String classplan_id;
	
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getLive_id() {
		return live_id;
	}
	public void setLive_id(String live_id) {
		this.live_id = live_id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getRecord_time() {
		return record_time;
	}
	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}
	public String getPeriod_id() {
		return period_id;
	}
	public void setPeriod_id(String period_id) {
		this.period_id = period_id;
	}
	public String getClassplan_id() {
		return classplan_id;
	}
	public void setClassplan_id(String classplan_id) {
		this.classplan_id = classplan_id;
	}
	@Override
	public String toString() {
		return "LearningRecords [_id=" + _id + ", live_id=" + live_id + ", type=" + type + ", user_id=" + user_id
				+ ", record_time=" + record_time + ", period_id=" + period_id + ", classplan_id=" + classplan_id + "]";
	}
	
	
}
