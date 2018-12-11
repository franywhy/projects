package com.izhubo.web

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.model.SignsStatus
import com.izhubo.web.api.Web
import com.izhubo.web.model.CommodityType
import com.izhubo.web.vo.AHCommodityControllerCommodityList
import com.izhubo.web.vo.AHCommoditysCommodityConfirmPriceVO
import com.izhubo.web.vo.AHCommoditysCommodityInfoVO
import com.izhubo.web.vo.AHCommoditysCommodityPriceListVO
import com.izhubo.web.vo.AHHomeCommodityListVO
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
@RequestMapping("/ahcommoditys")
class AHCommoditysController extends BaseController {
	
	/** 课程列表接口 */
	@Value("#{application['course.info.url']}")
	private String course_info_url ;
	
	@Resource
	private RedisController redisController;
	@Resource
	private CommoditysController commoditysController;
	
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
	
	/** 课程列表banner */
	public DBCollection course_banner() {
		return mainMongo.getCollection("course_banner");
	}
	
	
	
	
	/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  首页商品列表   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	/**
	 * 首页商品列表
	 * @date 2016年3月28日
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "commodityListHome", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "首页商品列表", httpMethod = "GET", response = AHHomeCommodityListVO.class, notes = "会答2.0 首页商品列表API")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
	])
	def commodityListHome(HttpServletRequest request){
		Map map = new HashMap();
		map["packageListHome"] = package_commodity_list();
		map["singleListHome"] = commoditySingleListHome(request);
		return getResultOK(map);
	}
	
	/**
	 * 首页商品列表-套餐商品列表		
	 * @Description: 套餐商品列表
	 * @date 2016年3月10日 下午5:33:36 
	 */
	def package_commodity_list(){
		//查询条件 已上架 套餐商品
		def query = commoditysQuery();
		query.put("type", CommodityType.会计上岗.ordinal() );
//		query.put("type", $$('$in' : [CommodityType.会计上岗.ordinal() , CommodityType.会计上岗_猎才计划.ordinal()]));
		//查询字段
		def show = $$(
			"_id" : 1  ,"short_name" : 1
			);
		//排序
		def sort = $$("order_app" : 1 , "order" : 1);
		//商品列表
		def commodityList = commoditys().find(query , show).sort(sort).limit(4)?.toArray();
		
		if(commodityList){
			commodityList.each {def row->
				row["url"] = course_info_url + "?id=" + row["_id"];
			}
		}
		
		return commodityList;
	}
	
	/**
	 * 首页商品列表-精品商品列表
	 * @Description: 商品列表-套餐商品列表
	 * @date 2016年3月28日
	 */
	def commoditySingleListHome(HttpServletRequest request){
		//查询条件 已上架 套餐商品
		def query = commoditysQuery();
//		query.put("type", $$( '$in' : [  CommodityType.会计考证.ordinal() , CommodityType.经营会计.ordinal() ]))
		//查询字段
		def show = $$(
//				"_id" : 1  , "name" : 1 , "price" : 1  , "app_thumbnail" : 1
				"_id" : 1  , "name" : 1 , "titles" : 1  , "app_thumbnail" : 1 , "original_price" : 1
				);
		//排序
		def sort = $$("order_app" : 1 , "order" : 1);
		//商品列表
		def commodityList = commoditys().find(query , show).sort(sort).limit(4)?.toArray();
		Web.currentUserId()
		if(commodityList){
			String token = request["token"];
			commodityList.each {def row->
				row["url"] = course_info_url + "?token=" + token +"?course_id=" + row["_id"];
				row["thumbnail"] = row["app_thumbnail"];
			}
		}
		
		return commodityList;
	}
	
	/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  首页商品列表   ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ */
	
	
	
