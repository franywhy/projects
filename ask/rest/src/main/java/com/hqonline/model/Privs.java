package com.hqonline.model;

public enum Privs {
	学员("priv0", "学员", 0), 抢答("priv1", "抢答", 1), 招生("priv2", "招生", 2), 特殊标签老师(
			"priv3", "特殊标签老师", 3);

	private String key;
	private String name;
	private Integer i;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getI() {
		return i;
	}

	public void setI(Integer i) {
		this.i = i;
	}

	private Privs(String key, String name, Integer i) {
		this.key = key;
		this.name = name;
		this.i = i;
	}

	@Override
	public String toString() {
		return "key=" + key + ",name=" + name + ",i=" + i;
	}

}
