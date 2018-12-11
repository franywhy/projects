package io.renren.pojo.course;

import java.io.Serializable;

import io.renren.entity.CourseLiveDetailsEntity;

public class CourseLiveDetailsPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//直播课次ID
	private Long liveId;
	//直播课次名称
	private String liveName;
	//直播课次班型权限
	private String liveClassTypeIds;
	//
	private String schoolId;
	//课程PK
	private Long courseId;
	
	/**
	 * 排序
	 */
	private Integer orderNum;

	//上期复习的文件名
	private String reviewName;
	//本期预习的文件名
	private String prepareName;
	//课堂资料的文件名
	private String coursewareName;
    //上期复习
    private String reviewUrl;
    //本期预习
    private String prepareUrl;
    //课堂资料
    private String coursewareUrl;
    //练习阶段ID
    private String practiceStageId;
    //阶段ID
    private String examStageId;
    //阶段名称
    private String examStageName;

	
	

	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 设置：直播课次ID
	 */
	public void setLiveId(Long liveId) {
		this.liveId = liveId;
	}
	/**
	 * 获取：直播课次ID
	 */
	public Long getLiveId() {
		return liveId;
	}
	/**
	 * 设置：直播课次名称
	 */
	public void setLiveName(String liveName) {
		this.liveName = liveName;
	}
	/**
	 * 获取：直播课次名称
	 */
	public String getLiveName() {
		return liveName;
	}
	/**
	 * 设置：直播课次班型权限
	 */
	public void setLiveClassTypeIds(String liveClassTypeIds) {
		this.liveClassTypeIds = liveClassTypeIds;
	}
	/**
	 * 获取：直播课次班型权限
	 */
	public String getLiveClassTypeIds() {
		return liveClassTypeIds;
	}
	/**
	 * 设置：
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：课程PK
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程PK
	 */
	public Long getCourseId() {
		return courseId;
	}


    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public String getPrepareUrl() {
        return prepareUrl;
    }

    public void setPrepareUrl(String prepareUrl) {
        this.prepareUrl = prepareUrl;
    }

    public String getCoursewareUrl() {
        return coursewareUrl;
    }

    public void setCoursewareUrl(String coursewareUrl) {
        this.coursewareUrl = coursewareUrl;
    }

    public String getPracticeStageId() {
        return practiceStageId;
    }

    public void setPracticeStageId(String practiceStageId) {
        this.practiceStageId = practiceStageId;
    }

    public String getExamStageId() {
        return examStageId;
    }

    public void setExamStageId(String examStageId) {
        this.examStageId = examStageId;
    }

    public String getExamStageName() {
        return examStageName;
    }

    public void setExamStageName(String examStageName) {
        this.examStageName = examStageName;
    }

	public String getReviewName() {
		return reviewName;
	}

	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}

	public String getPrepareName() {
		return prepareName;
	}

	public void setPrepareName(String prepareName) {
		this.prepareName = prepareName;
	}

	public String getCoursewareName() {
		return coursewareName;
	}

	public void setCoursewareName(String coursewareName) {
		this.coursewareName = coursewareName;
	}

	@Override
	public String toString() {
		return "CourseLiveDetailsPOJO{" +
				"liveId=" + liveId +
				", liveName='" + liveName + '\'' +
				", liveClassTypeIds='" + liveClassTypeIds + '\'' +
				", schoolId='" + schoolId + '\'' +
				", courseId=" + courseId +
				", orderNum=" + orderNum +
				", reviewName='" + reviewName + '\'' +
				", prepareName='" + prepareName + '\'' +
				", coursewareName='" + coursewareName + '\'' +
				", reviewUrl='" + reviewUrl + '\'' +
				", prepareUrl='" + prepareUrl + '\'' +
				", coursewareUrl='" + coursewareUrl + '\'' +
				", practiceStageId='" + practiceStageId + '\'' +
				", examStageId='" + examStageId + '\'' +
				", examStageName='" + examStageName + '\'' +
				'}';
	}

	public static CourseLiveDetailsEntity getEntity(CourseLiveDetailsPOJO pojo){
		CourseLiveDetailsEntity en = new CourseLiveDetailsEntity();
		if(null != pojo){
			en.setCourseId(pojo.getCourseId());
			en.setLiveClassTypeIds(pojo.getLiveClassTypeIds());
			en.setLiveId(pojo.getLiveId());
			en.setLiveName(pojo.getLiveName());
			en.setSchoolId(pojo.getSchoolId());
			en.setOrderNum(pojo.getOrderNum());
            en.setReviewUrl(pojo.getReviewUrl());
            en.setPrepareUrl(pojo.getPrepareUrl());
            en.setCoursewareUrl(pojo.getCoursewareUrl());
            en.setPracticeStageId(pojo.getPracticeStageId());
            en.setExamStageId(pojo.getExamStageId());
            en.setExamStageName(pojo.getExamStageName());
            en.setReviewName(pojo.getReviewName());
            en.setPrepareName(pojo.getPrepareName());
            en.setCoursewareName(pojo.getCoursewareName());
		}
		return en;
	}
	
}
