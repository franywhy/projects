package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-31 15:08:46
 */
public class GivingCoursesEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Long id;
	//创建者
	private Long createPerson;
	//创建时间
	private Date createTime;
	//修改者
	private Long modifyPerson;
	//更新时间
	private Date modifyTime;
	//赠送课程商品ID
	private Long mallGoodsId;
	//商品名称
	private String mallGoodsName;
	//NC商品ID
	private String ncCommodityId;
	//NC商品名称
	private String ncCommodityName;
	//产品线
	private Long productId;
	//0为未删除，1为软删除
	private Integer dr;
	//备注
	private String remark;
	//赠送的类型 0 为普通赠送  1 为组合班型 
	private Integer givingType;
	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：创建者
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：创建者
	 */
	public Long getCreatePerson() {
		return createPerson;
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
	 * 设置：修改者
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：修改者
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：更新时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：赠送课程商品ID
	 */
	public void setMallGoodsId(Long mallGoodsId) {
		this.mallGoodsId = mallGoodsId;
	}
	/**
	 * 获取：赠送课程商品ID
	 */
	public Long getMallGoodsId() {
		return mallGoodsId;
	}
	/**
	 * 设置：商品名称
	 */
	public void setMallGoodsName(String mallGoodsName) {
		this.mallGoodsName = mallGoodsName;
	}
	/**
	 * 获取：商品名称
	 */
	public String getMallGoodsName() {
		return mallGoodsName;
	}
	/**
	 * 设置：NC商品ID
	 */
	public void setNcCommodityId(String ncCommodityId) {
		this.ncCommodityId = ncCommodityId;
	}
	/**
	 * 获取：NC商品ID
	 */
	public String getNcCommodityId() {
		return ncCommodityId;
	}
	/**
	 * 设置：NC商品名称
	 */
	public void setNcCommodityName(String ncCommodityName) {
		this.ncCommodityName = ncCommodityName;
	}
	/**
	 * 获取：NC商品名称
	 */
	public String getNcCommodityName() {
		return ncCommodityName;
	}
	/**
	 * 获取：产品线
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * 设置：产品线
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 设置：0为未删除，1为软删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：0为未删除，1为软删除
	 */
	public Integer getDr() {
		return dr;
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
	 * 赠送的类型 0 为普通赠送  1 为组合班型 2 为多对一班型
	 *@return
	 * @author lintf
	 * 2018年9月25日
	 */
	public Integer getGivingType() {
		return givingType;
	}
	/**
	 * 赠送的类型 0 为普通赠送  1 为组合班型 2 为多对一班型
	 *@param givingType
	 * @author lintf
	 * 2018年9月25日
	 */
	public void setGivingType(Integer givingType) {
		this.givingType = givingType;
	}
	
	
}
