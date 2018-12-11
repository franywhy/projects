package com.hq.learningcenter.school.entity;

import java.io.Serializable;




/**
 * 排课计划和展示互动站点关联表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-11-15 17:12:30
 */
public class CourseClassplanGenseeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//排课计划id
	private String classplanId;
	//排课计划名称
	private String classplanName;
	//展视互动账号
	private String genseeloginname;
	//展视互动密码
	private String genseepassword;
	//展视互动回放记录地址
	private String genseewebcastvodlog;
	//展视互动直播记录
	private String genseewebcastlogurl;
	//展视互动域名
	private String genseedomain;
	//直播考勤系数
	private Float coefficient;
	//录播考勤统计系数
	private Float recordEfficient;
	//保利威视点播userId
	private String polyvuserid;
	//保利威视点播secretKey
	private String polyvsecretkey;
	//
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
	 * 设置：排课计划id
	 */
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	/**
	 * 获取：排课计划id
	 */
	public String getClassplanId() {
		return classplanId;
	}
	/**
	 * 设置：排课计划名称
	 */
	public void setClassplanName(String classplanName) {
		this.classplanName = classplanName;
	}
	/**
	 * 获取：排课计划名称
	 */
	public String getClassplanName() {
		return classplanName;
	}
	/**
	 * 设置：展视互动账号
	 */
	public void setGenseeloginname(String genseeloginname) {
		this.genseeloginname = genseeloginname;
	}
	/**
	 * 获取：展视互动账号
	 */
	public String getGenseeloginname() {
		return genseeloginname;
	}
	/**
	 * 设置：展视互动密码
	 */
	public void setGenseepassword(String genseepassword) {
		this.genseepassword = genseepassword;
	}
	/**
	 * 获取：展视互动密码
	 */
	public String getGenseepassword() {
		return genseepassword;
	}
	/**
	 * 设置：展视互动回放记录地址
	 */
	public void setGenseewebcastvodlog(String genseewebcastvodlog) {
		this.genseewebcastvodlog = genseewebcastvodlog;
	}
	/**
	 * 获取：展视互动回放记录地址
	 */
	public String getGenseewebcastvodlog() {
		return genseewebcastvodlog;
	}
	/**
	 * 设置：展视互动直播记录
	 */
	public void setGenseewebcastlogurl(String genseewebcastlogurl) {
		this.genseewebcastlogurl = genseewebcastlogurl;
	}
	/**
	 * 获取：展视互动直播记录
	 */
	public String getGenseewebcastlogurl() {
		return genseewebcastlogurl;
	}
	/**
	 * 设置：展视互动域名
	 */
	public void setGenseedomain(String genseedomain) {
		this.genseedomain = genseedomain;
	}
	/**
	 * 获取：展视互动域名
	 */
	public String getGenseedomain() {
		return genseedomain;
	}
	/**
	 * 设置：直播考勤系数
	 */
	public void setCoefficient(Float coefficient) {
		this.coefficient = coefficient;
	}
	/**
	 * 获取：直播考勤系数
	 */
	public Float getCoefficient() {
		return coefficient;
	}
	/**
	 * 设置：录播考勤统计系数
	 */
	public void setRecordEfficient(Float recordEfficient) {
		this.recordEfficient = recordEfficient;
	}
	/**
	 * 获取：录播考勤统计系数
	 */
	public Float getRecordEfficient() {
		return recordEfficient;
	}
	/**
	 * 设置：保利威视点播userId
	 */
	public void setPolyvuserid(String polyvuserid) {
		this.polyvuserid = polyvuserid;
	}
	/**
	 * 获取：保利威视点播userId
	 */
	public String getPolyvuserid() {
		return polyvuserid;
	}
	/**
	 * 设置：保利威视点播secretKey
	 */
	public void setPolyvsecretkey(String polyvsecretkey) {
		this.polyvsecretkey = polyvsecretkey;
	}
	/**
	 * 获取：保利威视点播secretKey
	 */
	public String getPolyvsecretkey() {
		return polyvsecretkey;
	}
	/**
	 * 设置：
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：
	 */
	public Integer getDr() {
		return dr;
	}

    @Override
    public String toString() {
        return "CourseClassplanGenseeEntity{" +
                "id=" + id +
                ", classplanId='" + classplanId + '\'' +
                ", classplanName='" + classplanName + '\'' +
                ", genseeloginname='" + genseeloginname + '\'' +
                ", genseepassword='" + genseepassword + '\'' +
                ", genseewebcastvodlog='" + genseewebcastvodlog + '\'' +
                ", genseewebcastlogurl='" + genseewebcastlogurl + '\'' +
                ", genseedomain='" + genseedomain + '\'' +
                ", coefficient=" + coefficient +
                ", recordEfficient=" + recordEfficient +
                ", polyvuserid='" + polyvuserid + '\'' +
                ", polyvsecretkey='" + polyvsecretkey + '\'' +
                ", dr=" + dr +
                '}';
    }
}
