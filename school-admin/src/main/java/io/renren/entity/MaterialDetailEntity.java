package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

import io.renren.constant.MaterialProperty;
import io.renren.constant.MaterialType;




/**
 * 
 * 
 * @author Yuanjp
 * @date 2017-05-12 11:16:41
 */
public class MaterialDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//资料ID
	private Integer id;
	//资料名称
	private String name;
	//类型
	private MaterialType type;
	
	private MaterialType s_type; //用于查询
	
	//属性
 	private MaterialProperty property;       
 	
 	private MaterialProperty s_property;  //用于查询      
 	
	//创建人
	private String createBy;
	//创建时间
	private Date createTime;
	//更新人
	private String updateBy;
	//更新时间
	private Date updateTime;
	//状态
	private Integer status;
	//视频地址
	private String vedioAddr;
	//回放地址
	private String returnAddr;
	//推送内容
	private String pushContent;
	//推送类型
	private Integer pushType;  // 0 一次    1 每日
	//推送时间
	private Date pushTime;
	//题库课程
	private String questionBankCourse;
	//题库章
	private String questionBankChapter;
	//题库节
	private String questionBankVerse;
	//题库知识点
	private String questionBankKnowledge;
	//是否关联 
	private String isRelevance;  //是,否
	//时间排序
	private String timeOrdering; //创建时间   更新时间
	
	private String creationName;  //创建人名

	private String modifiedName;  //修改人姓名 
	
//	private KnowledgePointEntity knowledgePoint;	//知识点
	
	private String courseName;		//课程名
	
	private String chapterName;	   //章名 
	
	private String verseName;	  //节名
	
	private String kpName;   //知识点名称
	
	/**
	 * 设置：资料ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：资料ID
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：资料名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：资料名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：类型
	 */
	public void setType(int value) {
		this.type = MaterialType.getMaterialType(value);
	}
	/**
	 * 获取：类型
	 */
	public MaterialType getType() {
		return type;
	}
	/**
	 * 设置：属性
	 */
	public void setProperty(int value) {
		this.property = MaterialProperty.getMaterialProperty(value);
	}
	/**
	 * 获取：属性
	 */
	public MaterialProperty getProperty() {
		return property;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人
	 */
	public String getCreateBy() {
		return createBy;
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
	 * 设置：更新人
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：更新人
	 */
	public String getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：视频地址
	 */
	public void setVedioAddr(String vedioAddr) {
		this.vedioAddr = vedioAddr;
	}
	/**
	 * 获取：视频地址
	 */
	public String getVedioAddr() {
		return vedioAddr;
	}
	/**
	 * 设置：回放地址
	 */
	public void setReturnAddr(String returnAddr) {
		this.returnAddr = returnAddr;
	}
	/**
	 * 获取：回放地址
	 */
	public String getReturnAddr() {
		return returnAddr;
	}
	/**
	 * 设置：推送内容
	 */
	public void setPushContent(String pushContent) {
		this.pushContent = pushContent;
	}
	/**
	 * 获取：推送内容
	 */
	public String getPushContent() {
		return pushContent;
	}
	/**
	 * 设置：推送类型
	 */
	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}
	/**
	 * 获取：推送类型
	 */
	public Integer getPushType() {
		return pushType;
	}
	/**
	 * 设置：推送时间
	 */
	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}
	/**
	 * 获取：推送时间
	 */
	public Date getPushTime() {
		return pushTime;
	}
	/**
	 * 设置：题库课程
	 */
	public void setQuestionBankCourse(String questionBankCourse) {
		this.questionBankCourse = questionBankCourse;
	}
	/**
	 * 获取：题库课程
	 */
	public String getQuestionBankCourse() {
		return questionBankCourse;
	}
	/**
	 * 设置：题库章
	 */
	public void setQuestionBankChapter(String questionBankChapter) {
		this.questionBankChapter = questionBankChapter;
	}
	/**
	 * 获取：题库章
	 */
	public String getQuestionBankChapter() {
		return questionBankChapter;
	}
	/**
	 * 设置：题库节
	 */
	public void setQuestionBankVerse(String questionBankVerse) {
		this.questionBankVerse = questionBankVerse;
	}
	/**
	 * 获取：题库节
	 */
	public String getQuestionBankVerse() {
		return questionBankVerse;
	}
	/**
	 * 设置：题库知识点
	 */
	public void setQuestionBankKnowledge(String questionBankKnowledge) {
		this.questionBankKnowledge = questionBankKnowledge;
	}
	/**
	 * 获取：题库知识点
	 */
	public String getQuestionBankKnowledge() {
		return questionBankKnowledge;
	}
	
	public String getIsRelevance() {
		return isRelevance;
	}
	public void setIsRelevance(String isRelevance) {
		this.isRelevance = isRelevance;
	}
	
	public String getTimeOrdering() {
		return timeOrdering;
	}
	public void setTimeOrdering(String timeOrdering) {
		this.timeOrdering = timeOrdering;
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
	
	public MaterialType getS_type() {
		return s_type;
	}
	public void setS_type(MaterialType s_type) {
		this.s_type = s_type;
	}
	public MaterialProperty getS_property() {
		return s_property;
	}
	public void setS_property(MaterialProperty s_property) {
		this.s_property = s_property;
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getChapterName() {
		return chapterName;
	}
	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}
	public String getVerseName() {
		return verseName;
	}
	public void setVerseName(String verseName) {
		this.verseName = verseName;
	}
	
	public String getKpName() {
		return kpName;
	}
	public void setKpName(String kpName) {
		this.kpName = kpName;
	}
	
	@Override
	public String toString() {
		return "MaterialDetailEntity [id=" + id + ", name=" + name + ", type=" + type + ", s_type=" + s_type
				+ ", property=" + property + ", s_property=" + s_property + ", createBy=" + createBy + ", createTime="
				+ createTime + ", updateBy=" + updateBy + ", updateTime=" + updateTime + ", status=" + status
				+ ", vedioAddr=" + vedioAddr + ", returnAddr=" + returnAddr + ", pushContent=" + pushContent
				+ ", pushType=" + pushType + ", pushTime=" + pushTime + ", questionBankCourse=" + questionBankCourse
				+ ", questionBankChapter=" + questionBankChapter + ", questionBankVerse=" + questionBankVerse
				+ ", questionBankKnowledge=" + questionBankKnowledge + ", isRelevance=" + isRelevance
				+ ", timeOrdering=" + timeOrdering + ", creationName=" + creationName + ", modifiedName=" + modifiedName
				+ "]";
	}
	
	private String qbCourseName;		
	
	private String qbChapterName;	   
	
	private String qbVerseName;	  

	public String getQbCourseName() {
		return qbCourseName;
	}
	public void setQbCourseName(String qbCourseName) {
		this.qbCourseName = qbCourseName;
	}
	public String getQbChapterName() {
		return qbChapterName;
	}
	public void setQbChapterName(String qbChapterName) {
		this.qbChapterName = qbChapterName;
	}
	public String getQbVerseName() {
		return qbVerseName;
	}
	public void setQbVerseName(String qbVerseName) {
		this.qbVerseName = qbVerseName;
	}
	
}
