package tim;

import java.util.ArrayList;

public class SmsMultiSenderResult {

/***
 *
	{
       "result": 0,
			"errmsg": "OK",
			"ext": "",
			"detail": [
		    {
			"result": 0,
				"errmsg": "OK",
				"mobile": "13788888888",
				"nationcode": "86",
				"sid": "xxxxxxx",
				"fee": 1
		    },
		    {
			"result": 0,
				"errmsg": "OK",
				"mobile": "13788888889",
				"nationcode": "86",
				"sid": "xxxxxxx",
				"fee": 1
             }
             ]
	}
 *
 * */
    private static final String detailFormat="Detail result %d\nerrMsg %s\nphoneNumber %s\nnationCode %s\nsid %s\nfee %d";
	private static final String resultFormat="result %d\nerrMsg %s\nphoneNumber %s\nnationCode %s";
	private static final String smsMultiSenderResultFormat="SmsMultiSenderResult\nresult %d\nerrMsg %s\next %s\ndetails %s";
	public int result;
	public String errMsg = "";
	public String ext = "";
	public ArrayList<Detail> details;

	public String toString() {
		return String.format(smsMultiSenderResultFormat, result, errMsg, ext, details);
	}

	public class Detail {
		public int result;
		public String errMsg = "";
		public String phoneNumber = "";
		public String nationCode = "";
		public String sid = "";
		public int fee;

		public String toString() {
			if (0 == result) {
				return String.format(detailFormat,result, errMsg, phoneNumber, nationCode, sid, fee);
			} else {
				return String.format(resultFormat, result, errMsg, phoneNumber, nationCode);
			}
		}
	}
}
