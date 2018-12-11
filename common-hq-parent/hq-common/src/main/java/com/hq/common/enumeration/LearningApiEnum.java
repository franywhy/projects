package com.hq.common.enumeration;

/**
 * Created by zhaowenwei on 2018/2/25 0009. Description 错误码的使用范围：11000 – 11999 .
 */
public enum LearningApiEnum {

	ADAPTIVELEARNING_NOT_COURSES(11101, "自适应平台不存在课程"), ADAPTIVELEARNING_PARAMETER_ERROR(11102,
			"自适平台应参数错误"), ADAPTIVELEARNING_NOT_MASTERED_KNOWLEDGE_EMPTY(11103, "自适平台未掌握知识点为空");

	/*
	 * OK(200, "操作成功"), LATEST_VERSION(201, "当前客户端最新版本"), NO_CONTENT(204,
	 * "无内容"),
	 * 
	 * BAD_REQUEST(400, "错误的请求"), USER_TOKEN_NOT_FOUND(401, "登录信息失效，请重新登录"),
	 * USER_NOT_FOUND(402, "用户不存在"), PASSWORD_ERROR(403, "密码输入错误"),
	 * INVALID_MOBILE_NUMBER(404,"错误手机号码"), INVALID_PASS_WORD(405, "密码格式非法"),
	 * NOT_FOUND(406, "请求资源未找到"), REQUEST_REJECT(407, "服务被拒绝"),
	 * UPLOAD_FAILED(408,"上传文件失败"), DAMAGE_FILE(409, "文件已经被损坏"),
	 * FILE_TOO_LARGE(410, "文件过大"), FROZEN_USER(411,"用户被冻结"),
	 * DUPLICATE_MOBILE_NUMBER(412,"号码重复注册"),
	 * UNKNOWN_BUSINESS_ID(413,"未知的业务ID"),
	 * 
	 * INTERNAL_SERVER_ERROR(500, "服务器内部错误"), CHECK_VERSION_FAILED(501,
	 * "更新检查失败"), NETWORK_AUTHENTICATION_REQUIRED(511,
	 * "Network Authentication Required");
	 */
	private final int value;
	private final String reasonPhrase;

	private LearningApiEnum(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}

	public int value() {
		return this.value;
	}

	public String getReasonPhrase() {
		return this.reasonPhrase;
	}

	public boolean is1xxInformational() {
		return Series.INFORMATIONAL.equals(this.series());
	}

	public boolean is2xxSuccessful() {
		return Series.SUCCESSFUL.equals(this.series());
	}

	public boolean is3xxRedirection() {
		return Series.REDIRECTION.equals(this.series());
	}

	public boolean is4xxClientError() {
		return Series.CLIENT_ERROR.equals(this.series());
	}

	public boolean is5xxServerError() {
		return Series.SERVER_ERROR.equals(this.series());
	}

	public Series series() {
		return Series.valueOf(this);
	}

	public String toString() {
		return Integer.toString(this.value);
	}

	public static LearningApiEnum valueOf(int statusCode) {
		LearningApiEnum[] var1 = values();
		int var2 = var1.length;
		for (int var3 = 0; var3 < var2; ++var3) {
			LearningApiEnum status = var1[var3];
			if (status.value == statusCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	}

	public static enum Series {
		INFORMATIONAL(1), SUCCESSFUL(2), REDIRECTION(3), CLIENT_ERROR(4), SERVER_ERROR(5);

		private final int value;

		private Series(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}

		public static Series valueOf(int status) {
			int seriesCode = status / 100;
			Series[] var2 = values();
			int var3 = var2.length;
			for (int var4 = 0; var4 < var3; ++var4) {
				Series series = var2[var4];
				if (series.value == seriesCode) {
					return series;
				}
			}
			throw new IllegalArgumentException("No matching constant for [" + status + "]");
		}

		public static Series valueOf(LearningApiEnum status) {
			return valueOf(status.value);
		}
	}
}