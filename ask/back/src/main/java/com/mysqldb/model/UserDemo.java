package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: �����-MagicalTools
 * Date: 2016-01-22 15:01:04
 */
@Entity
@Table(name = "user_demo")
@org.hibernate.annotations.Table(appliesTo = "user_demo", comment = "demo��")
public class UserDemo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * demo��
	 */
	public static final String REF="UserDemo";

	/**
	 * id ��������
	 */
	public static final String PROP_ID="id";
	
	/**
	 * �ǳ� ��������
	 */
	public static final String PROP_USERNAME="userName";
	
	/**
	 * ���� ��������
	 */
	public static final String PROP_UTIMESTAMP="utimestamp";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * �ǳ�
	 */
	private java.lang.String userName;
	
	/**
	 * ����
	 */
	private java.sql.Timestamp utimestamp;
	

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
	 * ��ȡ �ǳ� ������ֵ
     *
	 * @return userName :  �ǳ� 
	 */
	@Column(name = "USER_NAME",columnDefinition = "VARCHAR")
	public java.lang.String getUserName(){
		return this.userName;
	}

	/**
	 * Author name@mail.com
	 * ���� �ǳ� ������ֵ
	 *		
	 * @param userName :  �ǳ� 
	 */
	public void setUserName(java.lang.String userName){
		this.userName	= userName;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ���� ������ֵ
     *
	 * @return utimestamp :  ���� 
	 */
	@Column(name = "UTIMESTAMP",columnDefinition = "DATETIME")
	public java.sql.Timestamp getUtimestamp(){
		return this.utimestamp;
	}

	/**
	 * Author name@mail.com
	 * ���� ���� ������ֵ
	 *		
	 * @param utimestamp :  ���� 
	 */
	public void setUtimestamp(java.sql.Timestamp utimestamp){
		this.utimestamp	= utimestamp;
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ�ַ���
	 */
	public String toString(){		
		return "{ _name=UserDemo" + ",id=" + id +",userName=" + userName +",utimestamp=" + utimestamp + "}";
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ JSON �ַ���
	 */
	public String toJson(){
		return "{ _name:'UserDemo'" + ",id:'" + id + "'" +",userName:'" + userName + "'" +",utimestamp:'" + utimestamp + "'" +  "}";
	}
}
