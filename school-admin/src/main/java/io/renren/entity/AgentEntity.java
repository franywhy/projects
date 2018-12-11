package io.renren.entity;

public class AgentEntity {
	
	private String email;
	
	private String password;
	
	private int[] agent_role_ids; 
	
	private int[] user_group_ids;
	
	private int[] department_ids;
	
	private int im_ability_value;
	
	private String nick_name;
	
	private String aliase;
	
	private String cellphone;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int[] getAgent_role_ids() {
		return agent_role_ids;
	}

	public void setAgent_role_ids(int[] agent_role_ids) {
		this.agent_role_ids = agent_role_ids;
	}

	public int[] getUser_group_ids() {
		return user_group_ids;
	}

	public void setUser_group_ids(int[] user_group_ids) {
		this.user_group_ids = user_group_ids;
	}

	public int[] getDepartment_ids() {
		return department_ids;
	}

	public void setDepartment_ids(int[] department_ids) {
		this.department_ids = department_ids;
	}

	public int getIm_ability_value() {
		return im_ability_value;
	}

	public void setIm_ability_value(int im_ability_value) {
		this.im_ability_value = im_ability_value;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getAliase() {
		return aliase;
	}

	public void setAliase(String aliase) {
		this.aliase = aliase;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	
	
}
