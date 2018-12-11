package io.renren.mongo.entity;

import java.io.Serializable;

public class SectionEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String nc_chapter_id;
	private String nc_course_id;
	private String nc_id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNc_chapter_id() {
		return nc_chapter_id;
	}
	public void setNc_chapter_id(String nc_chapter_id) {
		this.nc_chapter_id = nc_chapter_id;
	}
	public String getNc_course_id() {
		return nc_course_id;
	}
	public void setNc_course_id(String nc_course_id) {
		this.nc_course_id = nc_course_id;
	}
	public String getNc_id() {
		return nc_id;
	}
	public void setNc_id(String nc_id) {
		this.nc_id = nc_id;
	}
}