	/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  商品列表   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	
	/**
	 * 商品列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "commodityList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "商品列表", httpMethod = "GET", response = AHCommodityControllerCommodityList.class , notes = "商品列表")
	def commodityList(HttpServletRequest request){
		Map map = new HashMap();
		//banner集合
		map["bannerList"] = courseBanner();
		
		//会计上岗
		map["tab0"] = commodityPackageList();
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
			$$("dr" : 0 , "app_is_show" : 1) ,
			$$("name" : 1 , "app_pic" : 1 , "app_url" : 1 )
			).sort($$("order" : 1)).limit(MAX_LIMIT).toArray();
		return courseBannerList;
	}
	
	/**
	 * 0.未分类 ,1.会计上岗 , 2.会计考证 , 3.会计学历 , 4.经营会计
	 * @param commodity_type 2.经营商品 3.管理会计 4.会计考证 5.会计学历
	 * @return
	 */
	def single_commodity_list(Integer commodity_type){
		
		Map map = new HashMap();
		//0.未分类 ,1.会计上岗 , 2.会计考证 , 3.会计学历 , 4.经营会计
		if(commodity_type == CommodityType.会计考证.ordinal()){
			map["name"] = "会计上岗";
		}else if(commodity_type == CommodityType.经营会计.ordinal()){
			map["name"] = "经营会计";
		}else{
			map["name"] = "-";
		}
		
		//查询条件 已上架 套餐商品
		def query = commoditysQuery();
		query.put("type", commodity_type);;
		//查询字段
//		def show = $$("_id" : 1  , "name" : 1 , "app_thumbnail" : 1 ,"price" : 1 , "original_price" : 1 , "try_video_app" : 1);
		def show = $$("_id" : 1  , "name" : 1 , "app_thumbnail" : 1 ,"titles" : 1 , "original_price" : 1 , "try_video_app" : 1);
		//排序
		def sort = $$("order_app" : 1 , "order" : 1);
		//商品列表
		def commodityList = commoditys().find(query , show).sort(sort).limit(MAX_LIMIT)?.toArray();
		
		
		map["item"] = commodityList;
		return map;
	}
	
