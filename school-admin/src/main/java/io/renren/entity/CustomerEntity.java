package io.renren.entity;

public class CustomerEntity {
	//学员姓名
	private String nick_name;
	//负责客服id
	private Integer owner_id;
	//负责客服组id
	private Integer owner_group_id;
	//电话
	private String[][] cellphones;
	
	private CustomFieldsEntity custom_fields;

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public Integer getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(Integer owner_id) {
		this.owner_id = owner_id;
	}

	public Integer getOwner_group_id() {
		return owner_group_id;
	}

	public void setOwner_group_id(Integer owner_group_id) {
		this.owner_group_id = owner_group_id;
	}

	public String[][] getCellphones() {
		return cellphones;
	}

	public void setCellphones(String[][] cellphones) {
		this.cellphones = cellphones;
	}


	public CustomFieldsEntity getCustom_fields() {
		return custom_fields;
	}

	public void setCustom_fields(CustomFieldsEntity custom_fields) {
		this.custom_fields = custom_fields;
	}
	
	
}
