package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;
import groovy.json.*

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.ServletRequestUtils;
import org.apache.commons.lang3.StringUtils

import com.izhubo.model.CommodityType;
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.doc.ParamKey;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder;

/**
 * 商品/课程序列
 * @author zhengxin
 * 2016-03-09
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CommoditysController extends BaseController {


	DBCollection table(){
		DBCollection cc = mainMongo.getCollection('commoditys');
		return mainMongo.getCollection('commoditys');
	}
	DBCollection coursestable(){
		return mainMongo.getCollection('commodity_courses');
	}
	DBCollection pricestable(){
		return mainMongo.getCollection('commodity_prices');
	}
	DBCollection schoolstable(){
		return mainMongo.getCollection('commodity_schools');
	}
	DBCollection areatable(){
		return mainMongo.getCollection('area');
	}
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		//stringQuery(query,req,'nick_name')
		String code = req.getParameter("code");
		String name = req.getParameter("name");
		String nc_name = req.getParameter("nc_name");
		String short_name = req.getParameter("short_name");
		String teacher_id = req.getParameter("teacher_id");
		String reader = req.getParameter("reader");
		String classmode = req.getParameter("classmode");
		String sprice = req.getParameter("sprice");
		String eprice = req.getParameter("eprice");
		String labels = req.getParameter("labels");
		String is_shelves = req.getParameter("is_shelves");
		String is_sort_course = req.getParameter("is_sort_course");
		String sup_shelves_time = req.getParameter("sup_shelves_time");
		String eup_shelves_time = req.getParameter("eup_shelves_time");
		String sdown_shelves_time = req.getParameter("sdown_shelves_time");
		String edown_shelves_time = req.getParameter("edown_shelves_time");
		String ssyn_time = req.getParameter("ssyn_time");
		String esyn_time = req.getParameter("esyn_time");
//		-1.所有已分类 0.未分类 1.会计上岗 2.会计考证 3.会计学历 4.经营会计
		Integer ctype = ServletRequestUtils.getIntParameter(req, "ctype", -1);
		
		//类型
		if(ctype == -1){//所有
			//query.and("type").in([CommodityType.会计上岗.ordinal(),CommodityType.会计上岗_猎才计划.ordinal(),CommodityType.会计学历.ordinal() , CommodityType.会计考证.ordinal()  , CommodityType.经营会计.ordinal()]);
			query.and("type").notIn([CommodityType.未分类.ordinal() , null])
		}else if(ctype == 0){//未分类
			query.and("type").in([0,null]);
		}
		else if(ctype == 1){//会计上岗
			query.and("type").in([CommodityType.会计上岗.ordinal(),CommodityType.会计上岗_猎才计划.ordinal()]);
		}
		else{
			query.and("type").is(ctype);
		}
		
		if(sup_shelves_time != null || eup_shelves_time != null){
			query = Web.fillTimeBetween(query,"up_shelves_time",new SimpleDateFormat(DFMT).parse(sup_shelves_time),new SimpleDateFormat(DFMT).parse(sup_shelves_time));
		}
		if(ssyn_time != null || esyn_time != null){
			query = Web.fillTimeBetween(query,"syn_time",new SimpleDateFormat(DFMT).parse(ssyn_time),new SimpleDateFormat(DFMT).parse(esyn_time));
		}
		if(sdown_shelves_time != null || edown_shelves_time != null){
			query = Web.fillTimeBetween(query,"down_shelves_time",new SimpleDateFormat(DFMT).parse(sdown_shelves_time),new SimpleDateFormat(DFMT).parse(edown_shelves_time));
		}
		if (StringUtils.isNotBlank(code)){
			Pattern pattern = Pattern.compile("^.*" + code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("code").regex(pattern)
		}
		if (StringUtils.isNotBlank(name)){
			Pattern pattern = Pattern.compile("^.*" + name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("name").regex(pattern)
		}
		if (StringUtils.isNotBlank(nc_name)){
			Pattern pattern = Pattern.compile("^.*" + nc_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(short_name)){
			Pattern pattern = Pattern.compile("^.*" + short_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("short_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(teacher_id)){
			Pattern pattern = Pattern.compile("^.*" + teacher_id + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("teacher_id").regex(pattern)
		}
		if (StringUtils.isNotBlank(reader)){
			Pattern pattern = Pattern.compile("^.*" + reader + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("reader").regex(pattern)
		}
		if (StringUtils.isNotBlank(classmode)){
			Pattern pattern = Pattern.compile("^.*" + classmode + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("classmode").regex(pattern)
		}
		if (StringUtils.isNotBlank(labels)){
			Pattern pattern = Pattern.compile("^.*" + labels + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("labels").regex(pattern)
		}
		if (StringUtils.isNotBlank(sprice) || StringUtils.isNotBlank(eprice)){
			if(StringUtils.isNotBlank(sprice)){
				query.greaterThanEquals(sprice);
			}
			if (StringUtils.isNotBlank(eprice)){
				query.lessThan(eprice);
			}
		}
		if (StringUtils.isNotBlank(is_shelves)){
			query.and("is_shelves").is(Integer.valueOf(is_shelves));
		}
		if (StringUtils.isNotBlank(is_sort_course)){
			query.and("is_sort_course").is(Integer.valueOf(is_sort_course));
		}
		
		def sort = $$("is_shelves" : -1 , "type" : 1 , "code" : 1);
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,sort)
	}
	/**
	 * 商品详情
	 */
	def detail(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		String _id = req.getParameter("_id");
		Pattern pattern = Pattern.compile("^.*" + _id + ".*\$", Pattern.CASE_INSENSITIVE)
		query.and("_id").regex(pattern)
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}
	/**
	 * 商品编辑
	 */
	def productedit(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		String _id = req.getParameter("_id");
		Pattern pattern = Pattern.compile("^.*" + _id + ".*\$", Pattern.CASE_INSENSITIVE)
		query.and("_id").regex(pattern)
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}
	
	/**
	 * 课程列表
	 */
	def courselist(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();
		List<BasicDBObject> courselist = new ArrayList<BasicDBObject>();
		Map map = new HashMap();
		map.put(ParamKey.Out.code, ParamKey.Out.SUCCESS);
		String _id = req.getParameter("_id");
		def course_ids = table().findOne(new BasicDBObject("_id" : _id) , new BasicDBObject("course_ids":1))?.get("course_ids") as String[];
		for(course_id in course_ids){
			BasicDBObject course = coursestable().findOne(new BasicDBObject("nc_id" : course_id))
			courselist.add(course);
		}
		if(courselist !=null && courselist.size()>=0){
			map.put(ParamKey.Out.count,courselist.size());
		}
		if(courselist !=null){
			map.put(ParamKey.Out.all_page,1);
		}
		map.put(ParamKey.Out.data,courselist);
		return map;
	}
	
	/**
	 * 课程修改保存
	 */
	def courseedit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id];
		String show_name = req.getParameter("show_name");
		String try_video = req.getParameter("try_video");
		String tiku_url = req.getParameter("tiku_url");
		String shixun_url = req.getParameter("shixun_url");
		String remark = req.getParameter("remark");
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		coursestable().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"show_name" : show_name,
						"try_video" : try_video,
						"tiku_url" : tiku_url,
						"shixun_url" : shixun_url,
						"remark" : remark,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("commodity_courses",[edit:id]);
		return OK();
	}
	
	/**
	 * 课程分类
	 */
	def classify(HttpServletRequest req){
		def jsonSlurper = new JsonSlurper()
		QueryBuilder query = QueryBuilder.start();
		List<BasicDBObject> courselist = new ArrayList<BasicDBObject>();
		Map map = new HashMap();
		map.put(ParamKey.Out.code, ParamKey.Out.SUCCESS);
		String _id = req.getParameter("_id");
		def commodity_type_list = table().findOne(new BasicDBObject("_id" : _id) , new BasicDBObject("commodity_type_list":1))?.get("commodity_type_list") as ArrayList;
		map.put(ParamKey.Out.data,commodity_type_list);
		map.put("commodity_type_list",commodity_type_list);
		map.put("_id",_id);
		return map;
	}
	
	/**
	 * 保存课程分类
	 */
	def saveclassify(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		def jsonSlurper = new JsonSlurper()
		QueryBuilder query = QueryBuilder.start();
		List<BasicDBObject> courselist = new ArrayList<BasicDBObject>();
		Map map = new HashMap();
		map.put(ParamKey.Out.code, ParamKey.Out.SUCCESS);
		String classify = req.getParameter("classify");
		String _id = req.getParameter("_id");
		def classarray = new ArrayList();
		if(classify != null){
			classarray = jsonSlurper.parseText(classify)
			table().update(
				new BasicDBObject("_id":_id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"commodity_type_list" : classarray,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					)
				));
		}
		map.put(ParamKey.Out.data,classify);
		return map;
	}
	
	/**
	 * 价格列表
	 */
	def pricelist(HttpServletRequest req){
		String nc_id = req.getParameter("nc_id");
		String _id = req.getParameter("_id");
		def nc_commodity_id = table().findOne(new BasicDBObject("_id" : _id) , new BasicDBObject("nc_id":1))?.get("nc_id");
		Map map = new HashMap();
		map.put(ParamKey.Out.code, ParamKey.Out.SUCCESS);
		List<BasicDBObject> pricelist = new ArrayList<BasicDBObject>();
		QueryBuilder query = QueryBuilder.start();
		if (StringUtils.isNotBlank(nc_commodity_id)){
			Pattern pattern = Pattern.compile("^.*" + nc_commodity_id + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_commodity_id").regex(pattern)
		}
		if (StringUtils.isNotBlank(nc_id)){
			Pattern pattern = Pattern.compile("^.*" + nc_id + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_school_pk").regex(pattern)
		}
		Crud.list(req,pricestable(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				for (BasicDBObject obj1: obj['itemList']){	
					obj1.append("school_code", obj['school_code']);
					obj1.append("school_name", obj['school_name']);
					obj1.append("syn_time", obj['syn_time']);
					pricelist.add(obj1);
			    }
			}
		};
		if(pricelist !=null && pricelist.size()>=0){
			map.put(ParamKey.Out.count,pricelist.size());
		}
		if(pricelist !=null){
			map.put(ParamKey.Out.all_page,1);
		}
		map.put(ParamKey.Out.data,pricelist);
		return map;
	}
	
	/**
	 * 获取校区下拉列表
	 */
	def getarea(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();
		query.and("is_school").is("1");
		Pattern pattern = Pattern.compile("^\\w{11,12}\$", Pattern.CASE_INSENSITIVE)
		query.and("code").regex(pattern)
		Map map = Crud.list(req,areatable(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
		return map;
	}
	
	/**
	 * 获取校区表格
	 */
	def school(HttpServletRequest req){
		def sort_sj = $$("code" : 1);
		String _id = req.getParameter("_id");
		//String _id = "000";
		QueryBuilder query = QueryBuilder.start();
		def school_pks = schoolstable().findOne(new BasicDBObject("commodity_id" : _id) , new BasicDBObject("school_pks":1))?.get("school_pks") as String[];
//		Pattern pattern = Pattern.compile("^\\w{1,4}\$", Pattern.CASE_INSENSITIVE)
//		query.and("code").regex(pattern)
//		Map firstmap = Crud.list(req,areatable(),query.get(),MongoKey.ALL_FIELD,sort_sj)
//		query = QueryBuilder.start();
//		pattern = Pattern.compile("^\\w{5,6}\$", Pattern.CASE_INSENSITIVE)
//		query.and("code").regex(pattern)
//		Map secondmap = Crud.list(req,areatable(),query.get(),MongoKey.ALL_FIELD,sort_sj)
//		query = QueryBuilder.start();
//		pattern = Pattern.compile("^\\w{7,8}\$", Pattern.CASE_INSENSITIVE)
//		query.and("code").regex(pattern)
//		Map thirdmap = Crud.list(req,areatable(),query.get(),MongoKey.ALL_FIELD,sort_sj)
//		query = QueryBuilder.start();
//		pattern = Pattern.compile("^\\w{9,10}\$", Pattern.CASE_INSENSITIVE)
//		query.and("code").regex(pattern)
//		Map fourthmap = Crud.list(req,areatable(),query.get(),MongoKey.ALL_FIELD,sort_sj)
//		query = QueryBuilder.start();
//		pattern = Pattern.compile("^\\w{11,12}\$", Pattern.CASE_INSENSITIVE)
//		query.and("code").regex(pattern)
//		Map fifthmap = Crud.list(req,areatable(),query.get(),MongoKey.ALL_FIELD,sort_sj)
		Map allmap = Crud.list(req,areatable(),query.get(),MongoKey.ALL_FIELD,sort_sj);
		Map map = new HashMap();
//		map.put("firstmap",firstmap);
//		map.put("secondmap",secondmap);
//		map.put("thirdmap",thirdmap);
//		map.put("fourthmap",fourthmap);
//		map.put("fifthmap",fifthmap);
		map.put("allmap",allmap);
		map.put("school_pks",school_pks);
		return map;
	}
	
	/**
	 * 商品详情
	 */
	def hmedit(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();
		List<BasicDBObject> courselist = new ArrayList<BasicDBObject>();
		Map map = new HashMap();
		map.put(ParamKey.Out.code, ParamKey.Out.SUCCESS);
		String _id = req.getParameter("_id");
		String content = table().findOne(new BasicDBObject("_id" : _id) , new BasicDBObject("content":1))?.get("content");
		if(content == null || content == "undefined"){
			content = " ";
		}
		map.put(ParamKey.Out.data,content);
		map.put("content",content);
		map.put("_id",_id);
		return map;
	}
	
	/**
	 * 保存商品详情
	 */
	def savehtml(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		QueryBuilder query = QueryBuilder.start();
		Map map = new HashMap();
		map.put(ParamKey.Out.code, ParamKey.Out.SUCCESS);
		String htmlcontent = req.getParameter("htmlcontent");
		String _id = req.getParameter("_id");
		if(htmlcontent != null){
			table().update(
				new BasicDBObject("_id":_id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"content" : htmlcontent,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					)
				));
		}
		map.put(ParamKey.Out.data,htmlcontent);
		return map;
	}
	
	/**
	 * 商品详情
	 */
	def apphmedit(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();
		List<BasicDBObject> courselist = new ArrayList<BasicDBObject>();
		Map map = new HashMap();
		map.put(ParamKey.Out.code, ParamKey.Out.SUCCESS);
		String _id = req.getParameter("_id");
		String content_app = table().findOne(new BasicDBObject("_id" : _id) , new BasicDBObject("content_app":1))?.get("content_app");
		if(content_app == null || content_app == "undefined"){
			content_app = " ";
		}
		map.put(ParamKey.Out.data,content_app);
		map.put("appcontent",content_app);
		map.put("_id",_id);
		return map;
	}
	
	/**
	 * 保存商品详情
	 */
	def appsavehtml(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		QueryBuilder query = QueryBuilder.start();
		Map map = new HashMap();
		map.put(ParamKey.Out.code, ParamKey.Out.SUCCESS);
		String content_app = req.getParameter("htmlcontent");
		String _id = req.getParameter("_id");
		if(content_app != null){
			table().update(
				new BasicDBObject("_id":_id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"content_app" : content_app,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					)
				));
		}
		map.put(ParamKey.Out.data,content_app);
		return map;
	}
	
	/**
	 * 保存所属校区
	 */
	def saveschool(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		def jsonSlurper = new JsonSlurper();
		def schoolsarray = new ArrayList();
		QueryBuilder query = QueryBuilder.start();
		Map map = new HashMap();
		map.put(ParamKey.Out.code, ParamKey.Out.SUCCESS);
		def schools = req.getParameter("schools");
		schoolsarray = jsonSlurper.parseText(schools)
		String _id = req.getParameter("_id");
		if(schoolsarray != null){
			BasicDBObject course = schoolstable().findOne(new BasicDBObject("commodity_id" : _id))
			if(course == null){
				schoolstable().insert(
					$$(_id: _id,
                        school_pks:schoolsarray,
                        commodity_id:_id,
						"update_user_id" : user.get("_id") as Integer,
                        create_time:System.currentTimeMillis()
                ));
			}else{
				schoolstable().update(
					new BasicDBObject("_id":_id),
					new BasicDBObject('$set':
						new BasicDBObject(
							"school_pks" : schoolsarray,
							"commodity_id" : _id,
							"update_user_id" : user.get("_id") as Integer,
							"update_time" : System.currentTimeMillis()
						)
					));
			}
		}
		map.put(ParamKey.Out.data,schools);
		removeRedisPrice(_id);
		return map;
	}
	

	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		String birthday = req["birthday"]

		def users = $$("_id" : UUID.randomUUID().toString());
		if(req["sex"] && req["sex"] != ""){
			users.put("sex", req["sex"] as Integer);
		}
		users.put("nick_name", req["nick_name"]);
		users.put("cardnumber", req["cardnumber"]);
		users.put("telephone", req["telephone"]);
		users.put("email", req["email"]);
		users.put("qq_number", req["qq_number"]);
		users.put("school_code", req["school_code"]);
		users.put("status", 0);
		if(birthday){
			users.put("birthday", new SimpleDateFormat(DFMT).parse(birthday).getTime());
		}
		table().save(users);
		Crud.opLog("users",[save:users["_id"]]);
		return OK();
	}

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		String id = req[_id];
		String nick_name = req.getParameter("nick_name");
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"nick_name" : nick_name,
						"update_time" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("commoditys",[edit:id]);
		return OK();
	}
	
	/**
	 * 保存商品
	 */
	def saveproduct(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id];
		if(StringUtils.isEmpty(id))
			return [code:0];
			
		String name = req.getParameter("name");
		String short_name = req.getParameter("short_name");
		String reader = req.getParameter("reader");
		String classtime = req.getParameter("classtime");
		String learntime = req.getParameter("learntime");
		String clasmode = req.getParameter("clasmode");
		String labels = req.getParameter("labels");
		String classmode = req.getParameter("classmode");
		String titles = req.getParameter("titles");
