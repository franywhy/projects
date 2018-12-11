package com.upomp.pay.util;

import com.tenpay.client.TenpayHttpClient;
import com.izhubo.rest.common.util.http.HttpClientUtil;
import com.upomp.pay.info.UpmpConfig;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: wubinjie@ak.cc
 * Date: 14-1-15 上午11:34
 */
public class UpmpService {
    /**
     * 交易接口处理
     * @param req 请求要素
     * @param resp 应答要素
     * @return 是否成功
     */
    public static boolean trade(Map<String, String> req, Map<String, String> resp) throws IOException {
        String nvp = buildReq(req);
        //System.out.println(nvp);
        //Map reqs = queryString2Map(nvp);
        //String respString = HttpUtil.post(UpmpConfig.TRADE_URL, nvp);

/*        Map<String,String> headers = new HashMap<String,String>();
        headers.put("Connection", "Keep-Alive");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        String respString = HttpClientUtil.post(UpmpConfig.TRADE_URL, reqs, headers);*/
        //System.out.println("trade nvp:" + nvp);
        TenpayHttpClient httpClient = new TenpayHttpClient(); //创建TenpayHttpClient，后台通信
        httpClient.setCharset("UTF-8");
        httpClient.setReqContent(UpmpConfig.TRADE_URL+"?"+nvp);
        httpClient.setMethod("POST");
        httpClient.call();
        String respString = httpClient.getResContent();
        //System.out.println("trade respString:" + respString);
        return verifyResponse(respString, resp);
    }

    /**
     * 交易查询处理
     * @param req 请求要素
     * @param resp 应答要素
     * @return 是否成功
     */
    public static boolean query(Map<String, String> req, Map<String, String> resp){
        String nvp = buildReq(req);
        //System.out.println("query nvp:" + nvp);
        //String respString = HttpUtil.post(UpmpConfig.QUERY_URL, nvp);
        //Map reqs = queryString2Map(nvp);
        TenpayHttpClient httpClient = new TenpayHttpClient(); //创建TenpayHttpClient，后台通信
        httpClient.setCharset("UTF-8");
        httpClient.setReqContent(UpmpConfig.QUERY_URL+"?"+nvp);
        httpClient.setMethod("POST");
        httpClient.call();
        String respString = httpClient.getResContent();
        //System.out.println("query respString:" + respString);
        return verifyResponse(respString, resp);
    }

    /**
     * 拼接保留域
     * @param req 请求要素
     * @return 保留域
     */
    public static String buildReserved(Map<String, String> req) {
        StringBuilder merReserved = new StringBuilder();
        merReserved.append("{");
        merReserved.append(UpmpCore.createLinkString(req, false, true));
        merReserved.append("}");
        return merReserved.toString();
    }

    /**
     * 拼接请求字符串
     * @param req 请求要素
     * @return 请求字符串
     */
    public static String buildReq(Map<String, String> req) {
        // 除去数组中的空值和签名参数
        Map<String, String> filteredReq = UpmpCore.paraFilter(req);
        // 生成签名结果
        String signature = UpmpCore.buildSignature(filteredReq);

        // 签名结果与签名方式加入请求提交参数组中
        filteredReq.put(UpmpConfig.SIGNATURE, signature);
        filteredReq.put(UpmpConfig.SIGN_METHOD, UpmpConfig.SIGN_TYPE);

        return UpmpCore.createLinkString(filteredReq, false, true);
    }

    /**
     * 异步通知消息验证
     * @param para 异步通知消息
     * @return 验证结果
     */
    public static boolean verifySignature(Map<String, String> para) {
        String respSignature = para.get(UpmpConfig.SIGNATURE);
        // 除去数组中的空值和签名参数
        Map<String, String> filteredReq = UpmpCore.paraFilter(para);
        String signature = UpmpCore.buildSignature(filteredReq);
        if (null != respSignature && respSignature.equals(signature)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 应答解析
     * @param respString 应答报文
     * @param resp 应答要素
     * @return 应答是否成功
     */
    private static boolean verifyResponse(String respString, Map<String, String> resp) {
        if (respString != null && !"".equals(respString)) {
            // 请求要素
            Map<String, String> para;
            try {
                para = UpmpCore.parseQString(respString);
            } catch (Exception e) {
                return false;
            }
            boolean signIsValid = verifySignature(para);

            resp.putAll(para);

            if (signIsValid) {
                return true;
            }else {
                return false;
            }

        }
        return false;
    }

    /**
     * 查询字符串转换成Map<br/>
     * name1=key1&name2=key2&...
     * @param queryString
     * @return
     */
    public static Map queryString2Map(String queryString) {
        if(null == queryString || "".equals(queryString)) {
            return null;
        }

        Map m = new HashMap();
        String[] strArray = queryString.split("&");
        for(int index = 0; index < strArray.length; index++) {
            String pair = strArray[index];
            putMapByPair(pair, m);
        }

        return m;

    }
    public static void putMapByPair(String pair, Map m) {

        if(null == pair || "".equals(pair)) {
            return;
        }

        int indexOf = pair.indexOf("=");
        if(-1 != indexOf) {
            String k = pair.substring(0, indexOf);
            String v = pair.substring(indexOf+1, pair.length());
            if(null != k && !"".equals(k)) {
                m.put(k, v);
            }
        } else {
            m.put(pair, "");
        }
    }
}
