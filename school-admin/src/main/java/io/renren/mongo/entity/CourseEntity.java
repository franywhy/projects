package io.renren.mongo.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="courses")
public class CourseEntity implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public List<ChapterEntity> getChapter() {
		return chapter;
	}
	public void setChapter(List<ChapterEntity> chapter) {
		this.chapter = chapter;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getChapter_number() {
		return chapter_number;
	}
	public void setChapter_number(int chapter_number) {
		this.chapter_number = chapter_number;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getClass_room_type() {
		return class_room_type;
	}
	public void setClass_room_type(String class_room_type) {
		this.class_room_type = class_room_type;
	}
	public int getDr() {
		return dr;
	}
	public void setDr(int dr) {
		this.dr = dr;
	}
	public Long getModified_time() {
		return modified_time;
	}
	public void setModified_time(Long modified_time) {
		this.modified_time = modified_time;
	}
	public String getNot_class() {
		return not_class;
	}
	public void setNot_class(String not_class) {
		this.not_class = not_class;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNc_id() {
		return nc_id;
	}
	public void setNc_id(String nc_id) {
		this.nc_id = nc_id;
	}
	public Long getSyn_time() {
		return syn_time;
	}
	public void setSyn_time(Long syn_time) {
		this.syn_time = syn_time;
	}
	public String getType_code() {
		return type_code;
	}
	public void setType_code(String type_code) {
		this.type_code = type_code;
	}
	private String _id;
	private List<ChapterEntity> chapter;
	private String type_name;
	private String code;
	private int chapter_number;
	private String remark;
	private String class_room_type;
	private int dr;
	private Long modified_time;
	private String not_class;
	private String name;
	private String nc_id;
	private Long syn_time;
	private String type_code;
	
}
