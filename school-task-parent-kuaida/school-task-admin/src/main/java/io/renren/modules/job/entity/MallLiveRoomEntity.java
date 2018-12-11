package io.renren.modules.job.entity;

import java.io.Serializable;
import java.util.Date;

import io.renren.modules.job.utils.Constant;



/**
 * 直播间档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-21 17:00:10
 */
public class MallLiveRoomEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//直播间id
	private Long liveRoomId;
	//机构id
	private String schoolId;
	//是否删除   0.未删除  1.删除   用于软删除
	private Integer dr;
	//直播间名称
	private String liveRoomName;
	//直播间描述
	private String liveRoomRemake;
	//直播间频道id
	private Long liveRoomChannelId;
	//直播间频道id
	private String liveRoomChannelSecretkey;
	//直播间频道密码
	private String liveRoomChannelPassword;
	//是否使用  1.正常  0.停用
	private Integer status = (int) Constant.Status.RESUME.getValue();
	//创建用户
	private Long creator;
	//创建时间
	private Date creationTime;
	//修改用户
	private Long modifier;
	//修改时间
	private Date modifiedTime;
	//创建用户名称
	private String creationName;
	//修改用户名称
	private String modifiedName;
	//展示互动直播id
    private String genseeLiveId;
    //展示互动直播房间号
    //mongoDB中m_id
     //展示互动直播名
    private String genseeLiveNum;
  
	//mongoDB中m_id
    private String mId;
    //产品pk
    private Long productId;
    //产品名称
    private String productName;
	
	public String getGenseeLiveNum() {
		return genseeLiveNum;
	}
	public void setGenseeLiveNum(String genseeLiveNum) {
		this.genseeLiveNum = genseeLiveNum;
	}
	public Long getLiveRoomId() {
		return liveRoomId;
	}
	public void setLiveRoomId(Long liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getLiveRoomName() {
		return liveRoomName;
	}
	public void setLiveRoomName(String liveRoomName) {
		this.liveRoomName = liveRoomName;
	}
	public String getLiveRoomRemake() {
		return liveRoomRemake;
	}
	public void setLiveRoomRemake(String liveRoomRemake) {
		this.liveRoomRemake = liveRoomRemake;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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
	public Long getLiveRoomChannelId() {
		return liveRoomChannelId;
	}
	public void setLiveRoomChannelId(Long liveRoomChannelId) {
		this.liveRoomChannelId = liveRoomChannelId;
	}
	public String getLiveRoomChannelPassword() {
		return liveRoomChannelPassword;
	}
	public void setLiveRoomChannelPassword(String liveRoomChannelPassword) {
		this.liveRoomChannelPassword = liveRoomChannelPassword;
	}
	public String getLiveRoomChannelSecretkey() {
		return liveRoomChannelSecretkey;
	}
	public void setLiveRoomChannelSecretkey(String liveRoomChannelSecretkey) {
		this.liveRoomChannelSecretkey = liveRoomChannelSecretkey;
	}
	
	public String getGenseeLiveId() {
		return genseeLiveId;
	}
	public void setGenseeLiveId(String genseeLiveId) {
		this.genseeLiveId = genseeLiveId;
	}
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
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
	@Override
	public String toString() {
		return "MallLiveRoomEntity [liveRoomId=" + liveRoomId + ", schoolId=" + schoolId + ", dr=" + dr
				+ ", liveRoomName=" + liveRoomName + ", liveRoomRemake=" + liveRoomRemake + ", liveRoomChannelId="
				+ liveRoomChannelId + ", liveRoomChannelSecretkey=" + liveRoomChannelSecretkey
				+ ", liveRoomChannelPassword=" + liveRoomChannelPassword + ", status=" + status + ", creator=" + creator
				+ ", creationTime=" + creationTime + ", modifier=" + modifier + ", modifiedTime=" + modifiedTime
				+ ", creationName=" + creationName + ", modifiedName=" + modifiedName + ", genseeLiveId=" + genseeLiveId
				+ ", genseeLiveNum=" + genseeLiveNum + ", mId=" + mId + ", productId=" + productId + ", productName="
				+ productName + "]";
	}
	
	
	
}
