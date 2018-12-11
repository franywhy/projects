package com.izhubo.web.interceptor;

import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * date: 13-7-3 下午5:41
 *
 * @author: wubinjie@ak.cc
 */
public abstract class LeDou {
    private static final String TTUS = "http://ly.feed.uu.cc/account/verify_osid";
    // 由乐逗提供的app key
    private static final String APP_KEY = "b6915d289c92162181e4";
    // 由乐逗提供的app secret
    public static final String APP_SECRET = "c0d2101f9c036356c9a4";

    public static final int OPEN_ID_LENGTH = 32;
    static final String ledou_openid = "ledou_openid";
    static final String game_id = "10371";

    static _ThirdUser userInfo(String access_token,String openId) throws IOException {
        StringBuilder sb = new StringBuilder(130);
        sb.append("openid=").append(openId).append("&sessionid=").append(access_token).append("&secret=").append(APP_SECRET);
        Map<String,String> form = new HashMap<>();
        form.put("game_id",game_id);
        form.put("open_id",openId);
        form.put("sessionid",access_token);
        form.put("sign",MsgDigestUtil.MD5.digest2HEX(sb.toString()));

        String code = HttpClientUtil.post(TTUS,form,null);
        if ( ! "ok".equals(code) ) {
            return _ThirdUser.EMPTY_USER;// 认证失败， token无效
        }
        _ThirdUser user = new _ThirdUser(openId,"showdou_"+openId.substring(0,4));
        user.setProp("pic","http://u.izhubo.com/user/images/avatar.png");
        return user;
    }




    private static final String extra_info_self_sign = "benp6mkCYY20130704Guess*!&";

    public static String newExtraInfo(Object userId){
        String extra_info  = userId+"_"+System.currentTimeMillis();
        return extra_info + '_'  + MsgDigestUtil.MD5.digest2HEX(extra_info+extra_info_self_sign).substring(4,10);
    }


    public static boolean validExtraInfo(String extra_info){
        int last_;
        return extra_info != null
                && (last_ = extra_info.lastIndexOf('_')) > 0
                && MsgDigestUtil.MD5.digest2HEX(extra_info.substring(0, last_) + extra_info_self_sign).substring(4, 10)
                .equals(extra_info.substring(last_ + 1));
    }
//
//    public static void main(String[] args) {
//        System.out.println(
//                validExtraInfo("12333_1372924497086_f91554")
//        );
//    }


}
