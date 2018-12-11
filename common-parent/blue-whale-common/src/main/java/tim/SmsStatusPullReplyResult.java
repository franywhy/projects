package tim;

import java.util.ArrayList;

public class SmsStatusPullReplyResult {
	private static final String replyFormat=
			"nationcode:%s\t" +
			"mobile:%s\t" +
			"text:%s\t" +
			"sign:%s\t" +
			"time:%d\n";

	private static final String str2=
			"SmsStatusReplyResult:\n" +
					"result:%d\n" +
					"errmsg:%s\n" +
					"count:%d\n" +
					"replys:%s\n";
	int result;
	String errmsg;
	int count;
	ArrayList<Reply> replys;
	
	public String toString() {
			return String.format(str2, result, errmsg, count,replys.toString());
	}

	public class Reply{
		String nationcode;
		String mobile;
		String text;
		String sign;
		long time;
		public String toString(){
			return String.format(replyFormat, nationcode, mobile, text, sign, time);
		}
	}
}

