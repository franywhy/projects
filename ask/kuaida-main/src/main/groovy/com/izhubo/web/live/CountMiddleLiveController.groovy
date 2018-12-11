package com.izhubo.web.live

import static com.izhubo.rest.common.doc.MongoKey.$set
import static com.izhubo.rest.common.util.WebUtils.$$

import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest

import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.apache.commons.lang3.StringUtils;

import com.izhubo.rest.AppProperties
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.rest.pushmsg.XinggePushService
import com.izhubo.rest.pushmsg.model.PushMsgActionEnum
import com.izhubo.rest.pushmsg.model.PushMsgBase
import com.izhubo.rest.pushmsg.model.PushMsgTypeEnum
import com.izhubo.web.BaseController
import com.izhubo.web.vo.MyMessageListVO
import com.izhubo.web.vo.BaseResultVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation



/**
 *
 * @ClassName: CountMiddleLiveController
 * @Description: 直播记录
 * @author zww
 * @date 2017年6月2日
 *
 */
@Controller
@RequestMapping("/live_data")
class CountMiddleLiveController extends BaseController{
	
	
	public DBCollection users() {
		return mainMongo.getCollection("users");
	}
	
	/** 排课计划 */
	public DBCollection class_plan() {
		return mainMongo.getCollection("class_plan");
	}
	
	/** 排课子表 */
	public DBCollection plan_courses() {
		return mainMongo.getCollection("plan_courses");
	}
	
	/** 记录表 */
	public DBCollection mid_watch_detail_log() {
		return mainMongo.getCollection("mid_watch_detail_log");
	}
	
	private static Logger logger = LoggerFactory
	.getLogger(CountMiddleLiveController.class);
	
