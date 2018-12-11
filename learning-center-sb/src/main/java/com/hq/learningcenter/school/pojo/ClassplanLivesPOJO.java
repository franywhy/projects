package com.hq.learningcenter.school.pojo;

public class ClassplanLivesPOJO {

	private String classplanLiveId;
	
	private String classplanLiveName;
	
	private String teacher;
	
	private String time;
	
	private Long timestamp;
	
	private Integer classStatus;
	
	private Integer isAttend;
	
	private String attendPer;

	/*app4.0新增字段*/
    //上期复习地址
    private String reviewUrl;

    //本期预习地址
    private String prepareUrl;

    //课堂作业地址
    private String coursewareUrl;

    //上传的文件名
    private String fileName;

    //上期复习文件名
    private String reviewName;

    //本期预习文件名
    private String prepareName;

    //课堂作文件名
    private String coursewareName;


    //课后作业地址
    //private String homeworkUrl;

    //阶段类型:练习阶段
    //private String practiceStageId;

    //阶段id
    private Long phaseId;

    //直播课次图片
    private String pic;

    //上课时间
    private String courseTime;
    //开始时间时间戳
    private Long startTimeStamp;
    //结束时间时间戳
    private Long endTimeStamp;

    //即将开始时间
    private Long readyTime;

    //进入直播间结束时间
    private Long closeTime;

    public Long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    public Long getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(Long readyTime) {
        this.readyTime = readyTime;
    }

    //app4.0.1课次的课堂资料就是原来的资料字段
    private String fileUrl;

    private Long courseFk;

    public Long getCourseFk() {
        return courseFk;
    }

    public void setCourseFk(Long courseFk) {
        this.courseFk = courseFk;
    }

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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


    public String getClassplanLiveId() {
		return classplanLiveId;
	}

	public void setClassplanLiveId(String classplanLiveId) {
		this.classplanLiveId = classplanLiveId;
	}

	public String getClassplanLiveName() {
		return classplanLiveName;
	}

	public void setClassplanLiveName(String classplanLiveName) {
		this.classplanLiveName = classplanLiveName;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getClassStatus() {
		return classStatus;
	}

	public void setClassStatus(Integer classStatus) {
		this.classStatus = classStatus;
	}

	public Integer getIsAttend() {
		return isAttend;
	}

	public void setIsAttend(Integer isAttend) {
		this.isAttend = isAttend;
	}

	public String getAttendPer() {
		return attendPer;
	}

	public void setAttendPer(String attendPer) {
		this.attendPer = attendPer;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
}
