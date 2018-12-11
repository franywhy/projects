package io.renren.pojo.studio;

import java.io.Serializable;
import java.util.Date;

import io.renren.entity.MallStudioEntity;
import io.renren.utils.Constant;

public class StudioPOJO implements Serializable {
	private static final long serialVersionUID = 1L;

	// 直播室id
	private Long studioId;
	// 机构id
	private String schoolId;
	// 是否删除 0.未删除 1.删除 用于软删除
	private Integer dr = Constant.DR.NORMAL.getValue();
	// 直播室名称
	private String studioName;
	// 是否使用 1.正常 0.停用
	private Integer status = (int) Constant.Status.RESUME.getValue();
	// 备注
	private String remake;
	// 创建用户
	private Long creator;
	// 创建时间
	private Date creationTime;
	// 修改用户
	private Long modifier;
	// 创建时间
	private Date modifiedTime;

	// 创建用户名称
	private String creationName;
	// 修改用户名称
	private String modifiedName;
	public Long getStudioId() {
		return studioId;
	}
	public void setStudioId(Long studioId) {
		this.studioId = studioId;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getStudioName() {
		return studioName;
	}
	public void setStudioName(String studioName) {
		this.studioName = studioName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemake() {
		return remake;
	}
	public void setRemake(String remake) {
		this.remake = remake;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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
	@Override
	public String toString() {
		return "StudioPOJO [studioId=" + studioId + ", schoolId=" + schoolId + ", dr=" + dr + ", studioName="
				+ studioName + ", status=" + status + ", remake=" + remake + ", creator=" + creator + ", creationTime="
				+ creationTime + ", modifier=" + modifier + ", modifiedTime=" + modifiedTime + ", creationName="
				+ creationName + ", modifiedName=" + modifiedName + "]";
	}
	
	public static MallStudioEntity getEntity(StudioPOJO pojo){
		MallStudioEntity en = new MallStudioEntity();
		if(null != pojo){
			//直播室id
			en.setStudioId(pojo.getStudioId());
			//机构id
			en.setSchoolId(pojo.getSchoolId());
			//是否删除   0.未删除  1.删除   用于软删除
			en.setDr(pojo.getDr());
			//备注
			en.setRemake(pojo.getRemake());
			//创建用户
			en.setCreator(pojo.getCreator());
			//创建时间
			en.setCreationTime(pojo.getCreationTime());
			//修改用户
			en.setModifier(pojo.getModifier());
			//创建时间
			en.setModifiedTime(pojo.getModifiedTime());
		}
		return en;
	}
}
