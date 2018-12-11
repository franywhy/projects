package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

public class CourseOlivePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long oliveId;
	//标题
	private String oliveTitle;

	private String oliveIntroduction;

	private String cardTitle;

	private String cardSubtitle;

	//讲师
	private Long teacherId;
	//开始时间
	private Date oliveStartTime;
	//结束时间
	private Date oliveEndTime;
	//封面
	private String olivePic;
	//上架状态:0.下架,1.上架
	private Integer appStatus;
	//直播间PK
	private Long liveRoomId;
	//回放地址
	private String replayUrl;
	//创建用户
	private Long createPerson;
	//创建时间
	private Date creationTime;
	//最近修改用户
	private Long modifyPerson;
	//最近修改日期
	private Date modifiedTime;
	//平台PK
	private String schoolId;
	//产品id
	private Long productId;
	
	//讲师
	private String teacherName;

	private String teacherAvatar;

	private String teacherIntroduction;

	private String suitable;

	private String content;

	private Integer pushStatus;

	private Date pushTime;

	private String pushMsgId;

	private String pushFindMsgId;

	//直播间
	private String liveRoomName;
	//创建用户
	private String creationName;
	//修改用户
	private String modifiedName;
	//产品名称
	private String productName;
	//直播类型ID
	private Integer authorityId;
	//直播类型名称
	private String authorityName;
	
	public Long getOliveId() {
		return oliveId;
	}
	public void setOliveId(Long oliveId) {
		this.oliveId = oliveId;
	}
	public String getOliveTitle() {
		return oliveTitle;
	}
	public void setOliveTitle(String oliveTitle) {
		this.oliveTitle = oliveTitle;
	}
	public Long getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}
	public Date getOliveStartTime() {
		return oliveStartTime;
	}
	public void setOliveStartTime(Date oliveStartTime) {
		this.oliveStartTime = oliveStartTime;
	}
	public Date getOliveEndTime() {
		return oliveEndTime;
	}
	public void setOliveEndTime(Date oliveEndTime) {
		this.oliveEndTime = oliveEndTime;
	}
	public String getOlivePic() {
		return olivePic;
	}
	public void setOlivePic(String olivePic) {
		this.olivePic = olivePic;
	}
	public Integer getAppStatus() {
		return appStatus;
	}
	public void setAppStatus(Integer appStatus) {
		this.appStatus = appStatus;
	}
	public Long getLiveRoomId() {
		return liveRoomId;
	}
	public void setLiveRoomId(Long liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	public String getReplayUrl() {
		return replayUrl;
	}
	public void setReplayUrl(String replayUrl) {
		this.replayUrl = replayUrl;
	}
	public Long getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Long getModifyPerson() {
		return modifyPerson;
	}
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getLiveRoomName() {
		return liveRoomName;
	}
	public void setLiveRoomName(String liveRoomName) {
		this.liveRoomName = liveRoomName;
	}
	public String getCreationName() {
		return creationName;
	}
	public void setCreationName(String creationName) {
		this.creationName = creationName;
	}
	public String getModifiedName() {
		return modifiedName;
	}
	public void setModifiedName(String modifiedName) {
		this.modifiedName = modifiedName;
	}
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTeacherAvatar() {
		return teacherAvatar;
	}

	public void setTeacherAvatar(String teacherAvatar) {
		this.teacherAvatar = teacherAvatar;
	}

	public String getTeacherIntroduction() {
		return teacherIntroduction;
	}

	public void setTeacherIntroduction(String teacherIntroduction) {
		this.teacherIntroduction = teacherIntroduction;
	}

	public String getSuitable() {
		return suitable;
	}

	public void setSuitable(String suitable) {
		this.suitable = suitable;
	}

	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public String getPushMsgId() {
		return pushMsgId;
	}

	public void setPushMsgId(String pushMsgId) {
		this.pushMsgId = pushMsgId;
	}

	public String getPushFindMsgId() {
		return pushFindMsgId;
	}

	public void setPushFindMsgId(String pushFindMsgId) {
		this.pushFindMsgId = pushFindMsgId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOliveIntroduction() {
		return oliveIntroduction;
	}

	public void setOliveIntroduction(String oliveIntroduction) {
		this.oliveIntroduction = oliveIntroduction;
	}

	public String getCardTitle() {
		return cardTitle;
	}

	public void setCardTitle(String cardTitle) {
		this.cardTitle = cardTitle;
	}

	public String getCardSubtitle() {
		return cardSubtitle;
	}

	public void setCardSubtitle(String cardSubtitle) {
		this.cardSubtitle = cardSubtitle;
	}

	public Integer getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Integer authorityId) {
		this.authorityId = authorityId;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}
}