	/**
	 * 会计上岗列表
	 * @Description: 会计上岗列表
	 * @date 2016年3月26日 上午11:29:32 
	 */
	def commodityPackageList(){
		Map map = new HashMap();
		map["name"] = "会计上岗";
		//查询条件 已上架 套餐商品
		def query = commoditysQuery();
		query.put("type", $$('$in' : [CommodityType.会计上岗.ordinal() , CommodityType.会计上岗_猎才计划.ordinal()]));
		//查询字段
//		def show = $$("_id" : 1  , "name" : 1 , "app_thumbnail" : 1 ,"price" : 1, "original_price" : 1 , "try_video_app" : 1);
		def show = $$("_id" : 1  , "name" : 1 , "app_thumbnail" : 1 ,"titles" : 1, "original_price" : 1 , "try_video_app" : 1);
		//排序
		def sort = $$("order_app" : 1 , "order" : 1);
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
	
	/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  商品列表   ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ */
	
	/**
	 * 商品(套餐和非套餐)详情页面
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "commodityInfo", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "商品(套餐和非套餐)详情页面", httpMethod = "GET", response = AHCommoditysCommodityInfoVO.class, notes = "商品(套餐和非套餐)详情页面")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "commodity_id", value = "商品id", required = true, dataType = "String", paramType = "query")
	])
	def commodityInfo(HttpServletRequest request){
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
				"_id" : 1  , "name" : 1 , 
//				"price" : 1 , "monthprice":1, "reader" : 1 , 
				"course_ids" : 1 , "titles" : 1,
				"learntime" : 1 , "classmode" : 1, "app_photo" : 1 ,
				"commodity_type_list" : 1 ,"content_app" : 1 , "type" : 1,
				"try_video" : 1
				);
		//商品
		def commodity = commoditys().findOne(query , show);
		
		if(commodity){
			
			String _id = commodity["_id"];
//			//商品所属校区和价格
//			commodity["school_price"] = redisController.get_commodity_school_price(_id);
			//课次
			commodity["classtime"] = commoditysController.getCommodityClassTimeByList(commodity["course_ids"]);
			
			
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
									"_id" : 1 , "nc_course_id" : 1 , "show_name" : 1 ,
									"is_online" :1 
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
								course_row["line_name"] = course["is_online"] == 0 ? "面授":"直播";
							}
						}
					}
				}
			}else{
				commodity["commodity_type_list"]= [];
			}
			
		}
		return getResultOK(commodity);
	}
	
	/**
	 * 获取商品价格列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "commodityPriceList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取商品价格列表", httpMethod = "GET", response = AHCommoditysCommodityPriceListVO.class ,notes = "根据商品id获取商品价格列表")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "commodity_id", value = "商品id", required = true, dataType = "String", paramType = "query")
	])
	def commodityPriceList(HttpServletRequest request){
		String commodity_id = request["commodity_id"];
		//校验飞空
		if(StringUtils.isBlank(commodity_id)){
			return getResultParamsError();
		}
		//校验商品为上架商品
		String c_id = commoditys().findOne($$("_id" : commodity_id , "is_shelves" : 1) , $$("_id" : 1))?.get("_id");
		if(StringUtils.isBlank(c_id)){
			return getResultParamsError();
		}
		//商品价格数组
		def priceList = redisController.get_commodity_school_price_Array(c_id);
		return getResultOK(priceList);
	}
	
	/**
	 * 购买第二部页面数据-包含价格页面
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "commodityConfirmPrice", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "购买第二部页面数据-包含价格页面", httpMethod = "GET", response = AHCommoditysCommodityConfirmPriceVO.class ,notes = "购买第二部页面数据-包含价格页面")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "commodity_id", value = "商品id", required = true, dataType = "String", paramType = "query")
	])
	def commodityConfirmPrice(HttpServletRequest request){
		
		String commodity_id = request["commodity_id"];
		if(StringUtils.isBlank(commodity_id)){
			return getResultParamsError();
		}
		
		Map rMap = new HashMap();
		//查询条件 已上架 套餐商品
		BasicDBObject query = commoditysQuery();
		query.append("_id" , commodity_id);
		
		//查询字段
		def show = $$(
			"_id" : 1  , "name" : 1 , "titles" : 1,
//			"price" : 1,"monthprice":1, 
			"reader" : 1 , "course_ids" : 1 ,
			"learntime" : 1 , "classmode" : 1  ,"app_photo" : 1, 
			"type" : 1 , "short_name" : 1
			);
		//排序
		def sort = $$("order" : 1);
		//商品
		def commodity = commoditys().findOne(query , show);
		if(commodity){
			//商品類型
			Integer comType = commodity["type"];
			//套餐商品
			if(comType == CommodityType.会计上岗.ordinal()){
				
				BasicDBObject pageQuery = commoditysQuery();
				pageQuery.append("type", CommodityType.会计上岗.ordinal());
				//商品列表
				def pageCommodityList = commoditys().find(pageQuery , show).sort(sort)?.toArray();
				if(pageCommodityList){
					pageCommodityList.each {def row->
						String _id = row["_id"];
						//商品所属校区和价格
						row["school_price"] = redisController.get_commodity_school_price_Array(_id);
						
						//课次
						row["classtime"] = commoditysController.getCommodityClassTimeByList(row["course_ids"]);
						
					}
					rMap["commodityList"] = pageCommodityList;
				}
				
				rMap["ctype"] = 1;
			}else{
				//商品id	
				String _id = commodity["_id"];
				//商品所属校区和价格
				commodity["school_price"] = redisController.get_commodity_school_price_Array(_id);
				
				//课次
				commodity["classtime"] = commoditysController.getCommodityClassTimeByList(commodity["course_ids"]);
					
				//結果集
				rMap["ctype"] = 2;
				
				//將商品組裝成數組
				List mList = new ArrayList();
				mList.add(commodity);
				rMap["commodityList"] = mList;
					
			}
			
		
		}
		return getResultOK(rMap);
	}
	
	
}
