package io.renren.mongo.sync.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class SyncUsersEntity {
	private Long _id;
	private String pic;
	private String tuid;
	private String nick_name;
	private Long create_time;
	private Long timestamp;
	private int sex;
	private String nc_id;
	private String mobile;
	private String email;
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getTuid() {
		return tuid;
	}

	public void setTuid(String tuid) {
		this.tuid = tuid;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getNc_id() {
		return nc_id;
	}

	public void setNc_id(String nc_id) {
		this.nc_id = nc_id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "SyncUsersEntity [_id=" + _id + ", pic=" + pic + ", tuid=" + tuid + ", nick_name=" + nick_name + ", create_time=" + create_time
				+ ", timestamp=" + timestamp + ", sex=" + sex + ", nc_id=" + nc_id + ", mobile=" + mobile + ", email=" + email + "]";
	}

}
