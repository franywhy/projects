package com.hq.learningapi.entity;

/**
 * Created by Glenn on 2017/4/26 0026.
 */
public class AppBannerEntity{

	//名称
	private String name;
	//图片
	private String pic;

	/**
	 * 设置：名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称
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
}
