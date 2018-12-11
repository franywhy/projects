package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 消息队列配置商品表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-12-22 10:55:29
 */
public class ConfigGoodsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//商品id
	private Long goodId;
	//商品nc_id
	private String goodNcId;
	//更改时间
	private Date ts;
	//软删除0：不删除 1：删除
	private Integer dr;

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
	 * 设置：商品id
	 */
	public void setGoodId(Long goodId) {
		this.goodId = goodId;
	}
	/**
	 * 获取：商品id
	 */
	public Long getGoodId() {
		return goodId;
	}
	/**
	 * 设置：商品nc_id
	 */
	public void setGoodNcId(String goodNcId) {
		this.goodNcId = goodNcId;
	}
	/**
	 * 获取：商品nc_id
	 */
	public String getGoodNcId() {
		return goodNcId;
	}
	/**
	 * 设置：更改时间
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}
	/**
	 * 获取：更改时间
	 */
	public Date getTs() {
		return ts;
	}
	/**
	 * 设置：软删除0：不删除 1：删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：软删除0：不删除 1：删除
	 */
	public Integer getDr() {
		return dr;
	}
}
