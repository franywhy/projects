package io.renren.mongo.entity;


import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="test")
public class TestEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -123423432423L;
	private String id;
	private String name;
	private String age;
	private String sex;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	

}
