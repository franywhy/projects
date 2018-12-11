package com.izhubo.web

import static com.izhubo.rest.common.util.WebUtils.$$

import java.util.concurrent.TimeUnit

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

import com.izhubo.common.util.KeyUtils
import com.izhubo.rest.common.util.JSONUtil
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

@Controller
@RequestMapping("/redis")
class RedisController extends BaseController {
	
	/** 商品所属校区 */
	public DBCollection commodity_schools() {
		return mainMongo.getCollection("commodity_schools");
	}
	
	/** 商品价格 */
	public DBCollection commodity_prices() {
		return mainMongo.getCollection("commodity_prices");
	}
	
	@Resource
	private CommoditysController commoditysController;
	
	/**
	 * 获取商品价格列表
	 * @Description: 默认从redis中获取。如果热redis中没有，则缓存如redis。
	 * @date 2016年3月10日 下午5:04:14 
	 * @param @param commodity_id 商品id
	 */
	def get_commodity_school_price_Array(String commodity_id){
		List list = null;
		
		if(StringUtils.isNotBlank(commodity_id)){
			def valOp = mainRedis.opsForValue();
			
			//redis key
			String key = KeyUtils.COMMODITYS.priceHash(commodity_id);
			String json = valOp.get(key);
			
			if(StringUtils.isNotBlank(json)){
				list = JSONUtil.jsonToBean(json, ArrayList.class);
			}else{
				list = update_commodity_school_price(commodity_id);
			}
		}
		return list;
	}
	
	/**
	 * 获取商品价格列表
	 * @Description: 默认从redis中获取。如果热redis中没有，则缓存如redis。
	 * @date 2016年3月10日 下午5:04:14
	 * @param @param commodity_id 商品id
	 */
	def get_commodity_school_price_String(String commodity_id){
		String resultJson = null;
		
		if(StringUtils.isNotBlank(commodity_id)){
			def valOp = mainRedis.opsForValue();
			
			//redis key
			String key = KeyUtils.COMMODITYS.priceHash(commodity_id);
			resultJson = valOp.get(key);
			
			if(StringUtils.isBlank(resultJson)){
				resultJson =net.sf.json.JSONArray.fromObject(update_commodity_school_price(commodity_id)).toString();
			}
		}
		return resultJson;
	}
	
	/**
	 * 同步商品价格
	 * @Description: 同步商品价格
	 * @date 2016年3月10日 上午11:47:29 
	 * @param @param commodity_id 商品id
	 */
	def update_commodity_school_price(String commodity_id){
//		//商品对应的NCid
//		String nc_commodity_id = commoditys.findOne($$("_id" : commodity_id), $$("nc_id" : 1))?.get("nc_id");
		
		//商品所属校区列表
		def school_pks_arrays = commodity_schools().findOne($$("commodity_id" : commodity_id) , $$("school_pks" : 1))?.get("school_pks");
//		def commodity_schools_list = commodity_schools().find($$("commodity_id" : commodity_id)).sort($$("school_code" : 1)).limit(MAX_LIMIT)?.toArray();
		
		//市-Map集合 去重
		Map citys = new HashMap();
		
		if(school_pks_arrays){
			school_pks_arrays.each {String nc_school_pk ->
				Map item = new HashMap();
//				//校区编码
//				String school_code = row["school_code"];
				item["school_code"] = nc_school_pk ;
				item["nc_id"] = nc_school_pk ;
				//校区价格
				item["normal_price"] = commoditysController.find_commodity_area_price(commodity_id, nc_school_pk);
				
				def school_area = area().findOne($$("nc_id" : nc_school_pk) , $$("name" : 1 , "parent_nc_id" : 1 ));
				if(school_area){
					//校区名称
					item["school_name"] = school_area.get("name");
					
					//通过校区的NCid找到公司 通过公司的parent_nc_id 找到市的id
					//市的NCid
					String city_ncid = area().findOne($$("nc_id" : school_area.get("parent_nc_id")) , $$( "parent_nc_id" : 1 ))?.get("parent_nc_id");
					
					//根据市的编码存
					Map city = citys.get(city_ncid);
					//如果市的资料存在 保存该市下的校区信息
					if(city){
						city.get("school").add(item);
					}else{//如果市的资料没有查询过
						city = new HashMap();
						
						//市-name
						//String city_name = area().findOne($$("nc_id" : city_ncid) , $$("name" : 1 , "parent_nc_id" : 1))?.get("name");
						def carea = area().findOne($$("nc_id" : city_ncid) , $$("name" : 1 , "parent_nc_id" : 1));
						//市-name
						String city_name = carea.get("name");
						//市编号
						city.put("code", city_ncid);
						city.put("nc_id", city_ncid);
						//省ncid
						city.put("province_id", carea.get("parent_nc_id"));
						//市名称
						city.put("name", city_name);
						List schoolList = new ArrayList();
						schoolList.add(item);
						//市校区
						city.put("school", schoolList);
						
						citys.put(city_ncid, city);
					}
				}
				
			}
		}
		//市 map->list
		List cityList = new ArrayList(citys.values());
		
		//省Map 去重
		Map provinces = new HashMap();
		
		if(cityList){
			cityList.each{Map row->
				//省nc_id
				String province_id = row.get("province_id");
				//省集合
				Map province = provinces.get(province_id);
				//如果省资料存在
				if(province){
					//补充市的资料
					province.get("city").add(row);
				}else{//如果省的资料没有
					province = new HashMap();
					//省名称
//					String province_name = area().findOne($$("code" : province_code) , $$("name" : 1 ))?.get("name");
					String province_name = area().findOne($$("nc_id" : province_id) , $$("name" : 1 ))?.get("name");
					//省nc_id
					province.put("code", province_id);
					//省nc_id
					province.put("nc_id", province_id);
					//省name
					province.put("name", province_name);
					List cList = new ArrayList();
					cList.add(row);
					//省下的市
					province.put("city", cList);
					
					provinces.put(province_id, province);
				}
			}
		}
		
		//商品所属校区及价格
		List provincesList = new ArrayList(provinces.values());
		
		//List->json
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(provincesList);
		String provinces_json = jsonArray.toString();
		
		//redis key
		String key = KeyUtils.COMMODITYS.priceHash(commodity_id);
		
		def valOp = mainRedis.opsForValue();
		//TODO redis save
		valOp.set(key, provinces_json , KeyUtils.COMMODITYS.COMMODITY_SCHOOL_PRICE_TIME , TimeUnit.MINUTES);
		
		return provincesList;
	}
	
	
	private String getAreaNcId(String parent_nc_id){
		
	}
	
}
