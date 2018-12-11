package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 学历层次表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-25 16:38:54
 */
public class MallLevelEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
	private Long levelId;
	//层次名称
	private String levelName;
	//创建用户
	private Long creator;
	//创建时间
	private Date creationTime;
	//修改用户
	private Long modifier;
	//修改时间
	private Date modifiedTime;
	//平台ID
	private String schoolId;

	/**
	 * 设置：ID
	 */
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}
	/**
	 * 获取：ID
	 */
	public Long getLevelId() {
		return levelId;
	}
	/**
	 * 设置：层次名称
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	/**
	 * 获取：层次名称
	 */
	public String getLevelName() {
		return levelName;
	}
	/**
	 * 设置：创建用户
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建用户
	 */
	public Long getCreator() {
		return creator;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	/**
	 * 设置：修改用户
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：修改用户
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * 设置：平台ID
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台ID
	 */
	public String getSchoolId() {
		return schoolId;
	}
}
