package com.izhubo.web

import static com.izhubo.rest.common.util.WebUtils.$$
import static com.izhubo.rest.common.doc.MongoKey.*;
import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.izhubo.model.Mission;
import com.izhubo.web.model.CommodityType
import com.izhubo.web.vo.CommodityCommodityPageInfo
import com.izhubo.web.vo.CommodityHomeBannerList
import com.izhubo.web.vo.CommodityListVO
import com.izhubo.web.vo.CommoditysCommoditySimpleInfoVO
import com.izhubo.web.vo.HomeSearchVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation

/**
 * 课程列表
* @ClassName: CommoditysListController 
* @Description: 课程列表
* @author shihongjie
* @date 2016年3月8日 上午11:02:55 
*
 */
@Controller
@RequestMapping("/commoditys")
class CommoditysController extends BaseController {
	
	@Resource
	private RedisController redisController;
	
	/** 商品所属校区 */
	public DBCollection commodity_schools() {
		return mainMongo.getCollection("commodity_schools");
	}
	
	/** 商品价格 */
	public DBCollection commodity_prices() {
		return mainMongo.getCollection("commodity_prices");
	}
	
	/** 商品课程 */
	public DBCollection commodity_courses() {
		return mainMongo.getCollection("commodity_courses");
	}
	
	/** 商品关联商品 */
	public DBCollection commodity_to_commodity() {
		return mainMongo.getCollection("commodity_to_commodity");
	}
	/** 首页推荐 */
	public DBCollection home_banner() {
		return mainMongo.getCollection("banner");
	}
	/** 课程列表banner */
	public DBCollection course_banner() {
		return mainMongo.getCollection("course_banner");
	}
	/** 课时 */
	public DBCollection courses_content() {
		return mainMongo.getCollection("courses_content");
	}
	
	
	
	
	
	/**
	 * 首页推荐列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "homeBannerList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "首页推荐", httpMethod = "GET"  ,  notes = "分为banner列表和商品列表")
	def homeBannerList(HttpServletRequest request){
		//首页推荐列表
		def bannerList = home_banner().find($$("is_show" : 1), $$("url": 1 , "icon" : 1 , "pic" : 1 , "title" : 1))
					.sort($$("order" : 1)).limit(4)?.toArray();
					
		Map map = new HashMap();			
		map["bannerList"] = bannerList;
		map["commodityList"] = homeCommodityList();
		return getResultOK(map);
	}
	
	def homeCommodityList(){
		//查询条件 已上架 套餐商品
		def query = commoditysQuery();
		def show = $$("_id" : 1  , "name" : 1 ,  "thumbnail" : 1 ,"titles" : 1, "original_price" : 1 , "try_video" : 1 , "is_hot" : 1 , "type" : 1, "price" : 1);
		return commoditys().find(query , show).sort($$("sort" : 1)).limit(4).toArray();
	}
	
	
	/**
	 * 商品关联商品
	 * @param commodity_id 商品id
	 * @return 关联列表
	 */
	private _commodityToCommodity(String commodity_id){
		return _commodityToCommodity(commodity_id, MAX_LIMIT);
	}
	/**
	 * 商品关联商品
	 * @param commodity_id 商品id
	 * @param size 		   查询数量
	 * @return 关联列表
	 */
	private _commodityToCommodity(String commodity_id , Integer size){
		def commodityList = null;
		if(StringUtils.isNotBlank(commodity_id)){
			//查询条件
			def query = $$("is_enable" : 1 , "main_commodity_id" : commodity_id);
			//显示字段
			def show = $$("_id" : 1 , "to_commodity_id" : 1);
			//配需
			def sort = $$("create_time" : -1);
			//商品被关联集合
			def commodity_id_list = commodity_to_commodity().find(query , show).sort(sort).limit(size)?.toArray().collect{it["to_commodity_id"]};
			if(commodity_id_list){
				commodityList = commoditys().find(
						$$("_id" : [$in : commodity_id_list ]),
						$$("_id" : 1 , "name" : 1 , "photo" : 1)
						)?.toArray();
			}
		}
		return commodityList;
	}
		
