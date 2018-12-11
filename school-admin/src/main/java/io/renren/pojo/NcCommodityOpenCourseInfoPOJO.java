package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 报读班型与公开课权限关系表
 * 
 * @date 2018-10-30 15:54:24
 */
public class NcCommodityOpenCourseInfoPOJO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 创建人ID
	 */
	private Long creator;
	/**
	 * 创建日期
	 */
	private Date createTime;
	/**
	 * 修改人ID
	 */
	private Long modifier;
	/**
	 * 修改时间
	 */
	private Date modifyTime;
	/**
	 * 商品ID(NC报读班型)
	 */
	private String ncCommodityId;
	/**
	 * 班型名称
	 */
	private String ncCommodityName;
	/**
	 * 权限ID
	 */
	private Integer authorityId;
	/**
	 * 权限名字
	 */
	private String authorityName;
	/**
	 * 删除标识
	 */
	private Integer dr;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 设置：编号
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：编号
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：创建人ID
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建人ID
	 */
	public Long getCreator() {
		return creator;
	}
	/**
	 * 设置：创建日期
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建日期
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改人ID
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：修改人ID
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：商品ID(NC报读班型)
	 */
	public void setNcCommodityId(String ncCommodityId) {
		this.ncCommodityId = ncCommodityId;
	}
	/**
	 * 获取：商品ID(NC报读班型)
	 */
	public String getNcCommodityId() {
		return ncCommodityId;
	}
	/**
	 * 设置：班型名称
	 */
	public void setNcCommodityName(String ncCommodityName) {
		this.ncCommodityName = ncCommodityName;
	}
	/**
	 * 获取：班型名称
	 */
	public String getNcCommodityName() {
		return ncCommodityName;
	}
	/**
	 * 设置：权限ID
	 */
	public void setAuthorityId(Integer authorityId) {
		this.authorityId = authorityId;
	}
	/**
	 * 获取：权限ID
	 */
	public Integer getAuthorityId() {
		return authorityId;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}
}
