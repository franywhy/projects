package com.hq.learningcenter.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 直播间档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-21 17:37:41
 */
public class MallLiveRoomEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//直播间id
	private Long liveRoomId;
	//机构id
	private String schoolId;
	////是否删除   0.未删除  1.删除   用于软删除
	private Integer dr;
	//直播间名称
	private String liveRoomName;
	//直播间描述
	private String liveRoomRemake;
	//直播间频道id
	private Long liveRoomChannelId;
	//直播间频道secretkey(自定义授权验证key)
	private String liveRoomChannelSecretkey;
	//直播间频道密码
	private String liveRoomChannelPassword;
	//是否使用  1.正常  0.停用
	private Integer status;
	//创建用户
	private Long creator;
	//创建时间
	private Date creationTime;
	//修改用户
	private Long modifier;
	//修改时间
	private Date modifiedTime;
	//展示互动直播id
	private String genseeLiveId;
	//展示互动直播房间号
	private String genseeLiveNum;
	//
	private String mId;
	//产品pk
	private Long productId;

    //直播间是否禁言
    private int banSpeaking;
    //直播间是否禁止问答
    private int banAsking;
    //直播间是否隐藏讨论模块
    private int hideDiscussion;
    //直播间是否隐藏问答模块
    private int hideAsking;

    //排课计划ID
    private String classplanId;

    public String getClassplanId() {
        return classplanId;
    }

    public void setClassplanId(String classplanId) {
        this.classplanId = classplanId;
    }

    public int getBanAsking() {
        return banAsking;
    }

    public void setBanAsking(int banAsking) {
        this.banAsking = banAsking;
    }

    public int getHideDiscussion() {
        return hideDiscussion;
    }

    public void setHideDiscussion(int hideDiscussion) {
        this.hideDiscussion = hideDiscussion;
    }

    public int getHideAsking() {
        return hideAsking;
    }

    public void setHideAsking(int hideAsking) {
        this.hideAsking = hideAsking;
    }

    public int getBanSpeaking() {
        return banSpeaking;
    }

    public void setBanSpeaking(int banSpeaking) {
        this.banSpeaking = banSpeaking;
    }

    /**
	 * 设置：直播间id
	 */
	public void setLiveRoomId(Long liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	/**
	 * 获取：直播间id
	 */
	public Long getLiveRoomId() {
		return liveRoomId;
	}
	/**
	 * 设置：机构id
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：机构id
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：//是否删除   0.未删除  1.删除   用于软删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：//是否删除   0.未删除  1.删除   用于软删除
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：直播间名称
	 */
	public void setLiveRoomName(String liveRoomName) {
		this.liveRoomName = liveRoomName;
	}
	/**
	 * 获取：直播间名称
	 */
	public String getLiveRoomName() {
		return liveRoomName;
	}
	/**
	 * 设置：直播间描述
	 */
	public void setLiveRoomRemake(String liveRoomRemake) {
		this.liveRoomRemake = liveRoomRemake;
	}
	/**
	 * 获取：直播间描述
	 */
	public String getLiveRoomRemake() {
		return liveRoomRemake;
	}
	/**
	 * 设置：直播间频道id
	 */
	public void setLiveRoomChannelId(Long liveRoomChannelId) {
		this.liveRoomChannelId = liveRoomChannelId;
	}
	/**
	 * 获取：直播间频道id
	 */
	public Long getLiveRoomChannelId() {
		return liveRoomChannelId;
	}
	/**
	 * 设置：直播间频道secretkey(自定义授权验证key)
	 */
	public void setLiveRoomChannelSecretkey(String liveRoomChannelSecretkey) {
		this.liveRoomChannelSecretkey = liveRoomChannelSecretkey;
	}
	/**
	 * 获取：直播间频道secretkey(自定义授权验证key)
	 */
	public String getLiveRoomChannelSecretkey() {
		return liveRoomChannelSecretkey;
	}
	/**
	 * 设置：直播间频道密码
	 */
	public void setLiveRoomChannelPassword(String liveRoomChannelPassword) {
		this.liveRoomChannelPassword = liveRoomChannelPassword;
	}
	/**
	 * 获取：直播间频道密码
	 */
	public String getLiveRoomChannelPassword() {
		return liveRoomChannelPassword;
	}
	/**
	 * 设置：是否使用  1.正常  0.停用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：是否使用  1.正常  0.停用
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：创建用户
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建用户
	 */
	public Long getCreator() {
		return creator;
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
	 * 设置：修改用户
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：修改用户
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * 设置：展示互动直播id
	 */
	public void setGenseeLiveId(String genseeLiveId) {
		this.genseeLiveId = genseeLiveId;
	}
	/**
	 * 获取：展示互动直播id
	 */
	public String getGenseeLiveId() {
		return genseeLiveId;
	}
	/**
	 * 设置：展示互动直播房间号
	 */
	public void setGenseeLiveNum(String genseeLiveNum) {
		this.genseeLiveNum = genseeLiveNum;
	}
	/**
	 * 获取：展示互动直播房间号
	 */
	public String getGenseeLiveNum() {
		return genseeLiveNum;
	}
	/**
	 * 设置：
	 */
	public void setMId(String mId) {
		this.mId = mId;
	}
	/**
	 * 获取：
	 */
	public String getMId() {
		return mId;
	}
	/**
	 * 设置：产品pk
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：产品pk
	 */
	public Long getProductId() {
		return productId;
	}

    @Override
    public String toString() {
        return "MallLiveRoomEntity{" +
                "liveRoomId=" + liveRoomId +
                ", schoolId='" + schoolId + '\'' +
                ", dr=" + dr +
                ", liveRoomName='" + liveRoomName + '\'' +
                ", liveRoomRemake='" + liveRoomRemake + '\'' +
                ", liveRoomChannelId=" + liveRoomChannelId +
                ", liveRoomChannelSecretkey='" + liveRoomChannelSecretkey + '\'' +
                ", liveRoomChannelPassword='" + liveRoomChannelPassword + '\'' +
                ", status=" + status +
                ", creator=" + creator +
                ", creationTime=" + creationTime +
                ", modifier=" + modifier +
                ", modifiedTime=" + modifiedTime +
                ", genseeLiveId='" + genseeLiveId + '\'' +
                ", genseeLiveNum='" + genseeLiveNum + '\'' +
                ", mId='" + mId + '\'' +
                ", productId=" + productId +
                ", banSpeaking=" + banSpeaking +
                ", banAsking=" + banAsking +
                ", hideDiscussion=" + hideDiscussion +
                ", hideAsking=" + hideAsking +
                ", classplanId='" + classplanId + '\'' +
                '}';
    }
}
