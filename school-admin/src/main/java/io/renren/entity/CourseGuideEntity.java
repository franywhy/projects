package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 流程指南
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-09 09:58:40
 */
public class CourseGuideEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long guideId;
	//专业ID
	private Long professionId;
	//省份ID
	private Long areaId;
	//层次ID
	private Long levelId;
	//名称
	private String guideName;
	//跳转地址
	private String guideUrl;
	//图片
	private String guidePic;
	//指南html内容
	private String guideHtml;
	//排序
	private Integer orderNum;
	//状态:0.禁用;1.启用
	private Integer status;
	//平台ID
	private String schoolId;
	//创建时间
	private Date createTime;
	//修改人
	private Long modifyPerson;
	//修改时间
	private Date modifyTime;
	//创建人
	private Long createPerson;

	/**
	 * 设置：主键
	 */
	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}
	/**
	 * 获取：主键
	 */
	public Long getGuideId() {
		return guideId;
	}
	/**
	 * 设置：专业ID
	 */
	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}
	/**
	 * 获取：专业ID
	 */
	public Long getProfessionId() {
		return professionId;
	}
	/**
	 * 设置：省份ID
	 */
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	/**
	 * 获取：省份ID
	 */
	public Long getAreaId() {
		return areaId;
	}
	/**
	 * 设置：层次ID
	 */
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}
	/**
	 * 获取：层次ID
	 */
	public Long getLevelId() {
		return levelId;
	}
	/**
	 * 设置：名称
	 */
	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}
	/**
	 * 获取：名称
	 */
	public String getGuideName() {
		return guideName;
	}
	/**
	 * 设置：跳转地址
	 */
	public void setGuideUrl(String guideUrl) {
		this.guideUrl = guideUrl;
	}
	/**
	 * 获取：跳转地址
	 */
	public String getGuideUrl() {
		return guideUrl;
	}
	/**
	 * 设置：图片
	 */
	public void setGuidePic(String guidePic) {
		this.guidePic = guidePic;
	}
	/**
	 * 获取：图片
	 */
	public String getGuidePic() {
		return guidePic;
	}
	/**
	 * 设置：指南html内容
	 */
	public void setGuideHtml(String guideHtml) {
		this.guideHtml = guideHtml;
	}
	/**
	 * 获取：指南html内容
	 */
	public String getGuideHtml() {
		return guideHtml;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：状态:0.禁用;1.启用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态:0.禁用;1.启用
	 */
	public Integer getStatus() {
		return status;
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
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改人
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：修改人
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreatePerson() {
		return createPerson;
	}
}
