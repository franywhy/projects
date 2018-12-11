package com.hq.learningapi.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 冷启动数据
 * 
 * @author linyuebin
 * @email trust_100@163.com
 * @date 2017-12-30 11:30:54
 */
public class TodayLearningEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
//	@Autowired(required=true)
	private Long id;
	//标题
	@Autowired
	private String title;
	//图片
	@Autowired
	private String pic;


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
	
}
