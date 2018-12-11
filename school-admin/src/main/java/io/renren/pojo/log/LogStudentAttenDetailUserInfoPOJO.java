package io.renren.pojo.log;

public class LogStudentAttenDetailUserInfoPOJO {

	private Long userId;
	private Long classtypeId;
	private String userName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getClasstypeId() {
		return classtypeId;
	}

	public void setClasstypeId(Long classtypeId) {
		this.classtypeId = classtypeId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "LogStudentAttenDetailUserInfoPOJO [userId=" + userId + ", classtypeId=" + classtypeId + ", userName=" + userName + "]";
	}

}
