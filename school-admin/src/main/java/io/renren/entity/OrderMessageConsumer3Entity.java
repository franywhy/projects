package io.renren.entity;

public class OrderMessageConsumer3Entity {

	private String area_name;//省份名称
	private String sign_code;//报名表号
	private String sign_time;//报名时间
	private String user_name;//学员姓名
	private String user_mobile;//学员手机号
	private String commodity_id;//商品id
	
	private Integer i;//业务线标志 1：自考 2：会计 3：学来学往
	
	
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


	public String getCommodity_id() {
		return commodity_id;
	}


	public void setCommodity_id(String commodity_id) {
		this.commodity_id = commodity_id;
	}

	public Integer getI() {
		return i;
	}


	public void setI(Integer i) {
		this.i = i;
	}


	@Override
	public String toString() {
		return "OrderMessageConsumer3Entity [area_name=" + area_name + ", sign_code=" + sign_code + ", sign_time="
				+ sign_time + ", user_name=" + user_name + ", user_mobile=" + user_mobile + ", commodity_id="
				+ commodity_id + ", i=" + i + "]";
	}

	
}
