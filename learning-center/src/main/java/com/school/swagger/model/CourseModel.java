package com.school.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author linchaokai
 * @Description
 * @date 2018/4/2 10:36
 */
@ApiModel
public class CourseModel {
    @ApiModelProperty(value="课程id",name="courseId")
    private Integer courseId;
    @ApiModelProperty(value="课程名称",name="courseName")
    private String courseName;


    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
