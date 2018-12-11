package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 报表实体基类,若以后周报,月报有不同字段新增,可继承此类
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-12-16 10:45:35
 */
public class AttendReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long userId;
	
	private Long classId;
	
	private Long areaId;
	
	private Long professionId;
	
	private Long levelId;
	
	private Long goodsId;
	
	private Long classTypeId;
	
	private String classplanId;
	
	private Long teacherId;
	
	private BigDecimal livePer;
	
	private BigDecimal attendPer;
	
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getProfessionId() {
		return professionId;
	}

	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}

	public Long getLevelId() {
		return levelId;
	}

	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(Long classTypeId) {
		this.classTypeId = classTypeId;
	}
	
	public String getClassplanId() {
		return classplanId;
	}

	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public BigDecimal getLivePer() {
		return livePer;
	}

	public void setLivePer(BigDecimal livePer) {
		this.livePer = livePer;
	}

	public BigDecimal getAttendPer() {
		return attendPer;
	}

	public void setAttendPer(BigDecimal attendPer) {
		this.attendPer = attendPer;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
