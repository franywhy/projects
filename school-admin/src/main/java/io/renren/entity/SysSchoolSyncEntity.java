package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 部门校区同步异常表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-30 16:47:45
 */
public class SysSchoolSyncEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//校区或部门编号
	private String code;
	//校区或部门nc_id
	private String ncId;
	//错误类型
	private Integer errType;
	//同步信息
	private String ncJson;
	//校区或部门名字
	private String name;

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
	 * 设置：校区或部门编号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：校区或部门编号
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：校区或部门nc_id
	 */
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	/**
	 * 获取：校区或部门nc_id
	 */
	public String getNcId() {
		return ncId;
	}
	/**
	 * 设置：错误类型
	 */
	public void setErrType(Integer errType) {
		this.errType = errType;
	}
	/**
	 * 获取：错误类型
	 */
	public Integer getErrType() {
		return errType;
	}
	/**
	 * 设置：同步信息
	 */
	public void setNcJson(String ncJson) {
		this.ncJson = ncJson;
	}
	/**
	 * 获取：同步信息
	 */
	public String getNcJson() {
		return ncJson;
	}
	/**
	 * 设置：校区或部门名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：校区或部门名字
	 */
	public String getName() {
		return name;
	}
}
