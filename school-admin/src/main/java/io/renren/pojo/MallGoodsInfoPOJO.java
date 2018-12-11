package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.renren.entity.MallGoodsInfoEntity;

public class MallGoodsInfoPOJO implements Serializable {
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

	// 班型名称
	private String classtypeName;
	// 专业名称
	private String professionName;
	// 层次名称
	private String levelName;
	// 合同模板名称
	private String templateName;
	// 创建人
	private String creationName;
	// 修改人
	private String modifiedName;
	// nc_id
	private String ncId;
	// m_id
	private Integer mId;
	// 子表集合
	private List<MallGoodsDetailsPOJO> detailList;

	// add by shihongjie 2017-08-09 加入有效期和产品线PK
	// 有效期(单位:天 默认5年)
	private Long dayValidity;
	// 产品线PK
	private Long productId;
	private String productName;
	// 是否开通题库权限
	private Integer onlyOne;
	// 题库课程编号
	private String tkCourseCode;
	//商品介绍
    private String goodRecomment;

    public String getGoodRecomment() {
        return goodRecomment;
    }

    public void setGoodRecomment(String goodRecomment) {
        this.goodRecomment = goodRecomment;
    }

    public String getTkCourseCode() {
		return tkCourseCode;
	}

	public void setTkCourseCode(String tkCourseCode) {
		this.tkCourseCode = tkCourseCode;
	}

	public Integer getOnlyOne() {
		return onlyOne;
	}

	public void setOnlyOne(Integer onlyOne) {
		this.onlyOne = onlyOne;
	}

	public String getTemplateName() {
		return templateName;
	}

	public Long getContractTemplateId() {
		return contractTemplateId;
	}

	public void setContractTemplateId(Long contractTemplateId) {
		this.contractTemplateId = contractTemplateId;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public Long getId() {
		return id;
	}

	public String getNcId() {
		return ncId;
	}

	public void setNcId(String ncId) {
		this.ncId = ncId;
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

	public Long getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(Long classTypeId) {
		this.classTypeId = classTypeId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getIsAudited() {
		return isAudited;
	}

	public void setIsAudited(Integer isAudited) {
		this.isAudited = isAudited;
	}

	public Long getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}

	public Long getModifyPerson() {
		return modifyPerson;
	}

	public void setModifyPerson(Long modifyPerson) {
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

	public String getClasstypeName() {
		return classtypeName;
	}

	public void setClasstypeName(String classtypeName) {
		this.classtypeName = classtypeName;
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

	public List<MallGoodsDetailsPOJO> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<MallGoodsDetailsPOJO> detailList) {
		this.detailList = detailList;
	}

    @Override
    public String toString() {
        return "MallGoodsInfoPOJO{" +
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
                ", classtypeName='" + classtypeName + '\'' +
                ", professionName='" + professionName + '\'' +
                ", levelName='" + levelName + '\'' +
                ", templateName='" + templateName + '\'' +
                ", creationName='" + creationName + '\'' +
                ", modifiedName='" + modifiedName + '\'' +
                ", ncId='" + ncId + '\'' +
                ", mId=" + mId +
                ", detailList=" + detailList +
                ", dayValidity=" + dayValidity +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", onlyOne=" + onlyOne +
                ", tkCourseCode='" + tkCourseCode + '\'' +
                ", goodRecomment='" + goodRecomment + '\'' +
                '}';
    }

    public static MallGoodsInfoEntity getEntity(MallGoodsInfoPOJO pojo) {
		MallGoodsInfoEntity en = new MallGoodsInfoEntity();
		if (pojo != null) {
			// 商品ID
			en.setId(pojo.getId());
			// 商品名称
			en.setName(pojo.getName());
			// 班型
			en.setClassTypeId(pojo.getClassTypeId());
			// 小图
			en.setThumbPath(pojo.getThumbPath());
			// 大图
			en.setOriginPath(pojo.getOriginPath());
			// 售价
			en.setPresentPrice(pojo.getPresentPrice());
			// 原价
			en.setOriginalPrice(pojo.getOriginalPrice());
			// 适用对象
			en.setSuitableUser(pojo.getSuitableUser());
			// 学习周期
			en.setLearningTime(pojo.getLearningTime());
			// 上课方式
			en.setPattern(pojo.getPattern());
			// 上架状态
			en.setStatus(pojo.getStatus());
			// 专业
			en.setProfessionId(pojo.getProfessionId());
			// 层次
			en.setLevelId(pojo.getLevelId());
			// 审核状态
			en.setIsAudited(pojo.getIsAudited());
			// 创建人
			en.setCreatePerson(pojo.getCreatePerson());
			// 修改人
			en.setModifyPerson(pojo.getModifyPerson());
			// 创建时间
			en.setCreateTime(pojo.getCreateTime());
			// 修改时间
			en.setModifyTime(pojo.getModifyTime());
			// 业务分类ID
			en.setSchoolId(pojo.getSchoolId());
			// 合同模板Id
			en.setContractTemplateId(pojo.getContractTemplateId());
			// 软删除
			en.setDr(pojo.getDr());
			en.setNcId(pojo.getNcId());
			en.setmId(pojo.getmId());
			// 产品线
			en.setProductId(pojo.getProductId());
			// 有效期
			en.setDayValidity(pojo.getDayValidity());
			en.setOnlyOne(pojo.getOnlyOne());
			en.setGoodRecomment(pojo.getGoodRecomment());
		}
		return en;
	}
}
