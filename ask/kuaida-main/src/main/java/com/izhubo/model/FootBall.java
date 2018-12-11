package com.izhubo.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.web.api.Web;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.izhubo.rest.common.doc.MongoKey.*;
import static com.izhubo.rest.common.util.WebUtils.$$;

/**
 * 世界杯射门游戏
 * date: 13-8-13 下午4:28
 *
 */
public enum FootBall {

    //爱的城堡,劳斯莱斯座驾,爱的火山,100000星币,旅行套礼上跑道

//	原来
//    XiGua10(265,10,null,null),
//    MeiGui60(6,60,null,null),
//    LangManHuaShu100(336,100,null,null),
//    YanHua1(21,1,null,null,true),
//    HuoShan1(253,1,null,null, true),
//    COIN100000(null,null,100000,null, true),
//    LvXingTaoLi1(271,1,null,null,true),
//    AiDeChengBao1(249,1,null,null, true),
//    RollsRoyce15Day(null,null,null,120, true),
    
	
//	正式
	XinYunDou10(104,10,null,null,false), //10个幸运豆
	KouHong60(183,60,null,null,false), //60朵口红
	LangManHuaShu8(122,8,null,null,true), //8浪漫花束
	Dan1(166,1,null,null,true), //1个蛋


	XinYunTaoZi10(114,10,null,null,false), //10个幸运桃子
	QingShu10(120,10,null,null,false), //10个情书
	LangManHuaShu10(122,10,null,null,false), //10个浪漫花束
	QiMaWu1(156,1,null,null,true), //1个骑马舞
	JinSeFeiMa15(null,null,null,9,true), //15天金色飞马
	Coin100000(null,null,100000,null,true), //100000金币


	XinYunXing10(106,10,null,null,false), //10个幸运星
	NvWangHuangGuang15(126,15,null,null,false), //15个女王皇冠
	QinQin10(123,10,null,null,false), //10个亲亲
	CaoQunWu2(134,2,null,null,false), //2个草裙舞
	BenCiPaoChe1(138,1,null,null,true), //1个奔驰泡车
	MaSaLaDi15(null,null,null,8,true), //15天玛莎拉蒂
	HuoShan1(139,1,null,null,true), //1个火山
//	Coin100000(null,null,100000,null,true), //100000金币

    
	
//	测试
//	XinYunDou10(117,10,null,null,false), //10个幸运豆
//	KouHong60(110,60,null,null,false), //60朵口红
//	LangManHuaShu8(108,8,null,null,true), //8浪漫花束
//	Dan1(113,1,null,null,true), //1个蛋
//
//
//	XinYunTaoZi10(106,10,null,null,false), //10个幸运桃子
//	QingShu10(118,10,null,null,false), //10个情书
//	LangManHuaShu10(108,10,null,null,false), //10个浪漫花束
//	QiMaWu1(119,1,null,null,true), //1个骑马舞
//	JinSeFeiMa15(null,null,null,9,true), //15天金色飞马
//	Coin100000(null,null,100000,null,true), //100000金币
//
//
//	XinYunXing10(104,10,null,null,false), //10个幸运星
//	NvWangHuangGuang15(120,15,null,null,false), //15个女王皇冠
//	QinQin10(121,10,null,null,false), //10个亲亲
//	CaoQunWu2(112,2,null,null,false), //2个草裙舞
//	BenCiPaoChe1(122,1,null,null,true), //1个奔驰泡车
//	MaSaLaDi15(null,null,null,8,true), //15天玛莎拉蒂
//	HuoShan1(116,1,null,null,true), //1个火山
//	Coin100000(null,null,100000,null,true), //100000金币

    
    ;

    private final Integer giftId, giftCount, coin, carId;

    public final boolean broadcast ;

    private final Map<String,Object> unmodifMap;

