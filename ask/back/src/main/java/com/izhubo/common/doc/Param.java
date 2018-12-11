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

    String access_token ="access_token";



    String first = "id1";

    String second = "id2";


    /**
     * 访问token 有效期 30天
     */
    long TOKEN_SECONDS = 24*3600L;
}
