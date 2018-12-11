package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * NC订单同步异常表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-24 16:28:16
 */
public class MallOrderSyncEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//创建时间
	private Date createTime;
	//NC主键
	private String ncId;
	//NC创建时间
	private Date ncCreateTime;
	//平台ID
	private String schoolId;
	//NC业务平台
	private String ncClassTypeId;
	//同步信息
	private String ncJson;
	//dr
	private Integer dr;
	//错误类型
	private Integer errType;
	//nc同步手机号码
	private String mobile;
	//nc同步code
	private String ncCode;
	//nc同步时间
	private Date synTime;
	//0:已处理 1:未处理 2:过期不处理
	private int state;

	public Date getSynTime() {
		return synTime;
	}
	public void setSynTime(Date synTime) {
		this.synTime = synTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNcCode() {
		return ncCode;
	}
	public void setNcCode(String ncCode) {
		this.ncCode = ncCode;
	}
	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
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
	/**
	 * 设置：NC主键
	 */
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	/**
	 * 获取：NC主键
	 */
	public String getNcId() {
		return ncId;
	}
	/**
	 * 设置：NC创建时间
	 */
	public void setNcCreateTime(Date ncCreateTime) {
		this.ncCreateTime = ncCreateTime;
	}
	/**
	 * 获取：NC创建时间
	 */
	public Date getNcCreateTime() {
		return ncCreateTime;
	}
	/**
	 * 设置：平台ID
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台ID
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：NC业务平台
	 */
	public void setNcClassTypeId(String ncClassTypeId) {
		this.ncClassTypeId = ncClassTypeId;
	}
	/**
	 * 获取：NC业务平台
	 */
	public String getNcClassTypeId() {
		return ncClassTypeId;
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
	 * 设置：dr
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：dr
	 */
	public Integer getDr() {
		return dr;
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
	@Override
	public String toString() {
		return "MallOrderSyncEntity [id=" + id + ", createTime=" + createTime + ", ncId=" + ncId + ", ncCreateTime=" + ncCreateTime + ", schoolId="
				+ schoolId + ", ncClassTypeId=" + ncClassTypeId + ", ncJson=" + ncJson + ", dr=" + dr + ", errType=" + errType + ", mobile=" + mobile
				+ ", ncCode=" + ncCode + ", synTime=" + synTime + ", state=" + state + "]";
	}
	
}
