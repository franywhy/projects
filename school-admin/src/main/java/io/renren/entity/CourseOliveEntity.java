package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 公开课
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 15:01:23
 */
public class CourseOliveEntity implements Serializable {
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
	
	//产品线PK
 	private Long productId;

	private String teacherName;

	private String teacherAvatar;

	private String teacherIntroduction;

	private String content;

	private String suitable;

	private Integer pushStatus;

	private Date pushTime;

	private String pushMsgId;

	private String pushFindMsgId;

	private Integer dr;

	private Integer authorityId;

	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 设置：主键
	 */
	public void setOliveId(Long oliveId) {
		this.oliveId = oliveId;
	}
	/**
	 * 获取：主键
	 */
	public Long getOliveId() {
		return oliveId;
	}
	/**
	 * 设置：标题
	 */
	public void setOliveTitle(String oliveTitle) {
		this.oliveTitle = oliveTitle;
	}
	/**
	 * 获取：标题
	 */
	public String getOliveTitle() {
		return oliveTitle;
	}
	/**
	 * 设置：讲师
	 */
	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}
	/**
	 * 获取：讲师
	 */
	public Long getTeacherId() {
		return teacherId;
	}
	/**
	 * 设置：开始时间
	 */
	public void setOliveStartTime(Date oliveStartTime) {
		this.oliveStartTime = oliveStartTime;
	}
	/**
	 * 获取：开始时间
	 */
	public Date getOliveStartTime() {
		return oliveStartTime;
	}
	/**
	 * 设置：结束时间
	 */
	public void setOliveEndTime(Date oliveEndTime) {
		this.oliveEndTime = oliveEndTime;
	}
	/**
	 * 获取：结束时间
	 */
	public Date getOliveEndTime() {
		return oliveEndTime;
	}
	/**
	 * 设置：封面
	 */
	public void setOlivePic(String olivePic) {
		this.olivePic = olivePic;
	}
	/**
	 * 获取：封面
	 */
	public String getOlivePic() {
		return olivePic;
	}
	/**
	 * 设置：上架状态:0.下架,1.上架
	 */
	public void setAppStatus(Integer appStatus) {
		this.appStatus = appStatus;
	}
	/**
	 * 获取：上架状态:0.下架,1.上架
	 */
	public Integer getAppStatus() {
		return appStatus;
	}
	/**
	 * 设置：直播间PK
	 */
	public void setLiveRoomId(Long liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	/**
	 * 获取：直播间PK
	 */
	public Long getLiveRoomId() {
		return liveRoomId;
	}
	/**
	 * 获取: 回放地址
	 */
	public String getReplayUrl() {
		return replayUrl;
	}
	/**
	 * 设置: 回放地址
	 */
	public void setReplayUrl(String replayUrl) {
		this.replayUrl = replayUrl;
	}
	/**
	 * 设置：创建用户
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：创建用户
	 */
	public Long getCreatePerson() {
		return createPerson;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	/**
	 * 设置：最近修改用户
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：最近修改用户
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：最近修改日期
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：最近修改日期
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * 设置：平台PK
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台PK
	 */
	public String getSchoolId() {
		return schoolId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
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
}
