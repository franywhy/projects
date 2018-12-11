package io.renren.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/9/7 0007.
 */
public class CommonUtil {

    private static String generateIv() {
        String uuid = UUID.randomUUID().toString();
        try {
            uuid = URLEncoder.encode(uuid, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String iv = uuid.substring(0, 16);
        return iv;
    }

    public static String encrypt(String strTest, String pwd) {
        String YOUSHANG_API_URL = "http://agent.youshang.com/federation/commonservice/servicePortal.do?shortName=shhq";
        String iv = generateIv();
        String strEnXml = "";
        try {
            strEnXml = CryptographUtil.AESEncrypt(strTest,iv,pwd);
            strEnXml = URLEncoder.encode(strEnXml,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String URL = YOUSHANG_API_URL + "&iv=" + iv + "&cipherCode=" + strEnXml;
        return URL;
    }

    public static void tryProduct(String userName, String pwd) {
        String strTest = getTryProductXml(userName);
        String retString = HttpUtils.httpRequest(encrypt(strTest, pwd));
    }

    private static String getTryProductXml(String username) {
        StringBuilder sbXml = new StringBuilder(1000);
        sbXml.append("<?xml version=\"1.0\" encoding = \"utf-8\"?>");
        sbXml.append("<request>");
        sbXml.append("<type>tryProductNew</type>");
        sbXml.append("<data>");
        sbXml.append("<attr name=\"userAccount\">" + username + "</attr>");
        sbXml.append("<attr name=\"freeTryProduct\">SERVICE_TYPY_SCMS,SERVICE_TYPE_ACCCPLUS</attr>");
        sbXml.append("<attr name=\"productName\">在线进销存3.0（标准版-试用版）,在线会计3.0企业版Plus</attr>");
        sbXml.append("</data>");
        sbXml.append("</request>");
        return sbXml.toString();
    }

    public static void userSync(String userName, int sex, String realName,String pwd) {
        String strTest = getXml(userName, sex, realName);
        String retString = HttpUtils.httpRequest(encrypt(strTest, pwd));
    }

    private static String getXml(String username, int sex, String realName) {
        StringBuilder sbXml = new StringBuilder(1000);
        sbXml.append("<?xml version=\"1.0\" encoding = \"utf-8\"?>");
        sbXml.append("<request>");
        sbXml.append("<type>userSync</type>");
        sbXml.append("<data>");
        sbXml.append("<attr name=\"userAccount\">" + username + "</attr>");
        sbXml.append("<attr name=\"mobile\">" + username + "</attr>");
        sbXml.append("<attr name=\"phone\">" + username + "</attr>");
        sbXml.append("<attr name=\"gender\">" + sex + "</attr>");//性别，男1，女0
        sbXml.append("<attr name=\"bindTo\">恒企</attr>");
        sbXml.append("<attr name=\"bindAsAdmin\">false</attr>");
        sbXml.append("<attr name=\"realName\">" + realName + "</attr>");
        sbXml.append("</data>");
        sbXml.append("</request>");
        return sbXml.toString();
    }

    public static List<String> getList() {
        List<String> stringList = Arrays.asList("1001A51000000004EZ44", "1001A5100000000B73MA", "1001A5100000000CPOHW", "1001A5100000000GF5Z7", "1001A5100000000LXYQ6", "1001A51000000000RKZB", "1001A51000000000W8S1", "1001A51000000000W8TB", "1001A51000000000W8TT", "1001A51000000000W8TW", "1001A51000000000W8WL", "1001A5100000000C73XM", "1001A5100000000C73ZF", "1001A5100000000BZ5MK", "1001A5100000000LXYP1", "1001A5100000000LZU0C", "1001A51000000005ZW4N", "1001A5100000000CPOGJ", "1001A5100000000CPOGL", "1001A5100000000CPOGR", "1001A5100000000CPZ5B", "1001A5100000000CPZ5C", "1001A5100000000CPWEI", "1001A51000000005ZW18", "1001A51000000005ZWZJ", "1001A51000000009N72F", "1001A51000000009N72K", "1001A5100000000LXYOF", "1001A5100000000QU5RR", "1001A5100000000QU5RS", "1001A5100000000QU5RT", "1001A5100000000WVZ0D", "1001A5100000001F9MCK", "1001A5100000001FEDAI", "1001A5100000001GCWIB", "1001A5100000001H5DAA", "1001A5100000001I8QGS", "1001A510000000084EEI", "1001A5100000000TCCX2", "1001A5100000001F6EPQ", "1001A5100000001F9BAN", "1001A5100000001J1CYK", "1001A5100000001J1D0H", "1001A51000000009N71J", "1001A5100000000OXUIX", "1001A5100000000WVZ08", "1001A5100000001GCWK1", "1001A5100000001HZQUR", "1001A5100000001J1D0V", "1001A5100000001J1D0Y", "1001A510000000084C02", "1001A5100000000WVZ0E", "1001A5100000000WVZ0I", "1001A5100000000WVZ0J", "1001A5100000000WVZ0S", "1001A5100000000CPWK3", "1001A71000000000L19G", "1001A71000000000L1A0", "1001A71000000000L1AI", "1001A71000000000L1AS", "1001A71000000000L1CO", "1001A71000000000L1DU", "1001A71000000000L1E3", "1001A71000000000L1E6", "1001A71000000000L1E8", "1001A71000000000L1ED", "1001A71000000000L1F0", "1001A51000000003A57C", "1001A5100000000LXYPJ", "1001A5100000000LXYPM", "1001A5100000000LZTZN", "1001A5100000000MPBUT", "1001A5100000000MPBUV", "1001A71000000000CA5H", "1001A71000000000CA53", "1001A71000000000CA6E", "1001A71000000000H9HX", "1001A71000000000H1GV", "1001A71000000000H1GW", "1001A71000000000C11B", "1001A71000000000C11G", "1001A5100000000BXMX6", "1001A5100000001RQ26L", "1001A5100000001QTCDT", "1001A5100000001PFO2Q", "1001A5100000001Q0066", "1001A5100000001QTCEK", "1001A5100000001QTCDJ", "1001A5100000001PZZFJ", "1001A5100000001QSATR", "1001A5100000001QX8PX", "1001A5100000001WQFAD", "1001A5100000002W7GCW", "1001A5100000002NATAU", "1001A5100000002NATB5", "1001A5100000002WOEBY", "1001A5100000002TPR1Q", "1001A5100000001TLPKI", "1001A5100000002SB34E", "1001A5100000002H9FJO", "1001A51000000032X6DZ", "1001A5100000002IT3NA", "1001A510000000274M4N", "1001A5100000002N2C04", "1001A5100000002R8XQO", "1001A5100000002RP591", "1001A5100000002JMMW7", "1001A5100000002N2MZK", "1001A5100000002N2FCN", "1001A5100000002UYOUD", "1001A5100000002NATB9", "1001A5100000002L2VY1", "1001A5100000002K7RDW", "1001A5100000002JMN0I", "1001A5100000002DIYKA", "1001A5100000002K753D", "1001A5100000002VBG4R", "1001A5100000002JMN5J", "1001A5100000002JMN98", "1001A5100000002VATU5", "1001A5100000002VBG9B", "1001A510000000301HPM", "1001A5100000002VATKU", "1001A510000000301HMY", "1001A5100000002NB0ZH", "1001A5100000002NB0ZQ", "1001A5100000002VATFP", "1001A5100000002QMWDK", "1001A5100000002VATWG", "1001A5100000002NATBC", "1001A5100000002NATBP", "1001A5100000002NATC0", "1001A5100000002UYFEY", "1001A5100000002VA4HP", "1001A5100000002VA4DL", "1001A5100000002VEK6B", "1001A5100000002VA4EQ" );
        return stringList;
    }
}
