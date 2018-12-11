package com.izhubo.web.interceptor;

import com.qq.open.OpensnsException;
import com.qq.open.SnsSigCheck;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: wubinjie@ak.cc
 * Date: 13-12-16 下午4:10
 */
public abstract class QqApp {


    static final Logger logger = LoggerFactory.getLogger(QqApp.class);

    static final String APPID = "1101118340";
    static final String APPKEY = "3r9XTJjVc4d4Ss4J";

    static final String USER_INFO_URL = "http://119.147.19.43/v3/user/get_info";

    //TODO QQ API URL need to chang before deploy
    //static final String USER_INFO_URL = "http://openapi.tencentyun.com/v3/user/get_info";

    static final String REQ_QQ_OPENID = "openid";
    static final String REQ_QQ_OPENKEY = "openkey";
    static final String REQ_QQ_PF = "pf";

    static _ThirdUser userInfo(String openkey, String openid, String pf) throws IOException {

        // 填充URL请求参数
        HashMap<String,String> params = new HashMap<>();
        params.put("openid", openid);
        params.put("openkey", openkey);
        params.put("pf", pf);
        params.put("appid", APPID);
        // 请求方法
        String method = "post";
        // 签名密钥
        String secret = APPKEY + "&";
        // 指定OpenApi Cgi名字
        String scriptName = "/v3/user/get_info";
        String sig = null;
        try {
            sig = SnsSigCheck.makeSig(method, scriptName, params, secret);
        } catch (OpensnsException e) {
            logger.error("qq makeSig error", e);
        }
        params.put("sig", sig);
        Map resMap = JSONUtil.jsonToMap(HttpClientUtil.post(USER_INFO_URL, params, null));
        logger.debug("req resMap : ${resMap}");
        Integer ret = Integer.valueOf(resMap.get("ret").toString());
        _ThirdUser user = null;
        if(ret == 0){
            String nickName = "qq_" + openid.substring(0,6);
            if(StringUtils.isNotEmpty(resMap.get("nickname").toString())){
                nickName = resMap.get("nickname").toString();
            }
            String pic = "http://u.izhubo.com/user/images/avatar.png";
            if(StringUtils.isNotEmpty(resMap.get("figureurl").toString())){
                pic = resMap.get("figureurl").toString();
            }
            user = new _ThirdUser(openid,nickName);
            user.setProp("pic", pic);
            user.setProp("via", "qqApp");
        }
        return user;
    }
}
