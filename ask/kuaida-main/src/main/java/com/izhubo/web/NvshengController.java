package com.izhubo.web;

import static com.izhubo.rest.common.util.WebUtils.$$;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.izhubo.web.BaseController;



@Controller
public class NvshengController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(NvshengController.class);
	
	public Object nvsheng(HttpServletRequest req,
			HttpServletResponse res) throws ParseException {
		logger.info("ZQHD2Controller:","start");
		//主播总榜
		Map<String, Object> map = new HashMap<String, Object>();

		List<DBObject> starresult = logMongo.getCollection("activity20140918").find($$("cat", "meiliDay")).sort($$("rank", 1)).toArray();
		logger.info("ZQHD2Controller.starresult: {} ",starresult.size());
		List<Map<String, Object>> starTotal = this.giftLogToShow(starresult);
		map.put("meiliDay", starTotal);

		List<DBObject> userresult = logMongo.getCollection("activity20140918").find($$("cat", "meiliSum")).sort($$("rank", 1)).toArray();
		logger.info("ZQHD2Controller.userresult: {}",userresult);
		List<Map<String, Object>> userTotal = this.giftLogToShow(userresult);
		map.put("meiliSum", userTotal);

		List<DBObject> starfordayresult = logMongo.getCollection("activity20140918").find($$("cat", "shanyaoDay")).sort($$("rank", 1)).toArray();
		logger.info("ZQHD2Controller.starDay: {}",starfordayresult);
		List<Map<String, Object>> starDay = this.giftLogToShow(starfordayresult);
		map.put("shanyaoDay", starDay);
		
		List<DBObject> userforday = logMongo.getCollection("activity20140918").find($$("cat", "shanyaoSum")).sort($$("rank", 1)).toArray();
		logger.info("ZQHD2Controller.userDay: {}",userforday);
		List<Map<String, Object>> userDay = this.giftLogToShow(userforday);
		map.put("shanyaoSum", userDay);
		
		List<DBObject> nvwangSum = logMongo.getCollection("activity20140918").find($$("cat", "nvwangSum")).sort($$("rank", 1)).toArray();
		logger.info("ZQHD2Controller.nvwangSum: {}",nvwangSum);
		List<Map<String, Object>> nvwangSumRank = this.giftLogToShow(nvwangSum);
		map.put("nvwangSum", nvwangSumRank);
		
		List<DBObject> nvwangDay = logMongo.getCollection("activity20140918").find($$("cat", "nvwangDay")).sort($$("rank", 1)).toArray();
		logger.info("ZQHD2Controller.nvwangDay: {}",nvwangDay);
		List<Map<String, Object>> nvwangDayRank = this.giftLogToShow(nvwangDay);
		map.put("nvwangDay", nvwangDayRank);

		
		logger.info("ZQHD2Controller:","end");
		return map;
	}
	
	private List<Map<String, Object>> giftLogToShow(List<DBObject> giftLogs){
		List<Map<String, Object>> toShow = new LinkedList<>();
		for(int index = 0;index < giftLogs.size();index++){
			DBObject giftlog = giftLogs.get(index);
			Integer userid = (Integer) giftlog.get("user_id");
			DBObject starresult1 = mainMongo.getCollection("users").findOne(new BasicDBObject("_id",userid));
			
			Map<String, Object> star = new HashMap<>();
			star.put("_id", starresult1.get("_id"));
			star.put("num", giftlog.get("num"));
			star.put("rank", giftlog.get("rank"));
			star.put("nick_name", starresult1.get("nick_name"));
			star.put("finance.coin_spend_total", ((DBObject)starresult1.get("finance")).get("coin_spend_total"));
			star.put("finance.bean_count_total", ((DBObject)starresult1.get("finance")).get("bean_count_total"));
			toShow.add(star);
		}
		
		return toShow;
	}
}