	/**
	 * 套餐商品详情 
	 * @date 2016年3月9日 下午5:42:49 
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "commodityPackageInfo", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "套餐商品详情 ", httpMethod = "GET", response = CommodityCommodityPageInfo.class,  notes = "套餐商品详情 ")
	def commodityPackageInfo(HttpServletRequest request){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		def query = commoditysQuery();
		query.put("type", CommodityType.会计上岗.ordinal());
		//查询字段
		def show = $$(
			"_id" : 1  , "name" : 1 , "titles" : 1,
//			 "price" : 1,"monthprice":1, 
			 "reader" : 1 , "course_ids" : 1 , 
			"learntime" : 1 , "classmode" : 1, 
			"is_hot" : 1 , "commodity_type_list" : 1 , "photo" : 1,
			"short_name" : 1 ,"code" : 1, "try_video" : 1
			);
		//排序
		def sort = $$("order_page" : 1 , "order" : 1);
		//商品列表
		def commodityList = commoditys().find(query , show).sort(sort).limit(6)?.toArray();
		
		if(commodityList){
			
			commodityList.each {BasicDBObject row ->
				String _id = row["_id"];
				//商品所属校区和价格
//				row["school_price"] = redisController.get_commodity_school_price_Array(_id);
				row["school_price_string"] = redisController.get_commodity_school_price_String(_id);
				//课次
				row["classtime"] = getCommodityClassTimeByList(row["course_ids"]);
				
				//商品下的分类列表
				def commodity_type_list = row["commodity_type_list"];
				if(commodity_type_list){
					commodity_type_list.each {BasicDBObject type_row ->
						//分类下的课程列表
						def course_list = type_row["course_list"];
						if(course_list){
							course_list.each {BasicDBObject course_row ->
								//课程id
								def commodity_courses_id = course_row["commodity_courses_id"];
								//课程查询字段
								def course_show = $$(
												"_id" : 1 , "nc_course_id" : 1 , 
												"show_name" : 1 ,
												"is_online" :1 , 
												"try_video" : 1 , "tiku_url" : 1 , "shixun_url" : 1 
											 );
								//商品下的课程信息
								def course = commodity_courses().findOne($$("_id" : commodity_courses_id) , course_show);
								
								if(course){
									//展示昵称
									course_row["show_name"] = course["show_name"];
									//nc_id
									course_row["nc_course_id"] = course["nc_course_id"];
									//名称
									course_row["show_name"] = course["show_name"];
									//线上线下标志
									course_row["is_online"] = course["is_online"];
									//试听地址
									course_row["try_video"] = course["try_video"];
									//题库地址
									course_row["tiku_url"] = course["tiku_url"];
									//实训地址
									course_row["shixun_url"] = course["shixun_url"];
								}
							}
						}
					}
					
					row["commodity_type_list_string"] = row["commodity_type_list"].toString();
				}
				
			}
		}
		return getResultOK(commodityList);
	}
	
	/**
	 * 套餐商品详情 
	 * @date 2016年3月9日 下午5:42:49 
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "commodityInfo", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "商品详情 ", httpMethod = "GET", notes = "商品详情 ")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "commodity_id", value = "商品id", required = true, dataType = "String", paramType = "query")
	])
	def commodityInfo(HttpServletRequest request){
		//商品id
		String commodity_id = request["commodity_id"];
		if(StringUtils.isBlank(commodity_id)){
			return getResultParamsError();
		}
		Map map = new HashMap();
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		def query = commoditysQuery();
		query.put("_id", commodity_id);
		//查询字段
		def show = $$(
				"_id" : 1  , "name" : 1 , "titles" : 1,
				"reader" : 1 , "course_ids" : 1 , "type" : 1,
				"learntime" : 1 , "classmode" : 1, 
				"is_hot" : 1 , "commodity_type_list" : 1 , "photo" : 1,
				"short_name" : 1 ,"code" : 1, "try_video" : 1 , "content" : 1
				);
		//排序
		def sort = $$("order_page" : 1 , "order" : 1);
		//商品列表
		def commodity = commoditys().findOne(query , show);
		
		if(commodity){
			map["_id"] = commodity["_id"];
			map["name"] = commodity["name"];
			map["titles"] = commodity["titles"];
			map["reader"] = commodity["reader"];
			map["learntime"] = commodity["learntime"];
			map["classmode"] = commodity["classmode"];
			map["photo"] = commodity["photo"];
			map["try_video"] = commodity["try_video"];
			map["content"] = commodity["content"];
			map["type"] = commodity["type"];
			
			
			//套餐商品
			if(CommodityType.会计上岗.ordinal() == commodity["type"]){
				def pquery = commoditysQuery();
				pquery.put("type", CommodityType.会计上岗.ordinal())
				def comList = commoditys().find(pquery , $$("_id" : 1 , "short_name" : 1)).sort($$("order_page" : 1)).limit(MAX_LIMIT).toArray();
				if(comList){
					for(int i = 0 ; i < comList.size() ; i++){
						def item = comList[i];
						if(commodity_id.equals(item["_id"])){
							item["active"] = true;
//							break;
						}else{
							item["active"] = false;
						}
						item["url"] = "ke_"+item["_id"]+".html";
					}
					map["pageList"] = comList;
				}
			}
			
			//商品所属校区和价格
			map["school_price_string"] = redisController.get_commodity_school_price_String(commodity_id);
			//课次
			map["classtime"] = getCommodityClassTimeByList(commodity["course_ids"]);
			//商品下的分类列表
			def commodity_type_list = commodity["commodity_type_list"];
			if(commodity_type_list){
				
				//课程分类集合
				List typeList = new ArrayList();
				//课程分类-线上集合
				List typeUpList = new ArrayList();
				//课程分类-线下集合
				List typeDownList = new ArrayList();
				
				map["tList"] = typeList;
				map["tUpList"] = typeUpList;
				map["tDownList"] = typeDownList;
				
				//循环课程分类
				commodity_type_list.each {BasicDBObject type_row ->
					//分类下的课程列表
					def course_list = type_row["course_list"];
					if(course_list){
						//课程查询字段
						def course_show = $$(
								"_id" : 1 , "nc_course_id" : 1 ,
								"show_name" : 1 ,
								"is_online" :1 ,
								"try_video" : 1 , "tiku_url" : 1 , "shixun_url" : 1
								);
						List courseList = new ArrayList();	
						List courseUpList = new ArrayList();	
						List courseDownList = new ArrayList();
						//循环课程	
						course_list.each {BasicDBObject course_row ->
							//课程id
							def commodity_courses_id = course_row["commodity_courses_id"];
						
							//商品下的课程信息
							def course = commodity_courses().findOne($$("_id" : commodity_courses_id) , course_show);
							if(course){
								Map cMap = new HashMap();
								//展示昵称
								cMap["show_name"] = course["show_name"];
								//nc_id
								cMap["nc_course_id"] = course["nc_course_id"];
								//名称
								cMap["show_name"] = course["show_name"];
								//线上线下标志
								cMap["is_online"] = course["is_online"];
								//试听地址
								cMap["try_video"] = course["try_video"];
								//题库地址
								cMap["tiku_url"] = course["tiku_url"];
								//实训地址
								cMap["shixun_url"] = course["shixun_url"];
								
								//全部集合
								courseList.add(course);
								
								//线上线下集合
								Integer online_type = course["is_online"];
								//线下
								if(online_type == 0){
									courseDownList.add(course);
								}else{
									courseUpList.add(course);
								}
							}
						}
						
						//类型下的课程集合
						if(courseList.size() > 0){
							//类型
							Map tMap = new HashMap();
							tMap["name"] = type_row["name"];
							tMap["list"] = courseList;
							//类型集合
							typeList.add(tMap);
						}
						//类型下的课程集合
						if(courseUpList.size() > 0){
							//类型
							Map tMap = new HashMap();
							tMap["name"] = type_row["name"];
							tMap["list"] = courseUpList;
							//类型集合
							typeUpList.add(tMap);
						}
						//类型下的课程集合
						if(courseDownList.size() > 0){
							//类型
							Map tMap = new HashMap();
							tMap["name"] = type_row["name"];
							tMap["list"] = courseDownList;
							//类型集合
							typeDownList.add(tMap);
						}
						
					}
				}
				
			}
			
		}
		return getResultOK(map);
	}
	
	/**
	 * 精品商品详情 
	 * @date 2016年3月9日 下午5:42:49 
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "commoditySimpleInfo", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "非套餐商品详情 ", httpMethod = "GET",response = CommoditysCommoditySimpleInfoVO.class,  notes = "非套餐商品详情 ")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "commodity_id", value = "商品id", required = true, dataType = "String", paramType = "query")
	])
	def commoditySimpleInfo(HttpServletRequest request){
		//TODO 待测试 消息返回结构VO
		//商品id
		String commodity_id = request["commodity_id"];
		if(StringUtils.isBlank(commodity_id)){
			return getResultParamsError();
		}
		
		//查询条件 已上架 套餐商品
		
		def query = commoditysQuery();
		query.put("_id", commodity_id);
		//查询字段
		def show = $$(
				"_id" : 1  , "name" : 1 , "type" : 1 , "titles" : 1,
//				"price" : 1,"monthprice":1,
				 "reader" : 1 , "course_ids" : 1 , 
				"learntime" : 1 , "classmode" : 1, "photo" : 1 ,
				"is_hot" : 1 ,"commodity_type_list" : 1 ,"code" : 1,
				"content" : 1, "try_video" : 1
				);
		//商品
		def commodity = commoditys().findOne(query , show);
		
		if(commodity){
			
			String _id = commodity["_id"];
			//商品所属校区和价格
//			commodity["school_price"] = redisController.get_commodity_school_price_Array(_id);
			commodity["school_price_string"] = redisController.get_commodity_school_price_String(_id);
			//商品关联商品集合
			commodity["commodity_to_commodity"] = _commodityToCommodity(_id , 3);
			
			//课次
			commodity["classtime"] = getCommodityClassTimeByList(commodity["course_ids"]);
			
			
			//商品下的分类列表
			def commodity_type_list = commodity["commodity_type_list"];
			if(commodity_type_list){
				commodity_type_list.each {BasicDBObject type_row ->
					//分类下的课程列表
					def course_list = type_row["course_list"];
					if(course_list){
						course_list.each {BasicDBObject course_row ->
							//课程id
							def commodity_courses_id = course_row["commodity_courses_id"];
							//课程查询字段
							def course_show = $$(
									"_id" : 1 , "nc_course_id" : 1 , 
									"show_name" : 1 ,
									"is_online" :1 , 
									"try_video" : 1 , "tiku_url" : 1 , "shixun_url" : 1 
									);
							//商品下的课程信息
							def course = commodity_courses().findOne($$("_id" : commodity_courses_id) , course_show);
							
							if(course){
								//展示昵称
								course_row["show_name"] = course["show_name"];
								//nc_id
								course_row["nc_course_id"] = course["nc_course_id"];
								//名称
								course_row["show_name"] = course["show_name"];
								//线上线下标志
								course_row["is_online"] = course["is_online"];
								//试听地址
								course_row["try_video"] = course["try_video"];
								//题库地址
								course_row["tiku_url"] = course["tiku_url"];
								//实训地址
								course_row["shixun_url"] = course["shixun_url"];
							}
						}
					}
				}
				//map to json
				commodity["commodity_type_list_string"] = commodity_type_list.toString();
			}
			
		}
		return getResultOK(commodity);
	}
	
	
	def get_commodity_byid(String id)
    {
		String commodity_id = id;
		if(StringUtils.isBlank(commodity_id)){
			return getResultParamsError();
		}
		
		//查询条件 已上架 套餐商品
		
		def query = commoditysQuery();
		query.put("_id", commodity_id);
		//查询字段
		def show = $$(
				"_id" : 1  , "name" : 1 , "titles" : 1,
//				"price" : 1, "monthprice":1, 
				"reader" : 1 , "course_ids" : 1 ,
				"learntime" : 1 , "classmode" : 1, "photo" : 1 ,
				"is_hot" : 1 ,"commodity_type_list" : 1 ,"code" : 1,
				"content" : 1, "try_video" : 1
				);
		//商品
		def commodity = commoditys().findOne(query , show);
		
		if(commodity){
			
			String _id = commodity["_id"];
			
			//课次
			commodity["classtime"] = getCommodityClassTimeByList(commodity["course_ids"]);
			
			
			//商品下的分类列表
			def commodity_type_list = commodity["commodity_type_list"];
			String TextList = "";
			if(commodity_type_list){
				commodity_type_list.each {BasicDBObject type_row ->
					//分类下的课程列表
					
				
					TextList = TextList+type_row["name"].toString()+"、";
					
					
			
				}
			}
			commodity["commodity_type_list"] = TextList.substring(0, TextList.length()-1);
			
		}
		
		return commodity;
	}	
	
	/**
	 * 商品列表
	 * @Description: 商品列表
	 * @date 2016年3月19日 上午9:32:44 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "commoditysList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "商品列表", httpMethod = "GET", response = CommodityListVO.class,  notes = "商品列表")
	def commoditysList(HttpServletRequest request) {
		//套餐商品
		def package_commodity = package_commodity_list();
		
		//0.未分类 ,1.会计上岗 , 2.会计考证 , 3.会计学历 , 4.经营会计
		Map map = new HashMap();
		map["bannerList"] = courseBanner();
		
		//会计上岗
		map["tab0"] = package_commodity_list();
		//会计考证
		map["tab1"] = single_commodity_list(CommodityType.会计考证.ordinal());
		//经营会计
		map["tab2"] = single_commodity_list(CommodityType.经营会计.ordinal());
		
		return getResultOK(map);
	}
	
	/**
	 * 课程列表banner
	 * @return	
	 */
	def courseBanner(){
		def courseBannerList = course_banner().find(
			$$("dr" : 0 , "pc_is_show" : 1) , 
			$$("name" : 1 , "pc_pic" : 1 , "pc_icon" : 1 , "pc_url" : 1 )
			).sort($$("order" : 1)).limit(MAX_LIMIT).toArray();
		return courseBannerList;
	}
	
			
	/**
	 * 会计上岗列表
	 * @Description: 会计上岗列表
	 * @date 2016年3月10日 下午5:33:36 
	 */
	def package_commodity_list(){
		Map map = new HashMap();
		map["name"] = "会计上岗";
		//查询条件 已上架 套餐商品
		def query = commoditysQuery();
//		query.put("type", CommodityType.会计上岗.ordinal());
		query.put("type", $$('$in' : [CommodityType.会计上岗.ordinal() , CommodityType.会计上岗_猎才计划.ordinal()]));
		//查询字段
//		def show = $$("_id" : 1  , "name" : 1 ,  "thumbnail" : 1 ,"price" : 1, "original_price" : 1 , "try_video" : 1 , "is_hot" : 1 , "type" : 1);
		def show = $$("_id" : 1  , "name" : 1 ,  "thumbnail" : 1 ,"titles" : 1, "original_price" : 1 , "try_video" : 1 , "is_hot" : 1 , "type" : 1, "price" : 1);
		//排序
		def sort = $$("order" : 1);
		//商品列表
		def commodityList = commoditys().find(query , show).sort(sort).limit(MAX_LIMIT)?.toArray();
//		if(commodityList){
//			commodityList.each {def item->
//				item["name"] = item["short_name"];
//				item.removeField("short_name");
//			}
//		}
		map["item"] = commodityList;
		return map;
	}
	
