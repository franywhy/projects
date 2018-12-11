package com.izhubo.web.interceptor;

import static com.izhubo.rest.common.util.MsgDigestUtil.MD5;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil;
import com.izhubo.common.constant.Constant;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.web.api.Web;

public enum UserFrom implements ThirdUserBuilder {
	
	

    快播图丽("kb-") {

       // private static final String KB_USER_API = "http://app.huaseji.com/api/user/profile/query/";
        private static final String KB_USER_API = "http://183.61.70.24:49090/api/user/profile/query/";
        // tuli provider identity
        public static final String identity = "1e923982b871b50faedfea4f1f811cd4";



        public _ThirdUser build(String access_token) throws IOException {
            Map<String, String> form = new HashMap<>();
            form.put("token", access_token.substring(3));
            form.put("identity", identity);
            Map json = JSONUtil.jsonToMap(HttpClientUtil.post(KB_USER_API, form, null));
            Map data;
            if (!Boolean.TRUE.equals(json.get("ok")) || (data = (Map) json.get("data")).isEmpty()) {
                return _ThirdUser.EMPTY_USER;
            } //"0"-代表圖利“1”-快玩  data.get("user_type")
            _ThirdUser user = new _ThirdUser(data.get("user_type") + "_" + data.get("user_id"), (String) data.get("nickname"));

            String pic = " http://u.izhubo.com/user/images/avatar.png"  ;
            if(StringUtils.isNotBlank((String) data.get("avatar")))
                pic =  (String) data.get("avatar") ;
            user.setProp("pic", pic);
            return user;
        }
    },
    快播移动("km-") {
       /* private static final String KB_USER_API = Constant.KB_USER_API;
        private static final String APP_SECURITY = Constant.APP_SECURITY;
        private static final String APP_ID = Constant.APP_ID;*/

        public _ThirdUser build(String access_token) throws IOException {
            
            Map<String, String> header = new HashMap<>();
            header.put("auth_token", access_token.substring(3));
            header.put("app_security", Constant.APP_SECURITY);
            header.put("app_id", Constant.APP_ID);

            Map json = JSONUtil.jsonToMap(HttpClientUtil.post(Constant.KB_USER_API, header, null));
            Map data;
            if (!Boolean.TRUE.equals(json.get("ok")) || (data = (Map) json.get("data")).isEmpty()) {
                return _ThirdUser.EMPTY_USER;
            }
            _ThirdUser user = new _ThirdUser(namespace + data.get("uid"), (String) data.get("username"));
            String pic = "http://u.izhubo.com/user/images/avatar.png"  ;
            if(StringUtils.isNotBlank((String) data.get("image")))
                pic =  (String) data.get("image");
            String gender = (String)data.get("gender") ;
            Integer sex = 1 ;
            if(StringUtils.isNotBlank(gender))
                sex =  Integer.parseInt((String)data.get("gender")) ;

           // logger.info("sex---->:{}",sex);
            user.setProp("pic", pic);
            user.setProp("sex", sex);
            user.setProp("user_name", (String) data.get("username"));
            return user;
        }
    },
    阿游戏("ay-"),
    qq应用("qq-"),
    水葫芦("hl-"),
    汉游("hy-"),
    缘来("yl-"){
        private static final String YL_USER_API = "http://www.yuanlai.com/authorize/game-userinfo.do";
        private static final String PRIVATEKEY = "uFmvNRinO9idZtND";
        private static final String APP = "10033";
        public _ThirdUser build(String access_token) throws IOException {
            String thirdId = Web.mainRedis.opsForValue().get(KeyUtils.USER.onlyToken2id(access_token));
            if (StringUtils.isBlank(thirdId)) {
                return _ThirdUser.EMPTY_USER;
            }
            Map<String, String> form = new HashMap<>();
            form.put("uid", thirdId);
            form.put("returntype", "json");
            form.put("app", APP);
            form.put("time", String.valueOf(System.currentTimeMillis()));
            //form.put("time", "20140228112359");
            //加密方式md5(app+time+uid+privateKey)
            form.put("sign", MD5.digest2HEX(form.get("app")+form.get("time")+form.get("uid")+PRIVATEKEY));
            Map json = JSONUtil.jsonToMap(HttpClientUtil.post(YL_USER_API, form, null));
            if (!"1".equals(json.get("code"))) {
                return _ThirdUser.EMPTY_USER;
            }
            //pic 头像 nickname 呢称 gender 性别
            String nickname = "izhubo_" + StringUtils.substring(thirdId, 0, 8);
            if(StringUtils.isNotBlank(nickname))
                nickname = (String) json.get("nickname");
            _ThirdUser user = new _ThirdUser(namespace + thirdId, nickname);

            String pic = "http://u.izhubo.com/user/images/avatar.png"  ;
            if(StringUtils.isNotBlank((String) json.get("pic"))){
                pic =  (String) json.get("pic");
                //修改为60尺寸
                pic =  StringUtils.replace(pic, "-20.jpg", "-60.jpg");
            }

            String gender = (String)json.get("gender") ;
            Integer sex = 1 ;
            if(StringUtils.isNotBlank(gender))
                sex =  Integer.parseInt((String)json.get("gender")) ;
            user.setProp("pic", pic);
            user.setProp("sex", sex);
            return user;
        }
    },
    欧朋("op-"){
        public _ThirdUser build(String access_token) throws IOException {
            String thirdId = Web.mainRedis.opsForValue().get(KeyUtils.USER.onlyToken2id(access_token));
            if (StringUtils.isBlank(thirdId)) {
                return _ThirdUser.EMPTY_USER;
            }
            _ThirdUser user = new _ThirdUser(namespace + thirdId, "izhubo_" + StringUtils.substring(thirdId, 0, 8));
            user.setProp("pic", "http://u.izhubo.com/user/images/avatar.png");
            return user;
        }
    },
    sun门户("3g-"){
        public _ThirdUser build(String access_token) throws IOException {
            String thirdId = Web.mainRedis.opsForValue().get(KeyUtils.USER.onlyToken2id(access_token));
            if (StringUtils.isBlank(thirdId)) {
                return _ThirdUser.EMPTY_USER;
            }
            _ThirdUser user = new _ThirdUser(namespace + thirdId, "izhubo_" + StringUtils.substring(thirdId, 0, 8));
            user.setProp("pic", "http://u.izhubo.com/user/images/avatar.png");
            return user;
        }
    },
    遇见("yj-"){
        public _ThirdUser build(String access_token) throws IOException {
            String thirdId = Web.mainRedis.opsForValue().get(KeyUtils.USER.onlyToken2id(access_token));
            if (StringUtils.isBlank(thirdId)) {
                return _ThirdUser.EMPTY_USER;
            }
            _ThirdUser user = new _ThirdUser(namespace + thirdId, "izhubo_" + StringUtils.substring(thirdId, 0, 8));
            user.setProp("pic", "http://u.izhubo.com/user/images/avatar.png");
            return user;
        }
    },
    叮咚("dd-"){
        public _ThirdUser build(String access_token) throws IOException {
            String thirdId = Web.mainRedis.opsForValue().get(KeyUtils.USER.onlyToken2id(access_token));
            if (StringUtils.isBlank(thirdId)) {
                return _ThirdUser.EMPTY_USER;
            }
            _ThirdUser user = new _ThirdUser(namespace + thirdId, "izhubo_" + StringUtils.substring(thirdId, 0, 8));
            user.setProp("pic", "http://u.izhubo.com/user/images/avatar.png");
            return user;
        }
    },
    迅雷看看("kk-") {
        static final String TTUS = "http://us.izhubo.com/user/show?access_token=";

        public _ThirdUser build(String access_token) throws IOException {
            String json = HttpClientUtil.get(TTUS + access_token, null, HttpClientUtil.UTF8);
            Map _jsonMap = JSONUtil.jsonToMap(json);
            if (((Number) _jsonMap.get("code")).intValue() != 1) {
                return _ThirdUser.EMPTY_USER;// 认证失败， token无效
            }
            final Map data = (Map) _jsonMap.get("data");
            String name = (String) data.get("nick_name");
            if (null == name || name.startsWith("izhubo")) {
                name = "ttxy" ;
                String old = (String) data.get("old_name");
                if (null != old && !"".equals(old)) {
                    name = old;
                }

            }
            _ThirdUser user = new _ThirdUser(data.get("tuid"), name);
            user.setProps(data);
            return user;
        }
    }, 
    //爱主播空间应用
    izhubo_qq("q2-"){
    	
    	
    	public _ThirdUser build(String access_token) throws IOException{
    		
    		// TODO q2- build
//    		String onlyToken2id = KeyUtils.USER.onlyToken2id(access_token);
//            String thirdId = Web.mainRedis.opsForValue().get(onlyToken2id);
//            logger.info("onlyToken2id: {}, thirdId: {}", onlyToken2id, thirdId);
//            if (StringUtils.isBlank(thirdId)) {
//            	logger.info("_ThirdUser.EMPTY_USER");
//                return _ThirdUser.EMPTY_USER;
//            }
            logger.info("in izhubo_qq build, access_token: {}", access_token);
            
    		
    		String us = AppProperties.get("us.domain") + "/user/info?access_token=";
    		String json = HttpClientUtil.get(us + access_token, null, HttpClientUtil.UTF8);
    		logger.info("json : {}", json);
            Map _jsonMap = JSONUtil.jsonToMap(json); 
            
            
            final Map data = (Map) _jsonMap.get("data");
            String name = (String) data.get("nickName");
            
            _ThirdUser user = new _ThirdUser(data.get("tuid"), name);
            user.setProps(data);
            return user;
    	}
    },
  //爱主播独立站
    izhubo("i1-"){
    	
    	
    	public _ThirdUser build(String access_token) throws IOException{
            logger.info("in izhubo_qq build, access_token: {}", access_token);
            
    		
    		String us = AppProperties.get("us.domain") + "/user/info?access_token=";
    		String json = HttpClientUtil.get(us + access_token, null, HttpClientUtil.UTF8);
    		logger.info("json : {}", json);
            Map _jsonMap = JSONUtil.jsonToMap(json); 
            
            
            final Map data = (Map) _jsonMap.get("data");
            if(data!=null)
            {
            String name = (String) data.get("nickName");
            
            _ThirdUser user = new _ThirdUser(data.get("tuid"), name);
            user.setProps(data);
            return user;
            }
            else
            {
            	return null;
            }
    	}
    },
    //ShareSDK for android QQ 
    saq("q3-"){
    	public _ThirdUser build(String access_token) throws IOException{
    		String us = AppProperties.get("us.domain") + "/t3user/info?access_token=";
    		String json = HttpClientUtil.get(us + access_token, null, HttpClientUtil.UTF8);
    		logger.info("json : {}", json);
            Map _jsonMap = JSONUtil.jsonToMap(json); 
            
            
            final Map data = (Map) _jsonMap.get("data");
            String name = (String) data.get("nickName");
            
            _ThirdUser user = new _ThirdUser(data.get("tuid"), name);
            user.setProps(data);
            return user;
    	}
    },
  //ShareSDK for android weibo 
    saw("w1-"){
    	public _ThirdUser build(String access_token) throws IOException{
    		String us = AppProperties.get("us.domain") + "/t3user/info?access_token=";
    		String json = HttpClientUtil.get(us + access_token, null, HttpClientUtil.UTF8);
    		logger.info("json : {}", json);
            Map _jsonMap = JSONUtil.jsonToMap(json); 
            
            
            final Map data = (Map) _jsonMap.get("data");
            String name = (String) data.get("nickName");
            
            _ThirdUser user = new _ThirdUser(data.get("tuid"), name);
            user.setProps(data);
            return user;
    	}
    },
  //ShareSDK for android QQ 
    siq("q4-"){
    	public _ThirdUser build(String access_token) throws IOException{
    		String us = AppProperties.get("us.domain") + "/t3user/info?access_token=";
    		String json = HttpClientUtil.get(us + access_token, null, HttpClientUtil.UTF8);
    		logger.info("json : {}", json);
            Map _jsonMap = JSONUtil.jsonToMap(json); 
            
            
            final Map data = (Map) _jsonMap.get("data");
            String name = (String) data.get("nickName");
            
            _ThirdUser user = new _ThirdUser(data.get("tuid"), name);
            user.setProps(data);
            return user;
    	}
    },
  //ShareSDK for android weibo 
    siw("w2-"){
    	public _ThirdUser build(String access_token) throws IOException{
    		String us = AppProperties.get("us.domain") + "/t3user/info?access_token=";
    		String json = HttpClientUtil.get(us + access_token, null, HttpClientUtil.UTF8);
    		logger.info("json : {}", json);
            Map _jsonMap = JSONUtil.jsonToMap(json); 
            
            
            final Map data = (Map) _jsonMap.get("data");
            String name = (String) data.get("nickName");
            
            _ThirdUser user = new _ThirdUser(data.get("tuid"), name);
            user.setProps(data);
            return user;
    	}
    };

