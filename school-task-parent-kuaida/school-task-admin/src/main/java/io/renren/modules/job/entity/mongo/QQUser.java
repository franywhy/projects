package io.renren.modules.job.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * @author hq
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "qQUser")
public class QQUser {

	@Id
	private BigInteger id;

	private String username;
	
	private String nickName;
	
	private String tuid;
	
	private String password;
	
	private String qd;
	
	private String pk;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTuid() {
		return tuid;
	}

	public void setTuid(String tuid) {
		this.tuid = tuid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQd() {
		return qd;
	}

	public void setQd(String qd) {
		this.qd = qd;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}
}
