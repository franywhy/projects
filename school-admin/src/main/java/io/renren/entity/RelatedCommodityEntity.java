package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

import io.renren.utils.Constant;



/**
 * 商品关联档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-28 14:41:33
 */
public class RelatedCommodityEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//关联商品id
	private Long relatedCommodityId;
	//机构id
	private String schoolId;
	//是否删除   0.未删除  1.删除   用于软删除
	private Integer dr = Constant.DR.NORMAL.getValue();
	//关联名称
	private String relatedName;
	//备注
	private String remake;
	//建档人
	private Long creator;
	//创建时间
	private Date creationTime;
	//修改人
	private Long modifier;
	//修改日期
	private Date modifiedTime;
	//状态  0：禁用  1：启用
	private Integer status = (int) Constant.Status.RESUME.getValue();
	
	//创建用户
	private String creationName;
	//修改用户
	private String modifiedName;
		

	public String getCreationName() {
		return creationName;
	}
	public void setCreationName(String creationName) {
		this.creationName = creationName;
	}
	public String getModifiedName() {
		return modifiedName;
	}
	public void setModifiedName(String modifiedName) {
		this.modifiedName = modifiedName;
	}
	/**
	 * 设置：关联商品id
	 */
	public void setRelatedCommodityId(Long relatedCommodityId) {
		this.relatedCommodityId = relatedCommodityId;
	}
	/**
	 * 获取：关联商品id
	 */
	public Long getRelatedCommodityId() {
		return relatedCommodityId;
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
	 * 设置：关联名称
	 */
	public void setRelatedName(String relatedName) {
		this.relatedName = relatedName;
	}
	/**
	 * 获取：关联名称
	 */
	public String getRelatedName() {
		return relatedName;
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
	 * 设置：建档人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：建档人
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
	 * 设置：修改人
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：修改人
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：修改日期
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：修改日期
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * 设置：状态  0：禁用  1：启用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态  0：禁用  1：启用
	 */
	public Integer getStatus() {
		return status;
	}
}
