package com.izhubo.web.mobile

import com.alibaba.fastjson.JSONObject
import com.hqonline.model.HK
import com.izhubo.model.AppLiveState
import com.izhubo.model.DailyRecommendType
import com.izhubo.rest.AppProperties
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.rest.common.util.http.HttpClientUtil4_3
import com.izhubo.utils.DataUtils
import com.izhubo.web.BaseController
import com.izhubo.web.vo.*
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiParam
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

import static com.izhubo.rest.common.doc.MongoKey.$or
import static com.izhubo.rest.common.doc.MongoKey.$set
import static com.izhubo.rest.common.util.WebUtils.$$

/**
 * 每日推送相关内容
 * @date 2016年3月9日 下午5:42:49
 * @param @param request
 */
@Controller
@RequestMapping("/dailypush")
class DailyPushController extends BaseController {


	private String live_rever_url= "http://m.kjcity.com/hqzx/lib/live.ashx";

	private String live_rever_key = "I87HDCcnMgkwcul09GxX";

	private String usdomain = AppProperties.get("us.domain").toString();
	private String hqjyh5domain = AppProperties.get("hqjyh5.domain").toString();
	private String picdomain = AppProperties.get("pic.domain").toString();
	private String promotiondomain = AppProperties.get("promotion.domain").toString()
	private String genseehlsurl = AppProperties.get("gensee.hls.url").toString()



	/** 教师文章 */
	public DBCollection hq_articles() {
		return mainMongo.getCollection("hq_articles");
	}


	/** 课程列表 */
	public DBCollection tk_article_type() {
		return mainMongo.getCollection("tk_article_type");
	}

	/** 课程列表 */
	public DBCollection bannerCourse() {
		return mainMongo.getCollection("banner_course");
	}
	/** 课程列表 */
	public DBCollection bannerCourse310() {
		return mainMongo.getCollection("banner_course_310");
	}


		/** banner */
	public DBCollection banner() {
		return mainMongo.getCollection("banner");
	}

	/** 3.0直播表 */
	public DBCollection app_live() {
		return mainMongo.getCollection("app_live");
	}

	/** 自考直播表 */
	public DBCollection app_live_zikao() {
		return mainMongo.getCollection("app_live_zikao");
	}

	public DBCollection jyxt_review() {
		return mainMongo.getCollection("jyxt_review");
	}
	
	/** 入口参数 */
	public DBCollection ah_url() {
		return mainMongo.getCollection("ah_url");
	}

	
	/** 每日推荐数据库 */
	public DBCollection dailypush_recommend() {
		return mainMongo.getCollection("daily_recommend");
	}

	/** 直播預約情況 */
	public DBCollection live_reservation() {
		return mainMongo.getCollection("app_live_reservation");
	}


	/** 直播預約情況 */
	public DBCollection live_reservation_other() {
		return mainMongo.getCollection("app_live_reservation_other");
	}

	/** 自考小程序直播預約情況 */
	public DBCollection live_reservation_zikao() {
		return mainMongo.getCollection("app_live_reservation_zikao");
	}

	/** 小程序手机号绑定 */
	private DBCollection applet_phone() {
		return mainMongo.getCollection("applet_phone");
	}