//		Double monthprice = ServletRequestUtils.getDoubleParameter(req, "monthprice", 0);
		Double paypoints = ServletRequestUtils.getDoubleParameter(req, "paypoints", 0);
		Double points = ServletRequestUtils.getDoubleParameter(req, "points", 0);
		Integer is_hot = ServletRequestUtils.getIntParameter(req, "is_hot", 0);
		Integer order = ServletRequestUtils.getIntParameter(req, "order", 0);
		Integer order_app = ServletRequestUtils.getIntParameter(req, "order_app", 0);
		Integer order_page = ServletRequestUtils.getIntParameter(req, "order_page", 0);
		String queryup_shelves_time = req.getParameter("up_shelves_time");
		Long up_shelves_time = null;
		if(StringUtils.isNotBlank(queryup_shelves_time)){
			up_shelves_time = new SimpleDateFormat(DFMT).parse(queryup_shelves_time).getTime();
			table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"up_shelves_time" : up_shelves_time,
					)
				));
		}
		String querydown_shelves_time = req.getParameter("down_shelves_time");
		Long down_shelves_time = null;
		if(StringUtils.isNotBlank(querydown_shelves_time)){
			down_shelves_time = new SimpleDateFormat(DFMT).parse(querydown_shelves_time).getTime();
			table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"down_shelves_time" : down_shelves_time,
					)
				));
		}
		String photo = req.getParameter("photo");
		String thumbnail = req.getParameter("thumbnail");
		String app_photo = req.getParameter("app_photo");
		String app_thumbnail = req.getParameter("app_thumbnail");
		String try_video = req.getParameter("try_video");
		String try_video_app = req.getParameter("try_video_app");
		String summary = req.getParameter("summary");