	/**
	 * 
	 * @param commodity_type 0.未分类 ,1.会计上岗 , 2.会计考证 , 3.会计学历 , 4.经营会计
	 * @return
	 */
	def single_commodity_list(Integer commodity_type){
		
		Map map = new HashMap();
		//0.未分类 ,1.会计上岗 , 2.会计考证 , 3.会计学历 , 4.经营会计
		if(commodity_type == CommodityType.会计考证.ordinal()){
			map["name"] = "会计考证";
		}else if(commodity_type == CommodityType.经营会计.ordinal()){
			map["name"] = "经营会计";
		}else{
			map["name"] = "-";
		}
		
		//查询条件 已上架 套餐商品
		def query = commoditysQuery();
		query.put("type", commodity_type);;
		//查询字段
//		def show = $$("_id" : 1  , "name" : 1 , "thumbnail" : 1 ,"price" : 1, "original_price" : 1 , "try_video" : 1 , "is_hot" : 1 , "type" : 1);
		def show = $$("_id" : 1  , "name" : 1 , "thumbnail" : 1 ,"titles" : 1, "original_price" : 1 , "try_video" : 1 , "is_hot" : 1 , "type" : 1 , "price" : 1);
		//排序
		def sort = $$("order" : 1);
		//商品列表
		def commodityList = commoditys().find(query , show).sort(sort).limit(MAX_LIMIT)?.toArray();
		map["item"] = commodityList;
		return map;
	}
	
	
	
