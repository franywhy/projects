package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: �����-MagicalTools
 * Date: 2016-04-20 14:52:03
 */
@Entity
@Table(name = "ib_school")
@org.hibernate.annotations.Table(appliesTo = "ib_school", comment = "ib_school")
public class IbSchool implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ib_school
	 */
	public static final String REF="IbSchool";

	/**
	 * id ��������
	 */
	public static final String PROP_ID="id";
	
	/**
	 * ���� ��������
	 */
	public static final String PROP_NAME="name";
	
	/**
	 * address ��������
	 */
	public static final String PROP_ADDRESS="address";
	
	/**
	 * ���� ��������
	 */
	public static final String PROP_LONGITUDE="longitude";
	
	/**
	 * γ�� ��������
	 */
	public static final String PROP_LATITUDE="latitude";
	
	/**
	 * ƫ��ֵ ��������
	 */
	public static final String PROP_DEVIATION="deviation";
	
	/**
	 * �������� ��������
	 */
	public static final String PROP_ISDEBUG="isDebug";
	
	/**
	 * ���ʱ�� ��������
	 */
	public static final String PROP_ADDTIME="addtime";
	
	/**
	 * �༭ʱ�� ��������
	 */
	public static final String PROP_EDITTIME="edittime";
	
	/**
	 * У��PKֵ ��������
	 */
	public static final String PROP_PKORG="pkOrg";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * ����
	 */
	private java.lang.String name;
	
	/**
	 * address
	 */
	private java.lang.String address;
	
	/**
	 * ����
	 */
	private java.lang.String longitude;
	
	/**
	 * γ��
	 */
	private java.lang.String latitude;
	
	/**
	 * ƫ��ֵ
	 */
	private java.lang.Integer deviation;
	
	/**
	 * ��������
	 */
	private java.lang.Integer isDebug;
	
	/**
	 * ���ʱ��
	 */
	private java.lang.Integer addtime;
	
	/**
	 * �༭ʱ��
	 */
	private java.lang.Integer edittime;
	
	/**
	 * У��PKֵ
	 */
	private java.lang.String pkOrg;
	

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
	 * ��ȡ ���� ������ֵ
     *
	 * @return name :  ���� 
	 */
	@Column(name = "NAME",columnDefinition = "VARCHAR")
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 * Author name@mail.com
	 * ���� ���� ������ֵ
	 *		
	 * @param name :  ���� 
	 */
	public void setName(java.lang.String name){
		this.name	= name;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ address ������ֵ
     *
	 * @return address :  address 
	 */
	@Column(name = "ADDRESS",columnDefinition = "VARCHAR")
	public java.lang.String getAddress(){
		return this.address;
	}

	/**
	 * Author name@mail.com
	 * ���� address ������ֵ
	 *		
	 * @param address :  address 
	 */
	public void setAddress(java.lang.String address){
		this.address	= address;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ���� ������ֵ
     *
	 * @return longitude :  ���� 
	 */
	@Column(name = "LONGITUDE",columnDefinition = "VARCHAR")
	public java.lang.String getLongitude(){
		return this.longitude;
	}

	/**
	 * Author name@mail.com
	 * ���� ���� ������ֵ
	 *		
	 * @param longitude :  ���� 
	 */
	public void setLongitude(java.lang.String longitude){
		this.longitude	= longitude;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ γ�� ������ֵ
     *
	 * @return latitude :  γ�� 
	 */
	@Column(name = "LATITUDE",columnDefinition = "VARCHAR")
	public java.lang.String getLatitude(){
		return this.latitude;
	}

	/**
	 * Author name@mail.com
	 * ���� γ�� ������ֵ
	 *		
	 * @param latitude :  γ�� 
	 */
	public void setLatitude(java.lang.String latitude){
		this.latitude	= latitude;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ƫ��ֵ ������ֵ
     *
	 * @return deviation :  ƫ��ֵ 
	 */
	@Column(name = "DEVIATION",columnDefinition = "INT")
	public java.lang.Integer getDeviation(){
		return this.deviation;
	}

	/**
	 * Author name@mail.com
	 * ���� ƫ��ֵ ������ֵ
	 *		
	 * @param deviation :  ƫ��ֵ 
	 */
	public void setDeviation(java.lang.Integer deviation){
		this.deviation	= deviation;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ �������� ������ֵ
     *
	 * @return isDebug :  �������� 
	 */
	@Column(name = "IS_DEBUG",columnDefinition = "TINYINT")
	public java.lang.Integer getIsDebug(){
		return this.isDebug;
	}

	/**
	 * Author name@mail.com
	 * ���� �������� ������ֵ
	 *		
	 * @param isDebug :  �������� 
	 */
	public void setIsDebug(java.lang.Integer isDebug){
		this.isDebug	= isDebug;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ���ʱ�� ������ֵ
     *
	 * @return addtime :  ���ʱ�� 
	 */
	@Column(name = "ADDTIME",columnDefinition = "INT")
	public java.lang.Integer getAddtime(){
		return this.addtime;
	}

	/**
	 * Author name@mail.com
	 * ���� ���ʱ�� ������ֵ
	 *		
	 * @param addtime :  ���ʱ�� 
	 */
	public void setAddtime(java.lang.Integer addtime){
		this.addtime	= addtime;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ �༭ʱ�� ������ֵ
     *
	 * @return edittime :  �༭ʱ�� 
	 */
	@Column(name = "EDITTIME",columnDefinition = "INT")
	public java.lang.Integer getEdittime(){
		return this.edittime;
	}

	/**
	 * Author name@mail.com
	 * ���� �༭ʱ�� ������ֵ
	 *		
	 * @param edittime :  �༭ʱ�� 
	 */
	public void setEdittime(java.lang.Integer edittime){
		this.edittime	= edittime;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ У��PKֵ ������ֵ
     *
	 * @return pkOrg :  У��PKֵ 
	 */
	@Column(name = "PK_ORG",columnDefinition = "VARCHAR")
	public java.lang.String getPkOrg(){
		return this.pkOrg;
	}

	/**
	 * Author name@mail.com
	 * ���� У��PKֵ ������ֵ
	 *		
	 * @param pkOrg :  У��PKֵ 
	 */
	public void setPkOrg(java.lang.String pkOrg){
		this.pkOrg	= pkOrg;
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ�ַ���
	 */
	public String toString(){		
		return "{ _name=IbSchool" + ",id=" + id +",name=" + name +",address=" + address +",longitude=" + longitude +",latitude=" + latitude +",deviation=" + deviation +",isDebug=" + isDebug +",addtime=" + addtime +",edittime=" + edittime +",pkOrg=" + pkOrg + "}";
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ JSON �ַ���
	 */
	public String toJson(){
		return "{ _name:'IbSchool'" + ",id:'" + id + "'" +",name:'" + name + "'" +",address:'" + address + "'" +",longitude:'" + longitude + "'" +",latitude:'" + latitude + "'" +",deviation:'" + deviation + "'" +",isDebug:'" + isDebug + "'" +",addtime:'" + addtime + "'" +",edittime:'" + edittime + "'" +",pkOrg:'" + pkOrg + "'" +  "}";
	}
}
