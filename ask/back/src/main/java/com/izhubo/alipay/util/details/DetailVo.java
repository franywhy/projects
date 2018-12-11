package com.izhubo.alipay.util.details;

import java.io.Serializable;

public class DetailVo implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -2627948386321108538L;
	
	/** 流水号 */
	private String no;
	/** 收款方账号 */
	private String email;
	/** 账号姓名 */
	private String name;
	/** 付款金额 */
	private String money;
	/** 成功标识 S F */
	private String tag;
	/** 成功/失败原因(null) */
	private String msg;
	/** 支付宝内部流水号 */
	private String alipayNo;
	/** 完成时间 */
	private String date;

	
	
	public DetailVo() {
		super();
	}

	public DetailVo(String no, String email, String name, String money, String tag, String msg, String alipayNo,
			String date) {
		super();
		this.no = no;
		this.email = email;
		this.name = name;
		this.money = money;
		this.tag = tag;
		this.msg = msg;
		this.alipayNo = alipayNo;
		this.date = date;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAlipayNo() {
		return alipayNo;
	}

	public void setAlipayNo(String alipayNo) {
		this.alipayNo = alipayNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "DetailVo [no=" + no + ", email=" + email + ", name=" + name + ", money=" + money + ", tag=" + tag
				+ ", msg=" + msg + ", alipayNo=" + alipayNo + ", date=" + date + "]";
	}

}
