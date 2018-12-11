package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-20 20:59:42
 */
public class MallOrderSyncXEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//报名表单号
	private String ncSignCode;
	//用户手机
	private String userMobile;
	//用户昵称
	private String userName;
	//订单编号
	private String orderNo;
	//商品ID
	private Integer zkCommodityId;
	//错误类型 1:商品不存在 2：商品对应省份不存在3：没有该班级存在4：系统异常
	private Integer errType;
	//0:已处理 1:未处理 2:过期不处理
	private Integer state;
	//同步时间
	private Date synTime;
	//NC报名表生成时间
	private Date ncSignTime;
	//创建时间
	private Date createTime;
	//json
	private String json;

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
	 * 设置：报名表单号
	 */
	public void setNcSignCode(String ncSignCode) {
		this.ncSignCode = ncSignCode;
	}
	/**
	 * 获取：报名表单号
	 */
	public String getNcSignCode() {
		return ncSignCode;
	}
	/**
	 * 设置：用户手机
	 */
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	/**
	 * 获取：用户手机
	 */
	public String getUserMobile() {
		return userMobile;
	}
	/**
	 * 设置：用户昵称
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：用户昵称
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：订单编号
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 获取：订单编号
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 设置：商品ID
	 */
	public void setZkCommodityId(Integer zkCommodityId) {
		this.zkCommodityId = zkCommodityId;
	}
	/**
	 * 获取：商品ID
	 */
	public Integer getZkCommodityId() {
		return zkCommodityId;
	}
	/**
	 * 设置：错误类型 1:商品不存在 2：商品对应省份不存在3：没有该班级存在4：系统异常
	 */
	public void setErrType(Integer errType) {
		this.errType = errType;
	}
	/**
	 * 获取：错误类型 1:商品不存在 2：商品对应省份不存在3：没有该班级存在4：系统异常
	 */
	public Integer getErrType() {
		return errType;
	}
	/**
	 * 设置：0:已处理 1:未处理 2:过期不处理
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：0:已处理 1:未处理 2:过期不处理
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * 设置：同步时间
	 */
	public void setSynTime(Date synTime) {
		this.synTime = synTime;
	}
	/**
	 * 获取：同步时间
	 */
	public Date getSynTime() {
		return synTime;
	}
	/**
	 * 设置：NC报名表生成时间
	 */
	public void setNcSignTime(Date ncSignTime) {
		this.ncSignTime = ncSignTime;
	}
	/**
	 * 获取：NC报名表生成时间
	 */
	public Date getNcSignTime() {
		return ncSignTime;
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
	 * 设置：json
	 */
	public void setJson(String json) {
		this.json = json;
	}
	/**
	 * 获取：json
	 */
	public String getJson() {
		return json;
	}
}
