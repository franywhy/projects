package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: �����-MagicalTools
 * Date: 2016-04-19 17:55:29
 */
@Entity
@Table(name = "user_wallet")
@org.hibernate.annotations.Table(appliesTo = "user_wallet", comment = "user_wallet")
public class UserWallet implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * user_wallet
	 */
	public static final String REF="UserWallet";

	/**
	 * id ��������
	 */
	public static final String PROP_ID="id";
	
	/**
	 * user_id ��������
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * ������ܽ��-�ۼ������ܽ�� ��������
	 */
	public static final String PROP_USERBALANCE="userBalance";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * user_id
	 */
	private java.lang.Integer userId;
	
	/**
	 * ������ܽ��-�ۼ������ܽ��
	 */
	private java.math.BigDecimal userBalance;
	

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
	 * ��ȡ ������ܽ��-�ۼ������ܽ�� ������ֵ
     *
	 * @return userBalance :  ������ܽ��-�ۼ������ܽ�� 
	 */
	@Column(name = "USER_BALANCE",columnDefinition = "DECIMAL")
	public java.math.BigDecimal getUserBalance(){
		return this.userBalance;
	}

	/**
	 * Author name@mail.com
	 * ���� ������ܽ��-�ۼ������ܽ�� ������ֵ
	 *		
	 * @param userBalance :  ������ܽ��-�ۼ������ܽ�� 
	 */
	public void setUserBalance(java.math.BigDecimal userBalance){
		this.userBalance	= userBalance;
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ�ַ���
	 */
	public String toString(){		
		return "{ _name=UserWallet" + ",id=" + id +",userId=" + userId +",userBalance=" + userBalance + "}";
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ JSON �ַ���
	 */
	public String toJson(){
		return "{ _name:'UserWallet'" + ",id:'" + id + "'" +",userId:'" + userId + "'" +",userBalance:'" + userBalance + "'" +  "}";
	}
}
