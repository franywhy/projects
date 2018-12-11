package com.hq.learningcenter.school.service.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.hq.learningcenter.config.LocalConfig;
import com.hq.learningcenter.school.dao.*;
import com.hq.learningcenter.school.entity.CourseClassplanGenseeEntity;
import com.hq.learningcenter.utils.JSONUtil;
import com.hq.learningcenter.utils.ProduceToken;
import com.hq.learningcenter.school.entity.MallLiveRoomEntity;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.hq.learningcenter.kuaiji.pojo.ZegoTokenObjPOJO;
import com.hq.learningcenter.school.entity.CourseClassplanLivesEntity;
import com.hq.learningcenter.school.entity.SysProductEntity;
import com.hq.learningcenter.school.pojo.LiveInfoPOJO;
import com.hq.learningcenter.school.service.LiveService;

@Service("liveService")
public class LiveServiceImpl implements LiveService {

    private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private MallLiveRoomDao mallLiveRoomDao;
	
	@Autowired
	private CourseClassplanLivesDao courseClassplanLivesDao;
	
	@Autowired
	private SysProductDao sysProductDao;
	@Autowired
	private UsersDao usersDao;
	@Autowired
	private CourseClassplanGenseeDao classplanGenseeDao;

	@Autowired
	private LocalConfig localConfig;

	@Resource
	protected StringRedisTemplate mainRedis;
	
	private static final  String K_VAULE = "k_value:";

	private static String CTX = "";
	@Value("")
	private void setCtx(){
		CTX = localConfig.getGenseeCtx();
	}
	
	private static String CC_REPLAY = "";
	@Value("")
	private void setCCReplay(){
		CC_REPLAY = localConfig.getCcVodUrl();
	}
	
	private static Long K_TIMEOUT = 0L;
	@Value("")
	private void setKTimeout(){
		K_TIMEOUT = localConfig.getGenseeKTimeout();
	}
	
	private static String WHITE_K = "";
	@Value("")
	private void setKWhite(){
		WHITE_K = localConfig.getGenseeKWhite();
	}


	private static final String LIVEROOM = "liveRoom:";
	private static final String SYSPRODUCT = "sysProduct:";
	private static final String NICKNAME = "nickName:";
	private static final String CLASSPLAN_GENSEE = "classplanGensee:";
	private static final String CLASSPLANLIVE = "classplanLive:";

	//redis缓存过期时间/秒
    //查询直播房间号
	private static final int LIVEROOM_TIMEOUT = 3*60;
	//根据产品线查询展示互动地址
	private static final int SYSPRODUCT_TIMEOUT = 30*60;
	//查询用户昵称
	private static final int NICKNAME_TIMEOUT = 60*60;
	//根据排课查询展示互动地址
	private static final int CLASSPLAN_GENSEE_TIMEOUT = 30*60;
	//查询课次信息
	private static final int CLASSPLANLIVE_TIMEOUT = 5*60;

