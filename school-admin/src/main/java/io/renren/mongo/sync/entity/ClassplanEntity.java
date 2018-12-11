package io.renren.mongo.sync.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="classplan")
public class ClassplanEntity {
	
	//id
	private String _id;
	//名称
	private String name;
	//备注
	private String remark;
	//课程id
	private String course_id;
	//时间说明
	private String live_detail;
	//dr
	private int dr;
	
	@Override
	public String toString() {
		return "ClassplanEntity [_id=" + _id + ", name=" + name + ", remark=" + remark + ", course_id=" + course_id + ", live_detail=" + live_detail
				+ ", dr=" + dr + "]";
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	public String getLive_detail() {
		return live_detail;
	}
	public void setLive_detail(String live_detail) {
		this.live_detail = live_detail;
	}
	public int getDr() {
		return dr;
	}
	public void setDr(int dr) {
		this.dr = dr;
	}
	
	
	
	
}
