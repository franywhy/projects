package io.renren.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
public class KnowledgePointEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 知识点ID
	private Integer id;
	// 知识点名称
	private String name;

	// 创建人
	private Long createBy;
	// 创建日期
	private Date createTime;
	// 更新人
	private Long updateBy;
	// 更新日期
	private Date updateTime;
	// 所属课程
	private String course;
	// 所属章
	private String chapter;
	// 所属节
	private String verse;

	/**
	 * 设置：知识点ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取：知识点ID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置：知识点名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：知识点名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：创建人
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	/**
	 * 获取：创建人
	 */
	public Long getCreateBy() {
		return createBy;
	}

	/**
	 * 设置：创建日期
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取：创建日期
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置：更新人
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 获取：更新人
	 */
	public Long getUpdateBy() {
		return updateBy;
	}

	/**
	 * 设置：更新日期
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取：更新日期
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置：所属课程
	 */
	public void setCourse(String course) {
		this.course = course;
	}

	/**
	 * 获取：所属课程
	 */
	public String getCourse() {
		return course;
	}

	/**
	 * 设置：所属章
	 */
	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	/**
	 * 获取：所属章
	 */
	public String getChapter() {
		return chapter;
	}

	/**
	 * 设置：所属节
	 */
	public void setVerse(String verse) {
		this.verse = verse;
	}

	/**
	 * 获取：所属节
	 */
	public String getVerse() {
		return verse;
	}

	private List ids;

	public List getIds() {
		return ids;
	}

	public void setIds(List ids) {
		this.ids = ids;
	}

	// 所属课程名称
	private String courseName;
	// 所属章 名称
	private String chapterName;
	// 所属节名称
	private String verseName;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getVerseName() {
		return verseName;
	}

	public void setVerseName(String verseName) {
		this.verseName = verseName;
	}
}
