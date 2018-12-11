package io.renren.mongo.sync.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "qQUser")
public class QuserEntity {
	@Override
	public String toString() {
		return "QuserEntity [username=" + username + "]";
	}

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
