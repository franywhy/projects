package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2016-03-31 15:44:05
 */
@Entity
@Table(name = "score_detail")
@org.hibernate.annotations.Table(appliesTo = "score_detail", comment = "score_detail")
public class ScoreDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * score_detail
	 */
	public static final String REF="ScoreDetail";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * score 的属性名
	 */
	public static final String PROP_SCORE="score";
	
	/**
	 * score_detail 的属性名
	 */
	public static final String PROP_SCOREDETAIL="scoreDetail";
	
	/**
	 * score_gain_type 的属性名
	 */
	public static final String PROP_SCOREGAINTYPE="scoreGainType";
	
	/**
	 * score_type 的属性名
	 */
	public static final String PROP_SCORETYPE="scoreType";
	
	/**
	 * user_id 的属性名
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * user_nickname 的属性名
	 */
	public static final String PROP_USERNICKNAME="userNickname";
	
	/**
	 * create_time 的属性名
	 */
	public static final String PROP_CREATETIME="createTime";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * score
	 */
	private java.math.BigDecimal score;
	
	/**
	 * score_detail
	 */
	private java.lang.String scoreDetail;
	
	/**
	 * score_gain_type
	 */
	private java.lang.Integer scoreGainType;
	
	/**
	 * score_type
	 */
	private java.lang.Integer scoreType;
	
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
	 * 获取 id 的属性值
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
	 * 设置 id 的属性值
	 *		
	 * @param id :  id 
	 */
	public void setId(java.lang.Integer id){
		this.id	= id;
	}
	/**
	 * Author name@mail.com
	 * 获取 score 的属性值
     *
	 * @return score :  score 
	 */
	@Column(name = "SCORE",columnDefinition = "FLOAT")
	public java.math.BigDecimal getScore(){
		return this.score;
	}

	/**
	 * Author name@mail.com
	 * 设置 score 的属性值
	 *		
	 * @param score :  score 
	 */
	public void setScore(java.math.BigDecimal score){
		this.score	= score;
	}
	/**
	 * Author name@mail.com
	 * 获取 score_detail 的属性值
     *
	 * @return scoreDetail :  score_detail 
	 */
	@Column(name = "SCORE_DETAIL",columnDefinition = "VARCHAR")
	public java.lang.String getScoreDetail(){
		return this.scoreDetail;
	}

	/**
	 * Author name@mail.com
	 * 设置 score_detail 的属性值
	 *		
	 * @param scoreDetail :  score_detail 
	 */
	public void setScoreDetail(java.lang.String scoreDetail){
		this.scoreDetail	= scoreDetail;
	}
	/**
	 * Author name@mail.com
	 * 获取 score_gain_type 的属性值
     *
	 * @return scoreGainType :  score_gain_type 
	 */
	@Column(name = "SCORE_GAIN_TYPE",columnDefinition = "INT")
	public java.lang.Integer getScoreGainType(){
		return this.scoreGainType;
	}

	/**
	 * Author name@mail.com
	 * 设置 score_gain_type 的属性值
	 *		
	 * @param scoreGainType :  score_gain_type 
	 */
	public void setScoreGainType(java.lang.Integer scoreGainType){
		this.scoreGainType	= scoreGainType;
	}
	/**
	 * Author name@mail.com
	 * 获取 score_type 的属性值
     *
	 * @return scoreType :  score_type 
	 */
	@Column(name = "SCORE_TYPE",columnDefinition = "INT")
	public java.lang.Integer getScoreType(){
		return this.scoreType;
	}

	/**
	 * Author name@mail.com
	 * 设置 score_type 的属性值
	 *		
	 * @param scoreType :  score_type 
	 */
	public void setScoreType(java.lang.Integer scoreType){
		this.scoreType	= scoreType;
	}
	/**
	 * Author name@mail.com
	 * 获取 user_id 的属性值
     *
	 * @return userId :  user_id 
	 */
	@Column(name = "USER_ID",columnDefinition = "INT")
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 * Author name@mail.com
	 * 设置 user_id 的属性值
	 *		
	 * @param userId :  user_id 
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId	= userId;
	}
	/**
	 * Author name@mail.com
	 * 获取 user_nickname 的属性值
     *
	 * @return userNickname :  user_nickname 
	 */
	@Column(name = "USER_NICKNAME",columnDefinition = "VARCHAR")
	public java.lang.String getUserNickname(){
		return this.userNickname;
	}

	/**
	 * Author name@mail.com
	 * 设置 user_nickname 的属性值
	 *		
	 * @param userNickname :  user_nickname 
	 */
	public void setUserNickname(java.lang.String userNickname){
		this.userNickname	= userNickname;
	}
	/**
	 * Author name@mail.com
	 * 获取 create_time 的属性值
     *
	 * @return createTime :  create_time 
	 */
	@Column(name = "CREATE_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getCreateTime(){
		return this.createTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 create_time 的属性值
	 *		
	 * @param createTime :  create_time 
	 */
	public void setCreateTime(java.sql.Timestamp createTime){
		this.createTime	= createTime;
	}
	
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=ScoreDetail" + ",id=" + id +",score=" + score +",scoreDetail=" + scoreDetail +",scoreGainType=" + scoreGainType +",scoreType=" + scoreType +",userId=" + userId +",userNickname=" + userNickname +",createTime=" + createTime + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'ScoreDetail'" + ",id:'" + id + "'" +",score:'" + score + "'" +",scoreDetail:'" + scoreDetail + "'" +",scoreGainType:'" + scoreGainType + "'" +",scoreType:'" + scoreType + "'" +",userId:'" + userId + "'" +",userNickname:'" + userNickname + "'" +",createTime:'" + createTime + "'" +  "}";
	}
}
