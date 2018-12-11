package io.renren.mongo.entity;

import java.io.Serializable;
import java.util.List;

public class ChapterEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dr;
	private String name;
	public int getDr() {
		return dr;
	}
	public void setDr(int dr) {
		this.dr = dr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public List<SectionEntity> getSection() {
		return section;
	}
	public void setSection(List<SectionEntity> section) {
		this.section = section;
	}
	private String nc_course_id;
	private String nc_id;
	private List<SectionEntity> section;
	
}
