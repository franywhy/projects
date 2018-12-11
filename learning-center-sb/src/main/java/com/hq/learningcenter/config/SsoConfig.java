package com.hq.learningcenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by LiuHai on 2018/01/12
 */
@ConfigurationProperties(prefix="sso-info")
@Component
public class SsoConfig {

	private String ssoUserinfoUrl;
	
	private String ssoUsertokendetailUrl;
	
	private String ssoUsermobilenoUrl;

	private String ssoGetbussinessidUrl;

	private String ssoNcidUrl;

	public String getSsoUserinfoUrl() {
		return ssoUserinfoUrl;
	}

	public void setSsoUserinfoUrl(String ssoUserinfoUrl) {
		this.ssoUserinfoUrl = ssoUserinfoUrl;
	}

	public String getSsoUsertokendetailUrl() {
		return ssoUsertokendetailUrl;
	}

	public void setSsoUsertokendetailUrl(String ssoUsertokendetailUrl) {
		this.ssoUsertokendetailUrl = ssoUsertokendetailUrl;
	}

	public String getSsoUsermobilenoUrl() {
		return ssoUsermobilenoUrl;
	}

	public void setSsoUsermobilenoUrl(String ssoUsermobilenoUrl) {
		this.ssoUsermobilenoUrl = ssoUsermobilenoUrl;
	}

	public String getSsoGetbussinessidUrl() {
		return ssoGetbussinessidUrl;
	}

	public void setSsoGetbussinessidUrl(String ssoGetbussinessidUrl) {
		this.ssoGetbussinessidUrl = ssoGetbussinessidUrl;
	}

	public String getSsoNcidUrl() {
		return ssoNcidUrl;
	}

	public void setSsoNcidUrl(String ssoNcidUrl) {
		this.ssoNcidUrl = ssoNcidUrl;
	}
}
