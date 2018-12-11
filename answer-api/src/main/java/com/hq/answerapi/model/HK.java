package com.hq.answerapi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * KEY值 例如:redis的key
 */
public abstract class HK {
	
	/** 登录 */
	public static class LOGIN {

		public static final String TOKEN = "token:";

		public static final String PRIV = "priv";
		public static final String TUID = "tuid";
		public static final String ID = "_id";
		public static final String NICK_NAME = "nick_name";
		public static final String STATUS = "status";
		public static final String TAG = "tag";
		public static final String PIC = "pic";
		public static final String NC_ID = "nc_id";
		public static final String SCHOOL_CODE = "school_code";
		public static final String PRIV0 = "priv0";
		public static final String PRIV1 = "priv1";
		public static final String PRIV2 = "priv2";
		public static final String REAL_NAME_FROM_NC = "real_name_from_nc";

		public static String hash(String access_token) {
			return TOKEN + access_token;
		}

	}
	/** 验证码 */
	public static class SECURITY {
		/** 短信验证码题头 */
		private static final String MESSAGE_KEY = "message:";

		/** 短信验证码存放时间 */
		private static final String SECURITY_KEY = "security:";

		/** 短信验证码次数 */
		private static final String TIMES_KEY = "times:";

		/** 验证码存放时间 */
		public static final long SECURITY_KEY_TIMEOUT = 600000; // 10*60*1000
		/** 验证码发送间隔时间 */
		public static final long SECURITY_TIMEOUT = 20000; // 3*60*1000
		/** 验证码发送间隔时间 */
		public static final long SECURITY_MAX_TIMES_TIMEOUT = 24; // 小时
		/** 一个手机号每天最大发送验证码次数 */
		public static final int SECURITY_MAX_TIMES = 8; // 学员反馈过多，开放到8次

		public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

		public static String getSecurityKey(String key) {
			return MESSAGE_KEY + SECURITY_KEY + key;
		}

		public static String getSecurityTimeout(String key) {
			return MESSAGE_KEY + SECURITY_KEY + key + ":time";
		}

		/**
		 * 手机发送验证码次数KEY
		 * 
		 * @Description: 手机发送验证码次数KEY
		 * @date 2015年11月4日 下午5:18:55
		 * @param @param mobile
		 */
		public static String getMessageTimeKey(String mobile) {
			return MESSAGE_KEY + TIMES_KEY + SDF.format(new Date()) + ":" + mobile;
		}

		public static String randomCode() {
			return new java.text.DecimalFormat("0").format(Math.random() * 9000 + 1000);
		}
	}

}
