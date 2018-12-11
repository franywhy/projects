package io.renren.modules.job.pojo;

import java.io.Serializable;

/**
 * Created by DL on 2018/10/16.
 */
public class CourseRecordDetailPOJO implements Serializable{

    //录播课次id
    private Long recordId;
    //课程id
    private Long courseId;
    //总时长(时分秒字符串)
    private String duration;
   //总时长/s
    private Long durationS;
    //课程产品线
    private  Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getDurationS() {
        return durationS;
    }

    public void setDurationS(Long durationS) {
        this.durationS = durationS;
    }
}
