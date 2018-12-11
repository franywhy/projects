package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-29 10:11:55
 */
public class MallGoodsInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 商品ID
	private Long id;
	// 商品名称
	private String name;
	// 班型
	private Long classTypeId;
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
	// 上架状态
	private Integer status;
	// 专业
	private Long professionId;
	// 层次
	private Long levelId;
	// 合同模板
	private Long contractTemplateId;
	// 审核状态
	private Integer isAudited;
	// 创建人
	private Long createPerson;
	// 修改人
	private Long modifyPerson;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date modifyTime;
	// 业务分类ID
	private String schoolId;
	// 软删除
	private Integer dr;
	// ncId
    private String ncId;
    // ncId
    private Integer mId;
    
    //add by shihongjie 2017-08-09  加入有效期和产品线PK
    //有效期(单位:天 默认5年)
 	private Long dayValidity;
 	//产品线PK
 	private Long productId;
 	//是否开通题库权限
    private Integer onlyOne;
    //商品介绍
    private String goodRecomment;

    public String getGoodRecomment() {
        return goodRecomment;
    }

    public void setGoodRecomment(String goodRecomment) {
        this.goodRecomment = goodRecomment;
    }

    public Integer getOnlyOne() {
        return onlyOne;
    }

	public void setOnlyOne(Integer onlyOne) {
        this.onlyOne = onlyOne;
    }

    public Long getContractTemplateId() {
		return contractTemplateId;
	}

	public void setContractTemplateId(Long contractTemplateId) {
		this.contractTemplateId = contractTemplateId;
	}
 
    public Long getDayValidity() {
		return dayValidity;
	}

	public void setDayValidity(Long dayValidity) {
		this.dayValidity = dayValidity;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getNcId() {
		return ncId;
	}

	public void setNcId(String ncId) {
		this.ncId = ncId;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	/**
	 * 设置：商品ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取：商品ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置：商品名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：商品名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：班型
	 */
	public void setClassTypeId(Long classTypeId) {
		this.classTypeId = classTypeId;
	}

	/**
	 * 获取：班型
	 */
	public Long getClassTypeId() {
		return classTypeId;
	}

	/**
	 * 设置：小图
	 */
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	/**
	 * 获取：小图
	 */
	public String getThumbPath() {
		return thumbPath;
	}

	/**
	 * 设置：大图
	 */
	public void setOriginPath(String originPath) {
		this.originPath = originPath;
	}

	/**
	 * 获取：大图
	 */
	public String getOriginPath() {
		return originPath;
	}

	/**
	 * 设置：售价
	 */
	public void setPresentPrice(Integer presentPrice) {
		this.presentPrice = presentPrice;
	}

	/**
	 * 获取：售价
	 */
	public Integer getPresentPrice() {
		return presentPrice;
	}

	/**
	 * 设置：原价
	 */
	public void setOriginalPrice(Integer originalPrice) {
		this.originalPrice = originalPrice;
	}

	/**
	 * 获取：原价
	 */
	public Integer getOriginalPrice() {
		return originalPrice;
	}

	/**
	 * 设置：适用对象
	 */
	public void setSuitableUser(String suitableUser) {
		this.suitableUser = suitableUser;
	}

	/**
	 * 获取：适用对象
	 */
	public String getSuitableUser() {
		return suitableUser;
	}

	/**
	 * 设置：学习周期
	 */
	public void setLearningTime(String learningTime) {
		this.learningTime = learningTime;
	}

	/**
	 * 获取：学习周期
	 */
	public String getLearningTime() {
		return learningTime;
	}

	/**
	 * 设置：上课方式
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * 获取：上课方式
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * 设置：上架状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取：上架状态
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置：专业
	 */
	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}

	/**
	 * 获取：专业
	 */
	public Long getProfessionId() {
		return professionId;
	}

	/**
	 * 设置：层次
	 */
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

	/**
	 * 获取：层次
	 */
	public Long getLevelId() {
		return levelId;
	}

	/**
	 * 设置：审核状态
	 */
	public void setIsAudited(Integer isAudited) {
		this.isAudited = isAudited;
	}

	/**
	 * 获取：审核状态
	 */
	public Integer getIsAudited() {
		return isAudited;
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

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

    @Override
    public String toString() {
        return "MallGoodsInfoEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classTypeId=" + classTypeId +
                ", thumbPath='" + thumbPath + '\'' +
                ", originPath='" + originPath + '\'' +
                ", presentPrice=" + presentPrice +
                ", originalPrice=" + originalPrice +
                ", suitableUser='" + suitableUser + '\'' +
                ", learningTime='" + learningTime + '\'' +
                ", pattern='" + pattern + '\'' +
                ", status=" + status +
                ", professionId=" + professionId +
                ", levelId=" + levelId +
                ", contractTemplateId=" + contractTemplateId +
                ", isAudited=" + isAudited +
                ", createPerson=" + createPerson +
                ", modifyPerson=" + modifyPerson +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", schoolId='" + schoolId + '\'' +
                ", dr=" + dr +
                ", ncId='" + ncId + '\'' +
                ", mId=" + mId +
                ", dayValidity=" + dayValidity +
                ", productId=" + productId +
                ", onlyOne=" + onlyOne +
                ", goodRecomment='" + goodRecomment + '\'' +
                '}';
    }
}
