package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 推送内容模板表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 11:31:58
 */
public class PushTimeTemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//模板名称
	private String name;
	//推送日期 -1：昨天 0：当天 1：明天
	private Integer pushDay;
	//推送时点，格式  07:00
	private String pushTime;
	//推送内容模板
	private String pushContentTemplate;
	//是否删除 0：正常 1：删除
	private Integer dr;
	//更新时间
	private Date ts;
	//备注
	private String remark;
	//推送类型 0:定点推送 1:定时推送
	private Integer type;

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：模板名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：模板名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：推送日期 -1：昨天 0：当天 1：明天
	 */
	public void setPushDay(Integer pushDay) {
		this.pushDay = pushDay;
	}
	/**
	 * 获取：推送日期 -1：昨天 0：当天 1：明天
	 */
	public Integer getPushDay() {
		return pushDay;
	}
	/**
	 * 设置：推送时点，格式  07:00
	 */
	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}
	/**
	 * 获取：推送时点，格式  07:00
	 */
	public String getPushTime() {
		return pushTime;
	}
	/**
	 * 设置：推送内容模板
	 */
	public void setPushContentTemplate(String pushContentTemplate) {
		this.pushContentTemplate = pushContentTemplate;
	}
	/**
	 * 获取：推送内容模板
	 */
	public String getPushContentTemplate() {
		return pushContentTemplate;
	}
	/**
	 * 设置：是否删除 0：正常 1：删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：是否删除 0：正常 1：删除
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：更新时间
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getTs() {
		return ts;
	}
}
