package io.renren.mongo.sync.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "qQUser")
public class SyncQqUserEntity {
	private String username;
	private String tuid;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTuid() {
		return tuid;
	}

	public void setTuid(String tuid) {
		this.tuid = tuid;
	}

	@Override
	public String toString() {
		return "SyncQqUserEntity [username=" + username + ", tuid=" + tuid + "]";
	}

}
