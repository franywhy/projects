package io.renren.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomFieldsEntity {
	//支付时间
	@JsonProperty
	private String TextField_21507;
	//省份
	@JsonProperty
	private String TextField_21508;
	//层次
	@JsonProperty
	private String TextField_21509;
	//班级
	@JsonProperty
	private String TextField_21510;
	//校区
	@JsonProperty
	private String TextField_21511;
	//专业
	@JsonProperty
	private String TextField_21512;
	//商品
	@JsonProperty
	private String TextField_21513;
	
	@JsonIgnore
	public String getTextField_21507() {
		return TextField_21507;
	}
	@JsonIgnore
	public void setTextField_21507(String textField_21507) {
		TextField_21507 = textField_21507;
	}
	@JsonIgnore
	public String getTextField_21508() {
		return TextField_21508;
	}
	@JsonIgnore
	public void setTextField_21508(String textField_21508) {
		TextField_21508 = textField_21508;
	}
	@JsonIgnore
	public String getTextField_21509() {
		return TextField_21509;
	}
	@JsonIgnore
	public void setTextField_21509(String textField_21509) {
		TextField_21509 = textField_21509;
	}
	@JsonIgnore
	public String getTextField_21510() {
		return TextField_21510;
	}
	@JsonIgnore
	public void setTextField_21510(String textField_21510) {
		TextField_21510 = textField_21510;
	}
	@JsonIgnore
	public String getTextField_21511() {
		return TextField_21511;
	}
	@JsonIgnore
	public void setTextField_21511(String textField_21511) {
		TextField_21511 = textField_21511;
	}
	@JsonIgnore
	public String getTextField_21512() {
		return TextField_21512;
	}
	@JsonIgnore
	public void setTextField_21512(String textField_21512) {
		TextField_21512 = textField_21512;
	}
	@JsonIgnore
	public String getTextField_21513() {
		return TextField_21513;
	}
	@JsonIgnore
	public void setTextField_21513(String textField_21513) {
		TextField_21513 = textField_21513;
	}
	
	
}
