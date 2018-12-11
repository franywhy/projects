package com.hqjy.pay;

public class HDDataEntity {
	/**
	 * 订单号
	 */
    private String trade_no;
    /**
	 * 状态
	 */
    private String order_status;
    /**
	 * 状态描述
	 */
    private String order_status_desc;
    /**
	 * 备注
	 */
    private String remark;
    /**
	 * 姓名
	 */
    private String id_name;
    /**
	 * 身份证号
	 */
    private String id_no;
    /**
	 * 手机号
	 */
    private String phone_no;
    /**
	 * 贷款机构名称
	 */
    private String capital_name;
    /**
	 * 申请贷款金额
	 */
    private String apply_amount;
    /**
	 * 申请贷款时间
	 */
    private String apply_time;
    /**
	 * 商品内容
	 */
    private String commodity;
    /**
	 * 所属校区
	 */
    private String school_zone;
    /**
	 * 放款订单号
	 */
    private String lending_trade_no;
    /**
	 * 放款时间
	 */
    private String lending_time;
    /**
	 * 放款金额
	 */
    private String lending_amount;
    /**
     * 审核通过时间
     */
    private String approve_time;
    
	public String getApprove_time() {
		return approve_time;
	}
	public void setApprove_time(String approve_time) {
		this.approve_time = approve_time;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getOrder_status_desc() {
		return order_status_desc;
	}
	public void setOrder_status_desc(String order_status_desc) {
		this.order_status_desc = order_status_desc;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getId_name() {
		return id_name;
	}
	public void setId_name(String id_name) {
		this.id_name = id_name;
	}
	public String getId_no() {
		return id_no;
	}
	public void setId_no(String id_no) {
		this.id_no = id_no;
	}
	public String getPhone_no() {
		return phone_no;
	}
	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}
	public String getCapital_name() {
		return capital_name;
	}
	public void setCapital_name(String capital_name) {
		this.capital_name = capital_name;
	}
	public String getApply_amount() {
		return apply_amount;
	}
	public void setApply_amount(String apply_amount) {
		this.apply_amount = apply_amount;
	}
	public String getApply_time() {
		return apply_time;
	}
	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}
	public String getCommodity() {
		return commodity;
	}
	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}
	public String getSchool_zone() {
		return school_zone;
	}
	public void setSchool_zone(String school_zone) {
		this.school_zone = school_zone;
	}
	public String getLending_trade_no() {
		return lending_trade_no;
	}
	public void setLending_trade_no(String lending_trade_no) {
		this.lending_trade_no = lending_trade_no;
	}
	public String getLending_time() {
		return lending_time;
	}
	public void setLending_time(String lending_time) {
		this.lending_time = lending_time;
	}
	public String getLending_amount() {
		return lending_amount;
	}
	public void setLending_amount(String lending_amount) {
		this.lending_amount = lending_amount;
	}
    
}
