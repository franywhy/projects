package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: �����-MagicalTools
 * Date: 2016-04-19 17:55:33
 */
@Entity
@Table(name = "user_wallet_detail")
@org.hibernate.annotations.Table(appliesTo = "user_wallet_detail", comment = "user_wallet_detail")
public class UserWalletDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * user_wallet_detail
	 */
	public static final String REF="UserWalletDetail";

	/**
	 * id ��������
	 */
	public static final String PROP_ID="id";
	
	/**
	 * ��� ��������
	 */
	public static final String PROP_MMONEY="mmoney";
	
	/**
	 * ��ȡ����ԭ�� ��������
	 */
	public static final String PROP_MMONEYDETAIL="mmoneyDetail";
	
	/**
	 * ��ȡ�������ͣ��ο�javaö�� ��������
	 */
	public static final String PROP_MMONEYTYPE="mmoneyType";
	
	/**
	 * user_id ��������
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * user_nickname ��������
	 */
	public static final String PROP_USERNICKNAME="userNickname";
	
	/**
	 * create_time ��������
	 */
	public static final String PROP_CREATETIME="createTime";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * ���
	 */
	private java.math.BigDecimal mmoney;
	
	/**
	 * ��ȡ����ԭ��
	 */
	private java.lang.String mmoneyDetail;
	
	/**
	 * ��ȡ�������ͣ��ο�javaö��
	 */
	private java.lang.Integer mmoneyType;
	
	/**
	 * user_id
	 */
	private java.lang.Integer userId;
	
	/**
	 * user_nickname
	 */
	private java.lang.String userNickname;
	
	/**
	 * create_time
	 */
	private java.sql.Timestamp createTime;
	

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
	 * ��ȡ ��� ������ֵ
     *
	 * @return mmoney :  ��� 
	 */
	@Column(name = "MMONEY",columnDefinition = "DECIMAL")
	public java.math.BigDecimal getMmoney(){
		return this.mmoney;
	}

	/**
	 * Author name@mail.com
	 * ���� ��� ������ֵ
	 *		
	 * @param mmoney :  ��� 
	 */
	public void setMmoney(java.math.BigDecimal mmoney){
		this.mmoney	= mmoney;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ��ȡ����ԭ�� ������ֵ
     *
	 * @return mmoneyDetail :  ��ȡ����ԭ�� 
	 */
	@Column(name = "MMONEY_DETAIL",columnDefinition = "VARCHAR")
	public java.lang.String getMmoneyDetail(){
		return this.mmoneyDetail;
	}

	/**
	 * Author name@mail.com
	 * ���� ��ȡ����ԭ�� ������ֵ
	 *		
	 * @param mmoneyDetail :  ��ȡ����ԭ�� 
	 */
	public void setMmoneyDetail(java.lang.String mmoneyDetail){
		this.mmoneyDetail	= mmoneyDetail;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ��ȡ�������ͣ��ο�javaö�� ������ֵ
     *
	 * @return mmoneyType :  ��ȡ�������ͣ��ο�javaö�� 
	 */
	@Column(name = "MMONEY_TYPE",columnDefinition = "INT")
	public java.lang.Integer getMmoneyType(){
		return this.mmoneyType;
	}

	/**
	 * Author name@mail.com
	 * ���� ��ȡ�������ͣ��ο�javaö�� ������ֵ
	 *		
	 * @param mmoneyType :  ��ȡ�������ͣ��ο�javaö�� 
	 */
	public void setMmoneyType(java.lang.Integer mmoneyType){
		this.mmoneyType	= mmoneyType;
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
	 * ��ȡ user_nickname ������ֵ
     *
	 * @return userNickname :  user_nickname 
	 */
	@Column(name = "USER_NICKNAME",columnDefinition = "VARCHAR")
	public java.lang.String getUserNickname(){
		return this.userNickname;
	}

	/**
	 * Author name@mail.com
	 * ���� user_nickname ������ֵ
	 *		
	 * @param userNickname :  user_nickname 
	 */
	public void setUserNickname(java.lang.String userNickname){
		this.userNickname	= userNickname;
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
	 * ת��Ϊ�ַ���
	 */
	public String toString(){		
		return "{ _name=UserWalletDetail" + ",id=" + id +",mmoney=" + mmoney +",mmoneyDetail=" + mmoneyDetail +",mmoneyType=" + mmoneyType +",userId=" + userId +",userNickname=" + userNickname +",createTime=" + createTime + "}";
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ JSON �ַ���
	 */
	public String toJson(){
		return "{ _name:'UserWalletDetail'" + ",id:'" + id + "'" +",mmoney:'" + mmoney + "'" +",mmoneyDetail:'" + mmoneyDetail + "'" +",mmoneyType:'" + mmoneyType + "'" +",userId:'" + userId + "'" +",userNickname:'" + userNickname + "'" +",createTime:'" + createTime + "'" +  "}";
	}
}
