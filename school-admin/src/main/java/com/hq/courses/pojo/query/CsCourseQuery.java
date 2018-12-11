package com.hq.courses.pojo.query;
/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月8日
 */
public class CsCourseQuery extends BaseQuery {
	
	// 自增id
	private Long courseId;
	// 课程名称
	private String courseName;
	// 课程编码
	private String courseNo;
	// 产品线pk1.自考;2.会计学历;3学来学网;5.会计猎才;6.会计培训
	private Long productId;
	// 部门ID
	private Long deptId;
	// 启用状态:0.作废;1.正常
	private Integer status;
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseNo() {
		return courseNo;
	}
	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "CsCourseQuery [courseId=" + courseId + ", courseName=" + courseName + ", courseNo=" + courseNo
				+ ", productId=" + productId + ", deptId=" + deptId + ", status=" + status + "]";
	}
	
}
