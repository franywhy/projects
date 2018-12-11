package io.renren.entity;

/**
 * 合同模板表
 * @author hquser
 *
 */
public class ContractTemplateEntity {
	
	//主键Id
	private Long id; 
	//合同模板Id
	private String templateId;
	//合同模板名称
	private String templateName;
	
	//合同的公司id
	private Integer  companyId;
	//合同的公司名称 
	private String  companyName;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
}
