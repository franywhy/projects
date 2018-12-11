package com.izhubo.web.school

import com.izhubo.rest.common.util.http.HttpClientUtil4_3
import com.izhubo.rest.common.util.school.SchoolUtils
import com.izhubo.utils.DataUtils
import com.izhubo.web.BaseController
import com.izhubo.web.CommoditysController
import com.izhubo.web.vo.BaseResultVO
import com.izhubo.web.vo.DailyRecommandVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.mysqldb.model.IbSchool
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.apache.commons.lang3.StringUtils
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import java.util.regex.Pattern

import static com.izhubo.rest.common.doc.MongoKey.$set
import static com.izhubo.rest.common.util.WebUtils.$$

@Controller
@RequestMapping("/school")
public class SchoolController extends BaseController {
	
	@Resource
	private SessionFactory sessionFactory;
	
	
	private DBCollection school_pic(){
		return mainMongo.getCollection("school_pic");
	}
	
	private DBCollection school_article(){
		return mainMongo.getCollection("school_notice");
	}
	private DBCollection commodity_schools(){
		return mainMongo.getCollection("commodity_schools");
	}
	private DBCollection school_menu(){
		return mainMongo.getCollection("school_menu");
	}
	
	private DBCollection school_notice(){
		return mainMongo.getCollection("school_notice");
	}
	@Resource
	private CommoditysController commoditysController;
	
	//注意距离要除以111.2（1度=111.2km）
	private float maxDistance = 0.009;              //    0.5/111.12  500米范围内，如果有目标校区，则有效

	//地球平均半径
	private static final double EARTH_RADIUS = 6378137;

	public DBCollection area() {
		return mainMongo.getCollection("area");
	}
	
	
	
	
	
	public boolean check_school_area(String school_code,float longitude,float latitude)
	{
		
		def mslist = mainMongo.getCollection("area").find($$("location" : [ '$near': [longitude,latitude],'$maxDistance':maxDistance ])).limit(5).toArray()?.collect{DBObject it->it.get("code")};
		
		if(mslist.contains(school_code))
		{
			return true;
		}
		else
		{
			 return false;
		}
		
	
	
	}
	
