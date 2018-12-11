package com.hq.courses.pojo;

/**
 * 类说明
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017年11月22日
 */
public enum CsZJEnum {

	Z(1, "章", "Z_", "-"), J(2, "节", "J_", "-"), K(3, "知识点", "K_", "-");

	// 类型
	private Integer value;
	// 名称
	private String name;
	// id头部
	private String head;

	private String codeSplit;

	private CsZJEnum(Integer value, String name, String head, String codeSplit) {
		this.value = value;
		this.name = name;
		this.head = head;
		this.codeSplit = codeSplit;
	}

	public String getCodeSplit() {
		return codeSplit;
	}

	public void setCodeSplit(String codeSplit) {
		this.codeSplit = codeSplit;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

}
