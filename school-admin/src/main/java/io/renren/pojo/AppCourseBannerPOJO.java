package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;



/**
 * app课程banner档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-18 14:23:20
 */
public class AppCourseBannerPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//标题
	private String title;
	//图片
	private String pic;
	//链接
	private String url;
	//创建人
	private Long createPerson;
	//创建时间
	private Date createTime;
	//修改人
	private Long modifyPerson;
	//修改时间
	private Date modifyTime;
	//排序
	private Integer orderNum;
	//软删除0：正常 1：删除
	private Integer dr;
	//产品线
	private Integer productId;
	
	//创建人姓名
	private String createName;
	//修改人姓名
	private String modifyName;
	//产品线名称
	private String productName;

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
	 * 设置：标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：标题
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：图片
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	/**
	 * 获取：图片
	 */
	public String getPic() {
		return pic;
	}
	/**
	 * 设置：链接
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：链接
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：创建人
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
	 * 设置：修改人
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：修改人
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：软删除0：正常 1：删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：软删除0：正常 1：删除
	 */
	public Integer getDr() {
		return dr;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getModifyName() {
		return modifyName;
	}
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@Override
	public String toString() {
		return "AppCourseBannerPOJO [id=" + id + ", title=" + title + ", pic=" + pic + ", url=" + url
				+ ", createPerson=" + createPerson + ", createTime=" + createTime + ", modifyPerson=" + modifyPerson
				+ ", modifyTime=" + modifyTime + ", orderNum=" + orderNum + ", dr=" + dr + ", productId=" + productId
				+ ", createName=" + createName + ", modifyName=" + modifyName + ", productName=" + productName + "]";
	}
	
	
}
