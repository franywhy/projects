package tim;

import java.util.ArrayList;

public class SmsStatusPullCallbackResult {
    private static final String str1 = "\t"
            + "user_receive_time:%s\t"
            + "nationcode:%s\t"
            + "mobile:%s\t"
            + "errmsg:%s\t"
            + "report_status:%s\t"
            + "description:%s\t"
            + "sid:%s\t";
    private static final String str2 =
            "SmsStatusPullCallbackResult:\n"
                    + "result:%d\n"
                    + "errmsg:%s\n"
                    + "count:%d\n"
                    + "callbacks:%s\n";
    int result;
    String errmsg;
    int count;
    ArrayList<Callback> callbacks;
    public String toString() {
        return String.format(str2, result, errmsg, count, callbacks.toString());
    }

    class Callback {
        String user_receive_time;
        String nationcode;
        String mobile;
        String report_status;
        String errmsg;
        String description;
        String sid;

        public String toString() {
            return String.format(str1, user_receive_time, nationcode, mobile, report_status, errmsg, description, sid);
        }
    }
}

