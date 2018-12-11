package com.izhubo.common.util;


import com.izhubo.rest.ext.RestExtension;
import com.izhubo.model.User;
import groovy.transform.CompileStatic;

import java.nio.charset.Charset;

/**
 *
 * 约定的key 值
 *
 * date: 12-8-17 上午11:34
 *
 * @author: wubinjie@ak.cc
 */
@CompileStatic
public  abstract class KeyUtils {
    public  static byte[] serializer(String string){
        return RestExtension.asBytes(string);
    }

    public  static String decode(byte[] data){
        return RestExtension.asString(data);
    }

    /**
     * 用作redis的标记值
     */
    public static final String MARK_VAL = "";

    private static final String SPLIT_CHAR = ":";


    public static String accessToken(String token){
        return "token:"+token;
    }

    public static String vistor_counts()
    {
        return "web:izhubovistor:counts";
    }

    public static String all_cars() {
        return "all:izhubochang:cars";
    }

    public static String all_props() {
        return "all:izhuboapp:props";
    }

    public static String all_medals() {
        return "all:izhubochang:medals";
    }

    public static String all_gifts() {
        return "all:izhubochang:gifts";
    }

    public static String local_gifts_flag() {
        return "local:izhubochang:gifts:flag";
    }

    public static String local_cars_flag() {
        return "local:izhubochang:cars:flag";
    }

    public static class FAMILIES
    {
        public static final String FAMILIES = "family:";

        public static String champion() {
            return FAMILIES +"support:champion";
        }
    }

    public static class USER {

        public static final String USER = "user:";

        public static String hash(Object uid) {
            return USER + uid ;
        }

        public static String vip(Object uid) {
            return USER + uid+":vip";
        }
        public static String vip_hiding(Object uid) {
            return USER + uid+":vip_hiding";
        }
        public static String vip_limit(Object uid) {
            return USER + uid+":vip_limit";
        }

        public static String vip_normal(Object uid) {
            return USER + uid+":vip_normal";
        }
        public static String vip_hiding_normal(Object uid) {
            return USER + uid+":vip_hiding_normal";
        }
        public static String vip_limit_normal(Object uid) {
            return USER + uid+":vip_limit_normal";
        }


        public static String car(Object uid) {
            return USER + uid+":car";
        }

        public static String authCode(Object uid) {
            return USER + uid +":auth";
        }

        public static String token(Object uid) {
            return USER + uid + SPLIT_CHAR+ User.access_token ;
        }

        public static String following(Object uid) {
            return USER + uid + SPLIT_CHAR+ User.following;
        }

        public static String followers(Object uid) {
            return USER + uid + SPLIT_CHAR +User.followers;
        }

        public static String giftList(Object uid) {
            return USER + uid + SPLIT_CHAR+ "gift_list" ;
        }
        public static String giftHash(Object uid) {
            return USER + uid + SPLIT_CHAR + "gift_hash";
        }


        public static String blackClient(String uid) {
            return "uidblack:"+uid;
        }

        /**
         * 限制注册 10分钟1个
         * @param uid
         * @return
         */
        public static String regLimit(String uid) {
            return "reg:"+uid;
        }

        /**
         * 同一IP一天限注册10个帐号
         */
        public static String regLimitTotalPerIp(String ip) {
            return "regip:"+ip;
        }

        /**
         * 同一用户一天通过paypal限充200美金
         */
        public static String paypalLimitTotalPerUserId(String userId) {
            return "paypaluserid:"+userId;
        }
        /**
         * 同一IP一天领取星币限50次
         */
        public static String awardLimitTotalPerIp(String ip) {
            return "award:"+ip;
        }

        public static String featherLimit(String uid) {
            return "feather:"+uid;
        }


        public static String onlyToken2id(String token) {
            return "ot2id:"+token;
        }

        /**
         * 同一用户一天购买礼包次数
         */
        public static String bagsLimitTotalPerUserId(String userId) {
            return "bagsuserid:"+userId;
        }

    }

    /**
     * k歌竞技
     */
    public static class SINGING {

        /**
         * 同一用户一天K歌竞技参数次数
         */
        public static String singingApplyLimitTotalPerUserId(String userId) {
            return "singingApplyUserid:"+userId;
        }

        /**
         * 每轮用户投票记录
         */
        public static String singingStarVotePerUserId(String roundId, String starId) {
            return "singingVoteUserid:"+roundId + ":" +starId;
        }

        /**
         *每轮 主播荧光棒排行榜
         */
        public static String singingStarRankGift(String roundId) {
            return "singingStarRankGift:"+roundId;
        }

        /**
         *每轮 主播荧光棒用户送礼排行榜
         */
        public static String singingStarUserRankGift(String roundId) {
            return "singingStarUserRankGift:"+roundId;
        }
        /**
         * 每轮荧光棒总价值
         */
        public static String singingStarGiftTotal(String roundId) {
            return "singingStarGiftTotal:"+roundId;
        }

        /**
         * 轮次运行状态
         * @return
         */
        public static String singingRoundRunning() {
            return "singingRoundRunning";
        }

        /**
         * 轮次信息缓存
         * @param roundId
         * @return
         */
        public static String singingRoundInfo(String roundId) {
            return "singingRoundInfo:"+roundId;
        }

        /**
         * 每轮主播前三名排行榜
         * @return
         */
        public static String singingRoundStarRank() {
            return "singingRoundStarRank";
        }

    }

    public static class CHANNEL {
        public static String room(Object roomId) {
            return "ROOMchannel:"+roomId;
        }

