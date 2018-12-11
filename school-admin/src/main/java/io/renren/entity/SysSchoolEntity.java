package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 校区管理档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-29 10:09:52
 */
public class SysSchoolEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//父id
	private Long parentId;
	//名称（省、市、校区）
	private String name;
	//图片
	private String pic;
	//电话号码
	private String telephone;
	//详细地址
	private String address;
	//经度
	private Double longitude;
	//维度
	private Double latitude;
	//校区编号
	private String code;
	//校区nc_id
	private String ncId;
	//类型0：省 1：市 2：校区
	private Integer type;
	//软删除0：正常 1：删除
	private Integer dr;
	//业务线id
	private String businessId;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：父id
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父id
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：名称（省、市、校区）
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称（省、市、校区）
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：图片
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	/**
	 * 获取：图片
	 */
	public String getPic() {
		return pic;
	}
	/**
	 * 设置：电话号码
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取：电话号码
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * 设置：详细地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：详细地址
	 */
	public String getAddress() {
		return address;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	/**
	 * 设置：校区编号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：校区编号
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：校区nc_id
	 */
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	/**
	 * 获取：校区nc_id
	 */
	public String getNcId() {
		return ncId;
	}
	/**
	 * 设置：类型0：省 1：市 2：校区
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型0：省 1：市 2：校区
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：软删除0：正常 1：删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：软删除0：正常 1：删除
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：业务线id
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	/**
	 * 获取：业务线id
	 */
	public String getBusinessId() {
		return businessId;
	}
}
