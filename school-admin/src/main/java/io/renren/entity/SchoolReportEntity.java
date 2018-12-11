package io.renren.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-19 09:17:36
 */
public class SchoolReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//班主任pk
	private Long classTeacherId;
	//班主任姓名
	private String classTeacherName;
	//在读人数
	private Integer learningNum;
	//休学人数
	private Integer pauseNum;
	//弃考人数
	private Integer abandonNum;
	//免考核人数
	private Integer exemptNum;
	//失联人数
	private Integer nonContactNum;
	//其他（班主任等非考核人员）
	private Integer othersNum;
	//实际总人数
	private Integer totalNums;
	//换算管理人数
	private Integer manageNum;
	//实际出勤总时长（毫秒）
	private Long watchDuration;
	//应出勤总时长（毫秒）
	private Long fullDuration;
	//作业达标率
	private BigDecimal complianceRates;
	//通过率
	private BigDecimal passRates;
	//转介绍回款额
	private Double introduceMoney;
	//退费人数
	private Integer refundNum;
	//统计开始日期
	private Date startDate;
	//统计结束日期
	private Date endDate;
	//创建时间
	private Date createTime;
	//软删除：0.有效，1.删除
	private Integer dr;
	//出勤率
	private String livePerTxt;

	//报表类型:0月报,1周报
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLivePerTxt() {
        return livePerTxt;
    }

    public void setLivePerTxt(String livePerTxt) {
        this.livePerTxt = livePerTxt;
    }

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
	 * 设置：班主任pk
	 */
	public void setClassTeacherId(Long classTeacherId) {
		this.classTeacherId = classTeacherId;
	}
	/**
	 * 获取：班主任pk
	 */
	public Long getClassTeacherId() {
		return classTeacherId;
	}
	/**
	 * 设置：班主任姓名
	 */
	public void setClassTeacherName(String classTeacherName) {
		this.classTeacherName = classTeacherName;
	}
	/**
	 * 获取：班主任姓名
	 */
	public String getClassTeacherName() {
		return classTeacherName;
	}
	/**
	 * 设置：在读人数
	 */
	public void setLearningNum(Integer learningNum) {
		this.learningNum = learningNum;
	}
	/**
	 * 获取：在读人数
	 */
	public Integer getLearningNum() {
		return learningNum;
	}
	/**
	 * 设置：休学人数
	 */
	public void setPauseNum(Integer pauseNum) {
		this.pauseNum = pauseNum;
	}
	/**
	 * 获取：休学人数
	 */
	public Integer getPauseNum() {
		return pauseNum;
	}
	/**
	 * 设置：弃考人数
	 */
	public void setAbandonNum(Integer abandonNum) {
		this.abandonNum = abandonNum;
	}
	/**
	 * 获取：弃考人数
	 */
	public Integer getAbandonNum() {
		return abandonNum;
	}
	/**
	 * 设置：免考核人数
	 */
	public void setExemptNum(Integer exemptNum) {
		this.exemptNum = exemptNum;
	}
	/**
	 * 获取：免考核人数
	 */
	public Integer getExemptNum() {
		return exemptNum;
	}
	/**
	 * 设置：失联人数
	 */
	public void setNonContactNum(Integer nonContactNum) {
		this.nonContactNum = nonContactNum;
	}
	/**
	 * 获取：失联人数
	 */
	public Integer getNonContactNum() {
		return nonContactNum;
	}
	/**
	 * 设置：其他（班主任等非考核人员）
	 */
	public void setOthersNum(Integer othersNum) {
		this.othersNum = othersNum;
	}
	/**
	 * 获取：其他（班主任等非考核人员）
	 */
	public Integer getOthersNum() {
		return othersNum;
	}
	/**
	 * 设置：实际总人数
	 */
	public void setTotalNums(Integer totalNums) {
		this.totalNums = totalNums;
	}
	/**
	 * 获取：实际总人数
	 */
	public Integer getTotalNums() {
		return totalNums;
	}
	/**
	 * 设置：换算管理人数
	 */
	public void setManageNum(Integer manageNum) {
		this.manageNum = manageNum;
	}
	/**
	 * 获取：换算管理人数
	 */
	public Integer getManageNum() {
		return manageNum;
	}
	/**
	 * 设置：实际出勤总时长（毫秒）
	 */
	public void setWatchDuration(Long watchDuration) {
		this.watchDuration = watchDuration;
	}
	/**
	 * 获取：实际出勤总时长（毫秒）
	 */
	public Long getWatchDuration() {
		return watchDuration;
	}
	/**
	 * 设置：应出勤总时长（毫秒）
	 */
	public void setFullDuration(Long fullDuration) {
		this.fullDuration = fullDuration;
	}
	/**
	 * 获取：应出勤总时长（毫秒）
	 */
	public Long getFullDuration() {
		return fullDuration;
	}
	/**
	 * 设置：作业达标率
	 */
	public void setComplianceRates(BigDecimal complianceRates) {
		this.complianceRates = complianceRates;
	}
	/**
	 * 获取：作业达标率
	 */
	public BigDecimal getComplianceRates() {
		return complianceRates;
	}
	/**
	 * 设置：通过率
	 */
	public void setPassRates(BigDecimal passRates) {
		this.passRates = passRates;
	}
	/**
	 * 获取：通过率
	 */
	public BigDecimal getPassRates() {
		return passRates;
	}
	/**
	 * 设置：转介绍回款额
	 */
	public void setIntroduceMoney(Double introduceMoney) {
		this.introduceMoney = introduceMoney;
	}
	/**
	 * 获取：转介绍回款额
	 */
	public Double getIntroduceMoney() {
		return introduceMoney;
	}
	/**
	 * 设置：退费人数
	 */
	public void setRefundNum(Integer refundNum) {
		this.refundNum = refundNum;
	}
	/**
	 * 获取：退费人数
	 */
	public Integer getRefundNum() {
		return refundNum;
	}
	/**
	 * 设置：统计开始日期
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * 获取：统计开始日期
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * 设置：统计结束日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * 获取：统计结束日期
	 */
	public Date getEndDate() {
		return endDate;
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
	 * 设置：软删除：0.有效，1.删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：软删除：0.有效，1.删除
	 */
	public Integer getDr() {
		return dr;
	}
}
