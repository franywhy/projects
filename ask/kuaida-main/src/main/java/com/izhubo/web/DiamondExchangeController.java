package com.izhubo.web;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.izhubo.rest.common.doc.MongoKey.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;

import com.izhubo.web.drawReward.DiamondExchangeEnum;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.izhubo.rest.anno.RestWithSession;
import com.izhubo.rest.web.spring.Interceptors;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.web.BaseController;
import com.izhubo.web.GiftCarIdEnum;
import com.izhubo.web.api.Web;

@Controller
@Interceptors
@RestWithSession
public class DiamondExchangeController extends BaseController {
	private static Logger logger = LoggerFactory
			.getLogger(DiamondExchangeController.class);
	Map<String, Object> map = new HashMap<String, Object>();
	public static final long Fifteen_day_mill = 24l * 3600 * 1000 * 15;

	public Object exchange(HttpServletRequest req, HttpServletResponse res) {
		/**
		 * exchangeid 兑换物品id 1： 30个 乌龟座驾15天 2： 50个 毛驴座驾15天 3： 80个 青牛座驾15天 4：
		 * 100个 金色飞马座驾15天 5： 120个 貔貅座驾15天 6： 150个 年兽座驾15天 7： 180个 狼骑士座驾15天
		 */
		int exchangeid = Integer.parseInt(req.getParameter("exchangeid"));
		int userid = Web.getCurrentUserId();
		Boolean bl;
		switch (exchangeid) {
		case 1:
			// 检验并扣除水晶数
			bl = checkAndDeductedDiamond(userid,
					DiamondExchangeEnum.CarTortoise15.getDiamondCount());
			if (bl) {
				sendCar(userid,
						DiamondExchangeEnum.CarTortoise15.getCarId(serverType),
						Fifteen_day_mill);
				addLog(userid,
						DiamondExchangeEnum.CarTortoise15.getDiamondCount(),
						"乌龟坐骑15天");
				map.put("Code", 2);
				map.put("Msg", "水晶兑换成功！");
			} else {
				map.put("Code", 1);
				map.put("Msg", "水晶数不足,兑换失败");
			}
			break;
		case 2:
			// 检验并扣除水晶数
			bl = checkAndDeductedDiamond(userid,
					DiamondExchangeEnum.CarDonkey15.getDiamondCount());
			if (bl) {
				sendCar(userid,
						DiamondExchangeEnum.CarDonkey15.getCarId(serverType),
						Fifteen_day_mill);
				addLog(userid,
						DiamondExchangeEnum.CarDonkey15.getDiamondCount(),
						"毛驴坐骑15天");
				map.put("Code", 2);
				map.put("Msg", "水晶兑换成功！");
			} else {
				map.put("Code", 1);
				map.put("Msg", "水晶数不足,兑换失败");
			}

			break;
		case 3:
			// 检验并扣除水晶数
			bl = checkAndDeductedDiamond(userid,
					DiamondExchangeEnum.CarGreen15.getDiamondCount());
			if (bl) {
				sendCar(userid,
						DiamondExchangeEnum.CarGreen15.getCarId(serverType),
						Fifteen_day_mill);
				addLog(userid,
						DiamondExchangeEnum.CarGreen15.getDiamondCount(),
						"青牛坐骑15天");
				map.put("Code", 2);
				map.put("Msg", "水晶兑换成功！");
			} else {
				map.put("Code", 1);
				map.put("Msg", "水晶数不足,兑换失败");
			}

			break;
		case 4:
			// 检验并扣除水晶数
			bl = checkAndDeductedDiamond(userid,
					DiamondExchangeEnum.CarGoldenPegasus15.getDiamondCount());
			if (bl) {
				sendCar(userid,
						DiamondExchangeEnum.CarGoldenPegasus15
								.getCarId(serverType), Fifteen_day_mill);
				addLog(userid,
						DiamondExchangeEnum.CarGoldenPegasus15
								.getDiamondCount(), "金色飞马坐骑15天");
				map.put("Code", 2);
				map.put("Msg", "水晶兑换成功！");
			} else {
				map.put("Code", 1);
				map.put("Msg", "水晶数不足,兑换失败");
			}

			break;
		case 5:
			// 检验并扣除水晶数
			bl = checkAndDeductedDiamond(userid,
					DiamondExchangeEnum.CarBraveTroops15.getDiamondCount());
			if (bl) {
				sendCar(userid,
						DiamondExchangeEnum.CarBraveTroops15
								.getCarId(serverType), Fifteen_day_mill);
				addLog(userid,
						DiamondExchangeEnum.CarBraveTroops15.getDiamondCount(),
						"貔貅坐骑15天");
				map.put("Code", 2);
				map.put("Msg", "水晶兑换成功！");
			} else {
				map.put("Code", 1);
				map.put("Msg", "水晶数不足,兑换失败");
			}

			break;
		case 6:
			// 检验并扣除水晶数
			bl = checkAndDeductedDiamond(userid,
					DiamondExchangeEnum.CarBeast15.getDiamondCount());
			if (bl) {
				sendCar(userid,
						DiamondExchangeEnum.CarBeast15.getCarId(serverType),
						Fifteen_day_mill);
				addLog(userid,
						DiamondExchangeEnum.CarBeast15.getDiamondCount(),
						"年兽坐骑15天");
				map.put("Code", 2);
				map.put("Msg", "水晶兑换成功！");
			} else {
				map.put("Code", 1);
				map.put("Msg", "水晶数不足,兑换失败");
			}
			break;
		case 7:
			// 检验并扣除水晶数
			bl = checkAndDeductedDiamond(userid,
					DiamondExchangeEnum.CarWolfRider15.getDiamondCount());
			if (bl) {
				sendCar(userid,
						DiamondExchangeEnum.CarWolfRider15.getCarId(serverType),
						Fifteen_day_mill);
				addLog(userid,
						DiamondExchangeEnum.CarWolfRider15.getDiamondCount(),
						"狼骑士坐骑15天");
				map.put("Code", 2);
				map.put("Msg", "水晶兑换成功！");
			} else {
				map.put("Code", 1);
				map.put("Msg", "水晶数不足,兑换失败");
			}
			break;
		}
		return map;

	}

