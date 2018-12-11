package com.school.pojo;

import java.io.Serializable;

public class LiveInfoPOJO implements Serializable  {

	private static final long serialVersionUID = 1436814577593284974L;

	private String k;
	
	private Long uid;
	
	private String nickname;
	
	private String livenum;
	
	private String genseeDomainName;
	
	private String serviceType;

	//直播间是否禁言,0正常,1禁言
    private int banSpeaking;
    //直播间是否禁止问答,0正常,1禁言
    private int banAsking;
    //直播间是否隐藏讨论模块,0正常,1禁言
    private int hideSpeaking;
    //直播间是否隐藏问答模块,0正常,1禁言
    private int hideAsking;

    public int getBanAsking() {
        return banAsking;
    }

    public void setBanAsking(int banAsking) {
        this.banAsking = banAsking;
    }

    public int getHideSpeaking() {
        return hideSpeaking;
    }

    public void setHideSpeaking(int hideSpeaking) {
        this.hideSpeaking = hideSpeaking;
    }

    public int getHideAsking() {
        return hideAsking;
    }

    public void setHideAsking(int hideAsking) {
        this.hideAsking = hideAsking;
    }

    public int getBanSpeaking() {
        return banSpeaking;
    }

    public void setBanSpeaking(int banSpeaking) {
        this.banSpeaking = banSpeaking;
    }

    public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLivenum() {
		return livenum;
	}

	public void setLivenum(String livenum) {
		this.livenum = livenum;
	}

	public String getGenseeDomainName() {
		return genseeDomainName;
	}

	public void setGenseeDomainName(String genseeDomainName) {
		this.genseeDomainName = genseeDomainName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
}
