package com.izhubo.model;

import static com.izhubo.rest.common.doc.MongoKey.$gt;
import static com.izhubo.rest.common.doc.MongoKey.$gte;
import static com.izhubo.rest.common.doc.MongoKey.$inc;
import static com.izhubo.rest.common.doc.MongoKey.$not;
import static com.izhubo.rest.common.doc.MongoKey.$set;
import static com.izhubo.rest.common.doc.MongoKey._id;
import static com.izhubo.rest.common.util.WebUtils.$$;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.web.api.Web;

/**
 * Egg. Broken.
 *
 * http://redmine.izhubo.com//redmine/projects/xinyuan/wiki/%E6%95%B0%E5%80%BC%E8%AE%BE%E5%AE%9A#砸金蛋
 *
 * date: 13-8-13 下午4:28
 *
 * @author: wubinjie@ak.cc
 */
public enum EggBroken {

//    BBTang1(117, 1, null, null),
//    /**
//     * 玫瑰
//     */
//    MeiGui1(6,1,null,null),
//    COIN100(null,null,100,null),
//    /**
//     * 西瓜
//     */
//    XiGua10(265,10,null,null),
//    COIN400(null,null,400,null),
//    /**
//     * 蛋糕
//     */
//    DanGao5(8,5,null,null),
//    COIN2000(null,null,2000,null,true),
//    /**
//     * 海洋？
//     */
//    HaiYang1(20,1,null,null,true),
//    /**
//     * 乌龟
//     */
//    WuGui30Day(null,null,null,3,true),
//    /**
//     * 金币10000
//     */
//    COIN10000(null,null,10000,null,true),
//
//    Pig1(5,1,null,null),
//    /**
//     * 掌声
//     */
//    ZhangSheng1(7,1,null,null),
//    MeiGui50(6,50,null,null),
//    BingQiLin50(182,50,null,null),
//    
//    MeiGui100(122,100,null,null),
//    YanHua1(21,1,null,null,true),
//    ChengBao1(249,1,null,null,true),
//    Ferrari30Day(null,null,null,50,true),
//
//    HongBao1(255,1,null,null),
//    NaiZui50(264,50,null,null),
//    MaiKeFeng1(9,1,null,null),
//    MeiGui300(6,300,null,null),
//    KouHong100(268,100,null,null),
//    Kiss1(225,1,null,null,true),
//    HuanQiu1(24,1,null,null,true),
//    HuoShan1(253,1,null,null,true),
    
    
    
    //伍斌杰新增
	XinYunXing1(106,1,null,null,false), //1个幸运星
	SiYeCao4(105,4,null,null,false), //4个四叶草
	LanManHuaShu2(122,2,null,null,false), //2个浪漫花束
	TieFen1(197,1,null,null,true), //1个铁粉
	WuGui30(null,null,null,3,true), //30天乌龟
	Coin10000(null,null,10000,null,true), //10000金币





	KouHong5(183,5,null,null,false), //5个口红
	BingQiLin15(182,15,null,null,false), //15个冰淇淋
	LanManHuaShu5(122,5,null,null,false), //5个浪漫花束
	CaoQunWu1(134,1,null,null,true), //1个草裙舞
	Dan1(166,1,null,null,true), //1个蛋
	FaLaLi30(null,null,null,10,true), //30天法拉利






	XinYunTaoZi10(114,10,null,null,false), //10个幸运桃子
	XinYunBaoZu30(110,30,null,null,false), //30个幸运爆竹
	KouHong60(183,60,null,null,false), //60个口红
	FuDai2(177,2,null,null,true), //2个福袋
	Nobody1(171,1,null,null,true), //1个nobody
	HuoShan1(139,1,null,null,true), //1个火山



	
	
