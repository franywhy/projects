package com.izhubo.web.live

import static com.izhubo.rest.common.doc.MongoKey.$set
import static com.izhubo.rest.common.util.WebUtils.$$

import javax.servlet.http.HttpServletRequest

import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.apache.commons.lang3.StringUtils

import com.izhubo.web.BaseController
import com.izhubo.web.vo.BaseResultVO
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
import com.izhubo.rest.common.util.http.HttpClientUtil4_3
import com.izhubo.rest.pushmsg.model.PushMsgBase;
import com.mongodb.DBCollection;

import org.json.JSONArray
import org.json.JSONObject

import java.lang.Exception

import java.text.SimpleDateFormat
import java.util.Map;
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors



/**
 *
 * @ClassName: WatchPlaybackLogController
 * @Description:录播观看记录
 * @author zww
 * @date 2017年6月16日
 *
 */
@Controller
@RequestMapping("/live_data")
class WatchPlaybackLogController extends BaseController{
	
	
	/** 直播录制件表 */
	public DBCollection live_debris_record() {
		return mainMongo.getCollection("live_debris_record");
	}
	
	/** 录播日志表 */
	public DBCollection mid_playback_watch_detail_log() {
		return mainMongo.getCollection("mid_playback_watch_detail_log");
	}
	
	/** 观看录播统计表 */
	public DBCollection mid_playback_watch_count() {
		return mainMongo.getCollection("mid_playback_watch_count");
	}
	
	/** 入口参数 */
	public DBCollection ah_url() {
		return mainMongo.getCollection("ah_url");
	}
	
	/**
	 * 获取并保存观看录播数据
	 * @date 2017年6月16日
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "save_watch_playback_data", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "保存观看录播数据", httpMethod = "GET",  notes = "获取并保存观看录播数据", response = BaseResultVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "start_count_time", value = "开始时间(例子):2017-05-05" , required = false , dataType = "String" , paramType = "query" ),
		@ApiImplicitParam(name = "end_count_time", value = "结束时间(例子):2017-05-06" , required = false , dataType = "String" , paramType = "query" )
	])
	def save_watch_playback_data(HttpServletRequest request){
		//获取昨天的时间
		String start_count_time = ServletRequestUtils.getStringParameter(request, "start_count_time");
		String end_count_time = ServletRequestUtils.getStringParameter(request, "end_count_time");

		//如果传入时间为空，为定时任务，时间减少一天
		if(StringUtils.isBlank(start_count_time)){
			Long today_time = get_start_time()
			String day_String = date_format_String(today_time)
			start_count_time = subtractOneday(day_String)
			end_count_time = subtractOneday(day_String)
		}
		//传入时间的对比
		Long start_count_time_long = get_start_time(start_count_time)
		Long end_count_time_long = get_end_time(end_count_time)
		if(start_count_time_long > end_count_time_long){
			return [code : -99];
		}
		Long reduce_time = end_count_time_long - start_count_time_long
		Integer except = (Integer)(reduce_time/86399999)
		
		for(int day = 0 ; day < except ; day++){
			Long live_start_time_long = get_start_time(start_count_time)
			Integer count = 1
			String pageNo = 1
			while(count){
				//获取观看录播数据
//				println "页码："+pageNo
				String URL = "http://hqyzx.gensee.com/integration/site/webcast/export/vod/history"
				String date = live_start_time_long
				def map = ['loginName':'2880696173@qq.com','password':'123456','date':date,'pageNo':pageNo]
				def data_list = HttpClientUtil4_3.post(URL,map,null);

				JSONObject jsonObject = new JSONObject(data_list)
				JSONArray record_list = jsonObject.getJSONArray("list")
				JSONObject page = jsonObject.getJSONObject("page")
				//println "page："+page["totalPages"]+"数据保存处理"
				//结果录入
				Integer size = record_list.length();
				for(int i=0 ; i<size ; i++){
					String vodId = record_list.get(i)["vodId"]
					String uid = record_list.get(i)["uid"]
					Long start_time = record_list.get(i)["startTime"]
					Long leave_time = record_list.get(i)["leaveTime"]
					Long duration = leave_time - start_time
					def now= new Date()
					Long create_time = now.time
					def course_time_id

					def course_time_id_list = live_debris_record().find($$("playback_video_id" : vodId),$$("course_time_id":1))?.toArray()
					if(course_time_id_list.size>0){
						course_time_id = course_time_id_list.get(0)["course_time_id"]
					}
					//拼接id
					String sb1 = start_time + ""
					String sb2 = leave_time + ""
					int ran = (int)(Math.random()*(9999-1000+1))+1000
					def id = uid+sb1.substring(3, 10)+sb2.substring(3, 10)+ran
					//保存结果数据
					def result_map = [:]
					result_map.put("id", id)
					result_map.put("vodId", vodId)
					result_map.put("uid", uid)
					result_map.put("course_time_id", course_time_id)
					result_map.put("start_time", start_time)
					result_map.put("leave_time", leave_time)
					result_map.put("duration", duration)
					result_map.put("create_time", create_time)
					
					savePlaybackResult(result_map)
				}
				//页码
				if(page["totalPages"]-count > 0){
					count++
					pageNo++
				}else{
					count = 0
				}
				//println "输出count:"+count
			}
			start_count_time = addOneday(start_count_time)//统计多日
		}
		return [code : 1]
	}
	
	
	/**
	 * 统计保存学员观看录播数据
	 * @date 2017年6月20日
	 * @param @param request
	 */
	