    public Integer getGiftCount(){
        return this.giftCount;
    }
    private FootBall(Integer giftId, Integer giftCount,  Integer coin, Integer carId){
        this(giftId, giftCount, coin, carId, false);
    }
    private FootBall(Integer giftId, Integer giftCount, Integer coin, Integer carId, boolean broadcast) {
        this.giftId = giftId;
        this.giftCount = giftCount;
        this.coin = coin;
        this.broadcast = broadcast;
        this.carId = carId;

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

    static final Long A_15_DAY_MILL = 24 * 3600 * 1000L * 15;

    public boolean awardOnly(Integer userId,DBCollection users){

        if( null != giftId ){
            if(1 ==  users.update(new BasicDBObject(_id,userId),
                    new BasicDBObject($inc,new BasicDBObject("bag."+giftId,giftCount))).getN()){
                Web.obtainGift(userId,giftId,giftCount);
                return true;
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
            if(users.update($$(_id,userId).append(entryKey,$$($not, $$($gte,now))), $$($set,$$(entryKey,now + A_15_DAY_MILL)) ).getN() == 1
                    || 1 == users.update($$(_id,userId).append(entryKey,$$($gt,now)),$$($inc,$$(entryKey,A_15_DAY_MILL))).getN() ){
                ValueOperations<String,String> valOp = mainRedis.opsForValue();
                String key = KeyUtils.USER.car(userId);
                String currRedis = valOp.get(key);
                if(StringUtils.isBlank(currRedis)){
                    valOp.set(key,carId.toString(),A_15_DAY_MILL, TimeUnit.MILLISECONDS);
                    users.update($$(_id, userId), $$($set, $$("car.curr", carId)));
                }else if ( carId.toString().equals(currRedis) ){
                    Long expSeconds = mainRedis.getExpire(key) + A_15_DAY_MILL/1000;
                    mainRedis.expire(key,expSeconds,TimeUnit.SECONDS);
                }

                return true;
            }
        }
        return false;
    }


    public Integer getPriceOfAward(){
        if(null != giftId){
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

    static final FootBall[] RAN1 = new FootBall[1000];
    static final FootBall[] RAN2 = new FootBall[1000];
    static final FootBall[] RAN3 = new FootBall[1000];

    static {//init RAN1
        int i=0;
        for(;i<600;i++){
            RAN1[i] = XinYunDou10;
        }
        for(;i<980;i++){
            RAN1[i] = KouHong60;
        }
        for(;i<995;i++){
            RAN1[i] = LangManHuaShu8;
        }
        
        for(;i<1000;i++){
            RAN1[i] = Dan1;
        }
        
        shuffle(RAN1);
    }


    static {//init RAN2
        int i=0;
        for(;i<462;i++){
            RAN2[i] = XinYunTaoZi10;
        }
        for(;i<862;i++){
            RAN2[i] = QingShu10;
        }
        for(;i<982;i++){
            RAN2[i] = LangManHuaShu10;
        }
        for(;i<992;i++){
            RAN2[i] = QiMaWu1;
        }
        for(;i<997;i++){
            RAN2[i] = JinSeFeiMa15;
        }
        
        for(;i<1000;i++){
            RAN2[i] = Coin100000;
        }

        shuffle(RAN2);
    }


    static {//init RAN3
        int i=0;
        for(;i<434;i++){
            RAN3[i] = XinYunXing10;
        }
        for(;i<754;i++){
            RAN3[i] = NvWangHuangGuang15;
        }
        for(;i<874;i++){
            RAN3[i] = QinQin10;
        }
        for(;i<974;i++){
            RAN3[i] = CaoQunWu2;
        }
        for(;i<984;i++){
            RAN3[i] = BenCiPaoChe1;
        }
        for(;i<994;i++){
            RAN3[i] = MaSaLaDi15;
        }
        for(;i<997;i++){
            RAN3[i] = HuoShan1;
        }
        for(;i<1000;i++){
            RAN3[i] = Coin100000;
        }

        shuffle(RAN3);
    }


    public static FootBall ran1(){
        return RAN1[new Random().nextInt(1000)];
    }
    public static FootBall ran2(){
        return RAN2[new Random().nextInt(1000)];
    }
    public static FootBall ran3(){
        return RAN3[new Random().nextInt(1000)];
    }

    static void shuffle(FootBall[] ar){
        Random rnd = new Random();
        for (int i = ar.length - 1; i >= 0; i--){
            int index = rnd.nextInt(i + 1);
            // Simple swap
            FootBall a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