	//测试
//	XinYunXing1(104,1,null,null,false), //1个幸运星
//	SiYeCao10(105,10,null,null,false), //10个四叶草
//	LanManHuaShu5(108,5,null,null,false), //5个浪漫花束
//	TieFen1(109,1,null,null,true), //1个铁粉
//	WuGui30(null,30,null,3,true), //30天乌龟
//	Coin10000(null,null,10000,null,true), //10000金币
//
//
//
//
//
//	KouHong1(110,1,null,null,false), //1个口红
//	BingQiLin50(111,50,null,null,false), //50个冰淇淋
//	LanManHuaShu20(108,20,null,null,false), //20个浪漫花束
//	CaoQunWu1(112,1,null,null,true), //1个草裙舞
//	Dan1(113,1,null,null,true), //1个蛋
//	NanGuaChe30(null,30,null,4,true), //30天南瓜车
//
//
//
//
//
//
//	XinYunTaoZi1(106,1,null,null,false), //1个幸运桃子
//	XinYunBaoZu50(107,50,null,null,false), //50个幸运爆竹
//	KouHong100(110,100,null,null,false), //100个口红
//	FuDai2(114,2,null,null,true), //2个福袋
//	Nobody2(115,2,null,null,true), //2个nobody
//	HuoShan1(116,1,null,null,true), //1个火山



    ;
    private final Integer giftId,giftCount, coin,carId;
    public final boolean broadcast ;
    private final Map<String,Object> unmodifMap;
    /**
     * 
     * @param giftId
     * @param giftCount
     * 数量？
     * @param coin
     * 金币？
     * @param carId
     * ？
     */
    private EggBroken(Integer giftId, Integer giftCount, Integer coin, Integer carId){
        this(giftId, giftCount, coin, carId,false);
    }
    private EggBroken(Integer giftId, Integer giftCount, Integer coin, Integer carId,boolean broadcast ) {
        this.giftId = giftId;
        this.giftCount = giftCount;
        this.coin = coin;
        this.carId = carId;
        this.broadcast = broadcast;

        Map<String,Object> jsonMap = new HashMap<String,Object>();
        jsonMap.put("_id",name());
        if(null != giftId){
            jsonMap.put("gift_id",giftId);
        }
        if(null != giftCount){
            jsonMap.put("gift_count",giftCount);
        }
        if(null != coin){
            jsonMap.put("coin",coin);
        }
        if(null != carId){
            jsonMap.put("car_id",carId);
        }
        unmodifMap = Collections.unmodifiableMap(jsonMap);
    }

    public Integer getGiftId(){
        return this.giftId;
    }

    public Integer getGiftCount(){
        return this.giftCount;
    }

    public Integer getCoin(){
        return this.coin;
    }

    public Integer getCarId(){
        return this.carId;
    }

    static final Long A_30_DAY_MILL = 24 * 3600 * 1000L * 30;

    public boolean awardOnly(Integer userId,DBCollection users){

        if( null != giftId ){
             if(1 ==  users.update(new BasicDBObject(_id,userId),
                    new BasicDBObject($inc,new BasicDBObject("bag."+giftId,giftCount))).getN())
            {
                Web.obtainGift(userId,giftId,giftCount);
                return true ;
            }
        }

        if(null != coin){
            return 1 == users.update(new BasicDBObject(_id,userId),
                    new BasicDBObject($inc,new BasicDBObject(Finance.finance$coin_count,coin))).getN();
        }

        if(null != carId){
            String entryKey = "car." + carId;
            Long now = System.currentTimeMillis();
            StringRedisTemplate mainRedis = Web.mainRedis;
            if(users.update($$(_id,userId).append(entryKey,$$($not, $$($gte,now))), $$($set,$$(entryKey,now + A_30_DAY_MILL)) ).getN() == 1
                    || 1 == users.update($$(_id,userId).append(entryKey,$$($gt,now)),$$($inc,$$(entryKey,A_30_DAY_MILL))).getN() ){
                ValueOperations<String,String> valOp = mainRedis.opsForValue();
                String key = KeyUtils.USER.car(userId);
                String currRedis = valOp.get(key);
                if(StringUtils.isBlank(currRedis)){
                    valOp.set(key,carId.toString(),A_30_DAY_MILL, TimeUnit.MILLISECONDS);
                    users.update($$(_id, userId), $$($set, $$("car.curr", carId)));
                }else if ( carId.toString().equals(currRedis) ){
                    Long expSeconds = mainRedis.getExpire(key) + A_30_DAY_MILL/1000;
                    mainRedis.expire(key,expSeconds,TimeUnit.SECONDS);
                }

                return true;
            }
        }

        return false;

//        Integer roomId,
//        BaseController.publish(KeyUtils.CHANNEL.room(roomId),
//                ""
//        );
//        if(broadcast){
//            BaseController.publish(KeyUtils.CHANNEL.all(),
//                    ""
//            );
//        }
    }


    
    private static Logger logger = LoggerFactory.getLogger(EggBroken.class);