        public static String user(Object userId) {
            return "USERchannel:"+userId;
        }
        public static String all() {
            return "ALLchannel";
        }

    }

    public static class LUCK {
        public static String powerList() {
            return "luck:powerlist";
        }
        public static String prizePool() {
            return "luck:prizepool";
        }
    }

    public static class PUBLIC {
        public static String taglist(Object tagId) {
            return "taglist:" + tagId;
        }

        public static String appleUsers() {
            return "apple:user" ;
        }
    }

    /**
     * 房间
     */
    public static class ROOM {
        public static final String ROOM = "room:";
        public static String shutup(Object roomId, Object uid) {
            return ROOM + roomId + ":shutup:" + uid;
        }
        @Deprecated
        public static String shutupSet(Object roomId) {
            return ROOM + roomId + ":shutup:set";
        }
        @Deprecated
        public static String kickSet(Object roomId) {
            return ROOM + roomId + ":kick:set";
        }
        public static String users(Object roomId) {
            return ROOM + roomId + ":users";
        }

        public static String kick(Object roomId, Object uid) {
            return ROOM + roomId + ":kick:" + uid;
        }


        public static String liveFlag(Object roomId) {
            return ROOM + roomId + ":live";
        }
        
        public static String stuliveFlag(Object roomId) {
             return ROOM + roomId + ":qiangda:stulive";
        }


        public static String clientFlagHash(Object roomId) {
            return ROOM + roomId + ":client";
        }

        public static String admin(Object roomId) {
            return ROOM + roomId + ":admin:string";
        }

        public static String familysRank(Object roomId) {
            return ROOM + roomId + ":familys:rank:string";
        }

        public static String photoCount(Object roomId) {
            return ROOM + roomId + ":photo:count:string";
        }
        /**
         * 保留最近的礼物跑马灯队列
         */
        public static String GIFT_MARQUEE_LIST = "gift:marquee:list";

    }

    /**
     * 宝藏
     */
    public static class TREASURE {
        public static final String TREASURE = "treasure:";

        public static String noticeIds() {
            return TREASURE +"roomIds:notice";
        }

        public static String room(Object roomId) {
            return TREASURE + "roomId:"+roomId ;
        }
    }

    public static class LIVE {


        public static String puzzle(Object roomId){
            return LIVE+roomId+":puzzle";
        }

        public static final String LIVE = "live:";
        public static String all(Object roomId){
            return LIVE+ roomId + ":*";
        }

        public static String sofaHash(Object roomId){
            return LIVE+ roomId + ":sofa:hash";
        }

        public static String sofaZset(Object roomId){
            return LIVE+ roomId + ":sofa:zset";
        }

        public static String userCostZset(Object roomId){
            return LIVE+ roomId + ":user:costzset";
        }

        public static String blackStar(Object uid){
            return LIVE+"black:"+uid;
        }

        public static String special_gifts_12json = LIVE +"12json";
        public static String myCount(Object roomId) {
            return LIVE+"me_special_gifts:"+roomId;
        }

        public static String tenyuan_userset = "tenyuan_userset";

        public static String roomFamilysRank(Object roomId){
            return LIVE + roomId + ":roomFamilysRank";
        }
    }

    /**
     * Message 消息
     */
    public static class MESSAGE {
        public static final String MESSAGE = "msg:" ;

        public static String hash(Object messageId) {
            return MESSAGE + messageId ;
        }
    }

    /**
     * Medal 徽章
     */
    public static class MEDAL {
        public static final String MEDAL = "medal:" ;

        //徽章累计进度
        public static String medalProgress(Object medalId, Object uid){
            return MEDAL+"progress:" + uid + ":" + medalId;
        }
        //用户徽章
        public static String medals(Object medalId, Object uid){
            return MEDAL + uid+":"+medalId;
        }
        //徽章id
        public static String medalsExist(Object uid){
            return MEDAL + "exists:" + uid;
        }

        //徽章id列表
        public static String medalsList(Object uid){
            return MEDAL + "list:" + uid;
        }
    }

    /**
     * black_blist
     */
    public static class BLACKBLIST
    {
        public static final String BLACKBLIST = "blackblist:" ;

        public static String blacklists(Integer type) {
            return BLACKBLIST + type + ":blacklists";
        }
    }

    public static class Actives
    {
        public static final String ACTIVES = "Actives:" ;
        /**
         * 同一IP一天限加机会10次
         */
        public static String chanceLimitTotalPerIp(String ip) {
            return "qshare_chance:"+ip;
        }

        /**
         * 劳动节活动获得徽章领取一次
         */
        public static String LaodongMedalLimit = "LaodongMedalLimit";
    }


    /**
     * 用户昵称 -> id 转换
     */
    public static final String NAME2ID_NS = "_name2id_:";
    
    public static class TOPICES{
    	/*********    提交问题       ********/
    	public static final String TOPICES = "topices:";
    	public static final String TOPICESLIST = "topiceQueue";
    	public static final Long TPICES_INDUSTRYS_TIME = 3L;
    	
    	public static String tpicesIndustrys(String topic_id){
    		return TOPICES + topic_id;
    	}
    	
    	/*********    聊天超时       ********/
    	public static final String TOPICES_CHAT_TIMEOUT = "topices_chat_timeout:";
    	public static final Long TPICES_CHAT_TIMEOUT = 24L;
    	public static String tpicesChatTimeOut(String topic_id){
    		return TOPICES_CHAT_TIMEOUT + topic_id;
    	}
    	
    	
    }
    
}
