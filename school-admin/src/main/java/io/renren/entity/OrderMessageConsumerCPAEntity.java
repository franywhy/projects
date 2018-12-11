package io.renren.entity;

import java.util.Date;

public class OrderMessageConsumerCPAEntity {
	private String area_name;//省份名称
	private String sign_code;//报名表号
	private String sign_time;//报名时间
	private String user_name;//学员姓名
	private String user_mobile;//学员手机号
	private Long class_id;//班级id
	private String commodity_id;//商品id
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public String getSign_code() {
		return sign_code;
	}
	public void setSign_code(String sign_code) {
		this.sign_code = sign_code;
	}
	public String getSign_time() {
		return sign_time;
	}
	public void setSign_time(String sign_time) {
		this.sign_time = sign_time;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_mobile() {
		return user_mobile;
	}
	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}
	public Long getClass_id() {
		return class_id;
	}
	public void setClass_id(Long class_id) {
		this.class_id = class_id;
	}
	public String getCommodity_id() {
		return commodity_id;
	}
	public void setCommodity_id(String commodity_id) {
		this.commodity_id = commodity_id;
	}
	@Override
	public String toString() {
		return "OrderMessageConsumerCPAEntity [area_name=" + area_name + ", sign_code=" + sign_code + ", sign_time="
				+ sign_time + ", user_name=" + user_name + ", user_mobile=" + user_mobile + ", class_id=" + class_id
				+ ", commodity_id=" + commodity_id + "]";
	}
	
	
}
