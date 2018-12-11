package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 班型档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-21 14:30:56
 */
public class MallClassTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//班型id
	private Long classtypeId;
	//是否删除   0.未删除  1.删除   用于软删除
	private Integer dr;
	//机构id
	private String schoolId;
	//班型名称
	private String classtypeName;
	//创建用户
	private Long creator;
	//创建时间
	private Date creationTime;
	//修改用户
	private Long modifier;
	//修改时间
	private Date modifiedTime;
	//状态  0：禁用   1：正常
	private Integer status;
	//备注
	private String remake;
	//产品pk
	private Long productId;

	/**
	 * 设置：班型id
	 */
	public void setClasstypeId(Long classtypeId) {
		this.classtypeId = classtypeId;
	}
	/**
	 * 获取：班型id
	 */
	public Long getClasstypeId() {
		return classtypeId;
	}
	/**
	 * 设置：是否删除   0.未删除  1.删除   用于软删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：是否删除   0.未删除  1.删除   用于软删除
	 */
	public Integer getDr() {
		return dr;
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
	/**
	 * 设置：班型名称
	 */
	public void setClasstypeName(String classtypeName) {
		this.classtypeName = classtypeName;
	}
	/**
	 * 获取：班型名称
	 */
	public String getClasstypeName() {
		return classtypeName;
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
	 * 设置：状态  0：禁用   1：正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态  0：禁用   1：正常
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：备注
	 */
	public void setRemake(String remake) {
		this.remake = remake;
	}
	/**
	 * 获取：备注
	 */
	public String getRemake() {
		return remake;
	}
	/**
	 * 获取：产品pk
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * 设置：产品pk
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
}
