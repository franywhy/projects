package io.renren.entity;

import java.util.Map;

public class DeptAndSchoolConsumerEntity {
	
	private String name;
	
	private String telephone;
	
	private String address;
	
	private String code;
	
	private String nc_id;
	
	private String parent_code;
	
	private String nc_parent_id;
	
	private Integer dr;
	
	private String company_type;
	
	private String city;
	
	private Map<String , Object> location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

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

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}

	public String getNc_parent_id() {
		return nc_parent_id;
	}

	public void setNc_parent_id(String nc_parent_id) {
		this.nc_parent_id = nc_parent_id;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getCompany_type() {
		return company_type;
	}

	public void setCompany_type(String company_type) {
		this.company_type = company_type;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Map<String, Object> getLocation() {
		return location;
	}

	public void setLocation(Map<String, Object> location) {
		this.location = location;
	}
	
}
