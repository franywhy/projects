package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

import io.renren.utils.Constant;



/**
 * 阶段实体
 * 
 * @author 
 * @email 
 * @date 2018-01-17 11:10:34
 */
public class PhasePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//阶段ID
	private Long phaseId;
	//课程ID
	private Long courseId;
	//阶段名称
	private String phaseName;
	//是否删除   0.未删除  1.删除   用于软删除
	private Integer dr = Constant.DR.NORMAL.getValue();
	//阶段编号
	private String phaseNo;
	//创建时间
	private String createTime;
	
	
	public Long getPhaseId() {
		return phaseId;
	}
	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public String getPhaseName() {
		return phaseName;
	}
	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getPhaseNo() {
		return phaseNo;
	}
	public void setPhaseNo(String phaseNo) {
		this.phaseNo = phaseNo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return "PhasePOJO [phaseId=" + phaseId + ", courseId=" + courseId + ", phaseName=" + phaseName + ", dr=" + dr
				+ ", phaseNo=" + phaseNo + ", createTime=" + createTime + "]";
	}
	
	
	

	
}
