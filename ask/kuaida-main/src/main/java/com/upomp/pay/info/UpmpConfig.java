package com.upomp.pay.info;

/**
 * @author: wubinjie@ak.cc
 * Date: 14-1-15 上午11:29
 */
public class UpmpConfig {

    // 版本号
    public static final String VERSION = "1.0.0";

    // 编码方式
    public static final String CHARSET = "UTF-8";

    // 交易网址
    //public static String TRADE_URL = "http://222.66.233.198:8080/gateway/merchant/trade";
    public static String TRADE_URL = "https://mgate.unionpay.com/gateway/merchant/trade";

    // 查询网址
    //public static String QUERY_URL = "http://222.66.233.198:8080/gateway/merchant/query";
    public static String QUERY_URL = "https://mgate.unionpay.com/gateway/merchant/query";

    // 商户代码
    public static String MER_ID;

    // 通知URL
    public static String MER_BACK_END_URL = "";

    // 前台通知URL
    public static String MER_FRONT_END_URL = "";

    // 返回URL
    public static String MER_FRONT_RETURN_URL ="";

    // 加密方式
    public static String SIGN_TYPE = "MD5";

    // 商城密匙，需要和银联商户网站上配置的一样
    public static String SECURITY_KEY = "";

    // 成功应答码
    public static final String RESPONSE_CODE_SUCCESS = "00";

    // 签名
    public final static String SIGNATURE = "signature";

    // 签名方法
    public final static String SIGN_METHOD = "signMethod";

    // 应答码
    public final static String RESPONSE_CODE = "respCode";

    // 应答信息
    public final static String RESPONSE_MSG = "respMsg";
}
