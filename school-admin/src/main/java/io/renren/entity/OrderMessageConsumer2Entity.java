package io.renren.entity;

public class OrderMessageConsumer2Entity {

//	private String i;
	private String area_name;
	private String nc_area_id;
	private String zk_area_id;
	private String nc_sign_code;
	private String nc_sign_time;
	private String user_name;
	private String order_no;
	private String user_mobile;
	private String zk_commodity_id;
//
//	public String getI() {
//		return i;
//	}
//
//	public void setI(String i) {
//		this.i = i;
//	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getNc_area_id() {
		return nc_area_id;
	}

	public void setNc_area_id(String nc_area_id) {
		this.nc_area_id = nc_area_id;
	}

	public String getZk_area_id() {
		return zk_area_id;
	}

	public void setZk_area_id(String zk_area_id) {
		this.zk_area_id = zk_area_id;
	}

	public String getNc_sign_code() {
		return nc_sign_code;
	}

	public void setNc_sign_code(String nc_sign_code) {
		this.nc_sign_code = nc_sign_code;
	}

	public String getNc_sign_time() {
		return nc_sign_time;
	}

	public void setNc_sign_time(String nc_sign_time) {
		this.nc_sign_time = nc_sign_time;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getUser_mobile() {
		return user_mobile;
	}

	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}

	public String getZk_commodity_id() {
		return zk_commodity_id;
	}

	public void setZk_commodity_id(String zk_commodity_id) {
		this.zk_commodity_id = zk_commodity_id;
	}

	@Override
	public String toString() {
		return "OrderMessageConsumer2Entity [area_name=" + area_name + ", nc_area_id=" + nc_area_id + ", zk_area_id=" + zk_area_id + ", nc_sign_code="
				+ nc_sign_code + ", nc_sign_time=" + nc_sign_time + ", user_name=" + user_name + ", order_no=" + order_no + ", user_mobile="
				+ user_mobile + ", zk_commodity_id=" + zk_commodity_id + "]";
	}

}
