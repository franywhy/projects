package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.common.util.DataUtils
import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicsType
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject

@RestWithSession
class AskdataController extends BaseController {
	
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
		
		
		String keyfJavascriptCodeString = "function(doc){" +
				'return {"gtime" :(Math.floor((doc.timestamp+ 28800000) / 86400000 ) * 86400000) - 28800000};'+
				"}";
		
		
		String reduceJavasriptCodeString = "function(doc , prev){ "+
				"    prev.num ++;      "+
				"}                     ";
		/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 每日提问人数 分组查询  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ **/
		//查询条件
		DBObject queryDBObject_topic_author = new BasicDBObject()
		queryDBObject_topic_author.put("timestamp", $$('$gte' : startTime , '$lte' : endTime))
		DBObject queryDBObject_sum_topic_author = new BasicDBObject()
		queryDBObject_sum_topic_author.put("timestamp", $$('$gte' : stime_long , '$lte' : etime_long))

		//所有提问人数合计
		Double sum_topic_author = topics().aggregate($$('$match', queryDBObject_sum_topic_author), $$('$group', [_id: '$author_id'])).results().iterator().size() as double

		String keyfJavascriptCode = "function(doc){" +
				'return {"gtime" :(Math.floor((doc.timestamp+ 28800000) / 86400000 ) * 86400000) - 28800000, "authorId":doc.author_id};'+
				"}"
		BasicDBObject args_topic_author  = new BasicDBObject()
		args_topic_author.put('$keyf' , keyfJavascriptCode)
		args_topic_author.put("initial" , $$("num" : 0))
		args_topic_author.put('$reduce' , reduceJavasriptCodeString)
		args_topic_author.put('condition' , queryDBObject_topic_author)

		BasicDBList groupResult_topic_author = topics().group(args_topic_author)

		//新增当前页总计
		Double page_sum_topic_author = 0d

		List dataList_topic_author = new ArrayList()
		//item时间
		Long itemTime_topic_author = startTime

		for(int i = 0 ; i < page_number ; i++){
			Double sumAuthor = 0d
			if(groupResult_topic_author != null && groupResult_topic_author.size() > 0){
				for(int j = 0 ; j < groupResult_topic_author.size(); j++){
					def item = groupResult_topic_author.get(j)
					if(item["gtime"] == itemTime_topic_author){
						sumAuthor++
						groupResult_topic_author.remove(groupResult_topic_author)
					}
				}
			}
			page_sum_topic_author = addBigDecimal(page_sum_topic_author, Float.valueOf(sumAuthor.toString()))

			Map map = new HashMap()
			map.put("gtime", itemTime_topic_author)
			map.put("num", sumAuthor)
			dataList_topic_author.add(map)

			itemTime_topic_author += DataUtils.DAY_MI
		}
		/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 每日提问人数 分组查询  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ **/


		/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 新增问答数 分组查询  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ **/

		//查询条件
		DBObject queryDBObject_addTopic = new BasicDBObject();
		queryDBObject_addTopic.put("timestamp", $$('$gte' : startTime , '$lte' : endTime));
		DBObject queryDBObject_sum_addTopic = new BasicDBObject();
		queryDBObject_sum_addTopic.put("timestamp", $$('$gte' : stime_long , '$lte' : etime_long));
		
		BasicDBObject args_addTopic  = new BasicDBObject();
		args_addTopic.put('$keyf' , keyfJavascriptCodeString);
		args_addTopic.put("initial" , $$("num" : 0));
		args_addTopic.put('$reduce' , reduceJavasriptCodeString);
		args_addTopic.put('condition' , queryDBObject_addTopic);

		BasicDBList groupResult_addTopic = topics().group(args_addTopic);
	
		//新增总计
		Double sum_addTopic = topics().count(queryDBObject_sum_addTopic);
		//新增当前页总计
		Double page_sum_addTopic = 0d;
		
		List dataList_addTopic = new ArrayList();
		//item时间
		Long itemTime_addTopic = startTime;
		
		for(int i = 0 ; i < page_number ; i++){
			def groupResult_addTopic_item = getItemByDateLong(itemTime_addTopic , groupResult_addTopic);
			if( groupResult_addTopic_item != null){
				page_sum_addTopic = addBigDecimal(page_sum_addTopic, Float.valueOf(groupResult_addTopic_item["num"].toString()) );
				dataList_addTopic.add(groupResult_addTopic_item);
			}else{
				Map map = new HashMap();
				map.put("gtime", itemTime_addTopic);
				map.put("num", 0);
				dataList_addTopic.add(map)
			}
			itemTime_addTopic += DataUtils.DAY_MI;
		}
		/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 新增问答数 分组查询  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ **/
		
		
		/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 未评价 分组查询  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ **/

