package io.renren.pojo.goodsinfo;

import java.util.Date;

public class GoodsInfoDisplayPOJO {
	// 商品ID
	private Long id;
	// 商品名称
	private String name;
	// 班型
	private String classTypeName;
	// 小图
	private String thumbPath;
	// 大图
	private String originPath;
	// 售价
	private Integer presentPrice;
	// 原价
	private Integer originalPrice;
	// 适用对象
	private String suitableUser;
	// 学习周期
	private String learningTime;
	// 上课方式
	private String pattern;
	// 专业
	private String professionName;
	// 层次
	private String levelName;
	// 审核状态
	private Integer isAudited;
	// 上架状态
    private Integer status;
	// 创建人
	private String createPerson;
	// 修改人
	private String modifyPerson;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date modifyTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassTypeName() {
		return classTypeName;
	}
	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}
	public String getThumbPath() {
		return thumbPath;
	}
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}
	public String getOriginPath() {
		return originPath;
	}
	public void setOriginPath(String originPath) {
		this.originPath = originPath;
	}
	public Integer getPresentPrice() {
		return presentPrice;
	}
	public void setPresentPrice(Integer presentPrice) {
		this.presentPrice = presentPrice;
	}
	public Integer getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(Integer originalPrice) {
		this.originalPrice = originalPrice;
	}
	public String getSuitableUser() {
		return suitableUser;
	}
	public void setSuitableUser(String suitableUser) {
		this.suitableUser = suitableUser;
	}
	public String getLearningTime() {
		return learningTime;
	}
	public void setLearningTime(String learningTime) {
		this.learningTime = learningTime;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getProfessionName() {
		return professionName;
	}
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public Integer getIsAudited() {
		return isAudited;
	}
	public void setIsAudited(Integer isAudited) {
		this.isAudited = isAudited;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	public String getModifyPerson() {
		return modifyPerson;
	}
	public void setModifyPerson(String modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
}
