package com.izhubo.common.doc;

/**
 *
 * Param key.
 *
 * date: 13-2-25 上午10:26
 *
 * @author: wubinjie@ak.cc
 */
public interface Param {


    String user_name = "user_name";
    String password = "password";

    String access_token ="ACCESS_TOKEN";
    String cookie_key_openId = "Cookie_Key_OpenId";
    String cookie_key_openKey = "Cookie_key_OpenKey";
    
    String redis_username_to_openIdAndOpenKey = "redis_username_to_openIdAndOpenKey";

    String timestamp ="timestamp";

    String first = "id1";

    String second = "id2";


    /**
     * 手机客户端传递的uid
     */
    String uid = "uid";
    /**
     * X-Forwarded-For
     */
    String XFF = "X-FORWARDED-FOR";


    /**
     * 访问token 有效期 30天
     */
    long TOKEN_SECONDS = 24*3600L;
}
