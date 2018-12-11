package io.renren.modules.job.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 产品线
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 17:46:22
 */
public class SysProductEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long productId;
	//名称
	private String productName;
	//备注
	private String remark;
	//ts
	private Date ts;
	//状态:1启用 0禁用
	private Integer status;
	//点播userId
	private String polyvuserid;
	//点播secretKey
	private String polyvsecretkey;
	//展视互动参数-展视互动账号
	private String genseeLoginname;
	//展视互动参数-展视互动密码
	private String genseePassword;
	//展视互动参数-展视互动回放记录地址
	private String genseeWebcastVodLog;
	//展视互动参数-展视互动直播记录
	private String genseeWebcastLogUrl;
	//展视互动参数-展视互动直播问答数据
	private String genseeWebcastTopicsUrl;
	//展视互动参数-展视互动域名
	private String genseeDomain;
	//产品线类型 0:自考 1:会计 2:学来学往 3:多迪
	private Integer type;
	
	

	@Override
	public String toString() {
		return "SysProductEntity [productId=" + productId + ", productName=" + productName + ", remark=" + remark + ", ts=" + ts + ", status="
				+ status + ", polyvuserid=" + polyvuserid + ", polyvsecretkey=" + polyvsecretkey + ", genseeLoginname=" + genseeLoginname
				+ ", genseePassword=" + genseePassword + ", genseeWebcastVodLog=" + genseeWebcastVodLog + ", genseeWebcastLogUrl="
				+ genseeWebcastLogUrl + ", genseeDomain=" + genseeDomain + "]";
	}
	public String getGenseeLoginname() {
		return genseeLoginname;
	}
	public void setGenseeLoginname(String genseeLoginname) {
		this.genseeLoginname = genseeLoginname;
	}
	public String getGenseePassword() {
		return genseePassword;
	}
	public void setGenseePassword(String genseePassword) {
		this.genseePassword = genseePassword;
	}
	public String getGenseeWebcastVodLog() {
		return genseeWebcastVodLog;
	}
	public void setGenseeWebcastVodLog(String genseeWebcastVodLog) {
		this.genseeWebcastVodLog = genseeWebcastVodLog;
	}
	public String getGenseeWebcastLogUrl() {
		return genseeWebcastLogUrl;
	}
	public void setGenseeWebcastLogUrl(String genseeWebcastLogUrl) {
		this.genseeWebcastLogUrl = genseeWebcastLogUrl;
	}

	public String getGenseeWebcastTopicsUrl() {
		return genseeWebcastTopicsUrl;
	}

	public void setGenseeWebcastTopicsUrl(String genseeWebcastTopicsUrl) {
		this.genseeWebcastTopicsUrl = genseeWebcastTopicsUrl;
	}

	public String getGenseeDomain() {
		return genseeDomain;
	}
	public void setGenseeDomain(String genseeDomain) {
		this.genseeDomain = genseeDomain;
	}
	/**
	 * 设置：主键
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：主键
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * 设置：名称
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * 获取：名称
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：ts
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}
	/**
	 * 获取：ts
	 */
	public Date getTs() {
		return ts;
	}
	/**
	 * 设置：状态:1启用 0禁用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态:1启用 0禁用
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：点播userId
	 */
	public void setPolyvuserid(String polyvuserid) {
		this.polyvuserid = polyvuserid;
	}
	/**
	 * 获取：点播userId
	 */
	public String getPolyvuserid() {
		return polyvuserid;
	}
	/**
	 * 设置：点播secretKey
	 */
	public void setPolyvsecretkey(String polyvsecretkey) {
		this.polyvsecretkey = polyvsecretkey;
	}
	/**
	 * 获取：点播secretKey
	 */
	public String getPolyvsecretkey() {
		return polyvsecretkey;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