    /**
     * 两位字母+ '-'
     */
    public final String namespace;

    private static final Map<String,UserFrom> cached = new HashMap<>();
    static {
        for(UserFrom  uf: UserFrom.values()){
            cached.put(uf.namespace,uf);
        }
    }
    UserFrom(String namespace) {
        this.namespace = namespace;
    }

    public static UserFrom from(String namespace){
        return cached.get(namespace);
    }


    public _ThirdUser build(String access_token) throws IOException {
        String thirdId = Web.mainRedis.opsForValue().get(KeyUtils.USER.onlyToken2id(access_token));
        if (StringUtils.isBlank(thirdId)) {
            return _ThirdUser.EMPTY_USER;
        }
        _ThirdUser user = new _ThirdUser(namespace + thirdId, "izhubo" + thirdId);
        user.setProp("pic", "http://u.izhubo.com/user/images/avatar.png");
        return user;
    }

    public static final char FLAG = '-';
    
    private static Logger logger = LoggerFactory.getLogger(UserFrom.class);
}

interface ThirdUserBuilder {
    _ThirdUser build(String access_token) throws IOException;
}

final class _ThirdUser {

    static final _ThirdUser EMPTY_USER = new _ThirdUser(null, null);

    final Object tuid;
    final String nick_name;

    private Map<String, Object> props;

    _ThirdUser(Object tuid, String nick_name) {
        this.tuid = tuid;
        this.nick_name = nick_name;
    }

    Object get(String key) {
        if (null == props) {
            return null;
        }
        return props.get(key);
    }

    void setProps(Map<String, Object> props) {
        this.props = props;
    }

    void setProp(String key, Object value) {
        if (null == props) {
            props = new HashMap<>();
        }
        props.put(key, value);
    }
}