    public Integer getPriceOfAward(){
        if(null != giftId){
        	
            logger.info("giftId: {}", giftId);
            
            DBObject gift = Web.getGiftById(giftId);

            Integer price = (Integer)gift.get("coin_price");
            Integer cost = giftCount * price;
            return cost;
        }

        if(null != coin){
            return coin;
        }

        if(null != carId){
            DBObject car = Web.getCarById(carId);
            Integer price = (Integer)car.get("coin_price");
            return price;
        }
        return 0;
    }

    public Map<String,Object> jsonMap(){
        return unmodifMap;
    }

    static final EggBroken[] RAN1 = new EggBroken[1000];
    static final EggBroken[] RAN2 = new EggBroken[1000];
    static final EggBroken[] RAN3 = new EggBroken[1000];

    static {//init RAN1
        int i=0;
        for(;i<620;i++){
            RAN1[i] = XinYunXing1;
        }
        for(;i<910;i++){
            RAN1[i] = SiYeCao4;
        }
        for(;i<990;i++){
            RAN1[i] = LanManHuaShu5;
        }
        for(;i<997;i++){
            RAN1[i] = TieFen1;
        }
        for(;i<999;i++){
            RAN1[i] = WuGui30;
        }
        RAN1[999] = Coin10000;
        shuffle(RAN1);
    }


    static {//init RAN2
        int i=0;
        for(;i<660;i++){
            RAN2[i] = KouHong5;
        }
        for(;i<940;i++){
            RAN2[i] = BingQiLin15;
        }
        for(;i<990;i++){
            RAN2[i] = LanManHuaShu5;
        }
        for(;i<995;i++){
            RAN2[i] = CaoQunWu1;
        }
        for(;i<998;i++){
            RAN2[i] = Dan1;
        }
        RAN2[998] = Dan1;
        RAN2[999] = Dan1;
        shuffle(RAN2);
    }


    static {//init RAN3
        int i=0;
        for(;i<630;i++){
            RAN3[i] = XinYunTaoZi10;
        }
        for(;i<915;i++){
            RAN3[i] = EggBroken.XinYunBaoZu30;
        }
        for(;i<985;i++){
            RAN3[i] = KouHong60;
        }
        for(;i<997;i++){
            RAN3[i] = FuDai2;
        }
        for(;i<999;i++){
            RAN3[i] = Nobody1;
        }
        RAN3[999] = HuoShan1;
        shuffle(RAN3);
    }


    public static EggBroken ran1(){
        return RAN1[new Random().nextInt(1000)];
    }
    public static EggBroken ran2(){
        return RAN2[new Random().nextInt(1000)];
    }
    public static EggBroken ran3(){
        return RAN3[new Random().nextInt(1000)];
    }

    static void shuffle(EggBroken[] ar){
        Random rnd = new Random();
        for (int i = ar.length - 1; i >= 0; i--){
            int index = rnd.nextInt(i + 1);
            // Simple swap
            EggBroken a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