	/**
	 * 统计并保存观看中级直播记录数据
	 * @date 2017年6月2日 
	 * @param @param request
	 */
		@ResponseBody
		@RequestMapping(value = "count_middle_course_live", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
		@ApiOperation(value = "统计并保存观看中级直播记录数据", httpMethod = "GET",  notes = "统计并保存观看直播记录", response = BaseResultVO.class)
		@ApiImplicitParams([
			@ApiImplicitParam(name = "start_count_time", value = "开始时间(例子):2017-05-05" , required = false , dataType = "String" , paramType = "query" ),
			@ApiImplicitParam(name = "end_count_time", value = "结束时间(例子):2017-05-06" , required = false , dataType = "String" , paramType = "query" )
		])
	def count_middle_course_live(HttpServletRequest request){
		String start_count_time = ServletRequestUtils.getStringParameter(request, "start_count_time");
		String end_count_time = ServletRequestUtils.getStringParameter(request, "end_count_time");
		//查找所有的课程（只有一个middle_course_ids）里面有3个数据需要循环
		def constants_list = constants().find(null,$$("middle_course_ids":1,"middle_course_liveinfo":1))?.toArray();
		//用于获取liveid
		def middle_course_liveinfo = constants_list.get(0)["middle_course_liveinfo"]
		//取出所有nc_id
		def constants = constants_list.get(0)["middle_course_ids"]
		def nc_id_list =[]
		for(i in constants){
			def class_plan_list = class_plan().find($$("nc_course_id" : i,"dr":0),$$("nc_id" : 1,"nc_course_id":1))?.toArray();
			for(k in class_plan_list){
				nc_id_list<<k
			}
		}
		//传入时间的对比
		Long start_count_time_long = get_start_time(start_count_time)
		Long end_count_time_long = get_end_time(end_count_time)
		if(start_count_time_long > end_count_time_long){
			return [code : -99];
			}
		Long reduce_time = end_count_time_long-start_count_time_long
		Integer except = (Integer)(reduce_time/86399999)
		//循环的日期
		for(int day ; day<except ; day++){
			//查询课程安排的时候
			Long live_start_time_long = get_start_time(start_count_time)
			Long live_end_time_long = get_end_time(start_count_time)
			for(k in nc_id_list){
				//获取当天的直播
				def plan_courses_list = plan_courses().find($$("nc_plan_id" : k["nc_id"],"dr":0,"live_start_time_long":$$('$gte':live_start_time_long),"live_end_time_long":$$('$lte':live_end_time_long)),$$("nc_id" : 1,"live_start_time_long" : 1,"live_end_time_long" : 1,"live_start_time":1,"live_end_time":1))?.toArray();
				if(plan_courses_list.size!=0){
					def end = plan_courses_list.get(0)["live_end_time"]
					def start = plan_courses_list.get(0)["live_start_time"]
					def end_long = plan_courses_list.get(0)["live_end_time_long"]
					def start_long = plan_courses_list.get(0)["live_start_time_long"]
					def course_time_id = plan_courses_list.get(0)["nc_id"]
					//有直播的课程
					if(end!=null && start!=null){
						//该直播的总时长
						Long live_time = end_long - start_long
						//http获取观看记录数据
						String URL = "http://hqyzx.gensee.com/integration/site/webcast/export/history";
						String webcastId
						String startTime = date_format_String(live_start_time_long)//"2017-06-04 00:00:00"
						String endTime = date_format_String(live_end_time_long)
						//课程ID
						String course_id
						for(liveid in middle_course_liveinfo){
							if(liveid.get("course_id") == k["nc_course_id"]){
								webcastId = liveid.get("liveid");
								course_id = liveid.get("course_id")
								break ;
							}
						}
						def map = ['loginName':'2880696173@qq.com','password':'123456' ,'webcastId':webcastId ,'startTime':startTime ,'endTime':endTime]
						def data_list = HttpClientUtil4_3.post(URL, map, null);

						JSONObject jsonObject = new JSONObject(data_list)
						JSONArray list = jsonObject.getJSONArray("list")
						//用户观看直播记录数据处理
						Integer uid
						Long watch_time
						Long join_time
						Long leave_time
						Integer size = list.length();
						//观看直接数据map
						def user_result_map = [:]
						for(int m = 0 ; m<size ; m++){
							uid = list.get(m)["uid"]-1000000000
							join_time = list.get(m)["joinTime"]
							leave_time = list.get(m)["leaveTime"]
							//时间越界的处理
							if(list.get(m)["joinTime"] < start_long){
								join_time = start_long
							}else if(list.get(m)["leaveTime"] > end_long){
								leave_time = end_long
							}
							//观看直播时间
							watch_time = leave_time - join_time
							//是否已存在记录判断
							if(user_result_map.get(uid)){
								watch_time = watch_time + user_result_map.get(uid)
							}
							if(watch_time > live_time){
								watch_time = live_time
							}
							//把数据存入map
							user_result_map[uid] = watch_time
						}
						//保存结果数据
						def result_map = [:]
						user_result_map.each { it ->
							result_map.put("live_id", webcastId)
							result_map.put("course_time_id", course_time_id)
							result_map.put("course_id", course_id)
							result_map.put("watchtime", it.value)
							result_map.put("user_id", it.key)
							String phone = getUserPhoneByUserId(it.key)
							result_map.put("phone", phone)
							saveResult(result_map)
						}
					}
				}
			}
			start_count_time = addOneday(start_count_time)//统计多日
		}
		return [code : 1]
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
	
	
	/**
	 * 保存学院观看直播记录
	 */
	public void saveResult(Map param){
		def live_id = param.get("live_id")
		def course_time_id = param.get("course_time_id")
		def course_id = param.get("course_id")
		Long watchtime = param.get("watchtime")
		Integer user_id = param.get("user_id")
		def phone = param.get("phone")
		def id = user_id+course_time_id
//		println "输出所有数据检查："+"/合体id:"+id+"/user_id:"+user_id+"/course_time_id:"+course_time_id+"/live_id："+live_id+"/course_id:"+course_id+"/watchtime:"+watchtime+"/phone:"+phone
		
		def result = $$("_id" : id);
		result.append("live_id", live_id);
		result.append("course_time_id", course_time_id);
		result.append("course_id", course_id);
		result.append("watchtime", watchtime);
		result.append("user_id", user_id);
		result.append("phone", phone);
		
		mid_watch_detail_log().save(result);
	}
	
}
