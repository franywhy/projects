package com.hq.learningcenter.school.web.model;

/**
 * 当前会话的用户信息。
 * @author XingNing OU
 */
public final class CurrentUsers implements java.io.Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = -8425115648438791741L;
	private String cookieId; // CookieId，防止账号重复登录
    private String mobileNo;

    private String nickname; // 用户昵称
    private String pic; // 用户头像
    private String schoolId; // 所属网校ID
    private Long userId; // 用户ID
    private String hostAddress;//pc端物理地址

	public String getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	/**
     * @param cookieId 用户登录时设置的CookieID
     * @param schoolId 所属网校ID
     * @param userId 用户ID
     */
    public CurrentUsers(String cookieId, String schoolId, Long userId) {
        this.cookieId = cookieId;
        this.schoolId = schoolId;
        this.userId = userId;
    }

    /**
     * @param cookieId 用户登录时设置的CookieID
     * @param schoolId 所属网校ID
     * @param userId 用户ID
     * @param nickname 用户昵称
     */
    public CurrentUsers(String cookieId, String schoolId, Long userId, String nickname) {
        this.cookieId = cookieId;
        this.schoolId = schoolId;
        this.userId = userId;
        this.nickname = nickname;
    }
   
    
    
    public String getCookieId() {
        return cookieId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPic() {
		return pic;
	}

    public String getSchoolId() {
        return schoolId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

	public void setPic(String pic) {
		this.pic = pic;
	}

	public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{cookieId=" + cookieId + ", schoolId=" + schoolId + ", userId=" + userId + ", nickname=" + nickname + ", pic=" + pic + "}";
    }
    
}
