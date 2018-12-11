package com.mysqldb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * 学分完成率报表	
 * @author lintf 
 * @Time 2017年11月13日14:23:32
 */
@Entity
@Table(name = "credit_percent")
@org.hibernate.annotations.Table(appliesTo = "credit_percent", comment = "credit_percent")
public class Creditpercent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Creditpercent(){
		
	}
	 





	/**
	 * id
	 */
	private Integer id;
	 
	private Date ts;
	private String pid;

     /**
     * 大区名称
     */
	private String largeAreaName;	
	/**
	 * 校区名称
	 */
	private String orgName;	
	private String orgId;
	private String orgCode;
	/**
	 * 月份
	 */
	private String months;	
	/**
	 * 总应修
	 */
	private Integer claimScore;	
	/**
	 * 总应排课人数
	 */
	private Integer claimNum;
	/**
	 * 总已收
	 */
	private Integer totalScore;	
	/**
	 * 总已排课人数
	 */
	private Integer totalNum;
	/**
	 * 学分完成率
	 */
	private String xfPercent;	
	/**
	 * 目标完成率
	 */
	private String mbpercent;
	/**
	 * 排课完成率
	 */
	private String pkpercent;
	/**
	 * 排课目标完成率
	 */
	private String pkmbpercent;
	/**
	 * 目标分数(用来运算合计的)
	 */
	private String mbscore;
	/**
	 * 排课目标人数(用来运算合计的)
	 */
	private String mbNum;
	/**
	 * 学分是否达标 1 是不达标 0是达标
	 */
	private Integer ispass=1;	
	/**
	 * 排课是否达标 1 是不达标 0是达标
	 */
	private Integer ispaike=1;	
	/**
	 * 1 是删除 0是不删除
	 */
	private Integer dr=1;	
	/**
	 * 排序时用到的 年月+orgcode
	 */
	private String orderIndex;
	/**
	 * 报表时用到的临时字段
	 */
	private String Remark;

	private String Pkremark;
	private String dbilldate;
	@Column(name = "dbilldate",columnDefinition = "VARCHAR")
	public String getDbilldate() {
		return dbilldate;
	}


	public void setDbilldate(String dbilldate) {
		this.dbilldate = dbilldate;
	}
	
	
	/**
	 * 获取 id 的属性值
     *
	 * @return id :  id 
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID",columnDefinition = "INT")
	public Integer getId(){
		return this.id;
	}

	/**
	 * 设置 id 的属性值
	 *		
	 * @param id :  id 
	 */
	public void setId(Integer id){
		this.id	= id;
	}
	
	
 
	/**
	 * 获取 时间戳
	 */
	@Column(name = "TS",insertable = false,updatable=false,columnDefinition = "DATETIME")
	public Date getTs(){
		return this.ts;
	}

	/**
	 * 设置 时间戳
	 */
	public void setTs(Date ts){
		this.ts	= ts;
	}
	@Column(name = "pid",columnDefinition = "VARCHAR") 
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	@Column(name = "largeAreaName",columnDefinition = "VARCHAR") 
	public String getLargeAreaName() {
		return largeAreaName;
	}

	public void setLargeAreaName(String largeAreaName) {
		this.largeAreaName = largeAreaName;
	}
	@Column(name = "orgName",columnDefinition = "VARCHAR") 
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	@Column(name = "orgId",columnDefinition = "VARCHAR") 
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	@Column(name = "months",columnDefinition = "VARCHAR") 
	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}
	@Column(name = "claim_score",columnDefinition = "INT")
	public Integer getClaimScore() {
		return claimScore;
	}

	public void setClaimScore(Integer claimScore) {
		this.claimScore = claimScore;
	}
	@Column(name = "total_score",columnDefinition = "INT")
	public Integer getTotalscore() {
		return totalScore;
	}

	public void setTotalscore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	/**
	 * 学分完成率
	 * @return
	 */
	@Column(name = "xf_percent",columnDefinition = "VARCHAR") 
	public String getXfPercent() {
		return xfPercent;
	}
/**
 * 学分完成率
 * @param xfPercent
 */
	public void setXfPercent(String xfPercent) {
		this.xfPercent = xfPercent;
	}
	/**
	 *  目标完成率
	 * @return
	 */
	@Column(name = "xf_mbpercent",columnDefinition = "VARCHAR") 
	public String getMbpercent() {
		return mbpercent;
	}
/**
 * 目标完成率
 * @param mbpercent
 */
	public void setMbpercent(String mbpercent) {
		this.mbpercent = mbpercent;
	}
	
	
	
	
	
	@Column(name = "is_pass",columnDefinition = "INT")
	public Integer getIspass() {
		return ispass;
	}

	public void setIspass(Integer ispass) {
		this.ispass = ispass;
	}
	
	
	
	
	
	
	@Column(name = "claim_num",columnDefinition = "INT")
	public Integer getClaimNum() {
		return claimNum;
	}

	public void setClaimNum(Integer claimNum) {
		this.claimNum = claimNum;
	}
	@Column(name = "total_num",columnDefinition = "INT")
	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	/**
	 * 学分完成率
	 * @return
	 */
	@Column(name = "pk_percent",columnDefinition = "VARCHAR") 
	public String getPkpercent() {
		return pkpercent;
	}
/**
 * 学分完成率
 * @param xfPercent
 */
	public void setPkpercent(String pkpercent) {
		this.pkpercent = pkpercent;
	}
	/**
	 *  目标完成率
	 * @return
	 */
	@Column(name = "pk_mbpercent",columnDefinition = "VARCHAR") 
	public String getPkmbpercent() {
		return pkmbpercent;
	}
/**
 * 目标完成率
 * @param mbpercent
 */
	public void setPkmbpercent(String pkmbpercent) {
		this.pkmbpercent = pkmbpercent;
	}
	
	/**
	 * 排课是否达标 1 是不达标 0是达标
	 * @return
	 */
	@Column(name = "is_paike",columnDefinition = "INT")
	public Integer getIspaike() {
		return ispaike;
	}
/**
 * 排课是否达标 1 是不达标 0是达标
 * @param ispaike
 */
	public void setIspaike(Integer ispaike) {
		this.ispaike = ispaike;
	}
	
	
	@Column(name = "dr",columnDefinition = "INT")
	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}
	@Column(name = "orgCode",columnDefinition = "VARCHAR") 
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	@Column(name = "orderIndex",columnDefinition = "VARCHAR") 
	public String getOrderindex() {
		return orderIndex;
	}
	public void setOrderindex(String orderIndex) {
		this.orderIndex = orderIndex;
	}
	
	
	@Column(name = "Remark",columnDefinition = "VARCHAR") 
	public String getRemark() {
		return Remark;
	}

	public void setRemark(String Remark) {
		this.Remark = Remark;
	}

	@Column(name = "Pkremark",columnDefinition = "VARCHAR") 
	public String getPkremark() {
		return Pkremark;
	}

	public void setPkremark(String Pkremark) {
		this.Pkremark = Pkremark;
	}
	
	
	@Column(name = "mbscore",columnDefinition = "VARCHAR") 
	public String getMbscore() {
		return mbscore;
	}


	public void setMbscore(String mbscore) {
		this.mbscore = mbscore;
	}
	@Column(name = "mbnum",columnDefinition = "VARCHAR") 
	public String getMbnum() {
		return mbNum;
	}


	public void setMbnum(String Mbnum) {
		this.mbNum = Mbnum;
	}
	
}