	public Boolean checkAndDeductedDiamond(long userid, long DiamondCount) {
		Integer userDiamondCount = 0;
		DBCollection users = users();
		DBObject query = new BasicDBObject(_id, userid);
		DBObject user = users.findOne(query);
		DBObject bag = (DBObject) user.get("bag");
		logger.info("bag:{}", bag);
		userDiamondCount = Integer.parseInt(bag.get(
				GiftCarIdEnum.Diamond.getId(serverType) + "").toString());
		Boolean bl = userDiamondCount >= DiamondCount ? true : false;
		if (bl) {
			users.update(query, new BasicDBObject($inc, new BasicDBObject(
					"bag." + GiftCarIdEnum.Diamond.getId(serverType), -1
							* DiamondCount)));
		}
		return bl;
	}

	/**
	 * 赠送座驾
	 * 
	 * @param userId
	 * @param carId
	 * @param expMillSecond
	 */
	public void sendCar(long userId, long carId, long expMillSecond) {
		String entryKey = "car." + carId;
		Long now = System.currentTimeMillis();
		StringRedisTemplate mainRedis = Web.mainRedis;
		if (users().update(
				$$(_id, userId).append(entryKey, $$($not, $$($gte, now))),
				$$($set, $$(entryKey, now + expMillSecond))).getN() == 1
				|| 1 == users().update(
						$$(_id, userId).append(entryKey, $$($gt, now)),
						$$($inc, $$(entryKey, expMillSecond))).getN()) {
			ValueOperations<String, String> valOp = mainRedis.opsForValue();
			String key = KeyUtils.USER.car(userId);
			String currRedis = valOp.get(key);
			if (StringUtils.isBlank(currRedis)) {
				valOp.set(key, Long.toString(carId).toString(), expMillSecond,
						TimeUnit.MILLISECONDS);
				users().update($$(_id, userId), $$($set, $$("car.curr", carId)));
			} else if (Long.toString(carId).equals(currRedis)) {
				Long expSeconds = (Long) mainRedis.getExpire(key)
						+ (Long) expMillSecond / 1000;
				mainRedis.expire(key, expSeconds, TimeUnit.SECONDS);
			}
		} else {
			logger.warn("sendCar fail: userId: {}, carId: {}, expMillSecond: ",
					userId, carId, expMillSecond);
		}
	}

	public static BasicDBObject $$(String key, Object value) {
		return new BasicDBObject(key, value);
	}

	private void addLog(long userid, int DiamondCount, String PrizeName) {
		// 通过id获取用户name
		BasicDBObject query_id = new BasicDBObject("_id", userid);
		DBObject user = mainMongo.getCollection("users").findOne(query_id);
		String nickName = user.get("nick_name").toString();

		Map<Object, Object> map = new HashMap<>();
		map.put("userid", userid);
		map.put("nickName", nickName);
		map.put("DiamondCount", DiamondCount);
		map.put("PrizeName", PrizeName);
		map.put("time", System.currentTimeMillis());
		adminMongo.getCollection("diamondExchange_log").save(
				new BasicDBObject(map));
	}

	public Object getCount(HttpServletRequest req, HttpServletResponse res) {
		Integer userDiamondCount = 0;
		DBCollection users = users();
		int userid = Web.getCurrentUserId();
		logger.info("userid:{},giftid:{}", userid,
				GiftCarIdEnum.Diamond.getId(serverType));
		try {
			DBObject query = new BasicDBObject(_id, userid);
			DBObject user = users.findOne(query);
			DBObject bag = (DBObject) user.get("bag");
			logger.info("bag:{}", bag);
			if(bag != null ){
				userDiamondCount = bag.get(GiftCarIdEnum.Diamond.getId(serverType) + "") == null ? 0 : Integer.parseInt(bag.get(GiftCarIdEnum.Diamond.getId(serverType) + "").toString());
			}			
		} catch (Exception e) {
			logger.info("e", e);
		}		
		map.put("count", userDiamondCount);
		return map;
	}
}
