package com.hq.learningcenter.school.pojo;

public class LcCoursePOJO {

	private Long userPlanId;
	
	private Long commodityId;
	
	private Long orderId;
	
	private String courseTitle;
	
	private Integer courseType;
	
	private String effectiveDuration;
	//Progress Rate 进度
	private Integer progressRate;
    //Participation Rate 出勤率
    private Integer participationRate;
    //Completed Rate 完成率
    private Integer completedRate;
	//Progress Rate 进度%
	private String progressRateStr;
    //Participation Rate 出勤率%
    private String participationRateStr;
    //Completed Rate 完成率%
    private String completedRateStr;
    
    private Integer isEffective;
    //是否没有课
    private Integer isNoClass;
    //是否可以联系班主任
    private String wxCode;
    //课程展示图片,关联商品图片,没有就从文件服务器获取默认图片
    private String pic;

    private UdeskPOJO udesk;

    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
	public Long getUserPlanId() {
		return userPlanId;
	}
	public void setUserPlanId(Long userPlanId) {
		this.userPlanId = userPlanId;
	}
	public Long getCommodityId() {
		return commodityId;
	}
	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public Integer getCourseType() {
		return courseType;
	}
	public void setCourseType(Integer courseType) {
		this.courseType = courseType;
	}
	public String getEffectiveDuration() {
		return effectiveDuration;
	}
	public void setEffectiveDuration(String effectiveDuration) {
		this.effectiveDuration = effectiveDuration;
	}
	public Integer getProgressRate() {
		return progressRate;
	}
	public void setProgressRate(Integer progressRate) {
		this.progressRate = progressRate;
	}
	public Integer getParticipationRate() {
		return participationRate;
	}
	public void setParticipationRate(Integer participationRate) {
		this.participationRate = participationRate;
	}
	public Integer getCompletedRate() {
		return completedRate;
	}
	public void setCompletedRate(Integer completedRate) {
		this.completedRate = completedRate;
	}
	public Integer getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(Integer isEffective) {
		this.isEffective = isEffective;
	}
	public String getProgressRateStr() {
		return progressRateStr;
	}
	public void setProgressRateStr(String progressRateStr) {
		this.progressRateStr = progressRateStr;
	}
	public String getParticipationRateStr() {
		return participationRateStr;
	}
	public void setParticipationRateStr(String participationRateStr) {
		this.participationRateStr = participationRateStr;
	}
	public String getCompletedRateStr() {
		return completedRateStr;
	}
	public void setCompletedRateStr(String completedRateStr) {
		this.completedRateStr = completedRateStr;
	}
	public Integer getIsNoClass() {
		return isNoClass;
	}
	public void setIsNoClass(Integer isNoClass) {
		this.isNoClass = isNoClass;
	}
	public String getWxCode() {
		return wxCode;
	}
	public void setWxCode(String wxCode) {
		this.wxCode = wxCode;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public UdeskPOJO getUdesk() {
		return udesk;
	}
	public void setUdesk(UdeskPOJO udesk) {
		this.udesk = udesk;
	}
	
}
