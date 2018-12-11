package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 双师排课,队列接收nc排课错误消息记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-21 15:04:35
 */
public class NcCourseClassplanLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//
	private Date createTime;
	//接收的nc的队列内容
	private String ncJson;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：接收的nc的队列内容
	 */
	public void setNcJson(String ncJson) {
		this.ncJson = ncJson;
	}
	/**
	 * 获取：接收的nc的队列内容
	 */
	public String getNcJson() {
		return ncJson;
	}
}
