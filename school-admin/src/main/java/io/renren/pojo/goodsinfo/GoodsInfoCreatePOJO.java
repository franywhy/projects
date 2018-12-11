package io.renren.pojo.goodsinfo;

import java.util.List;

public class GoodsInfoCreatePOJO {

	// 商品名称
	private String name;
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
	// 班型
	private Long classTypeId;
	// 专业
	private Long professionId;
	// 层次
	private Long levelId;

	List<GoodsDetails> details;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(Long classTypeId) {
		this.classTypeId = classTypeId;
	}

	public Long getProfessionId() {
		return professionId;
	}

	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}

	public Long getLevelId() {
		return levelId;
	}

	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

	public List<GoodsDetails> getDetails() {
		return details;
	}

	public void setDetails(List<GoodsDetails> details) {
		this.details = details;
	}

}
