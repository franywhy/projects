package com.hq.adaptive.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 章
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 14:47:57
 */
public class AdlChapterEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增id
	private Long chapterId;
	//章节名称
	private String chapterName;
	//章节编码
	private String chapterNo;
	//课程PK
	private Long courseId;
	//更新时间戳
	private Date updateTime;
	//创建时间戳
	private Date createTime;
	//更新时间戳
	private Date syncTime;
	//排序
	private int orderNum;
	
	//删除标记 0.正常; 1.删除
	private Integer dr;

	@Override
	public String toString() {
		return "AdlChapterEntity{" +
				"chapterId=" + chapterId +
				", chapterName='" + chapterName + '\'' +
				", chapterNo='" + chapterNo + '\'' +
				", courseId=" + courseId +
				", updateTime=" + updateTime +
				", createTime=" + createTime +
				", syncTime=" + syncTime +
				", orderNum=" + orderNum +
				", dr=" + dr +
				'}';
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 设置：自增id
	 */
	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}
	/**
	 * 获取：自增id
	 */
	public Long getChapterId() {
		return chapterId;
	}
	/**
	 * 设置：章节名称
	 */
	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}
	/**
	 * 获取：章节名称
	 */
	public String getChapterName() {
		return chapterName;
	}
	/**
	 * 设置：章节编码
	 */
	public void setChapterNo(String chapterNo) {
		this.chapterNo = chapterNo;
	}
	/**
	 * 获取：章节编码
	 */
	public String getChapterNo() {
		return chapterNo;
	}
	/**
	 * 设置：课程PK
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程PK
	 */
	public Long getCourseId() {
		return courseId;
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
