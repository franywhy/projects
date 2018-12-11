package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.json.*
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.rest.AppProperties
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.doc.UnmodifDBObject
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder

/**
 * 考前冲刺
 * @author zhengxin
 * 2016-05-24
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class AppLiveController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('app_live');
	}


	/**
	 *
	 */
	public static final String LIVE_REMIND = "liveremind";
	/**
	 * zhaokun add  中级直播直播提醒
	 */
	public static final String MID_LIVE_REMIND = LIVE_REMIND+":mid:";

	/**
	 * zhaokun add  app直播提醒
	 */
	public static final String APP_LIVE_REMIND = LIVE_REMIND+":app:";
	
	private String h5domain = AppProperties.get("h5.domain").toString();

	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		//query.and("dr").is(0);
		def order = new UnmodifDBObject(new BasicDBObject("order",1));
		SimpleDateFormat ddtf = new SimpleDateFormat(DFMT);
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				if (obj['live_start_time'] != null){
					obj.put("live_start_time", ddtf.format(new Date(obj['live_start_time'])));
				}
				if (obj['live_end_time'] != null){
					obj.put("live_end_time", ddtf.format(new Date(obj['live_end_time'])));
				}
			}
		};
	}

	/**
	 * 新增
	 */
	def add(HttpServletRequest req){

//		"_id" : "1",
//		"live_title" : "猎才计划-面向零基础学员",
//		"live_banner_url" : "http://answerimg.kjcity.com/coursebanner/4.png",
//		"live_teacher_name" : "春晓",
//		"live_teacher_pic" : "",
//		"live_teacher_detail" : "高级会计师、组里快基础上",
//		"live_applicable_people" : "零起步想入行当会计的学员",
//		"live_course_detail" : "这个是课程内容描述",
//		"live_time_detail" : "直播时间：09-02 18:00",
//		"live_start_time" : NumberLong(1491440400000),
//		"live_end_time" : NumberLong(1493082000000),
//		"live_num" : "09909534",
//		"live_id" : "b2794d22b70c44149349060ac9768e90",
//		"live_domain" : "hqyzx.gensee.com"

		Map user = (Map) req.getSession().getAttribute("user");
		def liveclassitem = $$("_id" : UUID.randomUUID().toString());
		String live_start_time = req.getParameter("live_start_time");
		Long start_time = null;
		if(StringUtils.isNotBlank(live_start_time)){
			start_time = new SimpleDateFormat(DFMT).parse(live_start_time).getTime();
			liveclassitem.put("live_start_time", start_time);
		}
		String live_end_time = req.getParameter("live_end_time");
		Long end_time = null;
		if(StringUtils.isNotBlank(live_end_time)){
			end_time = new SimpleDateFormat(DFMT).parse(live_end_time).getTime();
			liveclassitem.put("live_end_time", end_time);
		}
		liveclassitem.put("live_title", req["live_title"]);
		liveclassitem.put("live_url", h5domain +"kj-live-detail.html?live_id="+liveclassitem["_id"]);
		liveclassitem.put("live_banner_url", req["live_banner_url"]);
		liveclassitem.put("live_teacher_name", req["teacher_name"]);
		liveclassitem.put("live_teacher_pic", req["live_teacher_pic"]);
		liveclassitem.put("live_correlation_pic", req["live_correlation_pic"]);
		liveclassitem.put("live_teacher_detail", req["live_teacher_detail"]);
		liveclassitem.put("live_applicable_people", req["live_applicable_people"]);
		liveclassitem.put("live_course_detail", req["live_course_detail"]);
		liveclassitem.put("live_num", req["live_num"]);
		liveclassitem.put("live_id",  req["live_id"]);
		liveclassitem.put("live_domain",  req["live_domain"]);
		liveclassitem.put("live_reply_url",  req["live_reply_url"]);

		liveclassitem.put("live_ad_url",  req["live_ad_url"]);
		liveclassitem.put("live_ad_pic_url",  req["live_ad_pic_url"]);
		liveclassitem.put("live_ad_isshow",  ServletRequestUtils.getIntParameter(req, "live_ad_isshow", 0));
		liveclassitem.put("live_ad_app_isshow",  ServletRequestUtils.getIntParameter(req, "live_ad_app_isshow", 0));
		liveclassitem.put("live_type",  ServletRequestUtils.getIntParameter(req, "live_type", 1001));
		//回播需要的数据
		liveclassitem.put("live_studio_type",  ServletRequestUtils.getIntParameter(req, "live_studio_type", 0));
		liveclassitem.put("live_reply_room_number",  req["live_reply_room_number"]);
		liveclassitem.put("live_reply_id",  req["live_reply_id"]);
		
		liveclassitem.put("dr", 0);
		table().save(liveclassitem);
		Crud.opLog("applive",[save:liveclassitem["_id"]]);

		updateLivePushRemaindState(liveclassitem["_id"],start_time);
		return OK();
	}

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		//		"_id" : "1",
//		"live_title" : "猎才计划-面向零基础学员",
//		"live_banner_url" : "http://answerimg.kjcity.com/coursebanner/4.png",
//		"live_teacher_name" : "春晓",
//		"live_teacher_pic" : "",
//		"live_teacher_detail" : "高级会计师、组里快基础上",
//		"live_applicable_people" : "零起步想入行当会计的学员",
//		"live_course_detail" : "这个是课程内容描述",
//		"live_time_detail" : "直播时间：09-02 18:00",
//		"live_start_time" : NumberLong(1491440400000),
//		"live_end_time" : NumberLong(1493082000000),
//		"live_num" : "09909534",
//		"live_id" : "b2794d22b70c44149349060ac9768e90",
//		"live_domain" : "hqyzx.gensee.com"


		Map user = (Map) req.getSession().getAttribute("user");


		String id = req[_id];
		String live_title = req.getParameter("live_title");
		String live_url = req.getParameter("live_url");
		String live_banner_url = req.getParameter("live_banner_url");
		String live_teacher_name = req.getParameter("live_teacher_name");
		String live_teacher_pic = req.getParameter("live_teacher_pic");
		String live_correlation_pic = req.getParameter("live_correlation_pic");
		String live_teacher_detail = req.getParameter("live_teacher_detail");
		String live_applicable_people = req.getParameter("live_applicable_people");
		String live_course_detail = req.getParameter("live_course_detail");
		String live_num = req.getParameter("live_num");
		String live_id = req.getParameter("live_id");
		String live_domain = req.getParameter("live_domain");
		String live_reply_url =  req.getParameter("live_reply_url")


		String live_start_time = req.getParameter("live_start_time");
		String live_end_time = req.getParameter("live_end_time");


		String live_ad_url = req.getParameter("live_ad_url");
		String live_ad_pic_url = req.getParameter("live_ad_pic_url");
        int live_ad_isshow =ServletRequestUtils.getIntParameter(req, "live_ad_isshow", 0);
		int live_ad_app_isshow = ServletRequestUtils.getIntParameter(req, "live_ad_app_isshow", 0);
		int live_type = ServletRequestUtils.getIntParameter(req, "live_type", 1001);
		
		int live_studio_type = ServletRequestUtils.getIntParameter(req, "live_studio_type", 0);
		String live_reply_room_number = req.getParameter("live_reply_room_number");
		String live_reply_id = req.getParameter("live_reply_id");
		

		Long start_time = null;
		Long end_time = null;

		if(StringUtils.isNotBlank(live_start_time)){
			start_time = new SimpleDateFormat(DFMT).parse(live_start_time).getTime();
			table().update(
					new BasicDBObject("_id":id),
					new BasicDBObject('$set':
							new BasicDBObject(
									"live_start_time" : start_time,
							)
					));
		}
		if(StringUtils.isNotBlank(live_end_time)){
			end_time = new SimpleDateFormat(DFMT).parse(live_end_time).getTime();
			table().update(
					new BasicDBObject("_id":id),
					new BasicDBObject('$set':
							new BasicDBObject(
									"live_end_time" : end_time,
							)
					));
		}

		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
						new BasicDBObject(
								"live_title" : live_title,
								"live_url" : h5domain +"kj-live-detail.html?live_id="+id,
								"live_banner_url" : live_banner_url,
								"live_teacher_name" : live_teacher_name,
								"live_teacher_pic" : live_teacher_pic,
								"live_correlation_pic" : live_correlation_pic,
								"live_teacher_detail" : live_teacher_detail,
								"live_applicable_people" : live_applicable_people,
								"live_course_detail" : live_course_detail,
								"live_ad_url" : live_ad_url,
								"live_ad_pic_url" : live_ad_pic_url,
								"live_ad_isshow":live_ad_isshow,
								"live_ad_app_isshow":live_ad_app_isshow,
								"live_type":live_type,
								"live_num" : live_num,
								"live_id" : live_id,
								"live_domain" : live_domain,
								"live_reply_url" : live_reply_url,
								"live_studio_type" : live_studio_type,
								"live_reply_room_number" : live_reply_room_number,
								"live_reply_id" : live_reply_id,
								"update_time" : System.currentTimeMillis()
						)
				));

		updateLivePushRemaindState(id,start_time);

		Crud.opLog("applive",[edit:id]);
		return OK();
	}
	/**
	 * 新增修改，都要触发一次改动，更新直播推送的定时服务
	 */
	def updateLivePushRemaindState(String _id,long stattime)
	{
		String key = APP_LIVE_REMIND+_id;
		String val = "";
		//如当前的直播时间是30分钟之后的，则自动设置一个提醒
		long timettl = stattime-System.currentTimeMillis() -30*60*1000;
		if(timettl>0) {
			mainRedis.opsForValue().set(key, val, timettl, TimeUnit.MILLISECONDS);
			//chatRedis.opsForValue().set(key, val, timettl, TimeUnit.MILLISECONDS);
		}
		else
		{
			//mainRedis.opsForValue().set(key, val, 60000, TimeUnit.MILLISECONDS);
			//chatRedis.opsForValue().set(key, val, 30000, TimeUnit.MILLISECONDS);
		}
	}
	
	/**
	 * 删除
	 */
	def del(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"dr" : 1
					)
				));
		Crud.opLog("liveclass",[del:id]);
		return OK();
	}
	
	/**
	 * 上/下架
	 */
	def stand(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id];
		def status = req.getParameter("status");
		if(StringUtils.isBlank(status)){
			status = 0
		}
		status = status as Integer
		if(status == 0){
			status = 1
		}else if(status == 1){
			status = 0
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"dr" : status
					)
				));
		Crud.opLog("liveclass",[stand:id]);
		return OK();
	}
	


}
