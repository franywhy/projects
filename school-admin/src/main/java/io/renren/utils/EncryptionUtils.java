package io.renren.utils;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密工具类。
 * @author XingNing OU
 */
public class EncryptionUtils {

    /**
     * 对字符串做MD5加密，返回加密后的字符串。
     * @param text 待加密的字符串。
     * @return 加密后的字符串。
     */
    public static String md5Hex(String text) {
        return DigestUtils.md5Hex(text);
    }

    /**
     * 对字符串做SHA-1加密，返回加密后的字符串。
     * @param text 待加密的字符串。
     * @return 加密后的字符串。
     */
    public static String shaHex(String text) {
        return DigestUtils.sha1Hex(text);
    }

    /**
     * 对字符串做SHA-1加密，然后截取前面20个字符（遗留OVP系统的密码验证方式）。
     * @param text 待加密的字符串。
     * @return 加密后的前20个字符。
     */
    public static String getLittleSHA1(String text) {
        String encryptedStr = DigestUtils.sha1Hex(text).toUpperCase();
        return encryptedStr.substring(0, 20);
    }

    private static String userid = "a647f95e6e";
    private static String secretkey = "y9qdB0dV0I";
    private static String readtoken = "97c16110-01ce-41d2-a0d0-20708eaf50d6";
    private static String writetoken = "cb33fd26-1137-44dd-9214-6ef04e337f14";
    public static void main(String[] args) {
//    	demoList1();
//    	demoList2();
    	demoList3();
    }
    
    public static void demoList1(){
    	Long ptime = new Date().getTime();
    	String param1 = "ptime="+ptime+"&userid="+userid+secretkey;
    	
    	String sign = EncryptionUtils.shaHex(param1).toUpperCase();
        String url = "http://api.polyv.net/v2/video/"+userid+"/cataJson";
        String param2 = "ptime=" + ptime +"&sign="+sign;
        String result = HttpUtils.sendGet(url, param2);
        System.out.println(url);
        System.out.println(param1);
        System.out.println(param2);
        System.out.println(result);
    }
    public static void demoList3(){
    	Long ptime = new Date().getTime();
    	String param1 = "catatree="+"1,1494387689257,1495706226647"+"&numPerPage="+10+"&pageNum="+1+"&ptime="+ptime;
    	
    	String sign = EncryptionUtils.shaHex(param1+secretkey).toUpperCase();
    	String url = "http://api.polyv.net/v2/video/"+userid+"/get-new-list";
    	String param2 = param1+"&sign="+sign;
//    	String param2 = "readtoken=" + readtoken + "&pageNum=1&numPerPage=10&catatree=1,1494387689257,1495706226647&format=json";
//    	String param2 = "readtoken=" + readtoken +"&cataid=1500348502271";
    	String result = HttpUtils.sendGet(url, param2);
    	System.out.println(url);
    	System.out.println(param1);
    	System.out.println(param2);
    	System.out.println(result);
    }
}