	@ResponseBody
	@RequestMapping(value = "get_province_and_schoool", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取当前省份和省份下面校区 ", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取当前省份和省份下面校区")
	def get_province_and_schoool(HttpServletRequest request){ 

			
	  return getResultOK(get_province_school_list());
					
	
		
	}
	
	
	def phonearray(String phone)
	{
		def array=['400-070-6667'];
		
		if( phone.contains('/'))
		{
			return phone.split('/');
		}
		else
		{
			return array;
		}
	}
	@ResponseBody
	@RequestMapping(value = "get_school_notice_list", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取公告列表", httpMethod = "GET",response = BaseResultVO.class,  notes = "移动端公告列表")
	@ApiImplicitParams([
			@ApiImplicitParam(name = "school_code", value = "校区编码", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "第page页", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "每页size条数据", required = false, dataType = "int", paramType = "query")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def get_school_notice_list(HttpServletRequest request){


		String schoolcode  = ServletRequestUtils.getStringParameter(request, "school_code");
		Integer page  = ServletRequestUtils.getIntParameter(request, "page",1);
		Integer size  = ServletRequestUtils.getIntParameter(request, "size",20);
		def sort = $$("timestamp" : -1);
		def	query = $$(
				$or : [
						$$( "school_code" ,   schoolcode),
						$$( "school_code" ,   "all")
				]
		);
		query.append("is_recommand",1)
		query.append("dr",0);
		long count =school_notice().count(query);
		Integer allpage = count / size + ((count% size) >0 ? 1 : 0);
		//查询结果
		def queryResult = null;
		if(count > 0){
			//需要查询的字段
			BasicDBObject show = new BasicDBObject();
			show.append("activity_info" , 0);
			queryResult = school_notice().find(query,show).sort(sort).skip((page - 1) * size).limit(size).toArray();
		}





		return getResultOK(queryResult, allpage, count , page , size);





	}
	
	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "notice_detail", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "公告详情", httpMethod = "GET",  notes = "公告详情接口", response = DailyRecommandVO.class)
	@ApiImplicitParams([@ApiImplicitParam(name = "notice_id", value = "公告id", required = false, dataType = "String", paramType = "query")])
	def notice_detail(
		HttpServletRequest request
		){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		String notice_id  = ServletRequestUtils.getStringParameter(request, "notice_id");
		BasicDBObject query = new BasicDBObject();
	
		
		//query.append("recommend_type" , DailyRecommendType.文章.ordinal());
		query.append("_id" , notice_id);
		
		
		def queryResult = school_notice().findOne(query);
		
		Integer count = 0;
		
		count = (queryResult["read_count"] ==null?0:(Integer)queryResult["read_count"]);
		
		count++;
		
		
		BasicDBObject up = new BasicDBObject("read_count", count);
		school_notice().update(query, new BasicDBObject($set, up));
		
		
		queryResult["timestamp"] = DataUtils.dateToString(Long.valueOf(queryResult["timestamp"].toString()));
		
		queryResult["read_count"] = count;
		
		return getResultOK(queryResult);
	}
	
	@ResponseBody
	@RequestMapping(value = "get_province_and_school_byts", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取当前省份和省份下面校区-增加时间戳机制 ", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取当前省份和省份下面校区")
	@ApiImplicitParams([@ApiImplicitParam(name = "sync_time", value = "时间戳", required = false, dataType = "long", paramType = "query")])
	def get_province_and_school_byts(HttpServletRequest request){

		long synctime =ServletRequestUtils.getLongParameter(request, "sync_time", 0);

		def maxobj = area().find().sort($$("syn_time",-1)).limit(1).toArray().get(0);
		long maxsynctime = Long.valueOf(maxobj.get("syn_time").toString()) ;
		
		BasicDBObject result = new BasicDBObject();
		
		if(synctime<maxsynctime)
		{
			
			QueryBuilder query = QueryBuilder.start();
			Pattern pattern = Pattern.compile("^.{5}\$", Pattern.CASE_INSENSITIVE);
			query.and("code").regex(pattern)
			def provincelist = area().find(query.get()).toArray();
			Map lm = new HashMap();
			lm["longitude"] = 0;
			lm["latitude"] = 0;
			provincelist.each {BasicDBObject row ->
				
				
				
				String procode = row["code"];
				//^JH\w{7,7}$
				Pattern shoolpattern = Pattern.compile("^"+procode+"\\w{6,6}\$", Pattern.CASE_INSENSITIVE);
				QueryBuilder shoolquery = QueryBuilder.start();
				shoolquery.and("code").regex(shoolpattern);
				shoolquery.and("dr").is(0);
				shoolquery.and("is_school").is("1");
				def schoollist = area().find(shoolquery.get()).toArray();
				
				
				schoollist.each {s->
				
					s.put("telephones",	SchoolUtils.phonearray(s.get("telephone").toString()));
					//如果是总部校区，做特殊处理，将编码写成JH，方便客户端解析"code" : "JH20801GZZB"
					if("JH20801GZZB".equals(s.get("code").toString()))
					{
						s.put("code", "JH");
					}
					//校验坐标location字段是否存在
					if(null == s["location"]){
						s["location"]=lm;
					}
				}
     			row.put("schoollist",schoollist);
			}
			
			result.put("synctime", maxsynctime);
			result.put("provincelist", provincelist);
		}
		else
		{
			result.put("synctime", maxsynctime);
			result.put("provincelist", null);
			
		}
		
	
		
		
	
		
		
		
	  return getResultOK(result);
					
					
					

	
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "get_province_and_city", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取当前省份和城市 ", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取当前省份和城市")

	def get_province_and_city(HttpServletRequest request){

		
		//db.accommodations.find( { $where: "this.name.length > 1" } );
		BasicDBObject result = new BasicDBObject();
		QueryBuilder query = QueryBuilder.start();
		
		
		Pattern pattern = Pattern.compile("^.{5}\$", Pattern.CASE_INSENSITIVE);
		query.and("code").regex(pattern)
		
		//db.area.find({"$where":"function(){return this.code.length==5;}"})
		def provincelist = area().find(query.get()).toArray();
		
		provincelist.each {BasicDBObject row ->
			
			String procode = row["code"];
			//^JH\w{7,7}$
			Pattern citypattern = Pattern.compile("^"+procode+"\\w{2,2}\$", Pattern.CASE_INSENSITIVE);
			QueryBuilder cityquery = QueryBuilder.start();
			cityquery.and("code").regex(citypattern);
			cityquery.and("dr").is(0);
			cityquery.and("is_school").is("1");
			//新增一个字段和coed的值重复,wap端解析冲突加入的
			row.put("pro_code", procode)
			
			def schoollist = area().find(cityquery.get()).toArray();
			
			row.put("schoollist",schoollist);
		}
		
	  return getResultOK(provincelist);
				
	}



	
	@ResponseBody
	@RequestMapping(value = "get_nearby_city", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "根据经纬度获取最近的市和校区，并获取当前省份和省份下面的城市和校区 ", httpMethod = "GET",response = BaseResultVO.class,  notes = "根据地理位置获取最近的城市和下属校区")
	@ApiImplicitParams([@ApiImplicitParam(name = "longitude", value = "经度", required = false, dataType = "float", paramType = "query"),
		                @ApiImplicitParam(name = "latitude", value = "纬度", required = false, dataType = "float", paramType = "query")])
	def get_nearby_city(HttpServletRequest request){

		float longitude  = ServletRequestUtils.getFloatParameter(request, "longitude", 0);
		float latitude = ServletRequestUtils.getFloatParameter(request, "latitude", 0);
	
		BasicDBObject insert = new BasicDBObject();
		insert.append("longitude", longitude);
		insert.append("latitude", latitude);
		
		
		QueryBuilder query = QueryBuilder.start();
		Pattern pattern = Pattern.compile("^.{11}\$", Pattern.CASE_INSENSITIVE);
		query.and("code").regex(pattern);
		query.and("location").near(longitude, latitude);
		
		def mslist = mainMongo.getCollection("area").find(query.get()
								).limit(10).toArray();
	  
		BasicDBObject result = new BasicDBObject();
		
		BasicDBObject company =  area().findOne($$("code":mslist.get(0).get("parent_nc_code")));//获取距离最近的校区所属的城市
		BasicDBObject city = area().findOne($$("code":company.get("parent_nc_code")));
		city.put("schoolist", mslist);
	    result.put("nearby_city_andschoollist", city);
		
		
		BasicDBObject province =  area().findOne($$("code":city.get("parent_nc_code")));
		
		def citylist = mainMongo.getCollection("area").find($$("parent_nc_code" : province.get("code"))).toArray();
		
		
		result.put("province", province);
		
		
		citylist.each {BasicDBObject row ->
			
			row.put("schoolist",get_school_list(row.get("code").toString()));
		}
		
		
		
		result.put("citylist", citylist);
		
		
		
	  return getResultOK(result);
					
					
					

	
		
	}

	@ResponseBody
	@RequestMapping(value = "get_applet_nearby_school_zikao", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "自考小程序根据经纬度获取附近校区", httpMethod = "GET",response = BaseResultVO.class,  notes = "自考小程序根据经纬度获取附近校区")
	@ApiImplicitParams([
			@ApiImplicitParam(name = "province", value = "省份", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "city", value = "城市", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "页大小", required = false, dataType = "long", defaultValue="5", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "long", defaultValue="1", paramType = "query")])
	def get_applet_nearby_school_zikao(HttpServletRequest request){

		String province = ServletRequestUtils.getStringParameter(request,"province","广东省")
		String city = ServletRequestUtils.getStringParameter(request,"city","广州市")

		int size = ServletRequestUtils.getIntParameter(request, "size", 5)
		int page = ServletRequestUtils.getIntParameter(request, "page", 1)

		def schoolist = new ArrayList()
		def province_nc_id = mainMongo.getCollection("area").findOne($$("name":province),$$("nc_id":1))
		if(null != province_nc_id) {
			QueryBuilder cityQuery = QueryBuilder.start()
			Pattern cityPattern = Pattern.compile("^" + city, Pattern.MULTILINE)
			cityQuery.and("parent_nc_id").is(province_nc_id.get("nc_id"))
			cityQuery.and("name").regex(cityPattern)

			mainMongo.getCollection("area").find(cityQuery.get(),$$("nc_id":1)).toArray().each { city_nc_id->
				QueryBuilder query = QueryBuilder.start()
				query.and("parent_nc_id").is(city_nc_id.get("nc_id"))

				def schools = mainMongo.getCollection("area").find(query.get()
				).skip((page - 1) * size).limit(size)?.toArray()
				schoolist.addAll(schools)
			}
		}
		BasicDBObject result = new BasicDBObject()
		result.put("schoolist", schoolist)
		return getResultOK(result)
	}

	@ResponseBody
	@RequestMapping(value = "get_applet_nearby_school", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "根据经纬度获取附近校区", httpMethod = "GET",response = BaseResultVO.class,  notes = "根据经纬度获取附近校区")
	@ApiImplicitParams([@ApiImplicitParam(name = "longitude", value = "经度", required = false, dataType = "float", paramType = "query"),
			@ApiImplicitParam(name = "latitude", value = "纬度", required = false, dataType = "float", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "页大小", required = false, dataType = "long", defaultValue="5", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "long", defaultValue="1", paramType = "query")])
	def get_applet_nearby_school(HttpServletRequest request){

		float longitude  = ServletRequestUtils.getFloatParameter(request, "longitude", 0);
		float latitude = ServletRequestUtils.getFloatParameter(request, "latitude", 0);

		int size = ServletRequestUtils.getIntParameter(request, "size", 5);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		BasicDBObject insert = new BasicDBObject();
		insert.append("longitude", longitude);
		insert.append("latitude", latitude);

		QueryBuilder query = QueryBuilder.start();
		Pattern pattern = Pattern.compile("^.{11}\$", Pattern.CASE_INSENSITIVE);
		query.and("code").regex(pattern);
		query.and("location").near(longitude, latitude);

		def schoolist = mainMongo.getCollection("area").find(query.get()
		).skip((page - 1) * size).limit(size)?.toArray();

		schoolist.each { def item->
			BasicDBObject location = item.get("location")
			double distance = getDistance(longitude,latitude,location.get("longitude"),location.get("latitude"))
			item.put("distance",String.format("%.1f", distance/1000))
		}
		BasicDBObject result = new BasicDBObject();
		result.put("schoolist", schoolist);

		return getResultOK(result);
	}

	/**
	 * 把经纬度转为度（°）
	 */
	private static double rad(double d){
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	private static double getDistance(double lng1, double lat1, double lng2, double lat2){
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(
				Math.sqrt(
						Math.pow(Math.sin(a/2),2)
								+ Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)
				)
		);
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	@ResponseBody
	@RequestMapping(value = "get_nearby_only_city", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "根据经纬度获取最近的市 ", httpMethod = "GET",response = BaseResultVO.class,  notes = "根据地理位置获取最近的城市和下属校区")
	@ApiImplicitParams([@ApiImplicitParam(name = "longitude", value = "经度", required = false, dataType = "float", paramType = "query"),
						@ApiImplicitParam(name = "latitude", value = "纬度", required = false, dataType = "float", paramType = "query")])
	def get_nearby_only_city(HttpServletRequest request){

		float longitude  = ServletRequestUtils.getFloatParameter(request, "longitude", 0);
		float latitude = ServletRequestUtils.getFloatParameter(request, "latitude", 0);
	
		BasicDBObject insert = new BasicDBObject();
		insert.append("longitude", longitude);
		insert.append("latitude", latitude);
		
		
		BasicDBObject result = new BasicDBObject();
		QueryBuilder query = QueryBuilder.start();
		
		
		Pattern pattern = Pattern.compile("^.{7}\$", Pattern.CASE_INSENSITIVE);
		query.and("code").regex(pattern);
		query.and("location").near(longitude, latitude);
		
		
		def mslist = mainMongo.getCollection("area").find(query.get()
								).limit(2).toArray();
	  

		result.put("citylist", mslist);
		
		
		
	  return getResultOK(result);
					
					
					

	
		
	}
	
	
	def get_school_list(String citycode)
	{
		def companylist =  area().find($$("parent_nc_code":citycode));
		def schoollist =[];
		
		companylist.each {BasicDBObject row ->
			
			def school = mainMongo.getCollection("area").findOne($$(["parent_nc_code" : row.get("code"),"dr":0]));
			
			
			schoollist.add(school);
			
		}
		
		
        return schoollist;
	}
	
	
	def get_province_school_list()
	{
		
		QueryBuilder query = QueryBuilder.start();
			Pattern pattern = Pattern.compile("^.{5}\$", Pattern.CASE_INSENSITIVE);
			query.and("code").regex(pattern)
			def provincelist = area().find(query.get()).toArray();
			provincelist.each {BasicDBObject row ->	
				String procode = row["code"];
				//^JH\w{7,7}$
				Pattern shoolpattern = Pattern.compile("^"+procode+"\\w{6,6}\$", Pattern.CASE_INSENSITIVE);
				QueryBuilder shoolquery = QueryBuilder.start();
				shoolquery.and("code").regex(shoolpattern);
				shoolquery.and("dr").is(0);
				shoolquery.and("is_school").is("1");
				def schoollist = area().find(shoolquery.get()).toArray();
     			row.put("schoollist",schoollist);
			}
			
		
		return provincelist;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "get_school_detail", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取校区详情 ", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取校区详情")
	@ApiImplicitParams([@ApiImplicitParam(name = "school_code", value = "校区编码", required = false, dataType = "String", paramType = "query")])
	def get_school_detail(HttpServletRequest request){
		
		
		String schoolcode  = ServletRequestUtils.getStringParameter(request, "school_code");
		
		Map<String, Object> result = new HashMap<String, Object>();

		
		def school = mainMongo.getCollection("area").findOne($$(["code" : schoolcode]));
		
		result.put("school", school);
		
		//校区课程
		if(school){
			String nc_school_id = school["nc_id"];
			
			result.put("courselist", commodityList(nc_school_id));
		}
		
		//获取校区图片
		
		result.put("piclist", get_school_pic(schoolcode));
		
		//获取校区活动
		
		
		result.put("aclist", get_school_activity(schoolcode));
		
	  return getResultOK(result);
					
	}
	
	
	@ResponseBody
	@RequestMapping(value = "get_school_menu", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取校区菜单详情 ", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取校区详情")
	@ApiImplicitParams([@ApiImplicitParam(name = "school_code", value = "校区编码", required = false, dataType = "String", paramType = "query")])
	def get_school_menu(HttpServletRequest request){
			
		String schoolcode  = ServletRequestUtils.getStringParameter(request, "school_code");
		
		Map<String, Object> result = new HashMap<String, Object>();

		
		def MenuList = [];
		def	query = $$(
			$or : [
				   $$( "school_code" ,   schoolcode),
				   $$( "school_code" ,   "all")
				   ]
			);
		 MenuList = school_menu().find(query).sort($$("menu_order",1)).toArray();
		
	  return getResultOK(MenuList);
					
	}
	
	
	
	
	
	
	/**
	 * 校区下的课程列表 -返回数量不固定,最大限制1000
	 * @param nc_school_id
	 * @return
	 */
	def commodityList(String nc_school_id){
		if(StringUtils.isNotBlank(nc_school_id)){
			//查询
			List commodity_ids = commodity_schools().find(
				$$("school_pks" : $$('$in' : [nc_school_id])),
				$$("commodity_id" : 1)
				).limit(MAX_LIMIT).toArray()*.commodity_id;
			
			if(commodity_ids != null && commodity_ids.size() > 0){
				//查询商品表
				def comList = commoditys().find(
					$$("is_shelves" : 1 , "_id" : $$('$in' : commodity_ids)) ,
					$$("_id" : 1  , "name" : 1 , "thumbnail" : 1 ,"titles" : 1, "original_price" : 1 , "try_video" : 1 , "is_hot" : 1 , "type" : 1)
					).sort($$("order" : 1)).limit(MAX_LIMIT).toArray();
				return comList;
			}
		}
		return null;
	}
	
	
	
	def get_school_pic(String schoolcode)
	{
	   def	query = $$(
		   $or : [
				  $$( "school_code" ,   schoolcode),
				  $$( "school_code" ,   "all")
				  ]
		   );
         return  school_pic().find(query).sort($$("timestamp",1)).limit(4).toArray(); 
	   
	}
	
	def get_school_activity(String schoolcode)
	{
	 
	   def activlist = [];
	   def	query = $$(
		   $or : [
				  $$( "school_code" ,   schoolcode),
				  $$( "school_code" ,   "all")
				  ]
		   );
		query.append("dr",0);
		 return  school_article().find(query).sort($$("timestamp",-1)).limit(8).toArray();
	   
	   return activlist;
	}
	
	
	
	def get_school_activity_page(String schoolcode)
	{
	 
	   def activlist = [];
	   def	query = $$(
		   $or : [
				  $$( "school_code" ,   schoolcode),
				  $$( "school_code" ,   "all")
				  ]
		   );
		 return  school_article().find(query).sort($$("timestamp",-1)).limit(8).toArray();
	   
	   return activlist;
	}
	
	
	//http://api.map.baidu.com/geoconv/v1/?coords=114.21892767453,29.575429778924;114.21892734521,29.575429778924&from=1&to=5&ak=EF06cfb26173665ad80b8edf6a328192&callback=dealResult
	@ResponseBody
	@RequestMapping(value = "schoolposchange", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "批量转换校区坐标（测试预留接口，勿调用） ", httpMethod = "GET",response = BaseResultVO.class,  notes = "批量转换校区坐标  ")
	def schoolposchange(HttpServletRequest request){
		
	
		
		def mslist = area().find($$("dr",0)).toArray();
		
		
		mslist.each {BasicDBObject row ->
			String name = row["name"];
			//商品所属校区和价格
			
			String  longitude = row.get("location").get("longitude");
			String  latitude = row.get("location").get("latitude");
			
			Thread.sleep(1000);
			if(row.get("location"))
			{
           if(longitude!="0")
		   {
				String url = "http://api.map.baidu.com/geoconv/v1/?coords="+longitude+","+latitude+"&from=1&to=5&ak=EF06cfb26173665ad80b8edf6a328192&callback=dealResult";
				String resultOK =HttpClientUtil4_3.get(url, null);
			    resultOK =resultOK.replace("dealResult&&dealResult(", "");
				resultOK = resultOK.substring(0,resultOK.length()-1);

				JSONObject Json = new JSONObject(resultOK);
				JSONArray subjson = Json.getJSONArray("result");
				JSONObject subitem = subjson.get(0);
				BasicDBObject insert = new BasicDBObject();
				insert.append("longitude", Float.parseFloat(subitem.get("x").toString()));
				insert.append("latitude", Float.parseFloat(subitem.get("y").toString()));
		
				area().update(
					new BasicDBObject("_id":row.get("_id")),
					new BasicDBObject('$set':
						new BasicDBObject(
							"location" : insert
							)
						));
					
		   }
			}
				
				
			
			
							
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = "setschoolarea", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "批量设置校区的地理位置（测试预留接口，勿调用） ", httpMethod = "GET",response = BaseResultVO.class,  notes = "批量设置校区的地理位置  ")
	def setschoolarea(HttpServletRequest request){
		
	
		
		def mslist = area().find($$("dr",0)).toArray();
		
		
		mslist.each {BasicDBObject row ->
			String name = row["name"];
			//商品所属校区和价格
			IbSchool uScore = (IbSchool) sessionFactory.getCurrentSession()
			.createCriteria(IbSchool.class)
			.add(Restrictions.eq(IbSchool.PROP_NAME , name))
			.setMaxResults(1)
			.uniqueResult();

			
			if(uScore!=null)
			{
				BasicDBObject insert = new BasicDBObject();
				
				
				
				insert.append("longitude", Float.parseFloat(uScore.getLongitude()));
				insert.append("latitude", Float.parseFloat(uScore.getLatitude()));
		
				area().update(
					new BasicDBObject("name":uScore.getName()),
					new BasicDBObject('$set':
						new BasicDBObject(
							"location" : insert
							)
						));
				
				
			}
			else
			{
				BasicDBObject insert = new BasicDBObject();
				
				insert.append("longitude", 0);
				insert.append("latitude", 0);
					area().update(
					new BasicDBObject("_id":row["_id"]),
					new BasicDBObject('$set':
						new BasicDBObject(
							"location" : insert
							)
						));
			}
		
							
		}
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "get_school_by_city", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "根据城市获得对应校区详情 ", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取校区详情")
	@ApiImplicitParams([@ApiImplicitParam(name = "code", value = "城市编码", required = false, dataType = "String", paramType = "query")])
	def get_school_by_city(HttpServletRequest request){
		
		String code  = ServletRequestUtils.getStringParameter(request, "code");
		def result_list = []
		def company_list = mainMongo.getCollection("area").find($$("parent_nc_code":code,"dr":0),$$("_id" : 1,"code":1)).toArray();
		company_list.each {company->
			def school_detail = mainMongo.getCollection("area").find($$("parent_nc_code" : company["code"],"dr":0),$$("_id" : 1,"code":1,"name":1,"telephone":1,"address":1,"location":1,"header":1)).toArray();
			if(school_detail.size==0){}else{
				if(StringUtils.isNotBlank(school_detail.get(0)["address"])){
					result_list.add(school_detail.get(0));
				}
			}
		}
	  return getResultOK(result_list);		
	}
	
	@ResponseBody
	@RequestMapping(value = "get_all_school_info", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取省份城市校区全部信息 ", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取省份城市校区全部信息")
	def get_all_school_info(HttpServletRequest request){
		def result_list = []
		result_list = mainMongo.getCollection("area").find($$("dr":0)).toArray();
		return getResultOK(result_list);		
	}
	
	public static void main(String[] args) throws Exception {
		
		
			
	}
}

