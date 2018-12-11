package com.izhubo.admin

import com.izhubo.common.util.DataUtils
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.springframework.web.bind.ServletRequestUtils

import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.util.WebUtils.$$

@RestWithSession
class BonusdataController extends BaseController {
	
	private DBCollection topic_bunus(){
		return mainMongo.getCollection("topic_bunus");
	}
	private DBCollection topic_bunus_report(){
		return logMongo.getCollection("reportdata_topic_bunus");
	}
	private DBCollection topic_tip(){
		return mainMongo.getCollection("topic_tip");
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest request){
		//翻页查询
		int size = ServletRequestUtils.getIntParameter(request, "size", 20)
		int page = ServletRequestUtils.getIntParameter(request, "page", 1)

		Date stime = Web.getStime(request)
		Date etime = Web.getEtime(request)
		//非空校验
		if(stime == null || etime == null){
			return [code : 0 , data : "参数异常"]
		}

		//时间校验
		Long stime_long = DataUtils.beginTime(stime.getTime())
		Long etime_long = DataUtils.beginTime(etime.getTime())

		//总条数
		int count =(Integer)( (etime_long - stime_long) / DataUtils.DAY_MI + ((etime_long - stime_long) % DataUtils.DAY_MI >0 ? 1 : 0 ))
		//查询的开始时间
		Long startTime = stime_long + (page-1) * size * DataUtils.DAY_MI
		//查询的结束时间
		Long endTime = startTime + size * DataUtils.DAY_MI - 1
		//判断结束时间是否大于用户选择的结束时间
		if(endTime > etime_long ){
			endTime = etime_long
		}
		int page_number =( (endTime - startTime ) / DataUtils.DAY_MI )+ 1

		String keyfJavascriptCodeString = "function(doc){" +
				'return {"gtime" :(Math.floor((doc.timestamp+ 28800000) / 86400000 ) * 86400000) - 28800000};'+
				"}"

		String reduceJavasriptCodeStringBunus = "function(doc , prev){ "+
				"    prev.money =  prev.money + doc.bunus_money;      "+
				"    prev.count =  prev.count + doc.bunus_count;      "+
				"}                     "

		String reduceJavasriptCodeStringTip = "function(doc , prev){ "+
				"    prev.money =  prev.money + doc.kd_money;      "+
				"    prev.count ++;      "+
				"}                     "

		//时间查询条件
		def query_sum_page = [timestamp: [$gte: startTime, $lte: endTime]]
		def query_sum_all = [timestamp: [$gte: stime_long, $lte: etime_long]]

		/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 分组查询  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ **/
		//所有合计
		def all_bunus = topic_bunus_report().aggregate($$('$match', query_sum_all), $$('$group', [_id: null,money:[$sum: '$bunus_money'],count:[$sum: '$bunus_count']])).results().iterator()
		def all_tip = topic_tip().aggregate($$('$match', query_sum_all), $$('$group', [_id: null,money:[$sum: '$kd_money'],count:[$sum: 1]])).results().iterator()
		Integer bunus_count_all = 0
		Double bunus_money_all = 0d
		if(all_bunus.hasNext()){
			def obj = all_bunus.next()
			if(obj){
				bunus_count_all = (obj.get("count") as Double).intValue()
				bunus_money_all = obj.get("money") as Double
			}
		}
		Integer tip_count_all = 0
		Double tip_money_all = 0d
		if(all_tip.hasNext()){
			def obj = all_tip.next()
			if(obj){
				tip_count_all = (obj.get("count") as Double).intValue()
				tip_money_all = obj.get("money") as Double
			}
		}

		BasicDBObject args_bunus = [$keyf:keyfJavascriptCodeString, initial:[money:0d, count:0d], $reduce:reduceJavasriptCodeStringBunus, condition:query_sum_page]
		BasicDBObject args_tip = [$keyf:keyfJavascriptCodeString, initial:[money:0d, count:0d], $reduce:reduceJavasriptCodeStringTip, condition:query_sum_page]

		//当前页合计
		BasicDBList groupResult_bunus = topic_bunus_report().group(args_bunus)
		BasicDBList groupResult_tip = topic_tip().group(args_tip)

		Double page_sum_bunus_money = 0d
		Double page_sum_bunus_count = 0d
		Double page_sum_tip_money = 0d
		Double page_sum_tip_count = 0d

		List dataList = new ArrayList()
		//item时间
		Long itemTime = startTime

		for(int i = 0 ; i < page_number ; i++){
			def groupResult_bunus_item = getItemByDateLong(itemTime , groupResult_bunus)
			def groupResult_tip_item = getItemByDateLong(itemTime , groupResult_tip)
			Map map = new HashMap()
			map.put("gtime", itemTime)
			if( groupResult_bunus_item != null){
				page_sum_bunus_money = addBigDecimal(page_sum_bunus_money, Float.valueOf(groupResult_bunus_item["money"].toString()) )
				page_sum_bunus_count = addBigDecimal(page_sum_bunus_count, Float.valueOf(groupResult_bunus_item["count"].toString()) )
				map.put("bunus_money", groupResult_bunus_item["money"])
				map.put("bunus_count", groupResult_bunus_item["count"])
			}else{
				map.put("bunus_money", 0)
				map.put("bunus_count", 0)
			}

			if( groupResult_tip_item != null) {
				page_sum_tip_money = addBigDecimal(page_sum_tip_money, Float.valueOf(groupResult_tip_item["money"].toString()) )
				page_sum_tip_count = addBigDecimal(page_sum_tip_count, Float.valueOf(groupResult_tip_item["count"].toString()) )
				map.put("tip_money", groupResult_tip_item["money"])
				map.put("tip_count", groupResult_tip_item["count"])
			} else {
				map.put("tip_money", 0)
				map.put("tip_count", 0)
			}
			dataList.add(map)
			itemTime += DataUtils.DAY_MI
		}
		/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 分组查询  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ **/

		Map resultMap = new HashMap()
		resultMap["code"] = 1
		resultMap["data"]  = dataList
		resultMap["bunus_count_all"]  = bunus_count_all
		resultMap["bunus_money_all"]  = bunus_money_all
		resultMap["tip_count_all"]  = tip_count_all
		resultMap["tip_money_all"]  = tip_money_all
		resultMap["page_sum_bunus_count"]  = page_sum_bunus_count
		resultMap["page_sum_bunus_money"]  = page_sum_bunus_money
		resultMap["page_sum_tip_count"]  = page_sum_tip_count
		resultMap["page_sum_tip_money"]  = page_sum_tip_money
		resultMap["count"]  = count
		return resultMap

		/*def query = Web.fillTimeBetween(request);
//		println query.get();
		Crud.list(request,topic_bunus_report(),query.get(),ALL_FIELD, MongoKey.SJ_DESC);*/
	}


	/**
	 * 根据时间匹配数据库查询的结果集
	 * @param time
	 * @param dbo
	 * @return
	 */
	def getItemByDateLong(Long time , BasicDBList dbo){
		def res  = null
		if(dbo != null && dbo.size() > 0){
			for(int i = 0 ; i < dbo.size(); i++){
				def item = dbo.get(i)
				if(item["gtime"] == time){
					res = item
					dbo.remove(dbo)
					break
				}
			}
		}
		return res
	}

	/**
	 * java精度运算double 相加
	 * @param d1
	 * @param d2
	 * @return
	 */
	private double addBigDecimal(double d1,Float d2){
		BigDecimal bd1 = new BigDecimal(Double.toString(d1))
		BigDecimal bd2 = new BigDecimal(Float.toString(d2))
		return bd1.add(bd2).doubleValue()
	}
}



 
 