	@ResponseBody
	@RequestMapping(value = "watch_playback_count", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "统计保存学员观看录播数据", httpMethod = "GET",  notes = "统计保存学员观看录播数据", response = BaseResultVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "start_count_time", value = "开始时间(例子):2017-05-05" , required = false , dataType = "String" , paramType = "query" ),
		@ApiImplicitParam(name = "end_count_time", value = "结束时间(例子):2017-05-06" , required = false , dataType = "String" , paramType = "query" )
	])
	def watch_playback_count(HttpServletRequest request){

		String start_count_time = ServletRequestUtils.getStringParameter(request, "start_count_time");
		String end_count_time = ServletRequestUtils.getStringParameter(request, "end_count_time");

		//如果传入时间为空，为定时任务，时间减少一天
		if(StringUtils.isBlank(start_count_time)){
			Long today_time = get_start_time()
			String day_String = date_format_String(today_time)
			start_count_time = subtractOneday(day_String)
			end_count_time = subtractOneday(day_String)
		}

		//传入时间的对比
		Long start_count_time_long = get_start_time(start_count_time)
		Long end_count_time_long = get_end_time(end_count_time)
		if(start_count_time_long > end_count_time_long){
			return [code : -99];
		}
		Long reduce_time = end_count_time_long-start_count_time_long
		Integer except = (Integer)(reduce_time/86399999)
		
		for(int day = 0 ; day < except ; day++){
			Long live_start_time_long = get_start_time(start_count_time)
			Long live_end_time_long = get_end_time(start_count_time)
			//获取当天所有详细记录
			def playback_watch_log_list = mid_playback_watch_detail_log().find($$("start_time":$$('$gte':live_start_time_long,'$lte':live_end_time_long)),$$("vodId":1,"uid":1,"duration":1))?.toArray()
			//回看详细记录的统计
			def duration_result_map = [:]
			//临时过渡MAP
			def interim_map = [:]

			Integer size = playback_watch_log_list.size
			for(int i=0; i<size;i++){
				String uid = playback_watch_log_list.get(i)["uid"]
				String vodId = playback_watch_log_list.get(i)["vodId"]
				String key = uid + vodId
				String course_time_id = playback_watch_log_list.get(i)["course_time_id"]
				Long duration = playback_watch_log_list.get(i)["duration"]

				//是否已存在记录判断
				if(interim_map.get(key)){
					//存在就对比
					if(interim_map.get(key) > duration){
						duration = interim_map.get(key)
					}
				}
				//把数据存入临时的map
				interim_map.put(key , duration)

				def save_result_map = [:]
				save_result_map.put(key , duration)
				save_result_map.put("_id",key)
				save_result_map.put("uid",uid)
				save_result_map.put("vodId",vodId)
				save_result_map.put("course_time_id",course_time_id)

				duration_result_map.put(key , save_result_map)
			}
			//拿到了结果map进行对比保存
			duration_result_map.each { it ->
				def log_map = duration_result_map.get(it.key)
				
				String id = log_map.get("_id")
				String uid = log_map.get("uid")
				String vodId = log_map.get("vodId")
				Long duration = log_map.get(it.key)
				def course_time_id = log_map.get("course_time_id")
				def now= new Date()
				Long create_time = now.time
				//println "id:"+id+"//uid:"+uid+"//vodId:"+vodId+"//duration:"+duration+"//course_time_id:"+course_time_id+"//create_time:"+create_time

				def playback_watch_by_uid = mid_playback_watch_count().find($$("_id":it.key),$$("_id":1,"uid":1,"vodId":1,"duration":1,"create_time":1))?.toArray()
				//是否已存在数据
				if(playback_watch_by_uid){
					if(duration > playback_watch_by_uid.get(0)["duration"]){
						//执行覆盖保存保存
						def result_map = [:]
						result_map.put("id", id)
						result_map.put("uid", uid)
						result_map.put("vodId", vodId)
						result_map.put("duration", duration)
						result_map.put("course_time_id", course_time_id)
						result_map.put("create_time", create_time)

						saveCountResult(result_map)
					}
				}else{
					//直接保存数据
					def result_map = [:]
					result_map.put("id", id)
					result_map.put("uid", uid)
					result_map.put("vodId", vodId)
					result_map.put("duration", duration)
					result_map.put("course_time_id", course_time_id)
					result_map.put("create_time", create_time)

					saveCountResult(result_map)
				}
			}
			start_count_time = addOneday(start_count_time)//统计多日
		}
		return [code : 1]
	}
	
	
	
	/**
	 * 保存学员观看录播记录
	 */
	public void savePlaybackResult(Map param){
		def id = param.get("id")
		String uid = param.get("uid")
		String vodId = param.get("vodId")
		def course_time_id = param.get("course_time_id")
		Long start_time = param.get("start_time")
		Long leave_time = param.get("leave_time")
		Long duration = param.get("duration")
		Long create_time = param.get("create_time")
		
		def result = $$("_id", id);
		result.append("uid", uid);
		result.append("vodId", vodId);
		result.append("course_time_id", course_time_id);
		result.append("start_time", start_time);
		result.append("leave_time", leave_time);
		result.append("duration", duration)
		result.append("create_time", create_time);
		
		mid_playback_watch_detail_log().save(result);
	}
	
	
	/**
	 * 统计学员观看录播记录
	 */
	public void saveCountResult(Map param){
		def id = param.get("id")
		String uid = param.get("uid")
		String vodId = param.get("vodId")
		def course_time_id = param.get("course_time_id")
		Long duration = param.get("duration")
		Long create_time = param.get("create_time")
		
		def result = $$("_id", id);
		result.append("uid", uid);
		result.append("vodId", vodId);
		result.append("course_time_id", course_time_id);
		result.append("duration", duration);
		result.append("create_time", create_time);
		
		mid_playback_watch_count().save(result);
	}
	
	
	/**
	 * 获取当天起始时间戳
	 * @return
	 */
	private Long get_start_time(String time_param){
		Calendar todayStart = Calendar.getInstance();
		if(StringUtils.isNotBlank(time_param)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String time = time_param;
			Date date = format.parse(time);
			todayStart.setTime(date);
		}
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime().getTime();
	}
	 
	
	/**
	 * 获取当天结束时间戳
	 * @return
	 */
	private Long get_end_time(String time_param){
		Calendar todayEnd = Calendar.getInstance();
		if(StringUtils.isNotBlank(time_param)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String time = time_param;
			Date date = format.parse(time);
			todayEnd.setTime(date);
			}
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime().getTime();
	}
	
	
	/**
	 * 时间戳转化为Sting
	 * @param param_time
	 * @return
	 */
	private String date_format_String(Long param_time){
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long time=new Long(param_time);
		String d = format.format(time);
		return d
	}
	
	
	/**
	 * 日期减少一天
	 */
	public static String subtractOneday(String today){
		SimpleDateFormat f =  new SimpleDateFormat("yyyy-MM-dd");
		try   {
			Date  d  =  new Date(f.parse(today).getTime()-24*3600*1000);
			return  f.format(d);
		}
		catch(Exception ex) {
			return   "输入格式错误";
		}
	}
	
	
	/**
	 * 日期增加一天
	 */
	public static String addOneday(String today){
		SimpleDateFormat f =  new SimpleDateFormat("yyyy-MM-dd");
		try   {
			Date  d  =  new Date(f.parse(today).getTime()+24*3600*1000);
			return  f.format(d);
		}
		catch(Exception ex) {
			return   "输入格式错误";
		}
	}
	
}
