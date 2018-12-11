package io.renren.pojo.timetable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.renren.entity.TimeTableDetailEntity;
import io.renren.entity.TimetableEntity;

public class TimeTablePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//编号
	private Long number;
	//名称
	private String name;
	//建档时间
	private Date createTime;
	//最后修改日期
	private Date updateTime;
	//建档人
	private Long createPerson;
	//最后修改人
	private Long updatePerson;
	//备注
	private String comments;
	//是否停用
	private Integer status;
	//业务类型id
	private String schoolId;
	//产品pk
	private Long productId;
	
	//产品名字
	private String productName;
	
	//建档人名字
	private String creationName;
	//最后修改人名字
	private String modifiedName;
	
	//子表集合
	private List<TimeTableDetailPOJO> detailList;
	
	public List<TimeTableDetailPOJO> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<TimeTableDetailPOJO> detailList) {
		this.detailList = detailList;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	public Long getUpdatePerson() {
		return updatePerson;
	}
	public void setUpdatePerson(Long updatePerson) {
		this.updatePerson = updatePerson;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
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
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Override
	public String toString() {
		return "TimeTablePOJO [number=" + number + ", name=" + name + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", createPerson=" + createPerson + ", updatePerson=" + updatePerson + ", comments="
				+ comments + ", status=" + status + ", schoolId=" + schoolId + ", productId=" + productId
				+ ", productName=" + productName + ", creationName=" + creationName + ", modifiedName=" + modifiedName
				+ ", detailList=" + detailList + "]";
	}
	public static TimetableEntity getEntity(TimeTablePOJO pojo){
		TimetableEntity en = new TimetableEntity();
		if(null != pojo){
			//编号
			en.setNumber(pojo.getNumber());
			//名称
			en.setName(pojo.getName());
			//建档时间
			en.setCreateTime(pojo.getCreateTime());
			//最后修改日期
			en.setUpdateTime(pojo.getUpdateTime());
			//建档人
			en.setCreatePerson(pojo.getCreatePerson());
			//最后修改人
			en.setUpdatePerson(pojo.getUpdatePerson());
			//备注
			en.setComments(pojo.getComments());
			//是否停用
			en.setStatus(pojo.getStatus());
			//平台id
			en.setSchoolId(pojo.getSchoolId());
			//产品pk
			en.setProductId(pojo.getProductId());;
		}
		return en;
	}
}
