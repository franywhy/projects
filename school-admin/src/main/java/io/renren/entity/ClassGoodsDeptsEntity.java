package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 班级-商品-部门对照表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-11-29 15:45:01
 */
public class ClassGoodsDeptsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//班级id
	private Long classId;
	//商品id
	private Long goodId;
	//部门id
	private Long deptId;
	//软删除0：不删除 1：删除
	private Integer dr;
	//创建者
	private Long creator;
	//创建时间
	private Date creationTime;
	//修改者
	private Long modifier;
	//修改时间
	private Date modifyTime;

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
	 * 设置：班级id
	 */
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	/**
	 * 获取：班级id
	 */
	public Long getClassId() {
		return classId;
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
	 * 设置：部门id
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：部门id
	 */
	public Long getDeptId() {
		return deptId;
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
	/**
	 * 设置：创建者
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建者
	 */
	public Long getCreator() {
		return creator;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	/**
	 * 设置：修改者
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：修改者
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifyTime(Date  modifyTime) {
		this.modifyTime =  modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return  modifyTime;
	}
}
