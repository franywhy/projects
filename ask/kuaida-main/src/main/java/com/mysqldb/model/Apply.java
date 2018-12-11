package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: �����-MagicalTools
 * Date: 2015-07-30 10:14:29
 */
@Entity
@Table(name = "apply")
@org.hibernate.annotations.Table(appliesTo = "apply", comment = "apply")
public class Apply implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * apply
	 */
	public static final String REF="Apply";

	/**
	 * id ��������
	 */
	public static final String PROP_ID="id";
	
	/**
	 * user_id ��������
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * �ǳ� ��������
	 */
	public static final String PROP_NICKNAME="nickName";
	
	/**
	 * ��ʵ���� ��������
	 */
	public static final String PROP_REALNAME="realName";
	
	/**
	 * ֧�����˺� ��������
	 */
	public static final String PROP_ALIPAYACCOUNT="alipayAccount";
	
	/**
	 * ���뵥��ˮ�� ��������
	 */
	public static final String PROP_APPLYFLOWID="applyFlowId";
	
	/**
	 * apply_money ��������
	 */
	public static final String PROP_APPLYMONEY="applyMoney";
	
	/**
	 * update_time ��������
	 */
	public static final String PROP_UPDATETIME="updateTime";
	
	/**
	 * create_time ��������
	 */
	public static final String PROP_CREATETIME="createTime";
	
	/**
	 * ���뵥״̬0Ϊ������1Ϊ����ɹ� ��������
	 */
	public static final String PROP_APPLYSTATE="applyState";
	
	/**
	 * �ύ���뵥�·� ��������
	 */
	public static final String PROP_APPLYYEARMONTH="applyYearMonth";
	
	/**
	 * payment_flow_id ��������
	 */
	public static final String PROP_PAYMENTFLOWID="paymentFlowId";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * user_id
	 */
	private java.lang.Integer userId;
	
	/**
	 * �ǳ�
	 */
	private java.lang.String nickName;
	
	/**
	 * ��ʵ����
	 */
	private java.lang.String realName;
	
	/**
	 * ֧�����˺�
	 */
	private java.lang.String alipayAccount;
	
	/**
	 * ���뵥��ˮ��
	 */
	private java.lang.String applyFlowId;
	
	/**
	 * apply_money
	 */
	private java.math.BigDecimal applyMoney;
	
	/**
	 * update_time
	 */
	private java.sql.Timestamp updateTime;
	
	/**
	 * create_time
	 */
	private java.sql.Timestamp createTime;
	
	/**
	 * ���뵥״̬0Ϊ������1Ϊ����ɹ�
	 */
	private java.lang.Integer applyState;
	
	/**
	 * �ύ���뵥�·�
	 */
	private java.lang.String applyYearMonth;
	
	/**
	 * payment_flow_id
	 */
	private java.lang.Integer paymentFlowId;

	/**
	 * fee_ratio
	 */
	private String feeRatio;

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
	 * ��ȡ �ǳ� ������ֵ
     *
	 * @return nickName :  �ǳ� 
	 */
	@Column(name = "NICK_NAME",columnDefinition = "VARCHAR")
	public java.lang.String getNickName(){
		return this.nickName;
	}

	/**
	 * Author name@mail.com
	 * ���� �ǳ� ������ֵ
	 *		
	 * @param nickName :  �ǳ� 
	 */
	public void setNickName(java.lang.String nickName){
		this.nickName	= nickName;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ��ʵ���� ������ֵ
     *
	 * @return realName :  ��ʵ���� 
	 */
	@Column(name = "REAL_NAME",columnDefinition = "VARCHAR")
	public java.lang.String getRealName(){
		return this.realName;
	}

	/**
	 * Author name@mail.com
	 * ���� ��ʵ���� ������ֵ
	 *		
	 * @param realName :  ��ʵ���� 
	 */
	public void setRealName(java.lang.String realName){
		this.realName	= realName;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ֧�����˺� ������ֵ
     *
	 * @return alipayAccount :  ֧�����˺� 
	 */
	@Column(name = "ALIPAY_ACCOUNT",columnDefinition = "VARCHAR")
	public java.lang.String getAlipayAccount(){
		return this.alipayAccount;
	}

	/**
	 * Author name@mail.com
	 * ���� ֧�����˺� ������ֵ
	 *		
	 * @param alipayAccount :  ֧�����˺� 
	 */
	public void setAlipayAccount(java.lang.String alipayAccount){
		this.alipayAccount	= alipayAccount;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ���뵥��ˮ�� ������ֵ
     *
	 * @return applyFlowId :  ���뵥��ˮ�� 
	 */
	@Column(name = "APPLY_FLOW_ID",columnDefinition = "INT")
	public java.lang.String getApplyFlowId(){
		return this.applyFlowId;
	}

	/**
	 * Author name@mail.com
	 * ���� ���뵥��ˮ�� ������ֵ
	 *		
	 * @param applyFlowId :  ���뵥��ˮ�� 
	 */
	public void setApplyFlowId(java.lang.String applyFlowId){
		this.applyFlowId	= applyFlowId;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ apply_money ������ֵ
     *
	 * @return applyMoney :  apply_money 
	 */
	@Column(name = "APPLY_MONEY",columnDefinition = "FLOAT")
	public java.math.BigDecimal getApplyMoney(){
		return this.applyMoney;
	}

	/**
	 * Author name@mail.com
	 * ���� apply_money ������ֵ
	 *		
	 * @param applyMoney :  apply_money 
	 */
	public void setApplyMoney(java.math.BigDecimal applyMoney){
		this.applyMoney	= applyMoney;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ update_time ������ֵ
     *
	 * @return updateTime :  update_time 
	 */
	@Column(name = "UPDATE_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getUpdateTime(){
		return this.updateTime;
	}

	/**
	 * Author name@mail.com
	 * ���� update_time ������ֵ
	 *		
	 * @param updateTime :  update_time 
	 */
	public void setUpdateTime(java.sql.Timestamp updateTime){
		this.updateTime	= updateTime;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ create_time ������ֵ
     *
	 * @return createTime :  create_time 
	 */
	@Column(name = "CREATE_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getCreateTime(){
		return this.createTime;
	}

	/**
	 * Author name@mail.com
	 * ���� create_time ������ֵ
	 *		
	 * @param createTime :  create_time 
	 */
	public void setCreateTime(java.sql.Timestamp createTime){
		this.createTime	= createTime;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ���뵥״̬0Ϊ������1Ϊ����ɹ� ������ֵ
     *
	 * @return applyState :  ���뵥״̬0Ϊ������1Ϊ����ɹ� 
	 */
	@Column(name = "APPLY_STATE",columnDefinition = "INT")
	public java.lang.Integer getApplyState(){
		return this.applyState;
	}

	/**
	 * Author name@mail.com
	 * ���� ���뵥״̬0Ϊ������1Ϊ����ɹ� ������ֵ
	 *		
	 * @param applyState :  ���뵥״̬0Ϊ������1Ϊ����ɹ� 
	 */
	public void setApplyState(java.lang.Integer applyState){
		this.applyState	= applyState;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ �ύ���뵥�·� ������ֵ
     *
	 * @return applyYearMonth :  �ύ���뵥�·� 
	 */
	@Column(name = "APPLY_YEAR_MONTH",columnDefinition = "VARCHAR")
	public java.lang.String getApplyYearMonth(){
		return this.applyYearMonth;
	}

	/**
	 * Author name@mail.com
	 * ���� �ύ���뵥�·� ������ֵ
	 *		
	 * @param applyYearMonth :  �ύ���뵥�·� 
	 */
	public void setApplyYearMonth(java.lang.String applyYearMonth){
		this.applyYearMonth	= applyYearMonth;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ payment_flow_id ������ֵ
     *
	 * @return paymentFlowId :  payment_flow_id 
	 */
	@Column(name = "PAYMENT_FLOW_ID",columnDefinition = "INT")
	public java.lang.Integer getPaymentFlowId(){
		return this.paymentFlowId;
	}

	/**
	 * Author name@mail.com
	 * ���� payment_flow_id ������ֵ
	 *		
	 * @param paymentFlowId :  payment_flow_id 
	 */
	public void setPaymentFlowId(java.lang.Integer paymentFlowId){
		this.paymentFlowId	= paymentFlowId;
	}

	@Column(name = "fee_ratio",columnDefinition = "VARCHAR")
	public String getFeeRatio() {
		return feeRatio;
	}

	public void setFeeRatio(String feeRatio) {
		this.feeRatio = feeRatio;
	}

	/**
	 * Author name@mail.com
	 * ת��Ϊ�ַ���
	 */
	public String toString(){		
		return "{ _name=Apply" + ",id=" + id +",userId=" + userId +",nickName=" + nickName +",realName=" + realName +",alipayAccount=" + alipayAccount +",applyFlowId=" + applyFlowId +",applyMoney=" + applyMoney +",updateTime=" + updateTime +",createTime=" + createTime +",applyState=" + applyState +",applyYearMonth=" + applyYearMonth +",paymentFlowId=" + paymentFlowId + "}";
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ JSON �ַ���
	 */
	public String toJson(){
		return "{ _name:'Apply'" + ",id:'" + id + "'" +",userId:'" + userId + "'" +",nickName:'" + nickName + "'" +",realName:'" + realName + "'" +",alipayAccount:'" + alipayAccount + "'" +",applyFlowId:'" + applyFlowId + "'" +",applyMoney:'" + applyMoney + "'" +",updateTime:'" + updateTime + "'" +",createTime:'" + createTime + "'" +",applyState:'" + applyState + "'" +",applyYearMonth:'" + applyYearMonth + "'" +",paymentFlowId:'" + paymentFlowId + "'" +  "}";
	}
}
