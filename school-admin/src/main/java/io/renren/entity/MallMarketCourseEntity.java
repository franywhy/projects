package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-19 11:58:28
 */
public class MallMarketCourseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//父id
	private Long parentId;
	//级数1：第一级 2：第二级
	private Integer level;
	//类型、标题
	private String name;
	//图片
	private String pic;
	//pc端连接
	private String pcUrl;
	//移动端链接
	private String appUrl;
	//软删除0：正常 1：删除
	private Integer dr;
	//状态0：禁用 1：正常
	private Integer status;
	//排序
	private Integer orderNum;
	//创建时间
	private Date createTime;
	//创建人
	private Long createPerson;
	//修改时间
	private Date modifyTime;
	//修改人
	private Long modifyPerson;
	//产品线
	private Integer productId;
	//是否可分享0：可分享 1：不可
	private Integer isShare;
	//上课方式
	private String classWay;
	//试用对象
	private String suitableObject;

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
	 * 设置：父id
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父id
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：级数1：第一级 2：第二级
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	/**
	 * 获取：级数1：第一级 2：第二级
	 */
	public Integer getLevel() {
		return level;
	}
	/**
	 * 设置：类型、标题
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：类型、标题
	 */
	public String getName() {
		return name;
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
	 * 设置：pc端连接
	 */
	public void setPcUrl(String pcUrl) {
		this.pcUrl = pcUrl;
	}
	/**
	 * 获取：pc端连接
	 */
	public String getPcUrl() {
		return pcUrl;
	}
	/**
	 * 设置：移动端链接
	 */
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	/**
	 * 获取：移动端链接
	 */
	public String getAppUrl() {
		return appUrl;
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
	/**
	 * 设置：状态0：禁用 1：正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态0：禁用 1：正常
	 */
	public Integer getStatus() {
		return status;
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
	 * 设置：产品线
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * 获取：产品线
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * 设置：是否可分享0：可分享 1：不可
	 */
	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}
	/**
	 * 获取：是否可分享0：可分享 1：不可
	 */
	public Integer getIsShare() {
		return isShare;
	}
	public String getClassWay() {
		return classWay;
	}
	public void setClassWay(String classWay) {
		this.classWay = classWay;
	}
	public String getSuitableObject() {
		return suitableObject;
	}
	public void setSuitableObject(String suitableObject) {
		this.suitableObject = suitableObject;
	}
	
}
