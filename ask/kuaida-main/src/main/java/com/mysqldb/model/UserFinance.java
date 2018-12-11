package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: �����-MagicalTools
 * Date: 2015-07-31 10:20:26
 */
@Entity
@Table(name = "user_finance")
@org.hibernate.annotations.Table(appliesTo = "user_finance", comment = "user_finance")
public class UserFinance implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * user_finance
	 */
	public static final String REF="UserFinance";

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
	public static final String PROP_USERMONEY="userMoney";
	

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
	private java.math.BigDecimal userMoney;
	

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
	 * @return userMoney :  ������ܽ��-�ۼ������ܽ�� 
	 */
	@Column(name = "USER_MONEY",columnDefinition = "DECIMAL")
	public java.math.BigDecimal getUserMoney(){
		return this.userMoney;
	}

	/**
	 * Author name@mail.com
	 * ���� ������ܽ��-�ۼ������ܽ�� ������ֵ
	 *		
	 * @param userMoney :  ������ܽ��-�ۼ������ܽ�� 
	 */
	public void setUserMoney(java.math.BigDecimal userMoney){
		this.userMoney	= userMoney;
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ�ַ���
	 */
	public String toString(){		
		return "{ _name=UserFinance" + ",id=" + id +",userId=" + userId +",userMoney=" + userMoney + "}";
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ JSON �ַ���
	 */
	public String toJson(){
		return "{ _name:'UserFinance'" + ",id:'" + id + "'" +",userId:'" + userId + "'" +",userMoney:'" + userMoney + "'" +  "}";
	}
}
