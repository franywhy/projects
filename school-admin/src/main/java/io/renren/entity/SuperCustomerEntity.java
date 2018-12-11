package io.renren.entity;

public class SuperCustomerEntity {
	private CustomerEntity customer;
	private String tags;
	
	public CustomerEntity getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	
}
