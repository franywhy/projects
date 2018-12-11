package com.hq.learningcenter.kuaiji.pojo;

public class ZegoRequestPOJO {
	
	private Integer version;
	private Integer seq;
	private Long appid;
	private String token;
	private String sign;
	private Long timestamp;
	private String device_id;
	private Integer local_version;
	private Integer platform;
	private String client_ip;
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public Long getAppid() {
		return appid;
	}
	public void setAppid(Long appid) {
		this.appid = appid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public Integer getLocal_version() {
		return local_version;
	}
	public void setLocal_version(Integer local_version) {
		this.local_version = local_version;
	}
	public Integer getPlatform() {
		return platform;
	}
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}
	public String getClient_ip() {
		return client_ip;
	}
	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	
}
