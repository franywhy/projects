package io.renren.pojo.feedbackrecord;

import java.util.Date;

public class FeedbackDisplayPOJO {
	// 序号
	private Long id;
	// 反馈类型
	private String feedbackType;
	// 反馈内容
	private String feedbackContent;
	// 创建人
	private String createPerson;
	// 修改人
	private String modifyPerson;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date modifyTime;
	// 状态
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	public String getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getModifyPerson() {
		return modifyPerson;
	}

	public void setModifyPerson(String modifyPerson) {
		this.modifyPerson = modifyPerson;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
