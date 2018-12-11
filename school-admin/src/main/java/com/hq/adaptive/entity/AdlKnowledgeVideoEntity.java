package com.hq.adaptive.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 知识点视频档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public class AdlKnowledgeVideoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long videoId;
	//知识点PK
	private Long knowledgeId;
	//录播课id
	private String polyvVid;
	//保利威视视频名称
	private String polyvName;
	//播放时长:毫秒
	private Long polyvDurationS;
	//时长默认'00:00:00' 格式hh-mm-ss
	private String polyvDuration;
	//首截图
	private String screenShot;
	//更新时间戳
	private Date updateTime;
	//创建时间戳
	private Date createTime;

	/**
	 * 设置：主键
	 */
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	/**
	 * 获取：主键
	 */
	public Long getVideoId() {
		return videoId;
	}
	/**
	 * 设置：知识点PK
	 */
	public void setKnowledgeId(Long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}
	/**
	 * 获取：知识点PK
	 */
	public Long getKnowledgeId() {
		return knowledgeId;
	}
	/**
	 * 设置：录播课id
	 */
	public void setPolyvVid(String polyvVid) {
		this.polyvVid = polyvVid;
	}
	/**
	 * 获取：录播课id
	 */
	public String getPolyvVid() {
		return polyvVid;
	}
	/**
	 * 设置：保利威视视频名称
	 */
	public void setPolyvName(String polyvName) {
		this.polyvName = polyvName;
	}
	/**
	 * 获取：保利威视视频名称
	 */
	public String getPolyvName() {
		return polyvName;
	}
	/**
	 * 设置：播放时长:毫秒
	 */
	public void setPolyvDurationS(Long polyvDurationS) {
		this.polyvDurationS = polyvDurationS;
	}
	/**
	 * 获取：播放时长:毫秒
	 */
	public Long getPolyvDurationS() {
		return polyvDurationS;
	}
	/**
	 * 设置：时长默认'00:00:00' 格式hh-mm-ss
	 */
	public void setPolyvDuration(String polyvDuration) {
		this.polyvDuration = polyvDuration;
	}
	/**
	 * 获取：时长默认'00:00:00' 格式hh-mm-ss
	 */
	public String getPolyvDuration() {
		return polyvDuration;
	}
	/**
	 * 设置：首截图
	 */
	public void setScreenShot(String screenShot) {
		this.screenShot = screenShot;
	}
	/**
	 * 获取：首截图
	 */
	public String getScreenShot() {
		return screenShot;
	}
	/**
	 * 设置：更新时间戳
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间戳
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：创建时间戳
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间戳
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
