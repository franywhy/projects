package com.school.accountant.model;

/**
 * 当前会话的用户信息。
 */
public final class CurrentUser implements java.io.Serializable {

	private static final long serialVersionUID = 2793258035301016631L;
	
	private String cookieId; // CookieId，防止账号重复登录
    /*private String schoolId; // 所属网校ID*/
    private String userId; // 用户ID
    private String nickname; // 用户昵称
    private String avatar; // 用户头像

    private boolean isVip; // 是为VIP会员
    private boolean isStudent; // 是否为学员

    private boolean teacher; // 是否为老师
    private boolean administrator; // 是否为管理员

    /**
     * @param cookieId 用户登录时设置的CookieID
     * @param schoolId 所属网校ID
     * @param userId 用户ID
     */
    public CurrentUser(String cookieId, String userId) {
        this.cookieId = cookieId;
        this.userId = userId;
    }

    /**
     * @param cookieId 用户登录时设置的CookieID
     * @param schoolId 所属网校ID
     * @param userId 用户ID
     * @param nickname 用户昵称
     */
    public CurrentUser(String cookieId, String userId, String nickname) {
        this.cookieId = cookieId;
        this.userId = userId;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "{cookieId=" + cookieId + ", userId=" + userId + ", nickname=" + nickname
                + ", avatar=" + avatar + ", isVip=" + isVip + ", isStudent=" + isStudent + ", isTeacher=" + teacher
                + ", isAdministrator=" + administrator + "}";
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean isVip) {
        this.isVip = isVip;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean isTeacher) {
        this.teacher = isTeacher;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean isAdministrator) {
        this.administrator = isAdministrator;
    }
}