//		Double price = ServletRequestUtils.getDoubleParameter(req, "price", 0);
		Double original_price = ServletRequestUtils.getDoubleParameter(req, "original_price", 0);
		
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"name" : name,
						"short_name" : short_name,
						"reader" : reader,
						"classtime" : classtime,
						"learntime" : learntime,
						"clasmode" : clasmode,
//						"monthprice" : monthprice,
						"labels" : labels,
						"paypoints" : paypoints,
						"points" : points,
						"photo" : photo,
						"classmode" : classmode,
						"thumbnail" : thumbnail,
						"app_photo" : app_photo,
						"app_thumbnail" : app_thumbnail,
						"try_video" : try_video,
						"try_video_app" : try_video_app,
						"summary" : summary,
//						"price" : price,
						"titles" : titles,
						"is_hot" : is_hot,
						"order" : order,
						"order_app" : order_app,
						"order_page" : order_page,
						"original_price" : original_price,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("commoditys",[edit:id]);
			removeRedisPrice(id);
		return OK();
	}

	/**
	 * 冻结/解冻
	 */
	def freeze(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		def is_shelves = req.getParameter("status")
		if(StringUtils.isBlank(is_shelves)){
			is_shelves = 0
		}
		is_shelves = is_shelves as Integer
		if(is_shelves == 0){
			def nc_commodity_id = table().findOne(new BasicDBObject("_id" : id) , new BasicDBObject("nc_id":1))?.get("nc_id");
			if(pricestable().count($$("nc_commodity_id", nc_commodity_id)) > 0){
				is_shelves = 1
			}else{
				Map fail = new HashMap();
				fail.put("fail",1);
				fail.put("code",1);
				return fail;
			}
		}else{
			is_shelves = 0
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		if(is_shelves == 0){
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"is_shelves" : is_shelves,
						"down_shelves_time" : System.currentTimeMillis(),
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					)
				));
		}else if(is_shelves == 1){
		table().update(
			new BasicDBObject("_id":id),
			new BasicDBObject('$set':
				new BasicDBObject(
					"is_shelves" : is_shelves,
					"up_shelves_time" : System.currentTimeMillis(),
					"update_user_id" : user.get("_id") as Integer,
					"update_time" : System.currentTimeMillis()
				)
			));
		}
		removeRedisPrice(id);
		Crud.opLog("commoditys",[edit:id]);
		return OK();
	}
	
	/**
	 * 提交
	 */
	def submit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		def upload_flag = req.getParameter("upload_flag")
		if(StringUtils.isBlank(upload_flag)){
			upload_flag = 0
		}
		upload_flag = upload_flag as Integer
		if(upload_flag == 0){
			upload_flag = 1
		}else if(upload_flag == 1){
			return [code:500,msg:"您已经提交了！无需重复提交"]
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"upload_flag" : upload_flag,
						"update_user_id" : user.get("_id") as Integer,
						"upload_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"upload_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("commoditys",[edit:id]);
		return OK();
	}
	
	/**
	 * 反提交
	 */
	def unsubmit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		def upload_flag = req.getParameter("upload_flag")
		if(StringUtils.isBlank(upload_flag)){
			upload_flag = 1
		}
		upload_flag = upload_flag as Integer
		if(upload_flag == 1){
			upload_flag = 0
		}else if(upload_flag == 0){
			return [code:500,msg:"您已经反提交了！无需重复反提交"]
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"upload_flag" : upload_flag,
						"update_user_id" : user.get("_id") as Integer,
						"upload_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"upload_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("commoditys",[edit:id]);
		return OK();
	}
	
	/**
	 * 审核
	 */
	def audit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		def audit_flag = req.getParameter("audit_flag")
		if(StringUtils.isBlank(audit_flag)){
			audit_flag = 0
		}
		audit_flag = audit_flag as Integer
		if(audit_flag == 0){
			audit_flag = 1
		}else if(audit_flag == 1){
			return [code:500,msg:"您已经审核了！无需重复审核"]
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"audit_flag" : audit_flag,
						"update_user_id" : user.get("_id") as Integer,
						"audit_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"audit_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("commoditys",[edit:id]);
		return OK();
	}
	
	/**
	 * 反审核
	 */
	def unaudit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		def audit_flag = req.getParameter("audit_flag")
		if(StringUtils.isBlank(audit_flag)){
			audit_flag = 1
		}
		audit_flag = audit_flag as Integer
		if(audit_flag == 1){
			audit_flag = 0
		}else if(audit_flag == 0){
			return [code:500,msg:"您已经反审核了！无需重复反审核"]
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"audit_flag" : audit_flag,
						"update_user_id" : user.get("_id") as Integer,
						"audit_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"audit_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("commoditys",[edit:id]);
		return OK();
	}
	
	
	/**
	 * 删除redis缓存中的商品价格
	 * @param commodity_id
	 */
	private void removeRedisPrice(String commodity_id){
		String key = COMMODITYS.priceHash(commodity_id);
		mainRedis.delete(key);
	}
	
	
	public static class COMMODITYS {
		
		  public static final String COMMODITY_SCHOOL_PRICE = "commodity:school:prices:";
		  
		  public static String priceHash(Object commodity_id){
			  return COMMODITY_SCHOOL_PRICE+ commodity_id;
		  }

	}
}
