package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;
import java.text.SimpleDateFormat
import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value;

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.doc.UnmodifDBObject;
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder;

/**
 * 每日推送管理
 * @author zhengxin
 * 2016-05-09
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class recommendController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('daily_recommend');
	}
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";
	
	@Value("#{application['h5.domain']}")
	private String h5domain;
	
	
	DBCollection tk_article_type(){mainMongo.getCollection('tk_article_type')}

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		String recommend_title = req.getParameter("recommend_title");
		String recommend_type = req.getParameter("recommend_type");
		String srecommend_time = req.getParameter("srecommend_time");
		String erecommend_time = req.getParameter("erecommend_time");
		String is_recommend = req.getParameter("is_recommend");
		if(srecommend_time != null || erecommend_time != null){
			query = Web.fillTimeBetween(query,"recommend_time",new SimpleDateFormat(DFMT).parse(srecommend_time),new SimpleDateFormat(DFMT).parse(erecommend_time));
		}
		if (StringUtils.isNotBlank(recommend_title)){
			Pattern pattern = Pattern.compile("^.*" + recommend_title + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("recommend_title").regex(pattern)
		}
		if (StringUtils.isNotBlank(recommend_type)){
			query.and("recommend_type").is(Integer.valueOf(recommend_type));
		}
		if (StringUtils.isNotBlank(is_recommend)){
			query.and("is_recommend").is(Integer.valueOf(is_recommend));
		}
		def order = new UnmodifDBObject(new BasicDBObject("push_time",-1));
		SimpleDateFormat ddtf = new SimpleDateFormat(DFMT);
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,order){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				if (obj['push_time'] != null){
					obj.put("push_time", ddtf.format(new Date(obj['push_time'])));
				}
			}
		};
	}
	
	def tk_article_type(HttpServletRequest req){
		def query =  QueryBuilder.start();
		def order = new UnmodifDBObject(new BasicDBObject("_id",-1));
		Crud.list(req,tk_article_type(),query.get(),MongoKey.ALL_FIELD,order)
	}

	/**
	 * 新增文章recommend_type (integer, optional): 推荐类型：0：直播 1：语音 2：文章信息
	 */
	def addarticle(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def article = $$("_id" : UUID.randomUUID().toString());
		
		String day_push_time = req["push_time"];
		Long push_time = null;
		if(StringUtils.isNotBlank(day_push_time)){
			push_time = new SimpleDateFormat(DFMT).parse(day_push_time).getTime();
			article.put("push_time", push_time);
		} else {
			article.put("push_time", System.currentTimeMillis());
		}
		
		article.put("create_user_id", user.get("_id") as Integer);
		article.put("create_time", System.currentTimeMillis());
		article.put("timestamp", System.currentTimeMillis());
		article.put("recommend_title", req["recommend_title"]);
		article.put("recommend_author", req["recommend_author"]);
		article.put("recommend_picurl", req["recommend_picurl"]);
		article.put("recommend_picurl", req["recommend_picurl"]);
		article.put("recommend_infourl", buildinfourl(article["_id"].toString()));
		article.put("recommend_info", req["recommend_info"]);
		article.put("recommend_type", 2);
		table().save(article);
		Crud.opLog("article",[save:article["_id"]]);
		return OK();
	}
	
	
	/**
	 * 新增文章recommend_type (integer, optional): 推荐类型：0：直播 1：语音 2：文章信息
	 */
	def addtkarticle(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def article = $$("_id" : UUID.randomUUID().toString());
		
		String day_push_time = req["push_time"];
		Long push_time = null;
		if(StringUtils.isNotBlank(day_push_time)){
			push_time = new SimpleDateFormat(DFMT).parse(day_push_time).getTime();
			article.put("push_time", push_time);
		} else {
			article.put("push_time", System.currentTimeMillis());
		}
		
		article.put("create_user_id", user.get("_id") as Integer);
		article.put("create_time", System.currentTimeMillis());
		article.put("timestamp", System.currentTimeMillis());
		article.put("recommend_title", req["recommend_title"]);
		article.put("recommend_author", req["recommend_author"]);
		article.put("recommend_picurl", req["recommend_picurl"]);
		article.put("recommend_infourl", buildinfourl(article["_id"].toString()));
		article.put("recommend_info", req["recommend_info"]);
		article.put("tk_article_type_name", req["tk_article_type_name"]);	
		Integer order =Integer.valueOf( req["order"].toString());
		article.put("order", order);
		article.put("recommend_type", 4);
		table().save(article);
		Crud.opLog("article",[save:article["_id"]]);
		return OK();
	}
	
	
	
	/**
	 * 新增文章recommend_type (integer, optional): 推荐类型：0：直播 1：语音 2：文章信息 3 微课
	 */
	def addvideo(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def course = $$("_id" : UUID.randomUUID().toString());
		
		String day_push_time = req["push_time"];
		Long push_time = null;
		if(StringUtils.isNotBlank(day_push_time)){
			push_time = new SimpleDateFormat(DFMT).parse(day_push_time).getTime();
			course.put("push_time", push_time);
		} else {
			course.put("push_time", System.currentTimeMillis());
		}
		
		course.put("create_user_id", user.get("_id") as Integer);
		course.put("create_time", System.currentTimeMillis());
		course.put("timestamp", System.currentTimeMillis());
		course.put("recommend_title", req["recommend_title"]);
		course.put("recommend_author", req["recommend_author"]);
		course.put("recommend_picurl", req["recommend_picurl"]);
		course.put("recommend_video_id", req["recommend_video_id"]);
		course.put("recommend_type", 3);
		table().save(course);
		Crud.opLog("course",[save:course["_id"]]);
		return OK();
	}
	
	/**
	 * 修改文章
	 */
	def editvideo(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req["_id"];
		String recommend_title = req["recommend_title"];
		String recommend_author = req["recommend_author"];
		String recommend_picurl = req["recommend_picurl"];
		String recommend_video_id = req["recommend_video_id"];
		
		String day_push_time = req["push_time"];
		Long push_time = null;
		if(StringUtils.isNotBlank(day_push_time)){
			push_time = new SimpleDateFormat(DFMT).parse(day_push_time).getTime();
		} else {
			push_time = System.currentTimeMillis();
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"recommend_title" : recommend_title,
						"recommend_author" : recommend_author,
						"recommend_picurl" : recommend_picurl,
						"recommend_video_id" : recommend_video_id,
						"push_time" : push_time,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"timestamp" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("article",[edit:id]);
		return OK();
	}
	
	
	
	def buildinfourl(String id)
	{
		h5domain+"kj-article.html?article_id="+id;
	}
	def build_voiceinfourl(String id)
	{
		h5domain+"kj-radio.html?_id="+id;
	}
	/**
	 * 新增语音recommend_type (integer, optional): 推荐类型：0：直播 1：语音 2：文章信息
	 */
	def addvoice(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = UUID.randomUUID().toString();
		def voice = $$("_id" : id);
		
		String day_push_time = req["push_time"];
		Long push_time = null;
		if(StringUtils.isNotBlank(day_push_time)){
			push_time = new SimpleDateFormat(DFMT).parse(day_push_time).getTime();
			voice.put("push_time", push_time);
		} else {
			voice.put("push_time", System.currentTimeMillis());
		}
		
		voice.put("create_user_id", user.get("_id") as Integer);
		voice.put("create_time", System.currentTimeMillis());
		voice.put("recommend_title", req["recommend_title"]);
		voice.put("recommend_info", req["recommend_info"]);
		voice.put("recommend_infourl", req["recommend_infourl"]);
		voice.put("recommend_mp3url", req["recommend_mp3url"]);
		voice.put("recommend_radio_time", req["recommend_radio_time"]);
		voice.put("recommend_radio_teacher_name", req["recommend_radio_teacher_name"]);
		voice.put("recommend_radio_teacher_pic", req["recommend_radio_teacher_pic"]);
		voice.put("recommend_radio_banner", req["recommend_radio_banner"]);
		voice.put("recommend_html", req["recommend_html"]);
		voice.put("voiceurl", build_voiceinfourl(id));
		voice.put("summary", req["summary"]);
		voice.put("recommend_type", 1);
		table().save(voice);
		Crud.opLog("voice",[save:voice["_id"]]);
		return OK();
	}

	/**
	 * 修改文章
	 */
	def editarticle(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req["_id"];
		String recommend_title = req["recommend_title"];
		String recommend_author = req["recommend_author"];
		String recommend_picurl = req["recommend_picurl"];
		String recommend_infourl = req["recommend_infourl"];
		String recommend_info = req["recommend_info"];
		String summary = req["summary"];
		
		String day_push_time = req["push_time"];
		Long push_time = null;
		if(StringUtils.isNotBlank(day_push_time)){
			push_time = new SimpleDateFormat(DFMT).parse(day_push_time).getTime();
		} else {
			push_time = System.currentTimeMillis();
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"recommend_title" : recommend_title,
						"recommend_author" : recommend_author,
						"recommend_picurl" : recommend_picurl,
						"recommend_infourl" : buildinfourl(id),
						"recommend_info" : recommend_info,
						"summary" : summary,
						"push_time" : push_time,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"timestamp" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("article",[edit:id]);
		return OK();
	}
	
	
	/**
	 * 修改文章
	 */
	def edittkarticle(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req["_id"];
		String recommend_title = req["recommend_title"];
		String recommend_author = req["recommend_author"];
		String recommend_picurl = req["recommend_picurl"];
		String recommend_infourl = req["recommend_infourl"];
		String recommend_info = req["recommend_info"];
		String summary = req["summary"];
		String tk_article_type_name = req["tk_article_type_name"];
		Integer order =Integer.valueOf( req["order"].toString());

		String day_push_time = req["push_time"];
		Long push_time = null;
		if(StringUtils.isNotBlank(day_push_time)){
			push_time = new SimpleDateFormat(DFMT).parse(day_push_time).getTime();
		} else {
			push_time = System.currentTimeMillis();
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"recommend_title" : recommend_title,
						"recommend_author" : recommend_author,
						"recommend_picurl" : recommend_picurl,
						"recommend_infourl" : buildinfourl(id),
						"recommend_info" : recommend_info,
						"summary" : summary,
						"push_time" : push_time,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"tk_article_type_name" : tk_article_type_name,
						"order" : order,					
						"timestamp" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("article",[edit:id]);
		return OK();
	}
	
	/**
	 * 修改语音
	 */
	def editvoice(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req["_id"];
		String recommend_title = req["recommend_title"];
		String recommend_info = req["recommend_info"];
		String recommend_infourl = req["recommend_infourl"];
		String recommend_mp3url = req["recommend_mp3url"];
		String recommend_radio_time = req["recommend_radio_time"];
		String summary = req["summary"];
		String recommend_radio_teacher_name = req["recommend_radio_teacher_name"];
		String recommend_radio_teacher_pic = req["recommend_radio_teacher_pic"];
		String recommend_html = req["recommend_html"];
		String recommend_radio_banner = req["recommend_radio_banner"];
		
		String voiceurl = build_voiceinfourl(id);
	
		String day_push_time = req["push_time"];
		Long push_time = null;
		if(StringUtils.isNotBlank(day_push_time)){
			push_time = new SimpleDateFormat(DFMT).parse(day_push_time).getTime();
		} else {
			push_time = System.currentTimeMillis();
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"recommend_title" : recommend_title,
						"recommend_radio_time" : recommend_radio_time,
						"recommend_infourl" : recommend_infourl,
						"recommend_mp3url" : recommend_mp3url,				
						"recommend_info" : recommend_info,
						"recommend_html" : recommend_html,
						"voiceurl" : voiceurl,
						"summary" : summary,
						"recommend_radio_teacher_name" : recommend_radio_teacher_name,
						"recommend_radio_teacher_pic" : recommend_radio_teacher_pic,
						"recommend_radio_banner" : recommend_radio_banner,
						"push_time" : push_time,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"timestamp" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("voice",[edit:id]);
		return OK();
	}

	/**
	 * 推荐/不推荐
	 */
	def recommend(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		Map map = new HashMap();
		String id = req["_id"]
		def status = req.getParameter("status")
		if(StringUtils.isBlank(status)){
			status = 0
		}
		status = status as Integer
		if(status == 0){
			status = 1
		}else{
			status = 0
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"is_recommend" : status,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"timestamp" : System.currentTimeMillis()
					)
				));
		table().update(
				new BasicDBObject("_id":id, "push_time":['$lt': System.currentTimeMillis()]),
				new BasicDBObject('$set':
					new BasicDBObject(
						"push_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("recommend",[edit:id]);
		return OK();
	}
	
	/**
	 * 删除
	 */
	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog("recommend",[del:id]);
		return OK();
	}
}
