package com.hq.learningapi.pojo;

import java.io.Serializable;

public class AppSchoolPOJO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String pic;
	
	private String telephone;
	
	private String address;
	
	private Long distance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
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

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "AppSchoolEntity [id=" + id + ", name=" + name + ", pic=" + pic + ", telephone=" + telephone
				+ ", address=" + address + ", distance=" + distance + "]";
	}
	

}
