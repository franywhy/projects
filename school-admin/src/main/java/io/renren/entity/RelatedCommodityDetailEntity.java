package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 关联商品档案子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-29 10:35:56
 */
public class RelatedCommodityDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//关联商品子表id
	private Long id;
	//对应related_commodity主键
	private Long relatedCommodityId;
	//商品名
	private String commodity;
	//别名
	private String alias;
	//排序
	private Integer orderNum;
	
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 设置：关联商品子表id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：关联商品子表id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：对应related_commodity主键
	 */
	public void setRelatedCommodityId(Long long1) {
		this.relatedCommodityId = long1;
	}
	/**
	 * 获取：对应related_commodity主键
	 */
	public Long getRelatedCommodityId() {
		return relatedCommodityId;
	}
	/**
	 * 设置：商品名
	 */
	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}
	/**
	 * 获取：商品名
	 */
	public String getCommodity() {
		return commodity;
	}
	/**
	 * 设置：别名
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	/**
	 * 获取：别名
	 */
	public String getAlias() {
		return alias;
	}
}