		//查询条件
		DBObject queryDBObject_unevaluation = new BasicDBObject();
		queryDBObject_unevaluation.put("evaluation_type", TopicEvaluationType.未评价.ordinal());
		queryDBObject_unevaluation.put("type", TopicsType.问题已结束.ordinal());
		queryDBObject_unevaluation.put("timestamp", $$('$gte' : startTime , '$lte' : endTime));
		
		DBObject queryDBObject_sum_unevaluation = new BasicDBObject();
		queryDBObject_sum_unevaluation.put("evaluation_type", TopicEvaluationType.未评价.ordinal());
		queryDBObject_sum_unevaluation.put("type", TopicsType.问题已结束.ordinal());
		queryDBObject_sum_unevaluation.put("timestamp", $$('$gte' : stime_long , '$lte' : etime_long));
		
		BasicDBObject args_unevaluation  = new BasicDBObject();
		args_unevaluation.put('$keyf' , keyfJavascriptCodeString);
		args_unevaluation.put("initial" , $$("num" : 0));
		args_unevaluation.put('$reduce' , reduceJavasriptCodeString);
		args_unevaluation.put('condition' , queryDBObject_unevaluation);

		BasicDBList groupResult_unevaluation = topics().group(args_unevaluation);
	
		//未评价总计
		Double sum_unevaluation = topics().count(queryDBObject_sum_unevaluation);
		//未评价当前页总计
		Double page_sum_unevaluation = 0d;
		
		List dataList_unevaluation = new ArrayList(); 
		//item时间
		Long itemTime_unevaluation = startTime;
		
		for(int i = 0 ; i < page_number ; i++){
			
			def groupResult_unevaluation_item = getItemByDateLong(itemTime_unevaluation , groupResult_unevaluation);
			if( groupResult_unevaluation_item != null){
				page_sum_unevaluation = addBigDecimal(page_sum_unevaluation, Float.valueOf(groupResult_unevaluation_item["num"].toString()) );
				dataList_unevaluation.add(groupResult_unevaluation_item);
			}else{
				Map map = new HashMap();
				map.put("gtime", itemTime_unevaluation);
				map.put("num", 0);
				dataList_unevaluation.add(map)
			}
			itemTime_unevaluation += DataUtils.DAY_MI;
		}
		/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 未评价 分组查询  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ **/
		
		
		
		/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 满意 分组查询  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ **/
		
		//查询条件
		DBObject queryDBObject_wellevaluation = new BasicDBObject();
		queryDBObject_wellevaluation.put("evaluation_type", TopicEvaluationType.满意.ordinal());
		queryDBObject_wellevaluation.put("type", TopicsType.问题已结束.ordinal());
		queryDBObject_wellevaluation.put("timestamp", $$('$gte' : startTime , '$lte' : endTime));
//		queryDBObject_wellevaluation.put("evaluation.timestamp", $$('$gte' : startTime , '$lte' : endTime));
		
		DBObject queryDBObject_sum_wellevaluation = new BasicDBObject();
		queryDBObject_sum_wellevaluation.put("evaluation_type", TopicEvaluationType.满意.ordinal());
		queryDBObject_sum_wellevaluation.put("type", TopicsType.问题已结束.ordinal());
		queryDBObject_sum_wellevaluation.put("timestamp", $$('$gte' : stime_long , '$lte' : etime_long));
//		queryDBObject_sum_wellevaluation.put("evaluation.timestamp", $$('$gte' : stime_long , '$lte' : etime_long));
		
		BasicDBObject args_wellevaluation  = new BasicDBObject();
		args_wellevaluation.put('$keyf' , keyfJavascriptCodeString);
		args_wellevaluation.put("initial" , $$("num" : 0));
		args_wellevaluation.put('$reduce' , reduceJavasriptCodeString);
		args_wellevaluation.put('condition' , queryDBObject_wellevaluation);
		
		BasicDBList groupResult_wellevaluation = topics().group(args_wellevaluation);
		
		//满意总计
		Double sum_wellevaluation = topics().count(queryDBObject_sum_wellevaluation);
		//满意当前页总计
		Double page_sum_wellevaluation = 0d;
		
		List dataList_wellevaluation = new ArrayList(); 
		//item时间
		Long itemTime_wellevaluation = startTime;
		
