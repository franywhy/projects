package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 冷启动数据
 * 
 * @author linyuebin
 * @email trust_100@163.com
 * @date 2017-12-30 11:30:54
 */
public class ColdStartingEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//标题
	private String title;
	//图片
	private String pic;
	//地址
	private String url;
	//倒数秒数
	private String countdown;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//版本号
	private Integer version;
	//是否显示,1显示,0不显示
	private Integer isShow;
	//状态码,0启用,1不启用
	private Integer dr;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
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
	 * 设置：地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：地址
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：倒数秒数
	 */
	public void setCountdown(String countdown) {
		this.countdown = countdown;
	}
	/**
	 * 获取：倒数秒数
	 */
	public String getCountdown() {
		return countdown;
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
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：版本号
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	/**
	 * 获取：版本号
	 */
	public Integer getVersion() {
		return version;
	}
	/**
	 * 设置：是否显示,1显示,0不显示
	 */
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	/**
	 * 获取：是否显示,1显示,0不显示
	 */
	public Integer getIsShow() {
		return isShow;
	}
	/**
	 * 设置：状态码,0启用,1不启用
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：状态码,0启用,1不启用
	 */
	public Integer getDr() {
		return dr;
	}
}