	/**
	 * 搜索商品
	 * @Description: 搜索商品
	 * @date 2016年3月10日 下午5:32:19 
	 * @param @param keyword 关键字
	 * @param @param page    第几页
	 * @param @param size    每页条数
	 */
	@ResponseBody
	@RequestMapping(value = "search", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "搜索课程", httpMethod = "GET", response = HomeSearchVO.class, notes = "搜索课程,返回数据分页")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "keyword", value = "关键字", required = true, dataType = "String", paramType = "query")
	])
	def search(HttpServletRequest request) {
		
		String keyword = request["keyword"];
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		//正则匹配
		if(StringUtils.isNotBlank(keyword)){
			//查询条件
			def query = commoditysQuery();
			Pattern pattern = Pattern.compile("^.*" + keyword + ".*\$", Pattern.CASE_INSENSITIVE);
			def orobj = [
						  $$( "name" ,   pattern),
						  $$( "summary" , pattern)
						  ];
			query.put($or, orobj);
			
	
			
			
			
		
		
			//查询结果
			def commoditysList = null;
		
				//需要查询的字段
				def show = $$("_id" : 1 , "name" : 1 ,  "type":1);
				commoditysList = commoditys().find(query , show).sort($$("order" : 1)).toArray();
				
				commoditysList.each {def item->
					
					Integer Type = item["type"] ==null?0:Integer.valueOf(item["type"].toString());
					if(Type == CommodityType.会计上岗.ordinal())
					{
						item["url"] = "/course/package.html?commodity_id="+item["_id"];
					}
					else
					{
						item["url"] = "/course/ke_"+item["_id"]+".html";
					}
				}
			
			
			
			return getResultOK(commoditysList);
		}
		return getResultParamsError();
	}
	
			
			
	/**
	 * 获取商品校区价格
	 * @date 2016年3月10日 下午3:06:53 
	 * @param @param commodity_id
	 * @param @param area_code
	 */
	def find_commodity_area_price(String commodity_id , String nc_school_pk){
		Double price = null;
		if(StringUtils.isNotBlank(commodity_id) && StringUtils.isNotBlank(nc_school_pk)){
			//商品nc_id
			String nc_commodity_id = commoditys().findOne($$("_id" : commodity_id) , $$("nc_id" : 1))?.get("nc_id");
			//校区的nc_id
			String nc_id = nc_school_pk;
			//价格获取最多有5层 从底层往上找价格 顺序分别是:校区-城市-省-大区-上海恒企
			for(int i = 0 ; i < 5 ; i++){
				//商品价格信息
				def commodity_prices = commodity_prices().findOne($$("nc_school_pk" : nc_id , "nc_commodity_id" :nc_commodity_id) , $$("normal_price" : 1));
				//判断是否拿到价格
				if(commodity_prices){
					price = Double.valueOf(commodity_prices.get("normal_price").toString());
					break;
				}
				//如果没有获取到价格,获取父级nc_id
				
				nc_id = area().findOne($$("nc_id" : nc_id) , $$("parent_nc_id" : 1))?.get("parent_nc_id");
				//如果i=0 上面查到的是公司的层级 在查询一次获取到市的nc_id
				if(i == 0){
					nc_id = area().findOne($$("nc_id" : nc_id) , $$("parent_nc_id" : 1))?.get("parent_nc_id");
				}
			}
			
			
		}
		return price;
	}		
	
	/**
	 * 获取课次
	 * @param commodity_id	商品id
	 * @return
	 */
	public String getCommodityClassTime(String commodity_id){
		return getCommodityClassTimeByList(commoditys().findOne($$("_id" : commodity_id) , $$("course_ids" : 1))?.get("course_ids"));
	}
	
	/**
	 * 获取课次
	 * @param nc_commodity_id	NC商品id
	 * @return
	 */
	public String getCommodityClassTimeByNcId(String nc_commodity_id){
		return getCommodityClassTimeByList(commoditys().findOne($$("nc_id" : nc_commodity_id) , $$("course_ids" : 1))?.get("course_ids"));
	}
	
	/**
	 * 获取课次
	 * @param nc_course_ids	商品下的课程nc_id数组
	 * @return
	 */
	public String getCommodityClassTimeByList(List<String> nc_course_ids){
		Integer result = 0;
		if(nc_course_ids != null && nc_course_ids.size() > 0){
			nc_course_ids.each{String nc_course_id ->
				result += courses_content().count($$("nc_course_id" : nc_course_id)).intValue();
			}
		}
		return result.toString();
	}
	
	
//	/**
//	 * 获取商品校区价格
//	 * @date 2016年3月10日 下午3:06:53 
//	 * @param @param commodity_id
//	 * @param @param area_code
//	 */
//	def find_commodity_area_price(String commodity_id , String nc_school_pk){
//		Double price = null;
//		if(StringUtils.isNotBlank(commodity_id) && StringUtils.isNotBlank(nc_school_pk)){
//			
//			String nc_commodity_id = commoditys().findOne($$("_id" : commodity_id) , $$("nc_id" : 1))?.get("nc_id");
//			
//			price = commodity_prices().findOne($$("nc_school_pk" : nc_school_pk , "nc_commodity_id" :nc_commodity_id) , $$("normal_price" : 1))?.get("normal_price");
//		}
//		return price;
//	}		
			
			
			
	
}
