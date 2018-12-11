package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.common.util.DataUtils
import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicsType
import com.izhubo.model.UserType;
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject

@RestWithSession
class CampusdataController extends BaseController {
	
	public DBCollection topics(){return mainMongo.getCollection("topics");}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest request){
		
		//翻页查询
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		Date stime = Web.getStime(request);
		Date etime = Web.getEtime(request);
		//非空校验
		if(stime == null || etime == null){
			return [code : 0 , data : "参数异常"];
		}

		//时间校验
		Long stime_long = DataUtils.beginTime(stime.getTime());
		Long etime_long = DataUtils.beginTime(etime.getTime());
		
//		总条数
		int count =(Integer)( (etime_long - stime_long) / DataUtils.DAY_MI + ((etime_long - stime_long) % DataUtils.DAY_MI >0 ? 1 : 0 ));
		//查询的开始时间
		Long startTime = stime_long + (page-1) * size * DataUtils.DAY_MI;
		//查询的结束时间
		Long endTime = startTime + size * DataUtils.DAY_MI - 1;
		//判断结束时间是否大于用户选择的结束时间
		if(endTime > etime_long ){
			endTime = etime_long;
		}
		int page_number =( (endTime - startTime ) / DataUtils.DAY_MI )+ 1;
		
		
		//广州市
		Map gzData = queryByCity("广州市" , startTime , endTime , stime_long , etime_long);
		//长沙市
		Map csData = queryByCity("长沙市" , startTime , endTime , stime_long , etime_long);
		//邢台
		Map xtData = queryByCity("邢台市" , startTime , endTime , stime_long , etime_long);
		//蚌埠
		Map bbData = queryByCity("蚌埠市" , startTime , endTime , stime_long , etime_long);
		//韶关市
		Map sgData = queryByCity("韶关市" , startTime , endTime , stime_long , etime_long);
		//桂林市
		Map glData = queryByCity("桂林市" , startTime , endTime , stime_long , etime_long);
		//德阳市
		Map dyData = queryByCity("德阳市" , startTime , endTime , stime_long , etime_long);

		
		Map allData = new HashMap();
		//新增问答数	
		allData["gzData"] = gzData;
		allData["csData"] = csData;
		allData["xtData"] = xtData;
		allData["bbData"] = bbData;
		allData["sgData"] = sgData;
		allData["glData"] = glData;
		allData["dyData"] = dyData;

		Map map = new HashMap();
		map["code"] = 1;
		map["data"]  = allData;
		map["count"]  = count;

		return map;
	}
	
	def queryByCity(String city , Long startTime , Long endTime , Long pageStartTime , Long pageEndTime){
//		queryRegisters(city, startTime, endTime, pageStartTime, pageEndTime);
//		topicCount(city, startTime, endTime, pageStartTime, pageEndTime);
		return ["addUser" : queryRegisters(city, startTime, endTime, pageStartTime, pageEndTime) , "topicCount" : topicCount(city, startTime, endTime, pageStartTime, pageEndTime)]
	}
	
	/**
	 * 新增用户
	 * @param city                   城市
	 * @param startTime          用户选择的开始时间
	 * @param endTime           用户选择的结束时间
	 * @param pageStartTime  分页开始时间
	 * @param pageEndTime   分页结束时间
	 * @return Map 报表
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def queryRegisters(String city , Long startTime , Long endTime , Long pageStartTime , Long pageEndTime){
		int page_number =( (endTime - startTime ) / DataUtils.DAY_MI )+ 1;
		
		
		String keyfJavascriptCodeString = "function(doc){" +
				'return {"gtime" :(Math.floor((doc.timestamp+ 28800000) / 86400000 ) * 86400000) - 28800000};'+
				"}";

		String reduceJavasriptCodeString = "function(doc , prev){ "+
				"    prev.num ++;      "+
				"}                     ";

		//查询条件
		DBObject queryDBObject = new BasicDBObject();
		queryDBObject.put("timestamp", $$('$gte' : startTime , '$lte' : endTime));
		queryDBObject.put("priv", UserType.普通用户.ordinal());
		queryDBObject.put("city", city);

		DBObject queryDBObject_sum = new BasicDBObject();
		queryDBObject_sum.put("timestamp", $$('$gte' : pageStartTime , '$lte' : pageEndTime));
		queryDBObject_sum.put("priv", UserType.普通用户.ordinal());
		queryDBObject_sum.put("city", city);

		BasicDBObject args_addTopic  = new BasicDBObject();
		args_addTopic.put('$keyf' , keyfJavascriptCodeString);
		args_addTopic.put("initial" , $$("num" : 0));
		args_addTopic.put('$reduce' , reduceJavasriptCodeString);
		args_addTopic.put('condition' , queryDBObject);

		BasicDBList groupResult_add = users().group(args_addTopic);

		//新增总计
		Double sum = users().count(queryDBObject_sum);
		
		
		
		//新增当前页总计
		Double page_sum = 0d;
		
		List dataList = new ArrayList();
		//item时间
		Long itemTime = startTime;
		
		//某天时间未注册时 加入当天时间 数量0
		for(int i = 0 ; i < page_number ; i++){
			def groupResult_addTopic_item = getItemByDateLong(itemTime , groupResult_add);
			if( groupResult_addTopic_item != null){
				page_sum = addBigDecimal(page_sum, Float.valueOf(groupResult_addTopic_item["num"].toString()) );
				dataList.add(groupResult_addTopic_item);
			}else{
				Map map = new HashMap();
				map.put("gtime", itemTime);
				map.put("num", 0);
				dataList.add(map)
			}
			itemTime += DataUtils.DAY_MI;
		}
		
		return ["page_sum" : page_sum , "sum" :  sum , "data" : dataList ];
	}
	
	/**
	 * 老师答题报表
	 * @param city                   城市
	 * @param startTime          用户选择的开始时间
	 * @param endTime           用户选择的结束时间
	 * @param pageStartTime  分页开始时间
	 * @param pageEndTime   分页结束时间
	 * @return Map 报表
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def topicCount(String city , Long startTime , Long endTime , Long pageStartTime , Long pageEndTime){
		int page_number =( (endTime - startTime ) / DataUtils.DAY_MI )+ 1;
		
		def teacherList = users().find($$("priv":UserType.主播.ordinal() , "city" : city) , $$("_id" : 1)).toArray();
		BasicDBList groupResult_add = null;
		//新增总计
		Double sum = 0d;
		
		if(teacherList){
			String keyfJavascriptCodeString = "function(doc){" +
					'return {"gtime" :(Math.floor((doc.race_time+ 28800000) / 86400000 ) * 86400000) - 28800000};'+
					"}";

			String reduceJavasriptCodeString = "function(doc , prev){ "+
					"    prev.num ++;      "+
					"}                     ";
					
			List teacherIdList = new ArrayList();
			teacherList.each {def item->
				teacherIdList.add(item["_id"]);
			}
					
			//查询条件
			DBObject queryDBObject = new BasicDBObject();
			queryDBObject.put("race_time", $$('$gte' : startTime , '$lte' : endTime));
			queryDBObject.put("teach_id", $$('$in' : teacherIdList));

			DBObject queryDBObject_sum = new BasicDBObject();
			queryDBObject_sum.put("race_time", $$('$gte' : startTime , '$lte' : endTime));
			queryDBObject_sum.put("teach_id", $$('$in' : teacherIdList));

			BasicDBObject args_addTopic  = new BasicDBObject();
			args_addTopic.put('$keyf' , keyfJavascriptCodeString);
			args_addTopic.put("initial" , $$("num" : 0));
			args_addTopic.put('$reduce' , reduceJavasriptCodeString);
			args_addTopic.put('condition' , queryDBObject);

			groupResult_add = topics().group(args_addTopic);

			//新增总计
			sum = topics().count(queryDBObject_sum);
		}else{
			groupResult_add = new BasicDBList();
		}
		//新增当前页总计
		Double page_sum = 0d;
		
		List dataList = new ArrayList();
		//item时间
		Long itemTime = startTime;
		
		//某天时间未注册时 加入当天时间 数量0
		for(int i = 0 ; i < page_number ; i++){
			def groupResult_addTopic_item = getItemByDateLong(itemTime , groupResult_add);
			if( groupResult_addTopic_item != null){
				page_sum = addBigDecimal(page_sum, Float.valueOf(groupResult_addTopic_item["num"].toString()) );
				dataList.add(groupResult_addTopic_item);
			}else{
				Map map = new HashMap();
				map.put("gtime", itemTime);
				map.put("num", 0);
				dataList.add(map)
			}
			itemTime += DataUtils.DAY_MI;
		}
		return ["page_sum" : page_sum , "sum" :  sum , "data" : dataList ];
	}
	
	/**
	 * 根据时间匹配数据库查询的结果集
	 * @param time
	 * @param dbo
	 * @return
	 */
	def getItemByDateLong(Long time , BasicDBList dbo){
		def res  = null;
		if(dbo != null && dbo.size() > 0){
			for(int i = 0 ; i < dbo.size(); i++){
				def item = dbo.get(i);
				if(item["gtime"] == time){
					res = item;
					dbo.remove(dbo);
					break;
				}
			}
		}
		return res;
	}
	
	/**
	 * java精度运算double 相加
	 * @param d1
	 * @param d2
	 * @return
	 */
	public double addBigDecimal(double d1,Float d2){
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Float.toString(d2));
		return bd1.add(bd2).doubleValue();
	}
	


	
}


 
 