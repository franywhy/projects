package com.hq.adaptive.pojo;

import java.util.List;

/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月30日
 */
public class CourseTreePOJO {
	
	private Long id;
	
	private String name;
	
	private Integer level;
	
	private List<CourseTreePOJO> children;

	private Long knowledgeId;

	public CourseTreePOJO(Long id, String name, Integer level) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<CourseTreePOJO> getChildren() {
		return children;
	}

	public void setChildren(List<CourseTreePOJO> children) {
		this.children = children;
	}

//	@Override
//	public String toString() {
//		return "CourseTreePOJO [id=" + id + ", name=" + name + ", level=" + level + ", children=" + children + "]";
//	}


	public Long getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(Long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}
}