	@Override
	public LiveInfoPOJO queryLiveInfo(String classplanLiveId, UserInfoPOJO user) {
		try{
            SysProductEntity sysProductEntity = new SysProductEntity();
            MallLiveRoomEntity liveRoomEntity = getMallLiveRoomEntity(classplanLiveId);
            CourseClassplanGenseeEntity classplanGenseeEntity = getClassplanGenseeEntity(classplanLiveId);
            if (classplanGenseeEntity != null){
                sysProductEntity.setGenseedomain(classplanGenseeEntity.getGenseedomain());
            }else {
                sysProductEntity = getSysProductEntity(classplanLiveId);
            }
            String nickname = getNickname(user);

			LiveInfoPOJO pojo = new LiveInfoPOJO();
			if(null != liveRoomEntity){
				pojo.setUid(user.getUserId());
				pojo.setNickname(nickname != null ? nickname : user.getNickName());
				pojo.setLivenum(liveRoomEntity.getGenseeLiveNum());
				pojo.setGenseeDomainName(sysProductEntity.getGenseedomain());
				pojo.setServiceType(CTX);
				pojo.setK(buildKValue(user.getUserId().toString()));
				pojo.setBanSpeaking(liveRoomEntity.getBanSpeaking());
				pojo.setBanAsking(liveRoomEntity.getBanAsking());
				pojo.setHideAsking(liveRoomEntity.getHideAsking());
				pojo.setHideSpeaking(liveRoomEntity.getHideDiscussion());
			}
			return pojo;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

    private String getNickname(UserInfoPOJO user) {
        String nickname = "";
        String redisKey = NICKNAME + user.getUserId();
        String nicknameStr = mainRedis.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(nicknameStr)){
            nickname = nicknameStr;
            logger.info("获取直播间地址,从缓存获取nickname:{}",nickname);
        }else {
            nickname = usersDao.queryNickname(user.getUserId());
            mainRedis.opsForValue().set(redisKey,nickname == null ? user.getNickName() : nickname);
            mainRedis.expire(redisKey,NICKNAME_TIMEOUT, TimeUnit.SECONDS);
            logger.info("获取直播间地址,从数据库获取nickname:{}",nickname);
        }
        return nickname;
    }

    private SysProductEntity getSysProductEntity(String classplanLiveId) {
        SysProductEntity sysProductEntity = new SysProductEntity();
        String redisKey = SYSPRODUCT + classplanLiveId;
        String sysProductStr = mainRedis.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(sysProductStr)){
            sysProductEntity = JSONObject.parseObject(sysProductStr,SysProductEntity.class);
            logger.info("获取直播间地址,从缓存获取sysProductEntity:{}",sysProductEntity.toString());
        }else {
            sysProductEntity = sysProductDao.queryByclassplanLiveId(classplanLiveId);
            mainRedis.opsForValue().set(redisKey,JSONObject.toJSONString(sysProductEntity));
            mainRedis.expire(redisKey,SYSPRODUCT_TIMEOUT, TimeUnit.SECONDS);
            logger.info("获取直播间地址,从数据库获取sysProductEntity:{}",sysProductEntity.toString());
        }
        return sysProductEntity;
    }

