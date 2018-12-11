package com.hq.learningcenter.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 产品线
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-09-11 15:12:45
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
	//保利威视点播userId
	private String polyvuserid;
	//保利威视点播secretKey
	private String polyvsecretkey;
	//展视互动账号
	private String genseeloginname;
	//展视互动密码
	private String genseepassword;
	//展视互动回放记录地址
	private String genseewebcastvodlog;
	//展视互动直播记录
	private String genseewebcastlogurl;
	//展视互动域名
	private String genseedomain;
	//产品线类型 0:自考 1:会计 2:学来学往 3:多迪
	private Integer type;

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
	 * 设置：保利威视点播userId
	 */
	public void setPolyvuserid(String polyvuserid) {
		this.polyvuserid = polyvuserid;
	}
	/**
	 * 获取：保利威视点播userId
	 */
	public String getPolyvuserid() {
		return polyvuserid;
	}
	/**
	 * 设置：保利威视点播secretKey
	 */
	public void setPolyvsecretkey(String polyvsecretkey) {
		this.polyvsecretkey = polyvsecretkey;
	}
	/**
	 * 获取：保利威视点播secretKey
	 */
	public String getPolyvsecretkey() {
		return polyvsecretkey;
	}
	/**
	 * 设置：展视互动账号
	 */
	public void setGenseeloginname(String genseeloginname) {
		this.genseeloginname = genseeloginname;
	}
	/**
	 * 获取：展视互动账号
	 */
	public String getGenseeloginname() {
		return genseeloginname;
	}
	/**
	 * 设置：展视互动密码
	 */
	public void setGenseepassword(String genseepassword) {
		this.genseepassword = genseepassword;
	}
	/**
	 * 获取：展视互动密码
	 */
	public String getGenseepassword() {
		return genseepassword;
	}
	/**
	 * 设置：展视互动回放记录地址
	 */
	public void setGenseewebcastvodlog(String genseewebcastvodlog) {
		this.genseewebcastvodlog = genseewebcastvodlog;
	}
	/**
	 * 获取：展视互动回放记录地址
	 */
	public String getGenseewebcastvodlog() {
		return genseewebcastvodlog;
	}
	/**
	 * 设置：展视互动直播记录
	 */
	public void setGenseewebcastlogurl(String genseewebcastlogurl) {
		this.genseewebcastlogurl = genseewebcastlogurl;
	}
	/**
	 * 获取：展视互动直播记录
	 */
	public String getGenseewebcastlogurl() {
		return genseewebcastlogurl;
	}
	/**
	 * 设置：展视互动域名
	 */
	public void setGenseedomain(String genseedomain) {
		this.genseedomain = genseedomain;
	}
	/**
	 * 获取：展视互动域名
	 */
	public String getGenseedomain() {
		return genseedomain;
	}
	/**
	 * 获取：产品线类型 0:自考 1:会计 2:学来学往 3:多迪
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：产品线类型 0:自考 1:会计 2:学来学往 3:多迪
	 */
	public void setType(Integer type) {
		this.type = type;
	}

    @Override
    public String toString() {
        return "SysProductEntity{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", remark='" + remark + '\'' +
                ", ts=" + ts +
                ", status=" + status +
                ", polyvuserid='" + polyvuserid + '\'' +
                ", polyvsecretkey='" + polyvsecretkey + '\'' +
                ", genseeloginname='" + genseeloginname + '\'' +
                ", genseepassword='" + genseepassword + '\'' +
                ", genseewebcastvodlog='" + genseewebcastvodlog + '\'' +
                ", genseewebcastlogurl='" + genseewebcastlogurl + '\'' +
                ", genseedomain='" + genseedomain + '\'' +
                ", type=" + type +
                '}';
    }
}
