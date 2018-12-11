package io.renren.mongo.sync.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="users")
public class UsersEntity {
	private String tuid;
	private String nick_name;
	
	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getTuid() {
		return tuid;
	}

	public void setTuid(String tuid) {
		this.tuid = tuid;
	}

	@Override
	public String toString() {
		return "UsersEntity [tuid=" + tuid + ", nick_name=" + nick_name + "]";
	}

}
