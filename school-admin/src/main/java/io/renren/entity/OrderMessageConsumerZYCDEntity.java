package io.renren.entity;

import java.util.Date;

public class OrderMessageConsumerZYCDEntity {
	/**
	 * 报名表号(编号)
	 */
	private String code;
	/**
	 * nc报名表pk，对应订单表nc_id
	 */
	private String nc_id;
	/**
	 * 学员姓名
	 */
	private String user_name;
	/**
	 * 学员手机号
	 */
	private String phone;
	/**
	 * nc校区pk，对应部门表的nc_id
	 */
	private String nc_school_pk;
	/**
	 * nc学员pk，对应学员表的nc_id
	 */
	private String nc_user_id;
	/**
	 * nc省份pk，对应省份表的nc_id
	 */
	private String province_pk;
	/**
	 * 是否删除订单0删除，1同步
	 */
	private Integer spec_status;
	/**
	 * nc报名表创建时间
	 */
	private Long create_time;
	/**
	 * 性别0女，1男，2保密
	 */
	private Integer sex;
	/**
	 * nc同步时间
	 */
	private Long syn_time;
	/**
	 * nc班型pk，对应商品表的nc_code
	 */
	private String nc_commodity_id;
	/**
	 * 订单状态0不正常，1正常
	 */
	private Integer status;
	/**
	 * 订单支付时间
	 */
	private Long ts;
	/**
	 * 备注，起提示学员是否可以排课
	 */
	private String remark;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNc_id() {
		return nc_id;
	}
	public void setNc_id(String nc_id) {
		this.nc_id = nc_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNc_school_pk() {
		return nc_school_pk;
	}
	public void setNc_school_pk(String nc_school_pk) {
		this.nc_school_pk = nc_school_pk;
	}
	public String getNc_user_id() {
		return nc_user_id;
	}
	public void setNc_user_id(String nc_user_id) {
		this.nc_user_id = nc_user_id;
	}
	public String getProvince_pk() {
		return province_pk;
	}
	public void setProvince_pk(String province_pk) {
		this.province_pk = province_pk;
	}
	public Integer getSpec_status() {
		return spec_status;
	}
	public void setSpec_status(Integer spec_status) {
		this.spec_status = spec_status;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Long getSyn_time() {
		return syn_time;
	}
	public void setSyn_time(Long syn_time) {
		this.syn_time = syn_time;
	}
	public String getNc_commodity_id() {
		return nc_commodity_id;
	}
	public void setNc_commodity_id(String nc_commodity_id) {
		this.nc_commodity_id = nc_commodity_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getTs() {
		return ts;
	}
	public void setTs(Long ts) {
		this.ts = ts;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "OrderMessageConsumerZYCDEntity [code=" + code + ", nc_id=" + nc_id + ", user_name=" + user_name
				+ ", phone=" + phone + ", nc_school_pk=" + nc_school_pk + ", nc_user_id=" + nc_user_id
				+ ", province_pk=" + province_pk + ", spec_status=" + spec_status + ", create_time=" + create_time
				+ ", sex=" + sex + ", syn_time=" + syn_time + ", nc_commodity_id=" + nc_commodity_id + ", status="
				+ status + ", ts=" + ts + ", remark=" + remark + "]";
	}
	
	
}
