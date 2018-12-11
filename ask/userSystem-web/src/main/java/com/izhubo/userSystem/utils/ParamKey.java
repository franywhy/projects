package com.izhubo.userSystem.utils;

public interface ParamKey {
    interface In{


        String page = "page";

        String callback= "callback";

        String size = "size";

        int PAGE_DEFAULT = 1;

        int SIZE_DEFAULT = 6;

        int SIZE_MAX_ALLOW = 200;

        String user_id = "user_id";

    }



    interface Out{

        String code = "code";

        String msg = "msg";

        String count = "count";

        String data = "data";

        String all_page = "all_page";

        String exec = "exec";


        Integer SUCCESS = 1;

    }
    
    
    public interface Req {


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
}
