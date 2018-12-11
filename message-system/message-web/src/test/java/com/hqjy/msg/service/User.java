package com.hqjy.msg.service;

import java.io.Serializable;

public class User implements Serializable {

	private String username;
	private Integer age;
	private String sex;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", age=" + age + ", sex=" + sex + "]";
	}
	
}
