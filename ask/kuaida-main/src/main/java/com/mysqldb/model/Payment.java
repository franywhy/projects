package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: �����-MagicalTools
 * Date: 2015-07-25 16:40:09
 */
@Entity
@Table(name = "payment")
@org.hibernate.annotations.Table(appliesTo = "payment", comment = "payment")
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * payment
	 */
	public static final String REF="Payment";

	/**
	 * id ��������
	 */
	public static final String PROP_ID="id";
	
	/**
	 * pay_flow_id ��������
	 */
	public static final String PROP_PAYFLOWID="payFlowId";
	
	/**
	 * pay_money ��������
	 */
	public static final String PROP_PAYMONEY="payMoney";
	
	/**
	 * pay_time ��������
	 */
	public static final String PROP_PAYTIME="payTime";
	
	/**
	 * apply_flow_id ��������
	 */
	public static final String PROP_APPLYFLOWID="applyFlowId";
	
	/**
	 * user_id ��������
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * real_name ��������
	 */
	public static final String PROP_REALNAME="realName";
	
	/**
	 * alipay_account ��������
	 */
	public static final String PROP_ALIPAYACCOUNT="alipayAccount";
	
	/**
	 * payment_month ��������
	 */
	public static final String PROP_PAYMENTMONTH="paymentMonth";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * pay_flow_id
	 */
	private java.lang.Integer payFlowId;
	
	/**
	 * pay_money
	 */
	private java.lang.Float payMoney;
	
	/**
	 * pay_time
	 */
	private java.sql.Timestamp payTime;
	
	/**
	 * apply_flow_id
	 */
	private java.lang.Integer applyFlowId;
	
	/**
	 * user_id
	 */
	private java.lang.Integer userId;
	
	/**
	 * real_name
	 */
	private java.lang.String realName;
	
	/**
	 * alipay_account
	 */
	private java.lang.String alipayAccount;
	
	/**
	 * payment_month
	 */
	private java.lang.String paymentMonth;
	

	/**
	 * Author name@mail.com
	 * ��ȡ id ������ֵ
     *
	 * @return id :  id 
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID",columnDefinition = "INT")
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 * Author name@mail.com
	 * ���� id ������ֵ
	 *		
	 * @param id :  id 
	 */
	public void setId(java.lang.Integer id){
		this.id	= id;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ pay_flow_id ������ֵ
     *
	 * @return payFlowId :  pay_flow_id 
	 */
	@Column(name = "PAY_FLOW_ID",columnDefinition = "INT")
	public java.lang.Integer getPayFlowId(){
		return this.payFlowId;
	}

	/**
	 * Author name@mail.com
	 * ���� pay_flow_id ������ֵ
	 *		
	 * @param payFlowId :  pay_flow_id 
	 */
	public void setPayFlowId(java.lang.Integer payFlowId){
		this.payFlowId	= payFlowId;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ pay_money ������ֵ
     *
	 * @return payMoney :  pay_money 
	 */
	@Column(name = "PAY_MONEY",columnDefinition = "FLOAT")
	public java.lang.Float getPayMoney(){
		return this.payMoney;
	}

	/**
	 * Author name@mail.com
	 * ���� pay_money ������ֵ
	 *		
	 * @param payMoney :  pay_money 
	 */
	public void setPayMoney(java.lang.Float payMoney){
		this.payMoney	= payMoney;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ pay_time ������ֵ
     *
	 * @return payTime :  pay_time 
	 */
	@Column(name = "PAY_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getPayTime(){
		return this.payTime;
	}

	/**
	 * Author name@mail.com
	 * ���� pay_time ������ֵ
	 *		
	 * @param payTime :  pay_time 
	 */
	public void setPayTime(java.sql.Timestamp payTime){
		this.payTime	= payTime;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ apply_flow_id ������ֵ
     *
	 * @return applyFlowId :  apply_flow_id 
	 */
	@Column(name = "APPLY_FLOW_ID",columnDefinition = "INT")
	public java.lang.Integer getApplyFlowId(){
		return this.applyFlowId;
	}

	/**
	 * Author name@mail.com
	 * ���� apply_flow_id ������ֵ
	 *		
	 * @param applyFlowId :  apply_flow_id 
	 */
	public void setApplyFlowId(java.lang.Integer applyFlowId){
		this.applyFlowId	= applyFlowId;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ user_id ������ֵ
     *
	 * @return userId :  user_id 
	 */
	@Column(name = "USER_ID",columnDefinition = "INT")
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 * Author name@mail.com
	 * ���� user_id ������ֵ
	 *		
	 * @param userId :  user_id 
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId	= userId;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ real_name ������ֵ
     *
	 * @return realName :  real_name 
	 */
	@Column(name = "REAL_NAME",columnDefinition = "VARCHAR")
	public java.lang.String getRealName(){
		return this.realName;
	}

	/**
	 * Author name@mail.com
	 * ���� real_name ������ֵ
	 *		
	 * @param realName :  real_name 
	 */
	public void setRealName(java.lang.String realName){
		this.realName	= realName;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ alipay_account ������ֵ
     *
	 * @return alipayAccount :  alipay_account 
	 */
	@Column(name = "ALIPAY_ACCOUNT",columnDefinition = "VARCHAR")
	public java.lang.String getAlipayAccount(){
		return this.alipayAccount;
	}

	/**
	 * Author name@mail.com
	 * ���� alipay_account ������ֵ
	 *		
	 * @param alipayAccount :  alipay_account 
	 */
	public void setAlipayAccount(java.lang.String alipayAccount){
		this.alipayAccount	= alipayAccount;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ payment_month ������ֵ
     *
	 * @return paymentMonth :  payment_month 
	 */
	@Column(name = "PAYMENT_MONTH",columnDefinition = "VARCHAR")
	public java.lang.String getPaymentMonth(){
		return this.paymentMonth;
	}

	/**
	 * Author name@mail.com
	 * ���� payment_month ������ֵ
	 *		
	 * @param paymentMonth :  payment_month 
	 */
	public void setPaymentMonth(java.lang.String paymentMonth){
		this.paymentMonth	= paymentMonth;
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ�ַ���
	 */
	public String toString(){		
		return "{ _name=Payment" + ",id=" + id +",payFlowId=" + payFlowId +",payMoney=" + payMoney +",payTime=" + payTime +",applyFlowId=" + applyFlowId +",userId=" + userId +",realName=" + realName +",alipayAccount=" + alipayAccount +",paymentMonth=" + paymentMonth + "}";
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ JSON �ַ���
	 */
	public String toJson(){
		return "{ _name:'Payment'" + ",id:'" + id + "'" +",payFlowId:'" + payFlowId + "'" +",payMoney:'" + payMoney + "'" +",payTime:'" + payTime + "'" +",applyFlowId:'" + applyFlowId + "'" +",userId:'" + userId + "'" +",realName:'" + realName + "'" +",alipayAccount:'" + alipayAccount + "'" +",paymentMonth:'" + paymentMonth + "'" +  "}";
	}
}
