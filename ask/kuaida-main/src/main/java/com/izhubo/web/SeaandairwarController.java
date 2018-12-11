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
public class SeaandairwarController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(SeaandairwarController.class);
	
	public Object seaandairwar(HttpServletRequest req,
			HttpServletResponse res) throws ParseException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<DBObject> aircraftcarrierDay = logMongo.getCollection("activityseaandairwar").find($$("cat", "aircraftcarrierDay")).sort($$("rank", 1)).toArray();
		List<Map<String, Object>> aircraftcarrierDayList = this.giftLogToShow(aircraftcarrierDay);
		map.put("aircraftcarrierDay", aircraftcarrierDayList);
		logger.info("aircraftcarrierDayList: {}", aircraftcarrierDayList);
		
		List<DBObject> aircraftcarrierSum = logMongo.getCollection("activityseaandairwar").find($$("cat", "aircraftcarrierSum")).sort($$("rank", 1)).toArray();
		List<Map<String, Object>> aircraftcarrierSumList = this.giftLogToShow(aircraftcarrierSum);
		map.put("aircraftcarrierSumList", aircraftcarrierSumList);
		logger.info("aircraftcarrierSumList: {}", aircraftcarrierSumList);
		
		List<DBObject> tankDay = logMongo.getCollection("activityseaandairwar").find($$("cat", "tankDay")).sort($$("rank", 1)).toArray();
		List<Map<String, Object>> tankDayList = this.giftLogToShow(tankDay);
		map.put("tankDay", tankDayList);
		logger.info("tankDayList: {}", tankDayList);
		
		List<DBObject> tankSum = logMongo.getCollection("activityseaandairwar").find($$("cat", "tankSum")).sort($$("rank", 1)).toArray();
		List<Map<String, Object>> tankSumList = this.giftLogToShow(tankSum);
		map.put("tankSum", tankSumList);
		logger.info("tankSumList: {}", tankSumList);
		
		List<DBObject> fighterDay = logMongo.getCollection("activityseaandairwar").find($$("cat", "fighterDay")).sort($$("rank", 1)).toArray();
		List<Map<String, Object>> fighterDayRank = this.giftLogToShow(fighterDay);
		map.put("fighterDayRank", fighterDayRank);
		logger.info("fighterDayRank: {}", fighterDayRank);
		
		List<DBObject> fighterSum = logMongo.getCollection("activityseaandairwar").find($$("cat", "fighterSum")).sort($$("rank", 1)).toArray();
		List<Map<String, Object>> fighterSumRank = this.giftLogToShow(fighterSum);
		map.put("fighterSum", fighterSumRank);
		logger.info("fighterSumRank: {}", fighterSumRank);
		
		logger.info("map: {}", map);
		
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
