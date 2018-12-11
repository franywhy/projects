package com.hq.courses.pojo;
/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月22日
 */
public class CsZJPOJO {
	
	
	//主键
	private String id;
	//父ID
	private String parentId;
	//编码
	private String code;
	//名称
	private String name;
	//知识点数量
	private int knowledgeNum;
	//章节类型 1.章 2.节
	private int zj;
	//排序
	private int orderNum;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getKnowledgeNum() {
		return knowledgeNum;
	}
	public void setKnowledgeNum(int knowledgeNum) {
		this.knowledgeNum = knowledgeNum;
	}
	public int getZj() {
		return zj;
	}
	public void setZj(int zj) {
		this.zj = zj;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	
	/**
	 * 初始化章ID
	 * @param id
	 */
	public void initZId(Long id) {
		this.id = CsZJEnum.Z.getHead() + id;
		this.zj = CsZJEnum.Z.getValue();
		this.parentId = "0";
	}
	
	/**
	 * 初始化节id
	 * @param id
	 * @param parentId
	 */
	public void initJId(Long id , Long parentId) {
		this.id = CsZJEnum.J.getHead() + id;
		this.zj = CsZJEnum.J.getValue();
		this.parentId = CsZJEnum.Z.getHead() + parentId;
	}
	
}
