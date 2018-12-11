package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 性别表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-27 14:49:25
 */
public class SexEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer id;
	//性别
	private String name;
	//机构id
	private String schoolId;

	/**
	 * 设置：id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：性别
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：性别
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：机构id
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：机构id
	 */
	public String getSchoolId() {
		return schoolId;
	}
}
