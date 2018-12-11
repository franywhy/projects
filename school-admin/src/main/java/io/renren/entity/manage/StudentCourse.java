package io.renren.entity.manage;

public class StudentCourse {
    private Long id;

    private Long userId;

    private String ncId;

    private String ncUserId;

    private String ncCommodityId;

    private String courseNo;

    private String courseName;

    private String businessId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNcId() {
        return ncId;
    }

    public void setNcId(String ncId) {
        this.ncId = ncId;
    }

    public String getNcUserId() {
        return ncUserId;
    }

    public void setNcUserId(String ncUserId) {
        this.ncUserId = ncUserId;
    }

    public String getNcCommodityId() {
        return ncCommodityId;
    }

    public void setNcCommodityId(String ncCommodityId) {
        this.ncCommodityId = ncCommodityId;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}