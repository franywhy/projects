package io.renren.entity;

import java.util.Date;
/**
 * 学员档案-退费信息
 * @author Administrator
 *
 */
public class RecordReFundsEntity {
	/**
	 * 退费信息主键
	 */
	private Long recordRefundsId;
	/**
	 * 学员档案id
	 */
	private Long recordId;
	/**
	 * 退费日期
	 */
	private String refundsDate;
	/**
	 * 退费原因
	 */
	private String refundsReason;
	/**
	 * 退费结果
	 */
	private String refundsResult;
	/**
	 * 是否申请
	 */
	private Integer  applyStatus;
	private Long syncTime;
	/**
	 * nc退费单id
	 */
	private String ncDrawbackId;
	/**
	 * NC订单id
	 */
	private String ncId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	private Date ts;
	
	private Long orderId;
	
	private Long  userId;
	
	private String mobile;
	
	private  String orderNo;
	
	/**
	 * 班主任ID
	 */
	private Long  teacherId;
	/**
	 * 班主任名称
	 */
	private String   teacherName;
	/**
	 * 班级id
	 */
    private Long   classId;
    /**
     * 班级名称
     */
    private String className;
	
	/**
	 * 是否来自NC更新的
	 */
    public boolean inNCSync;
    
    
    
    
    
	
	public boolean isInNCSync() {
		return inNCSync;
	}




	public String getOrderNo() {
		return orderNo;
	}




	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}




	public void setInNCSync(boolean inNCSync) {
		this.inNCSync = inNCSync;
	}




	public Long getTeacherId() {
		return teacherId;
	}




	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}




	public String getTeacherName() {
		return teacherName;
	}




	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}




	public Long getClassId() {
		return classId;
	}




	public void setClassId(Long classId) {
		this.classId = classId;
	}




	public String getClassName() {
		return className;
	}




	public void setClassName(String className) {
		this.className = className;
	}




	public String getMobile() {
		return mobile;
	}




	public void setMobile(String mobile) {
		this.mobile = mobile;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	private String name;
	
	
	
	public Long getOrderId() {
		return orderId;
	}




	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}




	public Long getUserId() {
		return userId;
	}




	public void setUserId(Long userId) {
		this.userId = userId;
	}




	public RecordReFundsEntity( ) { 
	}
	
	 


	public Long getRecordRefundsId() {
		return recordRefundsId;
	}
	public void setRecordRefundsId(Long recordRefundsId) {
		this.recordRefundsId = recordRefundsId;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public String getRefundsDate() {
		return refundsDate;
	}
	public void setRefundsDate(String refundsDate) {
		this.refundsDate = refundsDate;
	}
	public String getRefundsReason() {
		return refundsReason;
	}
	public void setRefundsReason(String refundsReason) {
		this.refundsReason = refundsReason;
	}
	public String getRefundsResult() {
		return refundsResult;
	}
	public void setRefundsResult(String refundsResult) {
		this.refundsResult = refundsResult;
	}
	 
	
	
	
	public Integer getApplyStatus() {
		return applyStatus;
	}




	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}




	public Long getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(Long syncTime) {
		this.syncTime = syncTime;
	}
	public String getNcDrawbackId() {
		return ncDrawbackId;
	}
	public void setNcDrawbackId(String ncDrawbackId) {
		this.ncDrawbackId = ncDrawbackId;
	}
	public String getNcId() {
		return ncId;
	}
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}




	public void refundUpdate(RecordReFundsEntity old, RecordReFundsEntity re) {
		old.setNcDrawbackId(re.getNcDrawbackId());
		old.setRefundsDate(re.getRefundsDate());
		old.setRefundsReason(re.getRefundsReason());
		
		old.setOrderId(re.getOrderId());
		old.setUserId(re.getUserId());
		old.setSyncTime(re.getSyncTime());
		if (!re.inNCSync) {
			old.setRefundsResult(re.getRefundsResult());
			old.setApplyStatus(re.getApplyStatus());
		}
	 
		
	}

	
	
	
}
