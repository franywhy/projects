package io.renren.pojo.relatedCommodity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.renren.entity.RelatedCommodityDetailEntity;
import io.renren.entity.RelatedCommodityEntity;

public class RelatedCommodityPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// 关联商品id
	private Long relatedCommodityId;
	// 机构id
	private String schoolId;
	// 是否删除 0.未删除 1.删除 用于软删除
	private Integer dr;
	// 关联名称
	private String relatedName;
	// 备注
	private String remake;
	// 建档人
	private Long creator;
	// 创建时间
	private Date creationTime;
	// 修改人
	private Long modifier;
	// 修改日期
	private Date modifiedTime;
	// 状态 0：禁用 1：启用
	private Integer status;
	
	//创建人名称
	private String creationName;
	//修改人名称
	private String modifiedName;
	
	//子表集合
	private List<RelatedCommodityDetailPOJO> detailList;
	
	public List<RelatedCommodityDetailPOJO> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<RelatedCommodityDetailPOJO> detailList) {
		this.detailList = detailList;
	}
	public Long getRelatedCommodityId() {
		return relatedCommodityId;
	}
	public void setRelatedCommodityId(Long relatedCommodityId) {
		this.relatedCommodityId = relatedCommodityId;
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
	public String getRelatedName() {
		return relatedName;
	}
	public void setRelatedName(String relatedName) {
		this.relatedName = relatedName;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
		return "RelatedCommodityPOJO [relatedCommodityId=" + relatedCommodityId + ", schoolId=" + schoolId + ", dr="
				+ dr + ", relatedName=" + relatedName + ", remake=" + remake + ", creator=" + creator
				+ ", creationTime=" + creationTime + ", modifier=" + modifier + ", modifiedTime=" + modifiedTime
				+ ", status=" + status + ", creationName=" + creationName + ", mdifiedName=" + modifiedName + "]";
	}
	
	public static RelatedCommodityEntity getEntity(RelatedCommodityPOJO pojo){
		RelatedCommodityEntity en = new RelatedCommodityEntity();
		if(null != pojo){
			en.setRelatedCommodityId(pojo.getRelatedCommodityId());
			en.setSchoolId(pojo.getSchoolId());
			en.setDr(pojo.getDr());
			en.setRelatedName(pojo.getRelatedName());
			en.setRemake(pojo.getRemake());
			en.setCreator(pojo.getCreator());
			en.setCreationTime(pojo.getCreationTime());
			en.setModifier(pojo.getModifier());
			en.setModifiedTime(pojo.getModifiedTime());
			en.setStatus(pojo.getStatus());
		}
		return en;
	}
}