    private MallLiveRoomEntity getMallLiveRoomEntity(String classplanLiveId) {
        MallLiveRoomEntity liveRoomEntity = new MallLiveRoomEntity();
        String redisKey = LIVEROOM + classplanLiveId;
        String liveRoomStr = mainRedis.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(liveRoomStr)){
            liveRoomEntity = JSONObject.parseObject(liveRoomStr, MallLiveRoomEntity.class);
            logger.info("获取直播间地址,从缓存获取liveRoomEntity:{}",liveRoomEntity.toString());
        }else {
            liveRoomEntity = mallLiveRoomDao.queryByLiveRoomId(classplanLiveId);
            mainRedis.opsForValue().set(redisKey,JSONObject.toJSONString(liveRoomEntity));
            mainRedis.expire(redisKey,LIVEROOM_TIMEOUT, TimeUnit.SECONDS);
            logger.info("获取直播间地址,从数据库获取liveRoomEntity:{}",liveRoomEntity.toString());
        }
        return liveRoomEntity;
    }

    private CourseClassplanGenseeEntity getClassplanGenseeEntity(String classplanLiveId) {
        CourseClassplanGenseeEntity classplanGenseeEntity = null;
        MallLiveRoomEntity liveRoomEntity = this.getMallLiveRoomEntity(classplanLiveId);
        String redisKey = CLASSPLAN_GENSEE + liveRoomEntity.getClassplanId();
        String classplanGenseeStr = mainRedis.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(classplanGenseeStr)){
            classplanGenseeEntity = JSONObject.parseObject(classplanGenseeStr, CourseClassplanGenseeEntity.class);
            logger.info("获取直播间地址,从缓存获取classplanGenseeEntity:{}",classplanGenseeEntity.toString());
        }else {
            //根据排课id查询关联表
            classplanGenseeEntity  = classplanGenseeDao.getClassplanGenseeByClassplanId(liveRoomEntity.getClassplanId());
            if (classplanGenseeEntity != null) {
                mainRedis.opsForValue().set(redisKey, JSONObject.toJSONString(classplanGenseeEntity));
                mainRedis.expire(redisKey, CLASSPLAN_GENSEE_TIMEOUT, TimeUnit.SECONDS);
                logger.info("获取直播间地址,从数据库获取classplanGenseeEntity:{}", classplanGenseeEntity.toString());
            }
        }
        return classplanGenseeEntity;
    }

    private CourseClassplanLivesEntity getClassplanLives(String classplanLiveId) {
        CourseClassplanLivesEntity classPlanLive = null;
        String redisKey = CLASSPLANLIVE + classplanLiveId;
        String classPlanLiveStr = mainRedis.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(classPlanLiveStr)){
            classPlanLive = JSONObject.parseObject(classPlanLiveStr, CourseClassplanLivesEntity.class);
            logger.info("获取直播间地址,从缓存获取classPlanLive:{}",classPlanLive.toString());
        }else {
            //根据排课id查询关联表
            classPlanLive = courseClassplanLivesDao.queryByClassPlanLiveId(classplanLiveId,0);
            if (classPlanLive != null ) {
                mainRedis.opsForValue().set(redisKey, JSONObject.toJSONString(classPlanLive));
                mainRedis.expire(redisKey, CLASSPLANLIVE_TIMEOUT, TimeUnit.SECONDS);
                logger.info("获取直播间地址,从数据库获取classPlanLive:{}", classPlanLive.toString());
            }
        }
        return classPlanLive;
    }


    @Override
	public Map<String, Object> queryReplayInfo(String classplanLiveId, UserInfoPOJO user) {
		try{

            Map<String, Object> result = new HashMap<String, Object>();
            SysProductEntity sysProductEntity = new SysProductEntity();
			CourseClassplanLivesEntity classPlanLive = this.getClassplanLives(classplanLiveId);
            CourseClassplanGenseeEntity classplanGenseeEntity = this.getClassplanGenseeEntity(classplanLiveId);
            if (classplanGenseeEntity != null ){
                sysProductEntity.setGenseedomain(classplanGenseeEntity.getGenseedomain());
            }else {
                sysProductEntity = this.getSysProductEntity(classplanLiveId);
            }
			String nickname = this.getNickname(user);
			if(null != classPlanLive){
		        String replayUrl = classPlanLive.getBackUrl();
		        String vodId = classPlanLive.getBackId();
		        String title = classPlanLive.getClassplanLiveName();
		        
		        if (replayUrl.equals("")) {
		            result.put("type", 0);
		            result.put("replayUrl", "");
		        } else if (replayUrl.indexOf("webcast/site/vod/play") != -1) {
		            result.put("type", 1);
		            result.put("uid", user.getUserId());
		            result.put("nickname", nickname != null ? nickname+"_hq_"+classplanLiveId : user.getNickName()+"_hq_"+classplanLiveId);
		            result.put("k", buildKValue(user.getUserId().toString()));
		            result.put("vodId", vodId);
		            result.put("title", title);
		            result.put("genseeDomainName", sysProductEntity.getGenseedomain());
		            result.put("serviceType", CTX);
		            //result.put("replayUrl", null);
		        } else if (replayUrl.indexOf("http://my.polyv.net/front/video") != -1) {
		            result.put("type", 3);
		            result.put("replayUrl", replayUrl);
		        } else {
		            result.put("type", 2);
		            result.put("replayUrl", CC_REPLAY + replayUrl);
		        }
			}
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, Object> queryLiveUrl(String classplanLiveId, UserInfoPOJO user) {
		try{
            SysProductEntity sysProductEntity = new SysProductEntity();
			MallLiveRoomEntity liveRoomEntity = this.getMallLiveRoomEntity(classplanLiveId);
            CourseClassplanGenseeEntity classplanGenseeEntity = this.getClassplanGenseeEntity(classplanLiveId);
            if (classplanGenseeEntity != null ){
                sysProductEntity.setGenseedomain(classplanGenseeEntity.getGenseedomain());
            }else {
                sysProductEntity = this.getSysProductEntity(classplanLiveId);
            }
			Map<String, Object> result = new HashMap<String, Object>();
			String nickname = this.getNickname(user);
			if(null != liveRoomEntity){
				result.put("liveUrl", genseeUrl(user.getUserId(),URLEncoder.encode(nickname != null ? nickname : user.getNickName(), "UTF-8"),"http://"+sysProductEntity.getGenseedomain()+"/webcast/site/entry/join-"+liveRoomEntity.getGenseeLiveId()));
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, Object> queryReplayUrl(String classplanLiveId, UserInfoPOJO user) {
		try{
			CourseClassplanLivesEntity classPlanLive = this.getClassplanLives(classplanLiveId);
			Map<String, Object> result = new HashMap<String, Object>();
			String nickname = this.getNickname(user);
			if(null != classPlanLive){
		        String replayUrl = classPlanLive.getBackUrl();
		        
		        if (replayUrl.equals("")) {
		            //result.put("type", 0);
		            result.put("replayUrl", "");
		        } else if (replayUrl.indexOf("webcast/site/vod/play") != -1) {
		            result.put("replayUrl", genseeUrl(user.getUserId(), URLEncoder.encode(nickname != null ? nickname+"_hq_"+classplanLiveId : user.getNickName()+"_hq_"+classplanLiveId, "UTF-8"), replayUrl));
		        } else if (replayUrl.indexOf("http://my.polyv.net/front/video") != -1) {
		            //result.put("type", 3);
		            result.put("replayUrl", replayUrl);
		        } else {
		            //result.put("type", 2);
		            result.put("replayUrl", CC_REPLAY + replayUrl);
		        }
			}
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String verifyGenseeKey(String mapKey){
		if((K_VAULE+WHITE_K).equals(mapKey)) return "pass";
		
        Long timestamp = Long.valueOf(mainRedis.opsForValue().get(mapKey));
        if (null == timestamp
                || timestamp + K_TIMEOUT < System.currentTimeMillis()) {
            return "fail";
        }
        else {
        	mainRedis.delete(mapKey);
            return "pass";
        }
    }

	/**
     * 展示互动直播回放地址
     * @param userid 用户id
     * @param nickname 用户昵称
     * @param k 验证值
     * @param url 回放地址
     * @return
     */
    private String genseeUrl(Long userid, String nickname,String url){
        Map<String,Object> parameters = new HashMap<String, Object>();
        //避免与gensee的系统用户冲突,将userid格式化为1000000000以后
        Long uid = 1000000000L + userid;
        parameters.put("uid", uid);
        parameters.put("nickName", nickname);
        parameters.put("k", buildKValue(uid.toString()));
        return url + spliceParameter(parameters);
    }
    
    private static String spliceParameter(Map<String, Object> parameters){
        StringBuilder parameterStr = new StringBuilder();
        parameterStr.append("?");
        for (Map.Entry<String, Object> entry:parameters.entrySet()) {
            parameterStr.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return  parameterStr.deleteCharAt(parameterStr.length()-1).toString();
    }
    
    /**
     * 即构直播地址拼接
     * @param classplanLiveId 排课计划明细id
     * @param user 用户
     * @param businessId 业务线
     * @return
     */
	@Override
	public Map<String, Object> getZegoLiveUrl(String classplanLiveId, UserInfoPOJO user, String businessId) {
		try{
			MallLiveRoomEntity liveRoomEntity = mallLiveRoomDao.queryByLiveRoomId(classplanLiveId);
			
			String zkey = ProduceToken.getRandomStr(16);
			
			ZegoTokenObjPOJO zegoTokenObj = new ZegoTokenObjPOJO();
			zegoTokenObj.setCourse_id(classplanLiveId);
			zegoTokenObj.setCourse_name(liveRoomEntity.getLiveRoomName());
			zegoTokenObj.setTimestamp(System.currentTimeMillis());
			zegoTokenObj.setUser_id(user.getUserId().toString());
			zegoTokenObj.setUser_name(user.getNickName());
			zegoTokenObj.setRole(2);
			
			String zjson = JSONUtil.beanToJson(zegoTokenObj);
			mainRedis.opsForValue().set(zkey,zjson,180000,TimeUnit.MILLISECONDS);
			
			Map<String, Object> result = new HashMap<String, Object>();
			if(null != liveRoomEntity){
				result.put("liveUrl", "xuelxuew://login/?lang=zh&sign=9cbcf1c8321ca5ef31b803dc49530918&token="+zkey);
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
    
	@Override
    public String buildKValue(String uid){
        //用#将liveId,uid和毫秒级时间戳拼接成k值原码
        Long timestamp = new Date().getTime();
        String keyStr = uid + "#" + timestamp;
        //用md5加密后得到k值
        String k = DigestUtils.md5Hex(keyStr);
        String mapKey = K_VAULE + k;
        //保存k值到redis
        mainRedis.opsForValue().set(mapKey, String.valueOf(timestamp), K_TIMEOUT, TimeUnit.MILLISECONDS);
        return k;
    }
}
