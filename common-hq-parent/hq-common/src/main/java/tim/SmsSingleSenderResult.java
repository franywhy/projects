package tim;

public class SmsSingleSenderResult {
/*
{
    "result": 0,
    "errmsg": "OK", 
    "ext": "", 
    "sid": "xxxxxxx", 
    "fee": 1
}
*/
    private static final String str = "SmsSingleSenderResult\nresult %d\nerrMsg %s\next %s\nsid %s\nfee %d";
	public int result;
	public String errMsg = "";
	public String ext = "";
	public String sid = "";
	public int fee;
	
	public String toString() {
		return String.format(str,result, errMsg, ext, sid, fee);
	}
}
