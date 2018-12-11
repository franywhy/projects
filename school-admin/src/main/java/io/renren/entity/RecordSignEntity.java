package io.renren.entity;

import java.util.Date;
/**
 * 学员档案-报读信息
 * @author lintf
 *
 */
public class RecordSignEntity {
	/**
	 * 报读信息表主键
	 */
	private Long recordSignId;
	/**
	 * 学员档案主键
	 */
	private Long recordId;
	/**
	 * 报读班型
	 */
	private String courseName;
	/**
	 * 报读时间
	 */
	private String regDate;
	/**
	 * 报读院校
	 */
	private String bdyx;
	/**
	 * 专业
	 */
	private String zy;
	/**
	 * 自考班级
	 */
	private String className;
	/**
	 * 层次
	 */
	private String level;
	/**
	 * 状态(1在读,2休学,3退学)
	 */
	private Integer status;
	/**
	 * 订单id
	 */
	private Long orderId;
	
	private Long userId;
	
	private String name;
	private String mobile;
	
	
	
	/**
	 * NC中的报名表主键
	 */
	private String ncId;
	private Date createTime;
	private Date ts;
	private Long syncTime;
	public RecordSignEntity( ) {
	 	}
	/**
	 * 从订单处组装成学员档案报读信息
	 * @param order 队列中的NC报名表信息
	 */
	public RecordSignEntity(OrderMessageConsumerEntity order, MallOrderEntity m) {
		this.status=1;
		if (m!=null) {
			this.orderId=m.getOrderId();
			this.userId=m.getUserId();
		}
		
		
		if (order!=null) {
			
			this.bdyx=order.getBdyx();
			this.className=order.getZkClassName();
			this.courseName=order.getClass_name();
			this.ncId=order.getNc_id();
			this.regDate=order.getRegistdate();
			this.level=order.getRecord();
			
			
			
			
			this.zy=order.getZy();
			/*1	在读	
			2	休学	
			3	毕业	
			4	暂停	
			6	坏账*/
			//功能变更为不从NC中取得状态了,改为以蓝鲸的休学考勤中取  默认为在读 有休学单审批的为休学 在NC中退费的为退学
			//此处只判断NC退学 其他的判断在io.renren.service.impl.RecordSignServiceImpl.getRecordSignStatus中判断
			if (order.getSign_status()==null||order.getSign_status()==1||order.getSign_status()==3||order.getSign_status()==2) {
				this.status=1;
			 
			}else {
				this.status=3;
			}
			
		}
	
		
		
		
		
		
		 
		
	}
	public Long getRecordSignId() {
		return recordSignId;
	}
	public void setRecordSignId(Long recordSignId) {
		this.recordSignId = recordSignId;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getBdyx() {
		return bdyx;
	}
	public void setBdyx(String bdyx) {
		this.bdyx = bdyx;
	}
	public String getZy() {
		return zy;
	}
	public void setZy(String zy) {
		this.zy = zy;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	 
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
	public Long getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(Long syncTime) {
		this.syncTime = syncTime;
	}
	
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 从消息队列中来的数据更新旧的报读信息
	 *@param old
	 *@param s
	 *@param m
	 * @author lintf
	 * 2018年8月23日
	 */
	public void signUpdate(RecordSignEntity old, RecordSignEntity s,MallOrderEntity m) {
		if (m!=null) {
			if (m.getOrderId()!=null&&m.getOrderId()>0) {
				old.setOrderId(m.getOrderId());
			}
		
			old.setUserId(m.getUserId());
		}else if (s!=null) {
			if (s.getOrderId()!=null&&s.getOrderId()>0) {
				old.setOrderId(s.getOrderId());
			}
			if (s.getUserId()!=null&&s.getUserId()>0) {

				old.setUserId(s.getUserId());
			}
			 
		
		}
	
		old.setBdyx(s.getBdyx());
		old.setClassName(s.getClassName());
		old.setCourseName(s.getCourseName());
		old.setLevel(s.getLevel());
		old.setNcId(s.getNcId());
		old.setRegDate(s.getRegDate());
		old.setZy(s.getZy());
		old.setSyncTime(s.getSyncTime());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
