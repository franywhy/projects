package com.izhubo.web;

import com.hqonline.model.HK;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.model.Finance;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.rest.persistent.KGS;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.utils.AES;
import com.izhubo.web.api.Web;
import com.izhubo.web.vo.SSOResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import groovy.transform.TypeChecked;
import groovy.transform.TypeCheckingMode;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert;
import static com.izhubo.rest.common.doc.MongoKey._id;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
@Controller
public class SynUserController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ACAESKEY = "71bcbe39400d8328";

    private static final String PROVINCE_CITY_SPLIT = " ";

    private static final String ACCESS_TOKEN_TO_USERNAME_KEY = "access_token_to_username";
    private static final String USERNAME_TO_ACCESS_TOKEN_KEY = "username_to_access_token";

    private static final Long THREE_DAY_SECONDS = 3 * 24 * 3600L;

    @Autowired
    private QQUserRepositery qqUserRepositery;
    @Resource
    private MongoTemplate mainMongo;
    @Resource
    private KGS userKGS;
    @Resource
    private StringRedisTemplate mainRedis;

    @ResponseBody
    @RequestMapping(value = "/synUser", produces = "application/json; charset=utf-8")
    @TypeChecked(TypeCheckingMode.SKIP)
    public Map synUser(HttpServletRequest request, HttpServletResponse response) {
        String access_token = ServletRequestUtils.getStringParameter(request,"access_token","");
        String pic = ServletRequestUtils.getStringParameter(request,"pic","");
        String nick_name = ServletRequestUtils.getStringParameter(request,"nick_name","");
        int sex = ServletRequestUtils.getIntParameter(request,"sex",0);
        String mail = ServletRequestUtils.getStringParameter(request,"mail","");

        Map<String, String> hashResult = new HashMap<String, String>();
        //根据token 从 SSO 获取用户手机号
        SSOResult ssoResult = this.userMobileNo(access_token);
        String username = "";
        if(ssoResult != null && org.apache.commons.lang3.StringUtils.isNotBlank(ssoResult.getCode()) && "200".equals(ssoResult.getCode())){
            JSONObject dataObj = JSONObject.fromObject(ssoResult.getData());
            username = dataObj.getString("mobileNo");
            DBObject user = checkUser(access_token,username);
            if(null != user) {
                Integer userId = (Integer) user.get("_id");
                String pic_kj = user.get("pic").toString();
                String nick_name_kj = user.get("nick_name").toString();

                BasicDBObject updateDB = new BasicDBObject();
                if(StringUtils.isNoneBlank(pic) && !pic.equals(pic_kj)) {
                    updateDB.append("pic",pic);
                }
                if(StringUtils.isNoneBlank(nick_name) && !pic.equals(nick_name_kj)) {
                    updateDB.append("nick_name",nick_name);
                }
                if(updateDB.size() > 0) {
                    users().update(new BasicDBObject("_id", userId),new BasicDBObject("$set",updateDB));
                }

                Integer priv = Integer.parseInt(user.get("priv").toString());

                hashResult.put("_id", userId.toString());
                hashResult.put("sso_userid", user.get("sso_userid").toString());
                hashResult.put("nick_name", (String) user.get("nick_name"));
                hashResult.put("priv", priv.toString());

                Map finance = (Map) user.get(Finance.finance);
                if (null != finance) {
                    Number coin_spend = (Number) finance.get(Finance.coin_spend_total);
                    if (null != coin_spend) {
                        hashResult.put("spend", coin_spend.toString());
                    }
                }
                String token_key = KeyUtils.accessToken(access_token);
                mainRedis.opsForHash().putAll(token_key, hashResult);
                mainRedis.expire(token_key, THREE_DAY_SECONDS, TimeUnit.SECONDS);
                mainRedis.opsForValue().set(KeyUtils.USER.token(userId), access_token, THREE_DAY_SECONDS, TimeUnit.SECONDS);

                Web.setNewVIP(user);
            }
        }
        return getResultOK(hashResult);
    }

    private DBObject checkUser(String token, String username) {

        String password = "123456";

        List<QQUser> list;
        QQUser qqUser = null;
        DBObject user = null;

        list = this.qqUserRepositery.findByUsername(username);

        if (list.size() == 0) {
            //对于SSO中存在，但Mongodb中不存在的用户，需要把SSO的用户信息同步到Mongodb
            this.synUserToMongo(token,username, password);
            list = this.qqUserRepositery.findByUsername(username);
        }
        //判断用户
        if (list.size() > 0) {
            qqUser = list.get(0);
            user = mainMongo.getCollection("users").findOne(new BasicDBObject("tuid", qqUser.getTuid()));
            //对于MongoDB里SSOUserId为空的用户，需要把SSO的userId同步到MongoDB
            if(user != null && user.get("sso_userid") == null){
                this.synSSOUserIdToMongo(token, qqUser.getTuid());
            }
        }

        if (qqUser != null && user != null) {
            if (user.get("status").toString().startsWith("true")) {

                String access_token = token;

                // 写入username_to_access_token
                mainRedis.opsForHash().put(USERNAME_TO_ACCESS_TOKEN_KEY, username, access_token);
                // 写入access_token_to_username
                mainRedis.opsForHash().put(ACCESS_TOKEN_TO_USERNAME_KEY, access_token, username);

                String hash = HK.LOGIN.hash(access_token);
                // 类型
                mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV, user.get("priv").toString());
                // ID
                mainRedis.opsForHash().put(hash, HK.LOGIN.ID, user.get("_id").toString());
                // 昵称
                mainRedis.opsForHash().put(hash, HK.LOGIN.NICK_NAME, user.get("nick_name"));
                mainRedis.opsForHash().put(hash, HK.LOGIN.STATUS, "1");
                mainRedis.opsForHash().put(hash, HK.LOGIN.TAG, "");
                // 头像
                mainRedis.opsForHash().put(hash, HK.LOGIN.PIC, user.get("pic"));
                // 权限
                mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV0, user.get("priv0").toString());
                mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV1, user.get("priv1").toString());
                mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV2, user.get("priv2").toString());
                // nc_id
                if (user.get("nc_id") != null) {
                    mainRedis.opsForHash().put(hash, HK.LOGIN.NC_ID, user.get("nc_id"));
                }
                if (user.get("school_code") != null) {
                    mainRedis.opsForHash().put(hash, HK.LOGIN.SCHOOL_CODE, user.get("school_code"));
                }
            }
        }
        return user;
    }

    /**
     * 对于MongoDB里SSOUserId为空的用户，需要把SSO的userId同步到MongoDB
     * @param token
     * @param mobileNo
     */
    private void synSSOUserIdToMongo(String token, String tuid) {
        String ssoUserId = "";
        SSOResult userTokenDetail = this.userTokenDetail(token);
        if(userTokenDetail != null && StringUtils.isNotBlank(userTokenDetail.getCode()) && "200".equals(userTokenDetail.getCode())){
            JSONObject dataObj = JSONObject.fromObject(userTokenDetail.getData());
            ssoUserId = dataObj.getString("userId");
            if(StringUtils.isNotBlank(ssoUserId)){
                //设置sso_userid
                BasicDBObject updateDB = new BasicDBObject("sso_userid", ssoUserId);
                mainMongo.getCollection("users").update(new BasicDBObject("tuid", tuid),new BasicDBObject("$set", updateDB));
            }
        }
    }

    /**
     * 对于SSO中存在，但Mongodb中不存在的用户，需要把SSO的用户信息同步到Mongodb
     * @param mobileNo
     * @param password 密码(明文)
     * @return
     */
    private void synUserToMongo(String token, String mobileNo, String password) {

        String username = mobileNo;
        String nickname = "";
        String pic = "";
        String temppsw = password;
        String ssoUserId = "";
        String channel = "";//原业务是从入参中获取
        String qd = "kuaida";
        String tuid = com.izhubo.rest.common.util.RandomUtil.getTuid();
        SSOResult userInfo = this.userInfo(token);
        if(userInfo != null && org.apache.commons.lang3.StringUtils.isNotBlank(userInfo.getCode()) && "200".equals(userInfo.getCode())){
            JSONObject dataObj = JSONObject.fromObject(userInfo.getData());
            pic = dataObj.getString("avatar");
            nickname = dataObj.getString("nickName");
        }else{
            return;
        }

        SSOResult userTokenDetail = this.userTokenDetail(token);
        if(userTokenDetail != null && org.apache.commons.lang3.StringUtils.isNotBlank(userTokenDetail.getCode()) && "200".equals(userTokenDetail.getCode())){
            JSONObject dataObj = JSONObject.fromObject(userTokenDetail.getData());
            ssoUserId = dataObj.getString("userId");
        }else{
            return;
        }
        try {
            // 用户的昵称在默认未填写情况下 省略手机号中间4位，例如：138****8000
            if(org.apache.commons.lang3.StringUtils.isNotBlank(username) && username.equals(nickname) && nickname.length() == 11){
                nickname = nickname.substring(0,3) + "****" + nickname.substring(7, 11);
            }
            password = MsgDigestUtil.MD5.digest2HEX(password);
            QQUser qqUser = new QQUser();
            qqUser.setNickName(nickname);
            qqUser.setPassword(password);
            qqUser.setQd(qd);
            qqUser.setTuid(tuid);
            qqUser.setUsername(username);
            this.qqUserRepositery.save(qqUser);
            this.addUser(qqUser.getTuid(),qqUser.getNickName(), temppsw, username, "", "","", "", "", 0, qqUser.getTuid(),channel,qd,ssoUserId,pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SSOResult userMobileNo(String token) {
        SSOResult sSOResult = null;
        //拼装SSO接口的请求url
        String SSO_USERMOBILENO_GET = AppProperties.get("sso_userMobileNo_get");
        String mobileUrl = SSO_USERMOBILENO_GET+"?token="+token;
        String result = null;
        try {
            result = HttpClientUtil4_3.get(mobileUrl,null);
            if(StringUtils.isNotBlank(result)){
                sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
            }
        } catch (IOException e) {
            logger.error("SSO[/inner/userMobileNo]接口返回结果转换出错！接口返回结果result="+result);
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return sSOResult;
    }

    /**获取用户头像，昵称等信息
     * "avatar": "string",	头像
     "email": "string",
     "gender": 0,	性别
     "nickName": "string"	昵称
     * @param token
     * @return
     */
    private SSOResult userInfo(String token) {
        SSOResult sSOResult = null;
        //拼装SSO接口的请求url
        String SSO_USERINFO_GET = AppProperties.get("sso_userInfo_get");
        String userInfoUrl = SSO_USERINFO_GET+"?token="+token;
        String result = null;
        try {
            result = HttpClientUtil4_3.get(userInfoUrl, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(StringUtils.isNotBlank(result)){
            try {
                sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
            } catch (Exception e) {
                logger.error("SSO[/inner/userInfo]接口返回结果转换出错！接口返回结果result="+result);
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return sSOResult;
    }

    /**
     * 获取userId等
     * "clientType": "WEB",
     "oneTimeToken": true,
     "timeStamp": 0,
     "userId": 0,
     "versionCode": 0
     * @param token
     * @return
     */
    private SSOResult userTokenDetail(String token) {
        SSOResult sSOResult = null;
        //拼装SSO接口的请求url
        String SSO_USERTOKENDETAIL_GET = AppProperties.get("sso_userTokenDetail_get");
        String mobileUrl = SSO_USERTOKENDETAIL_GET+"?token="+token;
        String result = null;
        try {
            result = HttpClientUtil4_3.get(mobileUrl,null);
            if(StringUtils.isNotBlank(result)){
                sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
            }
        } catch (IOException e) {
            logger.error("SSO[/inner/userTokenDetail]出错！"+result);
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return sSOResult;
    }

    private DBObject addUser(String tuid, String nick_name, String psw,
                            String mobile, String area_name, String province, String city,
                            String school_code, String school_name, Integer nc_user_state,
                            String nc_id, String channel, String qd, String ssoUserId, String pic) throws Exception {

        DBCollection users = mainMongo.getCollection("users");

        BasicDBObject query_tuid = new BasicDBObject("tuid", tuid);
        DBObject user = users.findOne(query_tuid);
        if (user != null) {
            return user;
        } else {

            user = new BasicDBObject(_id, userKGS.nextId());
            if (null == nick_name) {
                nick_name = "kuaida_" + user.get(_id);
            }
            if(org.apache.commons.lang3.StringUtils.isBlank(pic)){
                pic = "http://answerimg.kjcity.com/default_student.png";
            }

            // user.putAll(basicInfoWithNickName);
            user.put("nick_name", HtmlUtils.htmlEscape(nick_name)); // 注意QQ需要返回QQ昵称，执行初始化
            user.put("tuid", tuid);
            user.put("sso_userid", ssoUserId);//增加保存sso的userid
            user.put("priv", 3);
            user.put("status", Boolean.TRUE);
            user.put("timestamp", System.currentTimeMillis());
            user.put("pic", pic);
            // user.put("pic", "http://answerimg.kjcity.com/logo.png");
            user.put("topic_count", 0);
            user.put("topic_evaluation_count", 0);
            user.put("ackey", AES.aesEncrypt(psw, ACAESKEY));
            user.put("vlevel", 0);
            user.put("is_show_update", 0);
            user.put("mobile", mobile);
            user.put("channel", channel);
            user.put("qd", qd);

            user.put("priv0", 1);// 学生权限
            user.put("priv1", 0);// 抢答权限
            user.put("priv2", 0);// 招生权限
            user.put("priv3", 0);
            user.put("priv4", 0);
            user.put("priv5", 0);
            user.put("priv6", 0);
            user.put("priv7", 0);
            user.put("priv8", 0);
            user.put("priv9", 0);

            // 省份也有可能是同步过来的，如果是同步过来的，则不需要
            if (province.equals("")) {
                // add by shihongjie 2015-10-19
                // 用户的省市
                Map<String, String> map = this.addressByMobile(mobile);
                user.put("province", map.get("province"));
                user.put("city", map.get("city"));
            } else {
                user.put("province", province);
                user.put("city", city);
            }
            // 同步NC的校区编码和校区名称还有学员状态，方便后期统计
            user.put("area_name", area_name);
            user.put("school_code", school_code);
            user.put("school_name", school_name);
            user.put("nc_user_state", nc_user_state);

            // 这里加一个NC_ID的判断，如果 原先日志中有，则直接获取nc_id并删除原先记录

            String new_ncid = GetNC_ID(mobile);
            if (new_ncid != "") {
                nc_id = new_ncid;
            }
            user.put("nc_id", nc_id);
            try {
                return users.findAndModify(
                        query_tuid.append(_id, user.removeField(_id)), null,
                        null, false, new BasicDBObject($setOnInsert, user),
                        true, true); // upsert
            } catch (MongoException e) {
                e.printStackTrace();
                query_tuid.remove(_id);
                return users.findOne(query_tuid);
            }
        }
    }

    private String GetNC_ID(String mobile) {
        DBCollection users_ncpk = mainMongo.getCollection("users_ncpk");
        DBObject userdata = users_ncpk
                .findOne(new BasicDBObject("_id", mobile));
        if (userdata == null) {
            return "";
        } else {
            // 删除 原先记录
            users_ncpk.remove(new BasicDBObject("_id", mobile));
            return userdata.get("nc_id").toString();
        }
    }

    /**
     * 根据电话号码获取地址
     * @Description: 根据电话号码获取地址
     * @date 2015年10月19日 上午10:27:47
     * @param @param mobile
     * @param @return Map map["mobile"]=手机号,map["province_city"]=省市,map["province"]=省,map["city"]=市
     */
    private Map<String , String> addressByMobile(String mobile){
        Map<String , String> map = new HashMap<String , String>();
        map.put("province_city", "");
        map.put("province", "");
        map.put("city", "");
        map.put("mobile", mobile);
        if(StringUtils.isNotBlank(mobile) && mobile.length() == 11){
            DBObject dm_mobile =  mainMongo.getCollection("dm_mobile").findOne(
                    new BasicDBObject("mobile_num" ,mobile.substring(0,7)) ,
                    new BasicDBObject("province_city" ,1)
            );
            if(dm_mobile != null){
                String province_city = dm_mobile.get("province_city").toString();
                if(StringUtils.isNotBlank(province_city)){
                    String[] arr = province_city.split(PROVINCE_CITY_SPLIT);

                    if(arr.length==1)
                    {
                        map.put("province_city", province_city);
                        map.put("province", province_city);
                        map.put("city", province_city);
                    }
                    if(arr.length==2)
                    {
                        map.put("province_city", province_city);
                        map.put("province", arr[0]);
                        map.put("city", arr[1]);
                    }
                }
            }
        }
        return map;
    }

}
