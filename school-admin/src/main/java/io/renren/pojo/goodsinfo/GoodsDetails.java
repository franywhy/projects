package io.renren.pojo.goodsinfo;

public class GoodsDetails {
	// 序号
	private Long id;
	// 省份
	private Long mallAreaId;
	private String mallAreaName;
	// 课程ID
	private Long courseId;
	private String courseName;
	// 被替代课程
	private Integer isSubstituted;
	// 代替课程
	private Integer isSubstitute;
	// 是否统考
	private Integer isUnitedExam;
	// 专业不对口课程
	private Integer isSuitable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public Long getMallAreaId() {
		return mallAreaId;
	}

	public void setMallAreaId(Long mallAreaId) {
		this.mallAreaId = mallAreaId;
	}

	public String getMallAreaName() {
		return mallAreaName;
	}

	public void setMallAreaName(String mallAreaName) {
		this.mallAreaName = mallAreaName;
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
	

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
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

	@Override
	public String toString() {
		return "GoodsDetails [id=" + id + ", mallAreaId=" + mallAreaId + ", mallAreaName=" + mallAreaName
				+ ", courseId=" + courseId + ", courseName=" + courseName + ", isSubstituted=" + isSubstituted
				+ ", isSubstitute=" + isSubstitute + ", isUnitedExam=" + isUnitedExam + ", isSuitable=" + isSuitable
				+ "]";
	}
	

}
