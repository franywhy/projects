package io.renren.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 记录学员转班前推送给题库的队列班级信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-02-26 14:37:05
 */
public class UserOldclassLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//订单号
	private String orderNo;
	//商品id
	private Long goodId;
	//用户id
	private Long userId;
	//用户名称
	private String nickName;
	//用户手机
	private String mobile;
	//班级id
	private Long classId;
	//班级名称
	private String className;
	//班型id
	private Long classtypeId;
	//班型名称
	private String classtypeName;
	//班主任id
	private Long classTeacherId;
	//班主任昵称
	private String classTeacherNickname;
	//班主任手机
	private String classTeacherMobile;
	//商品是否开通题库权限,1是,0否,从商品表读取
	private Integer onlyOne;
	//是否开通题库权限,1是,0否
	private Integer open;
	//是否是转班后的班级,1是新班,0是旧班级
	private Integer isNewClass;
	//
	private Date ts;

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
	 * 设置：订单号
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 获取：订单号
	 */
	public String getOrderNo() {
		return orderNo;
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
	 * 设置：用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：用户名称
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * 获取：用户名称
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * 设置：用户手机
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：用户手机
	 */
	public String getMobile() {
		return mobile;
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
	 * 设置：班级名称
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * 获取：班级名称
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * 设置：班型id
	 */
	public void setClasstypeId(Long classtypeId) {
		this.classtypeId = classtypeId;
	}
	/**
	 * 获取：班型id
	 */
	public Long getClasstypeId() {
		return classtypeId;
	}
	/**
	 * 设置：班型名称
	 */
	public void setClasstypeName(String classtypeName) {
		this.classtypeName = classtypeName;
	}
	/**
	 * 获取：班型名称
	 */
	public String getClasstypeName() {
		return classtypeName;
	}
	/**
	 * 设置：班主任id
	 */
	public void setClassTeacherId(Long classTeacherId) {
		this.classTeacherId = classTeacherId;
	}
	/**
	 * 获取：班主任id
	 */
	public Long getClassTeacherId() {
		return classTeacherId;
	}
	/**
	 * 设置：班主任昵称
	 */
	public void setClassTeacherNickname(String classTeacherNickname) {
		this.classTeacherNickname = classTeacherNickname;
	}
	/**
	 * 获取：班主任昵称
	 */
	public String getClassTeacherNickname() {
		return classTeacherNickname;
	}
	/**
	 * 设置：班主任手机
	 */
	public void setClassTeacherMobile(String classTeacherMobile) {
		this.classTeacherMobile = classTeacherMobile;
	}
	/**
	 * 获取：班主任手机
	 */
	public String getClassTeacherMobile() {
		return classTeacherMobile;
	}
	/**
	 * 设置：商品是否开通题库权限,1是,0否,从商品表读取
	 */
	public void setOnlyOne(Integer onlyOne) {
		this.onlyOne = onlyOne;
	}
	/**
	 * 获取：商品是否开通题库权限,1是,0否,从商品表读取
	 */
	public Integer getOnlyOne() {
		return onlyOne;
	}
	/**
	 * 设置：是否开通题库权限,1是,0否
	 */
	public void setOpen(Integer open) {
		this.open = open;
	}
	/**
	 * 获取：是否开通题库权限,1是,0否
	 */
	public Integer getOpen() {
		return open;
	}
	/**
	 * 设置：是否是转班后的班级,1是新班,0是旧班级
	 */
	public void setIsNewClass(Integer isNewClass) {
		this.isNewClass = isNewClass;
	}
	/**
	 * 获取：是否是转班后的班级,1是新班,0是旧班级
	 */
	public Integer getIsNewClass() {
		return isNewClass;
	}
	/**
	 * 设置：
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}
	/**
	 * 获取：
	 */
	public Date getTs() {
		return ts;
	}
}
