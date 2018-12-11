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
public class WudinvwangController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(WudinvwangController.class);
	
	public Object wudinvwang(HttpServletRequest req,
			HttpServletResponse res) throws ParseException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		//lv包包
		List<DBObject> lvDay = logMongo.getCollection("activity20140921").find($$("cat", "lvDay")).sort($$("rank", 1)).toArray();
//		List<DBObject> lvDay = logMongo.getCollection("activity20140918").find($$("cat", "nvwangDay")).sort($$("rank", 1)).toArray();
		List<Map<String, Object>> lvDayList = this.giftLogToShow(lvDay);
		map.put("lvDay", lvDayList);

		List<DBObject> lvSum = logMongo.getCollection("activity20140921").find($$("cat", "lvSum")).sort($$("rank", 1)).toArray();
//		List<DBObject> lvSum = logMongo.getCollection("activity20140918").find($$("cat", "nvwangDay")).sort($$("rank", 1)).toArray();
		List<Map<String, Object>> lvSumList = this.giftLogToShow(lvSum);
		map.put("lvSum", lvSumList);
		//水晶鞋
		List<DBObject> shuijingDay = logMongo.getCollection("activity20140921").find($$("cat", "shuijingDay")).sort($$("rank", 1)).toArray();
//		List<DBObject> shuijingDay = logMongo.getCollection("activity20140918").find($$("cat", "nvwangDay")).sort($$("rank", 1)).toArray();
		logger.info("NvshengController.shuijingDay: {}",shuijingDay);
		List<Map<String, Object>> shuijingDayList = this.giftLogToShow(shuijingDay);
		map.put("shuijingDay", shuijingDayList);
		
		List<DBObject> shuijingSum = logMongo.getCollection("activity20140921").find($$("cat", "shuijingSum")).sort($$("rank", 1)).toArray();
//		List<DBObject> shuijingSum = logMongo.getCollection("activity20140918").find($$("cat", "nvwangDay")).sort($$("rank", 1)).toArray();
		logger.info("NvshengController.shuijingSum: {}",shuijingSum);
		List<Map<String, Object>> shuijingSumList = this.giftLogToShow(shuijingSum);
		map.put("shuijingSum", shuijingSumList);
		//女王
		List<DBObject> nvwangSum = logMongo.getCollection("activity20140921").find($$("cat", "nvwangSum")).sort($$("rank", 1)).toArray();
//		List<DBObject> nvwangSum = logMongo.getCollection("activity20140918").find($$("cat", "nvwangDay")).sort($$("rank", 1)).toArray();
		logger.info("NvshengController.nvwangSum: {}",nvwangSum);
		List<Map<String, Object>> nvwangSumRank = this.giftLogToShow(nvwangSum);
		map.put("nvwangSum", nvwangSumRank);
		
		List<DBObject> nvwangDay = logMongo.getCollection("activity20140921").find($$("cat", "nvwangDay")).sort($$("rank", 1)).toArray();
//		List<DBObject> nvwangDay = logMongo.getCollection("activity20140918").find($$("cat", "nvwangDay")).sort($$("rank", 1)).toArray();
		logger.info("NvshengController.nvwangDay: {}",nvwangDay);
		List<Map<String, Object>> nvwangDayRank = this.giftLogToShow(nvwangDay);
		map.put("nvwangDay", nvwangDayRank);

		
		logger.info("NvshengController:","end");
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
