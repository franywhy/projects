package io.renren.mongo.sync.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 教师
 * @class io.renren.mongo.sync.entity.TeacherEntity.java
 * @Description:
 * @author shihongjie
 * @dete 2017年6月10日
 */
@Document(collection="teachers")
public class TeacherEntity extends BaseMongodbEntity<String>{
	
//    "_id" : "14dd4098-6408-4184-89b3-fb751ed27180",
//    "nick_name" : "波哥",
//    "pic" : "",
//    "name" : "孙波",
//    "sex" : 1,
//    "telephone" : "13802409623",
//    "email" : "13798156641@126.com",
//    "id_code" : "372922198103063098",
//    "dr" : 0,
//    "remark" : "考霸教练",
//    "create_user_id" : 12781,
//    "create_time" : NumberLong(1470276309659)
	//昵称
	private String nick_name;
	//头像
	private String pic;
	//真是名称
	private String name;
	//性别
	private String sex;
	//手机号码
	private String telephone;
	//邮箱
	private String email;
	//
	private String id_code;
	//dr
	private String dr;
	//备注
	private String remark;
	
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId_code() {
		return id_code;
	}
	public void setId_code(String id_code) {
		this.id_code = id_code;
	}
	public String getDr() {
		return dr;
	}
	public void setDr(String dr) {
		this.dr = dr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "TeacherEntity [nick_name=" + nick_name + ", pic=" + pic + ", name=" + name + ", sex=" + sex + ", telephone=" + telephone + ", email="
				+ email + ", id_code=" + id_code + ", dr=" + dr + ", remark=" + remark + "]";
	}
	
	
	
	
	
	
}
