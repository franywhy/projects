package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.connection.RedisConnection; 
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource; 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import io.renren.dao.GivingCoursesDao;
import io.renren.entity.GivingCoursesEntity;
import io.renren.entity.MallGoodsInfoEntity;
import io.renren.pojo.GivingCoursesPOJO;
import io.renren.service.GivingCoursesService;
import io.renren.service.MallGoodsInfoService;
import io.renren.utils.Constant;
import io.renren.utils.JSONUtil;



@Service("givingCoursesService")
public class GivingCoursesServiceImpl implements GivingCoursesService {
	@Autowired
	private GivingCoursesDao givingCoursesDao; 
	/** redis的key  组合班型的key **/
	private static String GROUPGOODS = "order:groupGoods:";

	@Resource
    public StringRedisTemplate mainRedis;
 
	@Override
	public GivingCoursesEntity queryObject(Map<String, Object> map){
		return givingCoursesDao.queryObject(map);
	}
	
	@Override
	public List<GivingCoursesEntity> queryList(Map<String, Object> map){
		return givingCoursesDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return givingCoursesDao.queryTotal(map);
	}
	
	@Override
	public void save(GivingCoursesEntity givingCourses){
		givingCoursesDao.save(givingCourses);
	}
	
	@Override
	public void update(GivingCoursesEntity givingCourses){
		givingCoursesDao.update(givingCourses);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		givingCoursesDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		givingCoursesDao.deleteBatch(map);
	}

	@Override
	public List<GivingCoursesPOJO> queryPojoList(Map<String, Object> map) {
		return this.givingCoursesDao.queryPojoList(map);
	}
	
	@Override
	public Map<String,String> SetGroupGoodToRedis( ) {
		
		
		
		Map<String,String> back= new HashMap<String,String>();
		Map<String, Object> queryMap= new HashMap<String,Object>();
		List<Integer> givingTypeList= new ArrayList<Integer>();
		givingTypeList.add(1);  //只查询来源为1 即为组合班型的
		queryMap.put("givingTypeList", givingTypeList);
		queryMap.put("hasDr", "1");//只要这个不为空的话则全取 包括dr=1的
		List<GivingCoursesPOJO> giveList=this.givingCoursesDao.queryPojoList(queryMap);
		
		
		
		
		    if (giveList!=null&&giveList.size()>0) {
		    	
		    	Map<String,Map<String,Integer>> goodMap=new HashMap<String,Map<String,Integer>>();
		    	for ( GivingCoursesPOJO g:giveList) {
		    		//当是普通赠送类型的不会组合 
		    		if (g.getGivingType()==null||g.getGivingType()==0||g.getMallGoodsId()==null||g.getNcCommodityId()==null) {
		    			continue;
		    		}
		    		Map<String,Integer> subMap= goodMap.get(g.getNcCommodityId())==null?new HashMap<String,Integer>():goodMap.get(g.getNcCommodityId());
		    		
		    		//判断dr，只要有一个为0 则为0，即取最小值
		    		int N_dr= g.getDr()==null?1:g.getDr();  //当前的
		    		
		    	 
		    		
		    		if (N_dr==0) { //如果当前的为0的 则直接赋值 
		    			subMap.put(String.valueOf( g.getMallGoodsId()), 0  );	//加上是否删除的 dr=1为删除 的	 
		    		}else { //当前的不为0 则要判断 之前的是否为0 如果以前是0的则现在的也为0 
		    			if (subMap.get(String.valueOf( g.getMallGoodsId()) )!=null&&subMap.get(String.valueOf( g.getMallGoodsId()) )==0) {
		    				subMap.put(String.valueOf( g.getMallGoodsId()),0 );
		    			}else {//如果旧的不是0则是1的了 那么这些还是为1
		    				subMap.put(String.valueOf( g.getMallGoodsId()),1 );
		    			}
		    			
		    		}
		    		
		    		
		    		
		    		
		    		   		
		    		 
		    		 goodMap.put(g.getNcCommodityId(), subMap);
					 
		    	}
		    	String key=GROUPGOODS+"list:";
		    	 if (goodMap!=null&&goodMap.size()>0) {
		    		 for(Entry<String, Map<String, Integer>> entry:goodMap.entrySet()) {
		    			 if (entry.getValue()!=null&&entry.getValue().size()>0) {
		    				 
		    				String value=JSONUtil.objToGson(entry.getValue());
		    				 this.mainRedis.opsForValue().set(key+entry.getKey(), value, 1, TimeUnit.DAYS);
		    				/*List<String> vlaueList = new ArrayList(entry.getValue().keySet());
		    				for (String str:vlaueList) {
		    				 this.mainRedis.opsForList().leftPush(key+entry.getKey(), str);
		    				 this.mainRedis.boundListOps(key+entry.getKey()).expire(1, TimeUnit.DAYS);
		    				}*/
		    				 
		    			 }
		    			 this.mainRedis.opsForList().leftPush(GROUPGOODS+"allKeys",entry.getKey() );
		    		 }
		    		 
		    		 this.mainRedis.boundListOps(GROUPGOODS+"allKeys").expire(1, TimeUnit.DAYS);
		    	 }
		    	
		    	
		    	
		    }
		 
			
		  //  this.mainRedis.opsForValue().set(GROUPGOODS+"lastTime",String.valueOf(  new Date().getTime()),  86400000);
		return back;
	}
	/**
	 * 是否为组合班型的
	 * @return true 是 false 否
	 *@param ncId
	 *@return
	 * @author lintf
	 * 2018年9月25日
	 */
	@Override
	public boolean checkNcCommodity(String ncId) {
		
		List<Long> list=this.getGroupGoodFromRedis(ncId,false);
		if (list!=null&&list.size()>0) {
			return true;
		}
		return false;
	}
	@Override
	public boolean DeleteGroupGoodFromRedis( ) {
		try {
			
			 List<String> range1 = this.mainRedis.opsForList().range(GROUPGOODS+"allKeys", 0, -1);
		        for (String ncId : range1) {
		        	
		        	mainRedis.delete(GROUPGOODS+"list:"+ncId );	
		        }

			
		mainRedis.delete(GROUPGOODS+"allKeys");	
		
	 	
		}catch(Exception es) {
			
			es.printStackTrace();
			return false;
		}
		return true;  
	}
	
	

	@Override
	public List<Long> getGroupGoodFromRedis(String ncId,boolean hasDr) {
	 
		List<Long> back= new ArrayList<Long>();
		if (!mainRedis.hasKey(GROUPGOODS+"allKeys")) {
			Map<String, String> cacheMap = SetGroupGoodToRedis();
		 
		}
		
		if (mainRedis.hasKey(GROUPGOODS+"allKeys")&&mainRedis.hasKey(GROUPGOODS+"list:"+ncId)) {
			 
			/* List<String> range1 = this.mainRedis.opsForList().range(GROUPGOODS+"list:"+ncId, 0, -1);
		        for (String str : range1) {
		        	back.add(Long.valueOf(str));
		        }*/

	     	String value=	this.mainRedis.opsForValue().get(GROUPGOODS+"list:"+ncId);
			
					Map<String, Object> map = JSONUtil.jsonToMap(value);
					
					for (Entry<String, Object> entry:map.entrySet()) {
						if (entry.getValue()!=null) {
							int dr= Integer.parseInt( entry.getValue().toString());
							if (hasDr||dr==0) {
								back.add(Long.valueOf(entry.getKey()));
							}
						}
					}
			
 
		      }
			return back;
			
 
		}
	
	 
	
}
