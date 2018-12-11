package io.renren.mongo.sync.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="course_times")
public class CourseTimeEntity{
	
	//主键
	private String _id;
	//名称
	private String name;
	//课程类型
	private String course_prop;
	//考试类型
	private String exam_type;
	//学分
	private String score;
	
	private String dr;
	
	private String url;
	
	private List<String> period_list;
	
	
	public List<String> getPeriod_list() {
		return period_list;
	}
	public void setPeriod_list(List<String> period_list) {
		this.period_list = period_list;
	}
	public String getDr() {
		return dr;
	}
	public void setDr(String dr) {
		this.dr = dr;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getCourse_prop() {
		return course_prop;
	}
	public void setCourse_prop(String course_prop) {
		this.course_prop = course_prop;
	}
	public String getExam_type() {
		return exam_type;
	}
	public void setExam_type(String exam_type) {
		this.exam_type = exam_type;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
}
