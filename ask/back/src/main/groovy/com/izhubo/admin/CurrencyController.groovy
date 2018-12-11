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
class CurrencyController extends BaseController {
	
	private DBCollection currency_log(){
		return mainMongo.getCollection("currency_log");
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
				'return {"gtime" :(Math.floor((doc.currency_time+ 28800000) / 86400000 ) * 86400000) - 28800000};'+
				"}"

		String reduceJavasriptCodeString = "function(doc , prev){ "+
				"    prev.money =  prev.money + doc.currency_money;      "+
				"}                     "

		//时间查询条件
		def query_sum_page_recharge = [currency_type: 0, currency_time: [$gte: startTime, $lte: endTime]]
		def query_sum_page_consume = [currency_type: 1, currency_time: [$gte: startTime, $lte: endTime]]
		def query_sum_all = [currency_time: [$gte: stime_long, $lte: etime_long]]

		/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 分组查询  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ **/
		//所有合计
		def all_currency = currency_log().aggregate($$('$match', query_sum_all), $$('$group', [_id: '$currency_type',money:[$sum: '$currency_money']])).results().iterator()
		Double all_money_recharge = 0d
		Double all_money_consume = 0d
		while (all_currency.hasNext()){
			def obj = all_currency.next()
			if(obj){ //currency_type  充值 ,扣减 , 打赏退回  0,1,2
				def currency_type = obj.get('_id') as int
				if (currency_type == 0) {
					all_money_recharge = obj.get('money') as double
				} else if (currency_type == 1) {
					all_money_consume = obj.get('money') as double
				}
			}
		}

		BasicDBObject args_recharge = [$keyf:keyfJavascriptCodeString, initial:[money:0d], $reduce:reduceJavasriptCodeString, condition:query_sum_page_recharge]
		BasicDBObject args_consume = [$keyf:keyfJavascriptCodeString, initial:[money:0d], $reduce:reduceJavasriptCodeString, condition:query_sum_page_consume]

		//当前页合计
		BasicDBList groupResult_recharge = currency_log().group(args_recharge)
		BasicDBList groupResult_consume = currency_log().group(args_consume)

		Double page_sum_money_recharge = 0d
		Double page_sum_money_consume = 0d

		List dataList = new ArrayList()
		//item时间
		Long itemTime = startTime

		for(int i = 0 ; i < page_number ; i++){
			def groupResult_recharge_item = getItemByDateLong(itemTime , groupResult_recharge)
			def groupResult_consume_item = getItemByDateLong(itemTime , groupResult_consume)
			Map map = new HashMap()
			map.put("gtime", itemTime)
			if( groupResult_recharge_item != null){
				page_sum_money_recharge = addBigDecimal(page_sum_money_recharge, Float.valueOf(groupResult_recharge_item["money"].toString()) )
				map.put("money_recharge", groupResult_recharge_item["money"])
			}else{
				map.put("money_recharge", 0)
			}

			if( groupResult_consume_item != null) {
				page_sum_money_consume = addBigDecimal(page_sum_money_consume, Float.valueOf(groupResult_consume_item["money"].toString()) )
				map.put("money_consume", Math.abs(groupResult_consume_item["money"]))
			} else {
				map.put("money_consume", 0)
			}
			dataList.add(map)
			itemTime += DataUtils.DAY_MI
		}
		/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 分组查询  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ **/

		Map resultMap = new HashMap()
		resultMap["code"] = 1
		resultMap["data"]  = dataList
		resultMap["page_sum_money_recharge"]  = page_sum_money_recharge
		resultMap["page_sum_money_consume"]  = Math.abs(page_sum_money_consume)
		resultMap["all_money_recharge"]  = all_money_recharge
		resultMap["all_money_consume"]  = Math.abs(all_money_consume)
		resultMap["count"]  = count
		return resultMap
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



 
 