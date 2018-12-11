package io.renren.entity;

import java.io.Serializable;

public class ExamEntityTemp implements Serializable {

	private static final long serialVersionUID = 1L;
		
	//报考id
	private Long id;
	//报考单号
	private String registrationNo;
	//产品线id
	private Long productId;		
	//原因
	private String reason;
	//备注
	private String remark;
	
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getRegistrationNo() {
		return registrationNo;
	}
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "ExamEntityTemp [id=" + id + ", registrationNo=" + registrationNo + ", productId=" + productId
				+ ", reason=" + reason + ", remark=" + remark + "]";
	}
	
	
	
	
}
