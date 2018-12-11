package io.renren.pojo;

import java.io.Serializable;

import io.renren.entity.MallGoodsDetailsEntity;

public class MallGoodsDetailsPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//商品ID
	private Long mallGoodsId;
	//省份
	private Long mallAreaId;
	//课程ID
	private Long courseId;
	//被替代课程
	private Integer isSubstituted;
	//代替课程
	private Integer isSubstitute;
	//是否统考
	private Integer isUnitedExam;
	//专业不对口课程
	private Integer isSuitable;
	//平台ID
	private String schoolId;
	//排序
	private Integer orderNum;
	//dr
	private Integer dr;
	
	//省份名称
	private String areaName;
	//课程名称
	private String courseName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMallGoodsId() {
		return mallGoodsId;
	}
	public void setMallGoodsId(Long mallGoodsId) {
		this.mallGoodsId = mallGoodsId;
	}
	public Long getMallAreaId() {
		return mallAreaId;
	}
	public void setMallAreaId(Long mallAreaId) {
		this.mallAreaId = mallAreaId;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public Integer getIsSubstituted() {
		return isSubstituted;
	}
	public void setIsSubstituted(Integer isSubstituted) {
		this.isSubstituted = isSubstituted;
	}
	public Integer getIsSubstitute() {
		return isSubstitute;
	}
	public void setIsSubstitute(Integer isSubstitute) {
		this.isSubstitute = isSubstitute;
	}
	public Integer getIsUnitedExam() {
		return isUnitedExam;
	}
	public void setIsUnitedExam(Integer isUnitedExam) {
		this.isUnitedExam = isUnitedExam;
	}
	public Integer getIsSuitable() {
		return isSuitable;
	}
	public void setIsSuitable(Integer isSuitable) {
		this.isSuitable = isSuitable;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	
	@Override
	public String toString() {
		return "MallGoodsDetailsPOJO [id=" + id + ", mallGoodsId=" + mallGoodsId + ", mallAreaId=" + mallAreaId
				+ ", courseId=" + courseId + ", isSubstituted=" + isSubstituted + ", isSubstitute=" + isSubstitute
				+ ", isUnitedExam=" + isUnitedExam + ", isSuitable=" + isSuitable + ", schoolId=" + schoolId
				+ ", orderNum=" + orderNum + ", dr=" + dr + ", areaName=" + areaName + ", courseName=" + courseName
				+ "]";
	}
	public static MallGoodsDetailsEntity getEntity(MallGoodsDetailsPOJO pojo){
		MallGoodsDetailsEntity en = new MallGoodsDetailsEntity();
		if(pojo != null){
			en.setId(pojo.getId());
			en.setMallGoodsId(pojo.getMallGoodsId());
			en.setMallAreaId(pojo.getMallAreaId());
			en.setCourseId(pojo.getCourseId());
			en.setIsSubstituted(pojo.getIsSubstituted());
			en.setIsSubstitute(pojo.getIsSubstitute());
			en.setIsUnitedExam(pojo.getIsUnitedExam());
			en.setIsSuitable(pojo.getIsSuitable());
			en.setSchoolId(pojo.getSchoolId());
			en.setOrderNum(pojo.getOrderNum());
			en.setDr(pojo.getDr());
		}
		return en;
	}
	
}