		for(int i = 0 ; i < page_number ; i++){
			
			def groupResult_wellevaluation_item = getItemByDateLong(itemTime_wellevaluation , groupResult_wellevaluation);
			if( groupResult_wellevaluation_item != null){
				page_sum_wellevaluation = addBigDecimal(page_sum_wellevaluation, Float.valueOf(groupResult_wellevaluation_item["num"].toString()) );
				dataList_wellevaluation.add(groupResult_wellevaluation_item);
			}else{
				Map map = new HashMap();
				map.put("gtime", itemTime_wellevaluation);
				map.put("num", 0);
				dataList_wellevaluation.add(map)
			}
			itemTime_wellevaluation += DataUtils.DAY_MI;
		}
		/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 满意 分组查询  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ **/
		
		/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 非常满意 分组查询  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ **/
		
		//查询条件
		DBObject queryDBObject_wellevaluation2 = new BasicDBObject();
		queryDBObject_wellevaluation2.put("evaluation_type", TopicEvaluationType.非常满意.ordinal());
		queryDBObject_wellevaluation2.put("type", TopicsType.问题已结束.ordinal());
		queryDBObject_wellevaluation2.put("timestamp", $$('$gte' : startTime , '$lte' : endTime));
//		queryDBObject_wellevaluation.put("evaluation.timestamp", $$('$gte' : startTime , '$lte' : endTime));
		
		DBObject queryDBObject_sum_wellevaluation2 = new BasicDBObject();
		queryDBObject_sum_wellevaluation2.put("evaluation_type", TopicEvaluationType.非常满意.ordinal());
		queryDBObject_sum_wellevaluation2.put("type", TopicsType.问题已结束.ordinal());
		queryDBObject_sum_wellevaluation2.put("timestamp", $$('$gte' : stime_long , '$lte' : etime_long));
//		queryDBObject_sum_wellevaluation.put("evaluation.timestamp", $$('$gte' : stime_long , '$lte' : etime_long));
		
		BasicDBObject args_wellevaluation2  = new BasicDBObject();
		args_wellevaluation2.put('$keyf' , keyfJavascriptCodeString);
		args_wellevaluation2.put("initial" , $$("num" : 0));
		args_wellevaluation2.put('$reduce' , reduceJavasriptCodeString);
		args_wellevaluation2.put('condition' , queryDBObject_wellevaluation2);
		
		BasicDBList groupResult_wellevaluation2 = topics().group(args_wellevaluation2);
		
		//满意总计
		Double sum_wellevaluation2 = topics().count(queryDBObject_sum_wellevaluation2);
		//满意当前页总计
		Double page_sum_wellevaluation2 = 0d;
		
		List dataList_wellevaluation2 = new ArrayList(); 
		//item时间
		Long itemTime_wellevaluation2 = startTime;
		
		for(int i = 0 ; i < page_number ; i++){
			
			def groupResult_wellevaluation_item2 = getItemByDateLong(itemTime_wellevaluation2 , groupResult_wellevaluation2);
			if( groupResult_wellevaluation_item2 != null){
				page_sum_wellevaluation2 = addBigDecimal(page_sum_wellevaluation2, Float.valueOf(groupResult_wellevaluation_item2["num"].toString()) );
				dataList_wellevaluation2.add(groupResult_wellevaluation_item2);
			}else{
				Map map = new HashMap();
				map.put("gtime", itemTime_wellevaluation2);
				map.put("num", 0);
				dataList_wellevaluation2.add(map)
			}
			itemTime_wellevaluation2 += DataUtils.DAY_MI;
		}
		/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 非常满意 分组查询  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ **/
		
		
		/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 不满意 分组查询  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ **/
		
		//查询条件
		DBObject queryDBObject_badevaluation = new BasicDBObject();
		queryDBObject_badevaluation.put("evaluation_type", TopicEvaluationType.不满意.ordinal());
		queryDBObject_badevaluation.put("type", TopicsType.问题已结束.ordinal());
		queryDBObject_badevaluation.put("timestamp", $$('$gte' : startTime , '$lte' : endTime));
//		queryDBObject_badevaluation.put("evaluation.timestamp", $$('$gte' : startTime , '$lte' : endTime));
		
		DBObject queryDBObject_sum_badevaluation = new BasicDBObject();
		queryDBObject_sum_badevaluation.put("evaluation_type", TopicEvaluationType.不满意.ordinal());
		queryDBObject_sum_badevaluation.put("type", TopicsType.问题已结束.ordinal());
		queryDBObject_sum_badevaluation.put("timestamp", $$('$gte' : stime_long , '$lte' : etime_long));
//		queryDBObject_sum_badevaluation.put("evaluation.timestamp", $$('$gte' : stime_long , '$lte' : etime_long));
		
		BasicDBObject args_badevaluation  = new BasicDBObject();
		args_badevaluation.put('$keyf' , keyfJavascriptCodeString);
		args_badevaluation.put("initial" , $$("num" : 0));
		args_badevaluation.put('$reduce' , reduceJavasriptCodeString);
		args_badevaluation.put('condition' , queryDBObject_badevaluation);
		