	@ResponseBody
	@RequestMapping(value = "articleList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "教师文章列表", httpMethod = "GET" , notes = "根据文章类型获取文章列表")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "type", value = "文章类型 0.企业动态 1.企业文化 2.总裁语录 3.2017年会", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "页大小", required = false, dataType = "int", defaultValue="20", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", defaultValue="1", paramType = "query")
	])
	def articleList(HttpServletRequest request){

		List list = new ArrayList();

		int type = ServletRequestUtils.getIntParameter(request, "type", 0);
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		def aList = hq_articles().find(
				$$("article_type" : type , "dr" : 0),
				$$(
				"_id" : 1 , "article_title" : 1 , "article_info" : 1, "article_pic" : 1,
				"article_url" : 1, "article_author" : 1, "timestamp_text" : 1, "read_count" : 1
				)
				).sort($$("timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();
		if(aList){
			aList.each {def obj->
				Map map = new HashMap();
				map["_id"] = obj["_id"];						//ID
				map["recommend_title"] = obj["article_title"];	//文章标题
				map["summary"] = obj["article_info"];			//文件简介
				map["recommend_infourl"] = obj["article_url"];	//文章地址
				map["recommend_picurl"] = obj["article_pic"];	//文章图片
				map["recommend_author"] = obj["article_author"];//文章作者
				map["timestamp_text"] = obj["timestamp_text"];	//文章时间
				map["read_count"] = obj["read_count"];			//阅读数量
				map["recommend_type"] = 2;						//类型
				list.add(map);
			}
		}
		return getResultOK(list);
	}

	@ResponseBody
	@RequestMapping(value = "articleTeacherDetail", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "教师文章详情", httpMethod = "GET" , notes = "根据ID获取教师文章详情")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "article_id", value = "文章id", required = true, dataType = "String", paramType = "query")
	])
	def articleTeacherDetail(HttpServletRequest request){
		String article_id = request["article_id"];
		if(StringUtils.isBlank(article_id)){
			return getResultParamsError();
		}
		Map map = new HashMap();
		def article = hq_articles().findOne(
				$$("_id" : article_id , "dr" : 0),
				$$("article_title" : 1 , "read_count" : 1 , "timestamp_text" : 1 , "article_author" : 1 , "article_html" : 1 )
				);
		if(article){
			//阅读量
			Integer read_count = article["read_count"] as Integer;

			if(read_count == null)read_count = 0;
			//增加阅读量
			read_count++;
			hq_articles().update($$("_id" : article_id), $$('$set' : $$("read_count" : read_count)));
			//修改数据格式
			map["recommend_title"] = article["article_title"];
			map["recommend_info"] = article["article_html"];
			map["read_count"] = read_count;
			map["timestamp"] = article["timestamp_text"];
			map["recommend_author"] = article["article_author"];
		}
		return getResultOK(map);
	}


	private buildLiveReverUrl(Integer user_id,String live_id) {
		if(mainMongo.getCollection("users").findOne($$("_id":user_id)) == null) {
			String url = live_rever_url+"?name="+""+"&id="+live_id+"&skey="+"";
			return url;
		}
		String tuid = mainMongo.getCollection("users").findOne($$("_id":user_id)).get("tuid").toString();
		String username = qquserMongo.getCollection("qQUser").findOne($$("tuid":tuid)).get("username");

		//?name=13826095803&id=720&skey=xxx skey=md5(id+密钥)


		String skey = MsgDigestUtil.MD5.digest2HEX( live_id+live_rever_key);

		String url = live_rever_url+"?name="+username+"&id="+live_id+"&skey="+skey;

		return url;


	}

	private buildjyxtUrl(String jyxturl,Integer user_id) {
		if(mainMongo.getCollection("users").findOne($$("_id":user_id)) == null) {
			String url = jyxturl+"?name="+"&skey="+"";
			return url;
		}
		String tuid = mainMongo.getCollection("users").findOne($$("_id":user_id)).get("tuid").toString();
		String username = qquserMongo.getCollection("qQUser").findOne($$("tuid":tuid)).get("username");

		//me=13826095803&id=720&skey=xxx skey=md5(id+密钥)


		String skey = MsgDigestUtil.MD5.digest2HEX(username+live_rever_key);

		String url = jyxturl+"?name="+username+"&skey="+skey;

		return url;


	}


	private int check_reserve(Integer user_id,String live_id) {
		long cou = live_reservation().count($$(["user_id" : user_id,"live_id" : live_id]));
		if(cou>0) {
			return 1;
		}
		else {
			return 0
		};
	}




	//判断时间是否在当前范围
	//是否在直播中 0：尚未开始  1：已经开始 2：已经结束
	def checkTimeZone(Date start,Date end,Date now)
	{
		if(now<start)
		{
			return  0;
		}
		else if(now>end)
		{
			return  2;
		}
		else
		{
			return  1;
		}
	}

	public static String getTimeShort(Date time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String dateString = formatter.format(time);
		return dateString;
	}


	public static String getTimeShortLive(Date time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String dateString = formatter.format(time);
		return dateString;
	}

	/**
	 * 获取现在时间
	 *
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getStringDateShort(Date time) {
		Date currentTime =time;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	public static String getStringDate(Date time) {
		Date currentTime =time;
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getStringDateNormal(Date time) {
		Date currentTime =time;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	@ResponseBody
	@RequestMapping(value = "dailypushInfo", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "每日推送详情", httpMethod = "GET" , response = DailyRecommandVO.DailyRecommandVOData.class , notes = "根据ID获取每日推送详情")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "id", value = "xxx", required = true, dataType = "String", paramType = "query")
	])
	def dailypushInfo(HttpServletRequest request){
		String id = request["id"];
		if(StringUtils.isNotBlank(id)){
			BasicDBObject query = new BasicDBObject();
			//		  query.append("is_recommend" , 1);
			query.append("_id" , id);
			def data = dailypush_recommend().findOne(query);
			return getResultOK(data);
		}
		return getResultParamsError();
	}

	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "daily_index_recommend", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "APP首页每日推送接口", httpMethod = "GET",  notes = "APP首页每日推送接口", response = DailyRecommandVO.class)

	def index_dailypush(
			@ApiParam(required = true, name = "user_id", value = "用户id")@RequestParam(value = "user_id") Integer user_id,
			HttpServletRequest request
	){
		if(!isLogin(user_id)){
			user_id = 0;
		}
		
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		BasicDBObject query = new BasicDBObject();
		query.append("is_recommend" , 1);
		query.append("push_time", [ '$lte': System.currentTimeMillis() ]);
		//query.append("type", CommodityType.单项商品.ordinal());
		//查询字段


		//排序
		def sort = $$("push_time" : -1);



		//商品列表
		//def commodity = commoditys().findOne(query , show);


		def queryResult = dailypush_recommend().find(query , $$("recommend_html" : 0)).limit(5).sort(sort).toArray();

		if(queryResult){
			queryResult.each {def dbo->

				dbo["timestamp"] = dbo["push_time"];
				
				//先判断是否在日期内，在日期内的，再具体的小时判断是否在直播

				if(dbo["recommend_type"].equals(DailyRecommendType.直播.ordinal()))
				{

					processLiveData(user_id,dbo);
				}

				if(dbo["recommend_type"].equals(DailyRecommendType.文章.ordinal()))
				{
					dbo["recommend_info"] = "";
				}

			}
		}


		return getResultOK(queryResult);
	}


	def getRecommandData(int user_id, int recommend_type,int limit)
	{
		BasicDBObject query = new BasicDBObject();
		query.append("push_time", [ '$lte': System.currentTimeMillis() ]);
		query.append("is_recommend" , 1);
		query.append("recommend_type", recommend_type);
		
		//查询字段

		//排序
		def sort = $$("push_time" : -1);

		def queryResult = dailypush_recommend().find(query).limit(limit).sort(sort).toArray();

		if(queryResult){
			queryResult.each {def dbo->

				dbo["timestamp"] = dbo["push_time"];
				
				//先判断是否在日期内，在日期内的，再具体的小时判断是否在直播

				if(dbo["recommend_type"].equals(DailyRecommendType.直播.ordinal()))
				{
					processLiveData(user_id,dbo);
				}

				if(dbo["recommend_type"].equals(DailyRecommendType.文章.ordinal()))
				{
					dbo["recommend_info"] = "";
				}
				if(dbo["recommend_type"].equals(DailyRecommendType.语音.ordinal()))
				{
					if( dbo["recommend_html"])
					{
						dbo["recommend_html"] = "";
					}
					dbo.put("radio_detail_url", BuildRadioUrl(dbo["_id"]));



				}

				dbo["timestamp_text"] = getStringDateNormal(new java.util.Date(Long.valueOf(dbo["timestamp"].toString())));
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date = format.parse(dbo["timestamp_text"]);
				dbo["str_time_text"] = com.izhubo.util.RelativeDateFormat.format(date);
			}
		}

		return queryResult;

	}
	def String BuildRadioUrl(String _id)
	{
		return hqjyh5domain +"/u-radio.html?_id="+_id;
	}

	def String BuildRadioUrl310(String _id)
	{
		return hqjyh5domain +"/kj-radio.html?_id="+_id;
	}

	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "daily_index_recommend_v250", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "APP首页每日推送接口v2", httpMethod = "GET",  notes = "APP首页每日推送接口", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "user_id", value = "user_id", required = false, dataType = "String", paramType = "query")
	])
	def index_dailypush_v250(HttpServletRequest request){
		String token = request["token"].toString();
		int user_id   = ServletRequestUtils.getIntParameter(request,"user_id", 0);
		if(token!="null")
		{
			user_id = getUserIdByToken(token);
		}
		else if(user_id>0)
		{
			token = 	getToken(user_id);
		}
		else
		{
			token = "";
			user_id = 0;
		}




		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		BasicDBObject query = new BasicDBObject();
		def jyxtlist = jyxt_review().find().toArray();
		if(token!="null")
		{
			jyxtlist.each {x->
				x.put("recommend_infourl", buildjyxtUrl(x.get("recommend_infourl").toString(),user_id));
				x.put("live_reservation_url", buildjyxtUrl(x.get("live_reservation_url").toString(),user_id));
			}
		}
		
		query.append("live" , jyxtlist);
		query.append("radio", getRecommandData(user_id,1,2));
		query.append("article", getRecommandData(user_id,2,2));
		def list = bannerCourse().find(null , $$("url" : 1 , "pic" : 1 , "_id" : 0)).sort($$("sort" : 1)).limit(4).toArray();
		if(token!="null")
		{
			list.each {x->
				x.put("url", BuildCourseUrl(token,x.get("url").toString()));
			}
		}
		query.append("course", list);


		//查询字段
		return getResultOK(query);
	}


	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "daily_index_recommend_v310", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "APP首页接口v310", httpMethod = "GET",  notes = "APP首页接口v310", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "user_id", value = "user_id", required = false, dataType = "String", paramType = "query")
	])
	def daily_index_recommend_v310(HttpServletRequest request){
		String token = request["token"].toString();
		int user_id   = ServletRequestUtils.getIntParameter(request,"user_id", 0);
		if(token!="null")
		{
			user_id = getUserIdByToken(token);
		}
		else if(user_id>0)
		{
			token = getToken(user_id);
		}
		else
		{
			token = "";
			user_id = 0;
		}




		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		BasicDBObject query = new BasicDBObject();
		List<DBObject> bannerList =  banner().find(null , $$("url" : 1 , "pic" : 1 , "_id" : 0)).sort($$("sort" : 1)).limit(10).toArray()
		for(DBObject db : bannerList) {
			db.put("url",db.get("url") + "&token=" + token)
		}
		query.append("banner" , bannerList);
		//直播在前 未开始第二 结束过后


		ArrayList livelist = get_app_livelist(4);

		livelist.each {def item->
			//循环获取直播的类型live_type
			String live_type = "1001"
			if(null.equals(item["live_type"]) || (item["live_type"])==""){
				item["live_type"] = "1001"
			}else{
				live_type = item["live_type"]
			}
			item["live_reservation_state"] = check_reserve(user_id,item["_id"]);
			item["live_detail_url"] = buildliveurl(token,item["_id"],live_type);

		}

		query.append("live" ,livelist);
		def list = bannerCourse310().find(null , $$("url" : 1 , "pic" : 1 , "_id" : 0)).sort($$("sort" : 1)).limit(4).toArray();

		if(token!="null")
		{
			list.each {x->
				x.put("url", BuildCourseUrl310(token,x.get("url").toString()));
			}
		}
		query.append("course", list);

		def jyxtlist = jyxt_review().find().toArray();
		if(token!="null")
		{
			jyxtlist.each {x->
				x.put("recommend_infourl", buildjyxtUrl(x.get("recommend_infourl").toString(),user_id));
				x.put("live_reservation_url", buildjyxtUrl(x.get("live_reservation_url").toString(),user_id));
			}
		}
		query.append("vod", jyxtlist);
		query.append("article", getRecommandData(user_id,2,3));

		def recommandaudio = getRecommandData(user_id,1,3);
		recommandaudio.each { item->
			item.put("radio_detail_url", BuildRadioUrl310(item["_id"]));
			//如果图片不存在，则给随机头像
			if(!item.get("recommend_radio_banner")||item.get("recommend_radio_banner").toString().equals("")||item.get("recommend_radio_banner").toString().equals("null"))
			{
				item.put("recommend_radio_banner", getRamdomTeacherPic());
			}

		}
		query.append("radio", recommandaudio);
		//学习中心入口
		def learningcenter = ah_url().findOne($$("_id":1002));
		//learningcenter的URL拼接起来
		String learningURL = learningcenter["url"] + token
		learningcenter.put("url", learningURL)
		
		query.append("learningcenter", learningcenter);
		//查询字段
		return getResultOK(query);
	}

	/**
	 * 随机生成4张头像
	 * @return
	 */
	def getRamdomTeacherPic()
	{
		//http://answerimg.kjcity.com/res/default_teacher_pic/t1.png
		int number = new Random().nextInt(3) + 1;
		String picurl = picdomain + "res/default_teacher_pic/t"+number+".png";
		return picurl;
	}



	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "get_app_live_list", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取直播列表页面", httpMethod = "GET",  notes = "获取直播列表页面", response = AppLiveListVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "user_id", value = "user_id", required = false, dataType = "String", paramType = "query")
	])
	def get_app_live_list(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品

		String token = request["token"].toString();
		int user_id   = ServletRequestUtils.getIntParameter(request,"user_id", 0);
		if(token!="null")
		{
			user_id = getUserIdByToken(token);
		}
		else if(user_id>0)
		{
			token = 	getToken(user_id);
		}
		else
		{
			token = "";
			user_id = 0;
		}

		ArrayList livelist = get_app_livelist(50);
		livelist.each {def item->
			String live_type = "1001"
			if(null.equals(item["live_type"]) || (item["live_type"])==""){
				item["live_type"] = "1001"
			}else{
				live_type = item["live_type"]
			}
			item["live_reservation_state"] = check_reserve(user_id,item["_id"]);
			item["live_detail_url"] = buildliveurl(token,item["_id"],live_type);

		}



		return getResultOK(livelist);
	}

	def  buildliveurl(String token,String live_id,String live_type)
	{
		return hqjyh5domain +"/kj-live-detail.html?live_id="+live_id+"&live_type="+live_type;
	}

	/**
	 * 获取直播间详情
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "get_app_live_detail", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取直播间详情", httpMethod = "GET",  notes = "获取直播间详情", response = AppLiveDetailVO.class)
	@ApiImplicitParams([
			@ApiImplicitParam(name = "access_token", value = "access_token", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "live_id", value = "live_id", required = true, dataType = "String", paramType = "query")
	])
	def get_app_live_detail(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO


		String token = request["access_token"].toString();
		Integer user_id = 0;
		if("null".equals(token)||null == token) {
			user_id = 0;
		}
		else
		{
			user_id = getUserIdByToken(token);
		}
        String live_id =  request["live_id"].toString();
		def applive =  app_live().findOne($$("_id":live_id));
		liveClassStateStringProcess((Long)applive.get("live_start_time"),(Long)applive.get("live_end_time"),applive);
		applive.put("live_time_detail",liveOpenTimeFormate((Long)applive.get("live_start_time"),(Long)applive.get("live_end_time")));
		if(0<user_id)
		{
			applive["live_reservation_state"] = check_reserve(user_id,live_id);
		}
		else
		{
			applive["live_reservation_state"] = 0;
		}
		if(null == applive["live_ad_app_isshow"]) {
			applive["live_ad_app_isshow"] = 0;
		}
		long cou = live_reservation().count($$("live_id" : live_id));
		long cou_other = live_reservation_other().count($$("live_id" : live_id));

		applive["live_reservation_count"] =  cou+cou_other;

		applive["live_refer_list"] = app_live().find($$("dr":0),$$("live_title":1,"live_teacher_name":1,"live_teacher_pic":1,"live_banner_url":1,"live_correlation_pic":1,"live_start_time":1,"live_end_time":1)).toArray();

		applive["live_refer_list"].each { sitem->
			if(sitem.get("live_correlation_pic")){ }else{
				sitem.put("live_correlation_pic",sitem.get("live_banner_url"));
			}
			sitem.put("live_time_detail",liveOpenTimeFormate((Long)sitem.get("live_start_time"),(Long)sitem.get("live_end_time")));
		}
		//新增sso_userid
		applive["sso_userid"] = 0
		if(user_id!=0){
			applive["sso_userid"] = users().findOne($$("_id":user_id)).get("sso_userid");
		}
		//增加一个是不是CPA学员的判断
		String phone = 0;
		Integer is_cpa_students = 0;
		if(user_id!=0 && applive["live_type"]==1201){
			phone = getUserPhoneByUserId(user_id);
				//CPA课时列表
				def cpa_course_list = constants().find(null,$$("cpa_course_ids":1))?.toArray();
				def cpa_course_ids = cpa_course_list.get(0)["cpa_course_ids"];
				//报名表中CPA学员的报名信息
				def cpa_signs_user = signs().find($$("nc_commodity_id" : $$('$in':cpa_course_ids),"phone":phone,"dr":0),$$("phone" : 1,"nc_course_id":1))?.toArray();
				if(cpa_signs_user.size() != 0){
					is_cpa_students = 1
				}
		}
		applive["is_cpa_students"] = is_cpa_students
		//增加回放平台字段
		Integer live_studio_type = 2
		if(null.equals(applive["live_studio_type"]) || (applive["live_studio_type"]=="")){
			if(null.equals(applive["live_reply_url"]) || (applive["live_reply_url"]=="")){
				live_studio_type = 1
			}else{
				live_studio_type = 2
			}
		}else{
			live_studio_type = applive["live_studio_type"]
		}
		applive["live_studio_type"] = live_studio_type
		
		//增加学员类型的判断
		def student_type_list = [1001];
		
		if(user_id!=0 && applive["live_type"]!=1001){
			//CPA字段留下的坑
			if(is_cpa_students){
				student_type_list<<1201
			}else{
				phone = getUserPhoneByUserId(user_id);
				student_type_list = getStudentTypeList(phone,applive["live_type"]);
			}
		}
		/*
		//2017-11-03日晚上的直播。临时解决方案。后续马上改进
		if(is_cpa_students){
			student_type_list<<1201
		}*/
		
		applive["student_type_list"] = student_type_list
		
		
		return getResultOK(applive);
	}
	/**
	 * 根据电话号码获取学员所有报名类型
	 */
	def ArrayList getStudentTypeList(String phone,Integer live_type)
	{
		def student_type_list = [1001];
		switch(live_type){
			case 1201:
			//CPA学员
				if(getStudentType(phone,"cpa_course_ids",1201)==1201){
					student_type_list<<1201
				}
				break;
			case 1301:
			//中央财大学员
				if(getStudentType(phone,"central_deep_course_ids",1301)==1301){
					student_type_list<<1301
				}
				break;
			case 1401:
			//全能级以上学员
				if(getStudentType(phone,"above_all_around_course_ids",1401)==1401){
					student_type_list<<1401
				}
				break;
		}
		return student_type_list
	}
	
	def Integer getStudentType(String phone,String table_course,Integer studentType)
	{
		Integer student_type = 0;
		if(StringUtils.isNotBlank(phone)&&StringUtils.isNotBlank(table_course)&&studentType!=0){
			//课时列表
			def course_list = constants().find()?.toArray();
			def student_course_ids = course_list.get(0)[table_course];
			if("null".equals(student_course_ids)){}else{
				//报名表中学员的报名信息
				def cpa_signs_user = signs().find($$("nc_commodity_id" : $$('$in':student_course_ids),"phone":phone,"dr":0),$$("phone" : 1,"nc_course_id":1))?.toArray();
				if(cpa_signs_user.size() != 0){
					student_type = studentType
				}
			}
			
		}
		
		return student_type
	}
	
	
	
	
	
	

	def String BuildCourseUrl(String token,String url)
	{
		return usdomain +"/redirect_to_other?access_token="+token+"&rd_url="+url;
	}
	def String BuildCourseUrl310(String token,String url)
	{
		return url+"&token="+token;
	}






	private void liveClassStateStringProcess(Long startTime,Long endTime,Map map){
		Long now = System.currentTimeMillis();
		if(startTime > now){
			if((startTime-30*1000*60)<now){
				map.put("live_state",AppLiveState.即将开始.ordinal())
				map.put("live_state_text",AppLiveState.即将开始.name())
			}else{
			map.put("live_state",AppLiveState.尚未开始.ordinal())
			map.put("live_state_text",AppLiveState.尚未开始.name())
			}
		}
		else if(startTime<now && endTime>now){
			map.put("live_state",AppLiveState.直播中.ordinal())
			map.put("live_state_text",AppLiveState.直播中.name())
		}
		else
		{
            if("".equals(map.get("live_reply_url")) && "".equals(map.get("live_reply_id"))){
                map.put("live_state", AppLiveState.已结束.ordinal());
                map.put("live_state_text", AppLiveState.已结束.name());
            }
            else
            {
                map.put("live_state", AppLiveState.看回放.ordinal())
                map.put("live_state_text", AppLiveState.看回放.name())
            }
		}
	}

	def ArrayList get_app_livelist(int limit)
	{
		def livelist = new ArrayList();
		def liveidlist = new ArrayList();
		int i=0;
		def showobj = $$("live_title":1,"live_banner_url":1,"live_time_detail":1,"live_start_time":1,"live_end_time":1,"live_num":1,"live_id":1,"live_domain":1,"live_course_detail":1,"live_reply_url":1,"live_type":1,"live_studio_type":1,"live_reply_room_number":1,"live_reply_id":1)
		//第一获取当前正在直播的
		app_live().find($$("live_start_time":$$($lt:System.currentTimeMillis()),"live_end_time":$$($gt:System.currentTimeMillis()),"dr":0),showobj).toArray().each { item->
			if(i<limit)
			{
				liveClassStateStringProcess((Long)item.get("live_start_time"),(Long)item.get("live_end_time"),item);
				//处理下开始时间和结束时间格式
				item.put("live_time_detail",liveOpenTimeFormate((Long)item.get("live_start_time"),(Long)item.get("live_end_time")));
				livelist.add(item);
				liveidlist.add(item.get("_id"));
				i++;
			}

		}

		app_live().find($$("dr":0),showobj).sort($$("live_start_time":-1)).toArray().each { item->
			if(i<limit)
			{
				if(!liveidlist.contains(item.get("_id")))
				{
					liveClassStateStringProcess((Long)item.get("live_start_time"),(Long)item.get("live_end_time"),item);
					//处理下开始时间和结束时间格式
					item.put("live_time_detail",liveOpenTimeFormate((Long)item.get("live_start_time"),(Long)item.get("live_end_time")));
					livelist.add(item);
					liveidlist.add(item.get("_id"));
					i++;
				}
			}

		}
		return livelist;

	}

	private  String liveOpenTimeFormate(Long start,Long end){
		if(start > 0&&end>0){
			SimpleDateFormat Startformatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
			SimpleDateFormat Endformatter = new SimpleDateFormat ("HH:mm");
			return Startformatter.format(new Date(start))+"~"+Endformatter.format(new Date(end));
		}
		return "-";
	}
	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "article_detail", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "文章详情接口", httpMethod = "GET",  notes = "文章详情接口", response = DailyRecommandVO.class)

	def article_detail(
			@ApiParam(required = true, name = "article_id", value = "文章id")@RequestParam(value = "article_id") String article_id,
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		BasicDBObject query = new BasicDBObject();
		def orobj = [
			$$( "recommend_type" ,   DailyRecommendType.文章.ordinal()),
			$$( "recommend_type" , DailyRecommendType.题库知识点.ordinal())
		];

		query.append($or, orobj);

		//query.append("recommend_type" , DailyRecommendType.文章.ordinal());
		query.append("_id" , article_id);


		def queryResult = dailypush_recommend().findOne(query);

		Integer count = 0;

		count = (queryResult["read_count"] ==null?0:(Integer)queryResult["read_count"]);

		count++;


		BasicDBObject up = new BasicDBObject("read_count", count);
		dailypush_recommend().update(query, new BasicDBObject($set, up));

		queryResult["timestamp_text"] = getStringDateNormal(new java.util.Date(Long.valueOf(queryResult["timestamp"].toString())));
		queryResult["timestamp"] = DataUtils.dateToString(Long.valueOf(queryResult["timestamp"].toString()));

		queryResult["read_count"] = count;
		
		return getResultOK(queryResult);
	}
	/**
	 * 文章语音详情
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "recommend_detail", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "文章/语音详情接口", httpMethod = "GET",  notes = "文章/语音详情接口", response = DailyRecommandVO.class)

	def recommend_detail(
			@ApiParam(required = true, name = "_id", value = "文章ID/语音ID")@RequestParam(value = "_id") String _id,
			HttpServletRequest request
	){
		//查询条件 已上架 套餐商品
		BasicDBObject query = new BasicDBObject();

		def orobj = [
			$$( "recommend_type" ,   DailyRecommendType.文章.ordinal()),
			$$( "recommend_type" ,   DailyRecommendType.语音.ordinal()),
			$$( "recommend_type" , DailyRecommendType.题库知识点.ordinal())
		];

		query.append($or, orobj);
		//			query.append("recommend_type", DailyRecommendType.语音.ordinal());

		query.append("_id" , _id);

		def queryResult = dailypush_recommend().findOne(query);

		Integer count = 0;

		count = (queryResult["read_count"] ==null?0:(Integer)queryResult["read_count"]);

		count++;


		BasicDBObject up = new BasicDBObject("read_count", count);
		dailypush_recommend().update(query, new BasicDBObject($set, up));


		queryResult["timestamp"] = DataUtils.dateToString(Long.valueOf(queryResult["timestamp"].toString()));

		queryResult["read_count"] = count;

		return getResultOK(queryResult);
	}
	def processLiveData(Integer user_id, DBObject dbo )
	{
		SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdfday = new SimpleDateFormat("MM月dd日");



		Date Start = sdf.parse(dbo["recommend_live_starttime"].toString());
		Date End = sdf.parse(dbo["recommend_live_endtime"].toString());
		Date now = new Date();
		String time_start = getTimeShort(sdftime.parse(dbo["recommend_live_starttime"].toString()));
		String time_end = getTimeShort(sdftime.parse(dbo["recommend_live_endtime"].toString()));

		String live_time_start = getTimeShortLive(sdftime.parse(dbo["recommend_live_starttime"].toString()));
		String live_time_end = getTimeShortLive(sdftime.parse(dbo["recommend_live_endtime"].toString()));

		Date todayStart =sdftime.parse(getStringDateShort()+" "+time_start);
		Date todayEnd =sdftime.parse(getStringDateShort()+" "+time_end);
		Date time_now = new Date();


		//是否在直播中 0：尚未开始  1：已经开始 2：已经结束
		if(now<Start)
		{
			dbo["is_live"] = 0;
		}
		else if(now>End)
		{
			dbo["is_live"] = 2;
		}
		else
		{
			dbo["is_live"] = checkTimeZone(todayStart,todayEnd,time_now);
		}

		dbo["is_reservation"] = check_reserve(user_id,dbo["_id"]);

		if(user_id>0)
		{
			String nick_name = mainMongo.getCollection("users").findOne($$("_id":user_id))?.get("nick_name")?.toString();
			dbo["recommend_infourl"]  =  dbo["recommend_infourl"].toString()+"?nickName="+nick_name+"_"+user_id;
		}
		dbo["live_reservation_url"] = buildLiveReverUrl(user_id,dbo["_id"]);

		if(getStringDateShort(sdftime.parse(dbo["recommend_live_starttime"].toString())) == getStringDateShort(sdftime.parse(dbo["recommend_live_endtime"].toString())))
		{
			dbo["live_time_directions"] =getStringDate(sdftime.parse(dbo["recommend_live_starttime"].toString()))+live_time_start+"-"+live_time_end;
		}
		else
		{
			dbo["live_time_directions"] = getStringDate(sdftime.parse(dbo["recommend_live_starttime"].toString()))+"-"+getStringDate(sdftime.parse(dbo["recommend_live_endtime"].toString()))+" "+live_time_start+"-"+live_time_end;
		}

		dbo["live_people_count"] = live_reservation().count($$("live_id" : dbo["live_id"]));
		//时间说明，按照格式

	}


	/**
	 * 微课观看人数叠加api
	 */
	@ResponseBody
	@RequestMapping(value = "video_readcount_add", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "微课观看人数叠加", httpMethod = "GET",  notes = "根据ID增加微课观看人数")
	def video_readcount_add(@ApiParam(required = true, name = "video_id", value = "微课记录id")@RequestParam(value = "video_id") String video_id, 
		@ApiParam(required = true, name = "play_channel", value = "播放渠道：0：推送播放 1：分享播放 2：原始播放")@RequestParam(value = "play_channel" ,defaultValue = "0") Integer play_channel) {
		
		Map map = new HashMap();
		if(play_channel == 0) {
			def video = dailypush_recommend().findOne($$("_id" : video_id),$$("push_read_count" : 1));
			if(null != video) {
				int push_read_count = 0;
				if(null != video["push_read_count"]) {
					push_read_count = video["push_read_count"] as Integer;
				}
				push_read_count++;
				dailypush_recommend().update($$("_id" : video_id), $$('$set' : $$("push_read_count" : push_read_count)));
				map["push_read_count"] = push_read_count;
			} else {
				map["msg"] = "微课ID不存在！";
			}
		} else if(play_channel == 1) {
			def video = dailypush_recommend().findOne($$("_id" : video_id),$$("share_read_count" : 1));
			if(null != video) {
				int share_read_count = 0;
				if(null != video["share_read_count"]) {
					share_read_count = video["share_read_count"] as Integer;
				}
				share_read_count++;
				dailypush_recommend().update($$("_id" : video_id), $$('$set' : $$("share_read_count" : share_read_count)));
				map["share_read_count"] = share_read_count;
			} else {
				map["msg"] = "微课ID不存在！";
			}
		} else if(play_channel == 2) {
			def video = dailypush_recommend().findOne($$("_id" : video_id),$$("original_read_count" : 1));
			if(null != video) {
				int original_read_count = 0;
				if(null != video["original_read_count"]) {
					original_read_count = video["original_read_count"] as Integer;
				}
				original_read_count++;
				dailypush_recommend().update($$("_id" : video_id), $$('$set' : $$("original_read_count" : original_read_count)));
				map["original_read_count"] = original_read_count;
			} else {
				map["msg"] = "微课ID不存在！";
			}
		}
		return getResultOK(map);
	}
		
	def buildvideourl(String recommend_video_id)
	{
		return hqjyh5domain +"/kj-weikeShare.html?recommend_video_id="+recommend_video_id;
	}

	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "get_dailypushlist_bytype", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "每日推送列表", httpMethod = "GET",  notes = "根據類型獲取每日推薦列表接口", response = DailyRecommandListVO.class)
	def get_dailypushlist_bytype(
			@ApiParam(required = true, name = "user_id", value = "用户id")@RequestParam(value = "user_id") Integer user_id,
			@ApiParam(required = true, name = "recommend_type", value = "推荐类型：0：直播		1：语音		2：文章信息 		3微课 ")@RequestParam(value = "recommend_type" ,defaultValue = "1") Integer recommend_type,
			@ApiParam(required = false, name = "page", value = "第page页")@RequestParam(required = false , value = "page" , defaultValue = "1") Integer page,
			@ApiParam(required = false, name = "size", value = "每页size条数据" )@RequestParam(required = false , value = "size" , defaultValue="15") Integer size,
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		BasicDBObject query = new BasicDBObject();
		query.append("recommend_type" , recommend_type);
		query.append("push_time", [ '$lte': System.currentTimeMillis() ]);
		
		//查询字段

		//排序
		def sort = $$("timestamp" : -1 , "push_time" : -1);


		def count = dailypush_recommend().count(query);
		int allpage = count / size + ((count% size) >0 ? 1 : 0);
		//查询结果
		def queryResult = [];
		if(count > 0){
			//需要查询的字段

			queryResult = dailypush_recommend().find(query).sort(sort).skip((page - 1) * size).limit(size).toArray();
		}


		if(queryResult){
			queryResult.each {def dbo->
				//push_time赋值在7月13号(初始化字段)之后使用
				if(dbo["push_time"]!=null && dbo["push_time"]>1499961599000){
					dbo["timestamp"] = dbo["push_time"];
				}
				
				//这里判断是否预约,預約表還沒設計

				if(dbo["recommend_type"].equals(DailyRecommendType.直播.ordinal()))
				{
					processLiveData(user_id,dbo);
				}

				if(dbo["recommend_type"].equals(DailyRecommendType.文章.ordinal()))
				{
					dbo["recommend_info"] = "";
				}

				if(dbo["recommend_type"].equals(DailyRecommendType.语音.ordinal()))
				{
					if( dbo["recommend_html"])
					{
						dbo["recommend_html"] = "";
					}
					dbo.put("radio_detail_url", BuildRadioUrl310(dbo["_id"]));
					if(!dbo.get("recommend_radio_banner")||dbo.get("recommend_radio_banner").toString().equals("")||dbo.get("recommend_radio_banner").toString().equals("null"))
					{
						dbo.put("recommend_radio_banner", getRamdomTeacherPic());
					}
					dbo["timestamp_text"] = getStringDateNormal(new java.util.Date(Long.valueOf(dbo["timestamp"].toString())));
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date date = format.parse(dbo["timestamp_text"]);
					dbo.put("str_time_text", com.izhubo.util.RelativeDateFormat.format(date));
					

				}
				
				if(dbo["recommend_type"].equals(DailyRecommendType.微课.ordinal())) 
				{
					if(null == dbo["push_read_count"]) dbo["push_read_count"] = 0;
					if(null == dbo["share_read_count"]) dbo["share_read_count"] = 0;
					if(null == dbo["original_read_count"]) dbo["original_read_count"] = 0;
					
					dbo["video_record_id"] = dbo["_id"];
					dbo["read_count"] = dbo["push_read_count"] + dbo["share_read_count"] + dbo["original_read_count"];
					dbo["video_url"] = buildvideourl(dbo["recommend_video_id"]);
				}
				
				if(dbo["timestamp"])
				{
					dbo["timestamp_text"]  = getStringDateNormal(new java.util.Date(Long.valueOf(dbo["timestamp"].toString())));
				}
			}
		}

		return getResultOK(queryResult, allpage, count , page , size);

	}
	
	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "get_tkarticle_list", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取题库文章列表", httpMethod = "GET",  notes = "根據類型獲取每日推薦列表接口", response = DailyRecommandListVO.class)
	def get_tkarticle_list(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		def typelist = tk_article_type().find().toArray();
		if(typelist){
			typelist.each {def dbo->
				//这里判断是否预约,預約表還沒設計
				BasicDBObject query = new BasicDBObject();
				query.append("recommend_type" , DailyRecommendType.题库知识点.ordinal());
				query.append("tk_article_type_name", dbo["type_name"].toString());
				BasicDBObject show = new BasicDBObject();
				show.append("recommend_info" , 0);
				BasicDBObject order = new BasicDBObject();
				order.append("order" , -1);
				dbo.put("list", dailypush_recommend().find(query,show).sort(order).toArray());
			}
		}
		//查询字段
		return getResultOK(typelist);
	}


	/**
	 * 会答首页-直播預約api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */

	@ResponseBody
	@RequestMapping(value = "reserve_live", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = " 会答首页-直播預約api", httpMethod = "POST",  notes = " 会答首页-直播預約api", response = BaseResultVO.class)
	def reserve_live(
			@ApiParam(required = true, name = "token", value = "token")@RequestParam(value = "token") String token,
			@ApiParam(required = true, name = "user_id", value = "user_id ")@RequestParam(value = "user_id" ) Integer user_id,
			@ApiParam(required = true, name = "live_id", value = "直播id ")@RequestParam(value = "live_id" ) String live_id,
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO

		BasicDBObject insert = new BasicDBObject();
		if(token!="null")
		{
			user_id = getUserIdByToken(token);
		}
		else if(user_id>0)
		{
			token = getToken(user_id);
		}
		else
		{
			token = "";
			user_id = 0;
		}
/**
		def cpa_live = app_live().find($$("_id":live_id,"live_type" : 1201))?.toArray();
		if(cpa_live){
			//使用user_id获取手机号码
			def user_phone = users().find($$("_id":user_id),$$("mobile":1))?.toArray();
			if(user_phone.size()==0){
				return getResult("99","抱歉，本场直播仅允许CPA的学员进行观看，不支持预约观看。");
			}else{
				def phone = user_phone.get(0)["mobile"];
				//判断是否是CPA直播
				def cpa_course_list = constants().find(null,$$("cpa_course_ids":1))?.toArray();
				//常量表中取出所有cpa的course_id
				def cpa_course_ids = cpa_course_list.get(0)["cpa_course_ids"];
				//报名表中CPA学员的报名信息
				def cpa_signs_user = signs().find($$("nc_commodity_id" : $$('$in':cpa_course_ids),"phone":phone,"dr":0),$$("phone" : 1,"nc_course_id":1))?.toArray();
				if(cpa_signs_user.size() != 0){
					//进入预约流程
					long cou = live_reservation().count($$(["user_id" : user_id,"live_id" : live_id]));
					if(cou>0){
						return getResult("99","预约失败，已经预约过了");
					}else{
						insert.append("user_id",user_id);
						insert.append("live_id",live_id);
						Long now = System.currentTimeMillis();
						insert.append("apply_time", now);
						live_reservation().insert(insert);

						return getResult("1","预约成功");
					}
				}else{
					return getResult("99","抱歉，本场直播仅允许CPA的学员进行观看，不支持预约观看。");
				}
			}
		}*/
		//判断是不是特定直播类型
		def live_type_def = app_live().find($$("_id":live_id))?.toArray();
		Integer live_type = live_type_def.get(0)["live_type"];
		if((!(null.equals(live_type))) && live_type!=1001){
			switch (live_type){
				case 1201 : 
					def result_map = get_live_limit(live_id,user_id,"cpa_course_ids")
					return getResult(result_map.get("code"),result_map.get("msg"));
					break;
				case 1301 : 
					def result_map = get_live_limit(live_id,user_id,"central_deep_course_ids")
					//println "测试："+result_map+"//////"+result_map.get("code")+"//////"+result_map.get("msg")
					return getResult(result_map.get("code"),result_map.get("msg"));
					break
				case 1401 :
					def result_map = get_live_limit(live_id,user_id,"above_all_around_course_ids")
					return getResult(result_map.get("code"),result_map.get("msg"));
					break;
				default : 
					
					break;
			}
		}else{
			long cou = live_reservation().count($$(["user_id" : user_id,"live_id" : live_id]));
			if(cou>0)
			{
				return getResult("99","预约失败，已经预约过了");
			}
			else
			{
				insert.append("user_id",user_id);
				insert.append("live_id",live_id);
				Long now = System.currentTimeMillis();
				insert.append("apply_time", now);
				live_reservation().insert(insert);

				return getResult("1","预约成功");
			}

		}
	}

	/**
	 * 根据直播间的类型获取限制提示语
	 * @author vince后期优化为（提示语从数据库获取）
	 */
	def Map<String,String> get_live_limit(String live_id, Integer user_id, String table_course){
		def res_map=[:]
		BasicDBObject insert = new BasicDBObject();
		//def cpa_live = app_live().find($$("_id":live_id,"live_type" : 1201))?.toArray();
		def cpa_live = app_live().find($$("_id":live_id))?.toArray();
		Integer live_type = cpa_live.get(0)["live_type"];
		switch (live_type){
			case 1201 :
			//def user_phone = users().find($$("_id":user_id),$$("mobile":1))?.toArray();
				String user_phone = getUserPhoneByUserId(user_id);
			//	println user_phone
				if(StringUtils.isBlank(user_phone)){
					res_map.put("code", "99")
					res_map.put("msg", "抱歉，本场直播仅允许已报名正式课程的学员进行观看，不支持预约观看。")
					//return getResult("99","抱歉，本场直播仅允许CPA的学员进行观看，不支持预约观看。");
					return res_map
				}else{
					def phone = user_phone
					//课时列表
					def course_list = constants().find()?.toArray();
					def course_ids = course_list.get(0)[table_course];
					//报名表中学员的报名信息
					def signs_user = signs().find($$("nc_commodity_id" : $$('$in':course_ids),"phone":phone,"dr":0),$$("phone" : 1,"nc_course_id":1))?.toArray();
					if(signs_user.size() != 0){
						//进入预约流程
						long cou = live_reservation().count($$(["user_id" : user_id,"live_id" : live_id]));
						if(cou>0){
							res_map.put("code", "99")
							res_map.put("msg", "预约失败，已经预约过了")
							//				return getResult("99","预约失败，已经预约过了");
							return res_map
						}else{
							insert.append("user_id",user_id);
							insert.append("live_id",live_id);
							Long now = System.currentTimeMillis();
							insert.append("apply_time", now);
							live_reservation().insert(insert);

							res_map.put("code", "1")
							res_map.put("msg", "预约成功")
							//return getResult("1","预约成功");
							return res_map
						}
					}else{
						res_map.put("code", "99")
						res_map.put("msg", "抱歉，本场直播仅允许已报名正式课程的学员进行观看，不支持预约观看。")
						//				return getResult("99","抱歉，本场直播仅允许CPA的学员进行观看，不支持预约观看。");
						return res_map
					}
				}
				break;
				
				
				case 1301 :
					String user_phone = getUserPhoneByUserId(user_id);
					if(StringUtils.isBlank(user_phone)){
						res_map.put("code", "99")
						res_map.put("msg", "抱歉，本场直播仅允许已报名正式课程的学员进行观看，不支持预约观看。")
						//return getResult("99","抱歉，本场直播仅允许CPA的学员进行观看，不支持预约观看。");
						return res_map
					}else{
						def phone = user_phone
						//课时列表
						def course_list = constants().find()?.toArray();
						def course_ids = course_list.get(0)[table_course];
						//报名表中学员的报名信息
						def signs_user = signs().find($$("nc_commodity_id" : $$('$in':course_ids),"phone":phone,"dr":0),$$("phone" : 1,"nc_course_id":1))?.toArray();
						if(signs_user.size() != 0){
							//进入预约流程
							long cou = live_reservation().count($$(["user_id" : user_id,"live_id" : live_id]));
							if(cou>0){
								res_map.put("code", "99")
								res_map.put("msg", "预约失败，已经预约过了")
								//return getResult("99","预约失败，已经预约过了");
								return res_map
							}else{
								insert.append("user_id",user_id);
								insert.append("live_id",live_id);
								Long now = System.currentTimeMillis();
								insert.append("apply_time", now);
								live_reservation().insert(insert);
	
								res_map.put("code", "1")
								res_map.put("msg", "预约成功")
								//return getResult("1","预约成功");
								return res_map
							}
						}else{
							res_map.put("code", "99")
							res_map.put("msg", "抱歉，本场直播仅允许已报名正式课程的学员进行观看，不支持预约观看。")
							//return getResult("99","抱歉，本场直播仅允许CPA的学员进行观看，不支持预约观看。");
							return res_map
						}
					}
					break;
					
					case 1401 :
					String user_phone = getUserPhoneByUserId(user_id);
					if(StringUtils.isBlank(user_phone)){
						res_map.put("code", "99")
						res_map.put("msg", "抱歉，本场直播仅允许已报名正式课程的学员进行观看，不支持预约观看。")
						//return getResult("99","抱歉，本场直播仅允许CPA的学员进行观看，不支持预约观看。");
						return res_map
					}else{
						def phone = user_phone
						//课时列表
						def course_list = constants().find()?.toArray();
						def course_ids = course_list.get(0)[table_course];
						//报名表中学员的报名信息
						def signs_user = signs().find($$("nc_commodity_id" : $$('$in':course_ids),"phone":phone,"dr":0),$$("phone" : 1,"nc_course_id":1))?.toArray();
						if(signs_user.size() != 0){
							//进入预约流程
							long cou = live_reservation().count($$(["user_id" : user_id,"live_id" : live_id]));
							if(cou>0){
								res_map.put("code", "99")
								res_map.put("msg", "预约失败，已经预约过了")
								return res_map
							}else{
								insert.append("user_id",user_id);
								insert.append("live_id",live_id);
								Long now = System.currentTimeMillis();
								insert.append("apply_time", now);
								live_reservation().insert(insert);
	
								res_map.put("code", "1")
								res_map.put("msg", "预约成功")
								return res_map
							}
						}else{
							res_map.put("code", "99")
							res_map.put("msg", "抱歉，本场直播仅允许已报名正式课程的学员进行观看，不支持预约观看。")
							return res_map
						}
					}
					break;
					
		}
		
		
		
	}
	
	
	


	/**
	 * 会答首页-直播預約api-外部预约接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */

	@ResponseBody
	@RequestMapping(value = "reserve_live_other", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = " 会答首页-直播预约-外部预约入口", httpMethod = "POST",  notes = "会答首页-直播预约-外部预约入口", response = BaseResultVO.class)
	def reserve_live_other(
			@ApiParam(required = true, name = "vcode", value = "vcode")@RequestParam(value = "vcode") String vcode,
			@ApiParam(required = true, name = "pass", value = "pass")@RequestParam(value = "pass") String pass,
			@ApiParam(required = true, name = "phone", value = "phone ")@RequestParam(value = "phone" ) String phone,
			@ApiParam(required = true, name = "live_id", value = "直播id ")@RequestParam(value = "live_id" ) String live_id,
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
	
		if(!isMobile(phone)) {
			return getResult("-1", "手机号不正确");
		}
		if(!isSixInteger(pass)) {
			return getResult("-2", "密码只能是6位数字");
		}

		//先判断是否预约
		long count = live_reservation_other().count($$("live_id":live_id,"phone":phone));
		if(count>0)
		{
			getResult("99","已经预约过了");
		}
		else
		{
			//如果没有预约判断验证码是否正确
			if(checkSecurity(phone,vcode)) {
				BasicDBObject insert = new BasicDBObject();
				insert.append("phone", phone);
				insert.append("live_id", live_id);
				insert.append("pass", pass);
				Long now = System.currentTimeMillis();
				insert.append("apply_time", now);
				live_reservation_other().insert(insert);
				removeRedisSecurity(phone);

				try
				{
					//对于预约成功的用户，推送商机到官网，来源显示会答
				}
				catch(Exception ex){

				}


				return getResult("1", "预约成功");
			}
			else {
				return getResult("0", "验证码错误");
			}

		}


	}

	/**
	 * 获取小程序直播列表页面api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "get_applet_app_live_list", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取小程序直播列表页面", httpMethod = "GET",  notes = "获取小程序直播列表页面", response = AppLiveListVO.class)
	def get_applet_app_live_list(
			@ApiParam(required = false, name = "phone", value = "phone ")@RequestParam(value = "phone" ) String phone,
	HttpServletRequest request){
		def livelist = new ArrayList();
		def liveidlist = new ArrayList();
		int i=0;
		int limit = 10
		def showobj = $$("live_title":1,"live_banner_url":1,"live_time_detail":1,"live_start_time":1,"live_end_time":1,"live_num":1,"live_id":1,"live_domain":1,"live_course_detail":1,"live_reply_url":1,"live_type":1,"live_studio_type":1,"live_reply_room_number":1,"live_reply_id":1)
		//第一获取当前正在直播的
		app_live().find($$("live_start_time":$$($lt:System.currentTimeMillis()),"live_end_time":$$($gt:System.currentTimeMillis()),"dr":0,"live_type":1001),showobj).toArray().each { item->
			if(i<limit)
			{
				liveClassStateStringProcess((Long)item.get("live_start_time"),(Long)item.get("live_end_time"),item);
				//处理下开始时间和结束时间格式
				item.put("live_time_detail",liveOpenTimeFormate((Long)item.get("live_start_time"),(Long)item.get("live_end_time")));
				//直播间预约人数
				long cou = live_reservation().count($$("live_id" : item.get("live_id")))
				long cou_other = live_reservation_other().count($$("live_id" : item.get("live_id")))
				item.put("live_reservation_count",cou+cou_other)
				if(!StringUtils.isBlank(phone)) {
					//判断是否预约
					long count = live_reservation_other().count($$("live_id":item.get("live_id"),"phone":phone))
					if(count>0) {
						item.put("is_reserve",1)
					} else {
						item.put("is_reserve",0)
					}
				} else {
					item.put("is_reserve",0)
				}
				item.put("live_hls_url",get_live_hls_address(item.get("live_id")))
				livelist.add(item);
				liveidlist.add(item.get("_id"));
				i++;
			}

		}

		app_live().find($$("dr":0,"live_type":1001),showobj).sort($$("live_start_time":-1)).toArray().each { item->
			if(i<limit)
			{
				if(!liveidlist.contains(item.get("_id")))
				{
					liveClassStateStringProcess((Long)item.get("live_start_time"),(Long)item.get("live_end_time"),item);
					//处理下开始时间和结束时间格式
					item.put("live_time_detail",liveOpenTimeFormate((Long)item.get("live_start_time"),(Long)item.get("live_end_time")));
					//直播间预约人数
					long cou = live_reservation().count($$("live_id" : item.get("live_id")));
					long cou_other = live_reservation_other().count($$("live_id" : item.get("live_id")));
					item.put("live_reservation_count",cou+cou_other);
					if(!StringUtils.isBlank(phone)) {
						//判断是否预约
						long count = live_reservation_other().count($$("live_id":item.get("live_id"),"phone":phone))
						if(count>0) {
							item.put("is_reserve",1)
						} else {
							item.put("is_reserve",0)
						}
					} else {
						item.put("is_reserve",0)
					}
					item.put("live_hls_url",get_live_hls_address(item.get("live_id")))
					livelist.add(item);
					liveidlist.add(item.get("_id"));
					i++;
				}
			}

		}
		return getResultOK(livelist);
	}

	/**
	 * 获取自考小程序直播列表页面api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "get_applet_app_live_list_zikao", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取自考小程序直播列表页面", httpMethod = "GET",  notes = "获取自考小程序直播列表页面", response = AppLiveListVO.class)
	@ApiImplicitParams([
			@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query")]
	)
	def get_applet_app_live_list_zikao(
			@ApiParam(required = false, name = "phone", value = "phone ")@RequestParam(value = "phone" ) String phone,
			HttpServletRequest request){
		int page  = ServletRequestUtils.getIntParameter(request,"page", 1)
		int size  = ServletRequestUtils.getIntParameter(request,"size", 10)
		def livelist = new ArrayList();
		def liveidlist = new ArrayList();
		def showobj = $$("live_title":1,"live_banner_url":1,"live_time_detail":1,"live_start_time":1,"live_end_time":1,"live_num":1,"live_id":1,"live_domain":1,"live_course_detail":1,"live_reply_url":1,"live_type":1,"live_studio_type":1,"live_reply_room_number":1,"live_reply_id":1)
		//第一获取当前正在直播的
		def itemLive = app_live_zikao().find($$("live_start_time":$$($lt:System.currentTimeMillis()),"live_end_time":$$($gt:System.currentTimeMillis()),"dr":0,"live_type":1001),showobj)
				.sort($$("live_start_time":-1)).limit(1).toArray()[0]
		if(null != itemLive) {
			liveClassStateStringProcess((Long)itemLive.get("live_start_time"),(Long)itemLive.get("live_end_time"),itemLive);
			//处理下开始时间和结束时间格式
			itemLive.put("live_time_detail",liveOpenTimeFormate((Long)itemLive.get("live_start_time"),(Long)itemLive.get("live_end_time")));
			//直播间预约人数
			long cou = live_reservation().count($$("live_id" : itemLive.get("live_id")))
			long cou_other = live_reservation_zikao().count($$("live_id" : itemLive.get("live_id")))
			itemLive.put("live_reservation_count",cou+cou_other)
			if(!StringUtils.isBlank(phone)) {
				//判断是否预约
				long count = live_reservation_zikao().count($$("live_id":itemLive.get("live_id"),"phone":phone))
				if(count>0) {
					itemLive.put("is_reserve",1)
				} else {
					itemLive.put("is_reserve",0)
				}
			} else {
				itemLive.put("is_reserve",0)
			}
			itemLive.put("live_hls_url",get_live_hls_address(itemLive.get("live_id")))
			livelist.add(itemLive);
			liveidlist.add(itemLive.get("_id"));
		}

		app_live_zikao().find($$("dr":0,"live_type":1001),showobj).sort($$("live_start_time":-1)).toArray().each { item->
			if(!liveidlist.contains(item.get("_id")))
			{
				liveClassStateStringProcess((Long)item.get("live_start_time"),(Long)item.get("live_end_time"),item);
				//处理下开始时间和结束时间格式
				item.put("live_time_detail",liveOpenTimeFormate((Long)item.get("live_start_time"),(Long)item.get("live_end_time")));
				//直播间预约人数
				long cou = live_reservation().count($$("live_id" : item.get("live_id")));
				long cou_other = live_reservation_zikao().count($$("live_id" : item.get("live_id")));
				item.put("live_reservation_count",cou+cou_other);
				if(!StringUtils.isBlank(phone)) {
					//判断是否预约
					long count = live_reservation_zikao().count($$("live_id":item.get("live_id"),"phone":phone))
					if(count>0) {
						item.put("is_reserve",1)
					} else {
						item.put("is_reserve",0)
					}
				} else {
					item.put("is_reserve",0)
				}
				item.put("live_hls_url",get_live_hls_address(item.get("live_id")))
				if(0 == liveidlist.size()) {
					livelist.add(item)
					liveidlist.add(item.get("_id"))
				} else if(4 == (int) item.get("live_state")) {
					livelist.add(item);
					liveidlist.add(item.get("_id"));
				}
			}
		}
		def sublivelist = new ArrayList()
		if(livelist.size() >= (page-1)*size+size) {
			sublivelist = livelist.subList((page-1)*size,(page-1)*size+size)
		} else if(livelist.size() > (page-1)*size && livelist.size() < (page-1)*size+size) {
			sublivelist = livelist.subList((page-1)*size,livelist.size())
		}
		return getResultOK(sublivelist)
	}

	private get_live_hls_address(String live_id) {
		String result = HttpClientUtil4_3.get(genseehlsurl.replace("{live_id}",live_id),null)
		Map<String, Object> map = JSONUtil.jsonToMap(result)
		if("0".equals(map.get("code"))) {
			return map.get("address")
		}
	}

	/**
	 * 会答首页-直播預約api-小程序预约接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "reserve_live_applet", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = " 会答首页-直播预约-小程序预约接口", httpMethod = "GET",  notes = "会答首页-直播预约-小程序预约接口", response = BaseResultVO.class)
	def reserve_live_applet(
			@ApiParam(required = true, name = "pass", value = "pass")@RequestParam(value = "pass") String pass,
			@ApiParam(required = true, name = "phone", value = "phone ")@RequestParam(value = "phone" ) String phone,
			@ApiParam(required = true, name = "live_id", value = "直播id ")@RequestParam(value = "live_id" ) String live_id,
			HttpServletRequest request
	){
		if(!isMobile(phone)) {
			return getResult("-1", "手机号不正确");
		}
		//判断是否绑定手机号
		long bindCount = applet_phone().count($$("phone":phone))
		if(bindCount==0) {
			return getResult("-2", "该手机号尚未绑定小程序")
		}

		//判断是否预约
		long count = live_reservation_other().count($$("live_id":live_id,"phone":phone));
		if(count>0) {
			getResult("99","已经预约过了");
		} else {
			BasicDBObject insert = new BasicDBObject();
			insert.append("phone", phone);
			insert.append("live_id", live_id);
			insert.append("pass", pass);
			Long now = System.currentTimeMillis();
			insert.append("apply_time", now);
			live_reservation_other().insert(insert);

			return getResult("200", "预约成功");
		}
	}

	/**
	 * 会答首页-直播預約api-自考小程序预约接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "reserve_live_applet_zikao", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = " 会答首页-直播预约-自考小程序预约接口", httpMethod = "GET",  notes = "会答首页-直播预约-自考小程序预约接口", response = BaseResultVO.class)
	def reserve_live_applet_zikao(
			@ApiParam(required = true, name = "pass", value = "pass")@RequestParam(value = "pass") String pass,
			@ApiParam(required = true, name = "phone", value = "phone ")@RequestParam(value = "phone" ) String phone,
			@ApiParam(required = true, name = "live_id", value = "直播id ")@RequestParam(value = "live_id" ) String live_id,
			HttpServletRequest request
	){
		if(!isMobile(phone)) {
			return getResult("-1", "手机号不正确");
		}
		//判断是否绑定手机号
		long bindCount = applet_phone().count($$("phone":phone))
		if(bindCount==0) {
			return getResult("-2", "该手机号尚未绑定小程序")
		}

		//判断是否预约
		long count = live_reservation_zikao().count($$("live_id":live_id,"phone":phone));
		if(count>0) {
			getResult("99","已经预约过了");
		} else {
			BasicDBObject insert = new BasicDBObject();
			insert.append("phone", phone);
			insert.append("live_id", live_id);
			insert.append("pass", pass);
			Long now = System.currentTimeMillis();
			insert.append("apply_time", now);
			live_reservation_zikao().insert(insert);

			return getResult("200", "预约成功");
		}
	}

	/**
	 * 小程序获取省份
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "get_provinces", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = " 小程序获取省份", httpMethod = "GET",  notes = "小程序获取省份", response = BaseResultVO.class)
	def get_provinces() {
		return HttpClientUtil4_3.get(promotiondomain+"/promotion/ncArea/get-provinces",null)
	}

	/**
	 * 小程序根据省份获取校区
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "get_school", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = " 小程序根据省份获取校区", httpMethod = "GET",  notes = "小程序根据省份获取校区", response = BaseResultVO.class)
	def get_school(
			@ApiParam(required = true, name = "parentNcCode", value = "parentNcCode")@RequestParam(value = "parentNcCode") String parentNcCode
	) {
		return HttpClientUtil4_3.get(promotiondomain+"/promotion/ncSchool/get-school?parentNcCode="+parentNcCode,null)
	}

	/**
	 * 小程序活动接口
	 */
	@ResponseBody
	@RequestMapping(value = "checkCodedata", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = " 小程序活动接口", httpMethod = "POST",  notes = "小程序活动接口", response = BaseResultVO.class)
	def checkCodedata(@RequestBody String body) {
		Map map = JSONObject.parseObject(body,Map.class)
		return com.izhubo.utils.HttpClientUtil4_3.sendHttpPost(promotiondomain+"/promotion/guestbook/checkCodedata",map)
	}

	/**
	 * 自考小程序预约数接口
	 */
	@ResponseBody
	@RequestMapping(value = "zkSmallprograms", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = " 自考小程序预约数接口", httpMethod = "GET",  notes = "自考小程序预约数接口", response = BaseResultVO.class)
	def zkSmallprograms() {
		return HttpClientUtil4_3.get(promotiondomain+"/promotion/guestbook/zkSmallprograms",null)
	}

	/**
	 * 自考小程序活动接口
	 */
	@ResponseBody
	@RequestMapping(value = "zkCheckCodedata", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = " 自考小程序活动接口", httpMethod = "POST",  notes = "自考小程序活动接口", response = BaseResultVO.class)
	def zkCheckCodedata(@RequestBody String body) {
		Map map = JSONObject.parseObject(body,Map.class)
		return com.izhubo.utils.HttpClientUtil4_3.sendHttpPost(promotiondomain+"/promotion/guestbook/checkCodedata",map)
	}

	/**
	 * 通过密码获取直播间地址
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */

	@ResponseBody
	@RequestMapping(value = "get_live_url", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "通过密码获取直播间地址", httpMethod = "GET",  notes = "通过密码获取直播间地址", response = BaseResultVO.class)
	def get_live_url(
			@ApiParam(required = true, name = "pass", value = "pass")@RequestParam(value = "pass") String pass,
			@ApiParam(required = true, name = "phone", value = "phone ")@RequestParam(value = "phone" ) String phone,
			@ApiParam(required = true, name = "live_id", value = "直播id ")@RequestParam(value = "live_id" ) String live_id,
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO

		if(!isMobile(phone)) {
			return getResult("-1", "手机号不正确");
		}
		if(!isSixInteger(pass)) {
			return getResult("-2", "密码只能是6位数字");
		}

		//先判断是否预约
		long count = live_reservation_other().count($$("live_id":live_id,"phone":phone,"pass":pass));
		if(count>0)
		{
            def liveobj = app_live().findOne($$("_id",live_id));
			if(liveobj)
			{
				String live_domain=liveobj.get("live_domain");
				//直播开始前30分，则返回直播间地址，否则提示直播尚未开始
				if( System.currentTimeMillis()>((long)liveobj.get("live_start_time") - 30*60*1000)) {
					String webcastid = liveobj.get("live_id");
					getResult("1", "", buildotherliveurl(webcastid, phone,live_domain));
				}
				else
				{
					getResult("2","直播尚未开始，稍后再试");
				}
			}
			else{
				getResult("99","系统异常，稍后再试");
			}

		}
		else
		{

			return getResult("0", "密码不正确或尚未预约");

		}


	}
	
	/**
	 * 通过密码获取CPA直播间地址
	 * @date 2016年7月14日 下午10:12:49
	 * @param @param request
	 */

	@ResponseBody
	@RequestMapping(value = "get_cpa_live_url", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "通过密码获取CPA直播间地址", httpMethod = "GET",  notes = "通过密码获取CPA直播间地址", response = BaseResultVO.class)
	def get_CPA_live_url(
			@ApiParam(required = true, name = "pass", value = "pass")@RequestParam(value = "pass") String pass,
			@ApiParam(required = true, name = "phone", value = "phone ")@RequestParam(value = "phone" ) String phone,
			@ApiParam(required = true, name = "live_id", value = "直播id ")@RequestParam(value = "live_id" ) String live_id,
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO

		if(!isMobile(phone)) {
			return getResult("-1", "手机号不正确");
		}
		
		//获取CPA直播Lie
		def cpa_course_list = constants().find(null,$$("cpa_course_ids":1))?.toArray();
		//常量表中取出所有cpa的course_id
		def cpa_course_ids = cpa_course_list.get(0)["cpa_course_ids"];
		//报名表中CPA学员的报名信息
		def cpa_signs_user = signs().find($$("nc_commodity_id" : $$('$in':cpa_course_ids),"phone":phone,"dr":0),$$("phone" : 1,"nc_course_id":1))?.toArray();
		if(cpa_signs_user.size() != 0)
		{
			if(!(pass.equals("888888"))) {
				return getResult("-2", "密码错误");
			}else{
				def liveobj = app_live().findOne($$("_id",live_id));
				if(liveobj)
				{
					String live_domain=liveobj.get("live_domain");
					//直播开始前30分，则返回直播间地址，否则提示直播尚未开始
					if( System.currentTimeMillis()>((long)liveobj.get("live_start_time") - 30*60*1000)) {
						String webcastid = liveobj.get("live_id");
						getResult("1", "", buildotherliveurl(webcastid, phone,live_domain));
					}
					else
					{
						getResult("2","直播尚未开始，稍后再试");
					}
				}
				else{
					getResult("99","系统异常，稍后再试");
				}
			}
		}
		else
		{
			return getResult("0", "抱歉，本场直播仅允许已报名正式课程的学员进行观看，不支持预约观看。");
		}
	}

	
	/**
	 * 通过密码获取特定直播间地址
	 * @date 2017年8月3日 上午10:12:49
	 * @param @param request
	 */

	@ResponseBody
	@RequestMapping(value = "get_specific_live_url", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "通过密码获取特殊直播间地址", httpMethod = "GET",  notes = "通过密码获取特定直播间地址", response = BaseResultVO.class)
	def get_specific_live_url(
			@ApiParam(required = true, name = "pass", value = "pass")@RequestParam(value = "pass") String pass,
			@ApiParam(required = true, name = "phone", value = "phone ")@RequestParam(value = "phone" ) String phone,
			@ApiParam(required = true, name = "live_id", value = "直播id ")@RequestParam(value = "live_id" ) String live_id,
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO

		if(!isMobile(phone)) {
			return getResult("-1", "手机号不正确");
		}

		def res_live = app_live().find($$("_id":live_id))?.toArray();
		Integer live_type = res_live.get(0)["live_type"];
		switch (live_type){
			case 1201 :
			//获取特定直播
				def cpa_course_list = constants().find(null,$$("cpa_course_ids":1))?.toArray();
			//常量表中取出所有cpa的course_id
				def cpa_course_ids = cpa_course_list.get(0)["cpa_course_ids"];
			//报名表中CPA学员的报名信息
				def cpa_signs_user = signs().find($$("nc_commodity_id" : $$('$in':cpa_course_ids),"phone":phone,"dr":0),$$("phone" : 1,"nc_course_id":1))?.toArray();
				if(cpa_signs_user.size() != 0)
				{
					if(!(pass.equals("888888"))) {
						return getResult("-2", "密码错误");
					}else{
						def liveobj = app_live().findOne($$("_id",live_id));
						if(liveobj)
						{
							String live_domain=liveobj.get("live_domain");
							//直播开始前30分，则返回直播间地址，否则提示直播尚未开始
							if( System.currentTimeMillis()>((long)liveobj.get("live_start_time") - 30*60*1000)) {
								String webcastid = liveobj.get("live_id");
								getResult("1", "", buildotherliveurl(webcastid, phone,live_domain));
							}
							else
							{
								getResult("2","直播尚未开始，稍后再试");
							}
						}
						else{
							getResult("99","系统异常，稍后再试");
						}
					}
				}
				else
				{
					return getResult("0", "抱歉，本场直播仅允许已报名正式课程的学员进行观看，不支持预约观看。");
				}
				break;


			case 1301 :
			//获取中央财大研修班直播
			def course_list = constants().find(null,$$("central_deep_course_ids":1))?.toArray();
			//常量表中取出所有中央财大研修班的course_id
			def course_ids = course_list.get(0)["central_deep_course_ids"];
			//报名表中央财大研修班的报名信息
				def cpa_signs_user = signs().find($$("nc_commodity_id" : $$('$in':course_ids),"phone":phone,"dr":0),$$("phone" : 1,"nc_course_id":1))?.toArray();
				if(cpa_signs_user.size() != 0)
				{
					if(!(pass.equals("888888"))) {
						return getResult("-2", "密码错误");
					}else{
						def liveobj = app_live().findOne($$("_id",live_id));
						if(liveobj)
						{
							String live_domain=liveobj.get("live_domain");
							//直播开始前30分，则返回直播间地址，否则提示直播尚未开始
							if( System.currentTimeMillis()>((long)liveobj.get("live_start_time") - 30*60*1000)) {
								String webcastid = liveobj.get("live_id");
								getResult("1", "", buildotherliveurl(webcastid, phone,live_domain));
							}
							else
							{
								getResult("2","直播尚未开始，稍后再试");
							}
						}
						else{
							getResult("99","系统异常，稍后再试");
						}
					}
				}
				else
				{
					return getResult("0", "抱歉，本场直播仅允许已报名正式课程的学员进行观看，不支持预约观看。");
				}
				break;
				
				
			case 1401 :
			//获取全能级以上直播
				def course_list = constants().find(null,$$("above_all_around_course_ids":1))?.toArray();
			//常量表中取出所有全能级以上班的course_id
				def course_ids = course_list.get(0)["above_all_around_course_ids"];
			//报名表全能级以上班的报名信息
				def cpa_signs_user = signs().find($$("nc_commodity_id" : $$('$in':course_ids),"phone":phone,"dr":0),$$("phone" : 1,"nc_course_id":1))?.toArray();
				if(cpa_signs_user.size() != 0)
				{
					if(!(pass.equals("888888"))) {
						return getResult("-2", "密码错误");
					}else{
						def liveobj = app_live().findOne($$("_id",live_id));
						if(liveobj)
						{
							String live_domain=liveobj.get("live_domain");
							//直播开始前30分，则返回直播间地址，否则提示直播尚未开始
							if( System.currentTimeMillis()>((long)liveobj.get("live_start_time") - 30*60*1000)) {
								String webcastid = liveobj.get("live_id");
								getResult("1", "", buildotherliveurl(webcastid, phone,live_domain));
							}
							else
							{
								getResult("2","直播尚未开始，稍后再试");
							}
						}
						else{
							getResult("99","系统异常，稍后再试");
						}
					}
				}
				else
				{
					return getResult("0", "抱歉，本场直播仅允许已报名正式课程的学员进行观看，不支持预约观看。");
				}
				break;
		}
	}

	private  String buildotherliveurl(String live_id,String phone,String live_domain)
	{
		phone = phone.substring(0,3) + "****" + phone.substring(7, 11);
		String liveurl ="http://"+live_domain+"/webcast/site/entry/join-"+live_id+'?nickName='+phone;
		return liveurl;


	}



	// 校验验证码
	public boolean checkSecurity(String phone, String security) {
		boolean res = false;
		if (org.apache.commons.lang.StringUtils.isNotBlank(security) && org.apache.commons.lang.StringUtils.isNotBlank(phone)) {
			String rs = mainRedis.opsForValue().get(
					HK.SECURITY.getSecurityKey(phone));
//			SecurityController.getSecurityKey(phone));
			res = security.equals(rs);
		}
		return res;
	}

	// 删除验证码
	public void removeRedisSecurity(String phone) {
		String k1 = mainRedis.opsForValue().get(HK.SECURITY.getSecurityKey(phone));

		if (k1 != null) {
			if (mainRedis.hasKey(k1)) {
				mainRedis.delete(k1);
			}
		}
	}

	/**
	 * 手机号验证
	 *
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^1\\d{10}\$"); // 验证手机号


		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	public static boolean isSixInteger(String str) {

         return str ==~/^\d{6}$/;

	}
	



	@ResponseBody
	@RequestMapping(value = "setPushTime", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "批量设置定时推送时间 （测试预留接口，勿调用） ", httpMethod = "GET",response = BaseResultVO.class,  notes = "批量设置定时推送时间 ")
	def setPushTime(HttpServletRequest request){
		
		def mslist = dailypush_recommend().find().toArray();
		
		mslist.each {BasicDBObject row ->
			dailypush_recommend().update(
				new BasicDBObject("_id":row["_id"]),
				new BasicDBObject('$set':
					new BasicDBObject(
							"push_time" : System.currentTimeMillis()
						)
					));
		}
	}

	@ResponseBody
	@RequestMapping(value = "setCreateTime", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "批量设置创建时间 （测试预留接口，勿调用） ", httpMethod = "GET",response = BaseResultVO.class,  notes = "批量设置创建时间 ")
	def setCreateTime(HttpServletRequest request){
		
		def mslist = dailypush_recommend().find().toArray();
		
		mslist.each {BasicDBObject row ->
			dailypush_recommend().update(
				new BasicDBObject("_id":row["_id"]),
				new BasicDBObject('$set':
					new BasicDBObject(
							"create_time" : System.currentTimeMillis()
						)
					));
		}
	}

	@ResponseBody
	@RequestMapping(value = "setAppLiveDr", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "批量处理直播Dr （测试预留接口，勿调用） ", httpMethod = "GET",response = BaseResultVO.class,  notes = "批量处理直播Dr ")
	def setAppLiveDr(HttpServletRequest request){
		
		def mslist = app_live().find().toArray();
		
		mslist.each {BasicDBObject row ->
			app_live().update(
				new BasicDBObject("_id":row["_id"]),
				new BasicDBObject('$set':
					new BasicDBObject(
							"dr" : 0
						)
					));
		}
	}





}
