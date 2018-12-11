package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 系统日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-29 09:57:44
 */
public class SysLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//用户名
	private String username;
	//用户操作
	private String operation;
	//请求方法
	private String method;
	//请求参数
	private String params;
	//执行时长(毫秒)
	private Long executionTime;
	//IP地址
	private String ip;
	//创建时间
	private Date createTime;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：用户操作
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	/**
	 * 获取：用户操作
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * 设置：请求方法
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取：请求方法
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * 设置：请求参数
	 */
	public void setParams(String params) {
		this.params = params;
	}
	/**
	 * 获取：请求参数
	 */
	public String getParams() {
		return params;
	}
	/**
	 * 设置：执行时长(毫秒)
	 */
	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}
	/**
	 * 获取：执行时长(毫秒)
	 */
	public Long getExecutionTime() {
		return executionTime;
	}
	/**
	 * 设置：IP地址
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * 获取：IP地址
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