		BasicDBList groupResult_badevaluation = topics().group(args_badevaluation);
		
		//满意总计
		Double sum_badevaluation = topics().count(queryDBObject_sum_badevaluation);
		//满意当前页总计
		Double page_sum_badevaluation = 0d;
		
		List dataList_badevaluation = new ArrayList(); 
		//item时间
		Long itemTime_badevaluation = startTime;
		
		for(int i = 0 ; i < page_number ; i++){
			
			def groupResult_badevaluation_item = getItemByDateLong(itemTime_badevaluation , groupResult_badevaluation);
			if( groupResult_badevaluation_item != null){
				page_sum_badevaluation = addBigDecimal(page_sum_badevaluation, Float.valueOf(groupResult_badevaluation_item["num"].toString()) );
				dataList_badevaluation.add(groupResult_badevaluation_item);
			}else{
				Map map = new HashMap();
				map.put("gtime", itemTime_badevaluation);
				map.put("num", 0);
				dataList_badevaluation.add(map)
			}
			itemTime_badevaluation += DataUtils.DAY_MI;
		}
		/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 不满意 分组查询  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ **/
		
		/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 提问超时 分组查询  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ **/
		
		//查询条件
		DBObject queryDBObject_timeout = new BasicDBObject();
		queryDBObject_timeout.put("type", TopicsType.抢答失败.ordinal());
		queryDBObject_timeout.put("timestamp", $$('$gte' : startTime , '$lte' : endTime));
		
		DBObject queryDBObject_sum_timeout = new BasicDBObject();
		queryDBObject_sum_timeout.put("type", TopicsType.抢答失败.ordinal());
		queryDBObject_sum_timeout.put("timestamp", $$('$gte' : stime_long , '$lte' : etime_long));
		
		BasicDBObject args_timeout  = new BasicDBObject();
		args_timeout.put('$keyf' , keyfJavascriptCodeString);
		args_timeout.put("initial" , $$("num" : 0));
		args_timeout.put('$reduce' , reduceJavasriptCodeString);
		args_timeout.put('condition' , queryDBObject_timeout);
		
		BasicDBList groupResult_timeout = topics().group(args_timeout);
		
		//满意总计
		Double sum_timeout = topics().count(queryDBObject_sum_timeout);
		//满意当前页总计
		Double page_sum_timeout = 0d;
		
		List dataList_timeout = new ArrayList(); 
		//item时间
		Long itemTime_timeout = startTime;
		
		for(int i = 0 ; i < page_number ; i++){
			
			def groupResult_timeout_item = getItemByDateLong(itemTime_timeout , groupResult_timeout);
			if( groupResult_timeout_item != null){
				page_sum_timeout = addBigDecimal(page_sum_timeout, Float.valueOf(groupResult_timeout_item["num"].toString()) );
				dataList_timeout.add(groupResult_timeout_item);
			}else{
				Map map = new HashMap();
				map.put("gtime", itemTime_timeout);
				map.put("num", 0);
				dataList_timeout.add(map)
			}
			itemTime_timeout += DataUtils.DAY_MI;
		}
		/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 提问超时 分组查询  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ **/
		
		
		Map allData = new HashMap();
		//每日提问人数
		allData["topic_author_data"] = ["page_sum" : page_sum_topic_author , "sum" : sum_topic_author , "data" : dataList_topic_author ];
		//新增问答数	
		allData["addtopic_data"] = ["page_sum" : page_sum_addTopic , "sum" : sum_addTopic , "data" : dataList_addTopic ];
//		满意问答数	
		allData["wellevaluation_data"] = ["page_sum" : page_sum_wellevaluation , "sum" : sum_wellevaluation , "data" : dataList_wellevaluation ];
//		非常满意问答数	
		allData["wellevaluation_data2"] = ["page_sum" : page_sum_wellevaluation2 , "sum" : sum_wellevaluation2 , "data" : dataList_wellevaluation2 ];
//		不满意问答数
		allData["badevaluation_data"] = ["page_sum" : page_sum_badevaluation , "sum" : sum_badevaluation , "data" : dataList_badevaluation ];
//		未评价问答数
		allData["unevaluation_data"] = ["page_sum" : page_sum_unevaluation , "sum" : sum_unevaluation , "data" : dataList_unevaluation ];
//		提问超时
		allData["timeout_data"] = ["page_sum" : page_sum_timeout , "sum" : sum_timeout , "data" : dataList_timeout ];
		Map map = new HashMap();
		map["code"] = 1;
		map["data"]  = allData;
		map["count"]  = count;

		return map;
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


 
 