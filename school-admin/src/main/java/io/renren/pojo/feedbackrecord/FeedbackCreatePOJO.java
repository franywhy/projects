package io.renren.pojo.feedbackrecord;

public class FeedbackCreatePOJO {
	
	// 反馈类型
	private String feedbackType;
	// 反馈内容
	private String feedbackContent;

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

}
