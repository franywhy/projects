


package com.izhubo.web.otherside

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.izhubo.rest.common.util.MsgDigestUtil;

import com.izhubo.web.BaseController;
import com.izhubo.web.CommoditysController;
import com.mongodb.DBCollection;









import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import static com.izhubo.rest.common.util.WebUtils.$$
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody;








import org.apache.commons.lang3.StringUtils
import org.json.JSONArray
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.web.model.CommodityType
import com.izhubo.web.vo.DailyRecommandVO
import com.izhubo.web.vo.DailyRecommandListVO
import com.izhubo.web.vo.BaseResultVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.izhubo.common.util.KeyUtils.LIVE;
import com.izhubo.model.DailyRecommendType;
import static com.izhubo.rest.common.doc.MongoKey.$set;

import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiParam

import static com.izhubo.rest.common.util.WebUtils.$$

/**
 * 每日推送相关内容 恶意访问，ip，设备被禁
 * @date 2016年3月9日 下午5:42:49
 * @param @param request
 */
@Controller
@RequestMapping("/OtherSideApi")
class OtherSideApiController extends BaseController {
	
	/** 每日推荐数据库 */
	public DBCollection dailypush_recommend() {
		return mainMongo.getCollection("daily_recommend");
	}
	
	/** 直播預約情況 */
	public DBCollection live() {
		return mainMongo.getCollection("live_reservation");
	}
	
	private static final String AESKEY = "I87HDCcnMgkwcul09GxX";
	
	
	private static Logger logger = LoggerFactory
	.getLogger(OtherSideApiController.class);
	

	
	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "sync_live_data", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "同步直播接口", httpMethod = "POST",  notes = "同步直播接口", response = BaseResultVO.class)
	def sync_live_data(
		@ApiParam(required = true, name = "live_id", value = "直播唯一id")@RequestParam(value = "live_id") String live_id,
		@ApiParam(required = true, name = "live_title", value = "直播标题")@RequestParam(value = "live_title") String live_title,
		@ApiParam(required = true, name = "live_info", value = "直播情況說明：格式為  老師名稱 3月30日 19:00~21:00 ")@RequestParam(value = "live_info") String live_info,
		@ApiParam(required = true, name = "live_teacher", value = "直播教师名称")@RequestParam(value = "live_teacher") String live_teacher,
		@ApiParam(required = true, name = "live_infourl", value = "直播需要跳转的url")@RequestParam(value = "live_infourl") String live_infourl,
		@ApiParam(required = true, name = "live_starttime", value = "直播开始时间 yyyy-mm-dd HH:mm:ss")@RequestParam(value = "live_starttime") String live_starttime,
		@ApiParam(required = true, name = "live_endtime", value = "直播结束时间 yyyy-mm-dd HH:mm:ss")@RequestParam(value = "live_endtime") String live_endtime,
		@ApiParam(required = true, name = "live_picurl", value = "直播图片url")@RequestParam(value = "live_picurl") String live_picurl,
		@ApiParam(required = true, name = "skey", value = "md5验签  （live_id+约定秘钥）")@RequestParam(value = "skey") String skey,
		HttpServletRequest request
		){
		//TODO 待测试 消息返回结构VO


		
		String md5str = live_id+AESKEY;
		String MYMD5Result = MsgDigestUtil.MD5.digest2HEX(md5str);	
		logger.info("md5result:" + MYMD5Result);
		if (!skey.toLowerCase().equals(MYMD5Result.toLowerCase())) {
	
			return getResult("-1","验签失败");
		}
			
		
		BasicDBObject insert = new BasicDBObject();
		insert.append("_id", live_id);
		insert.append("live_id", live_id);
		insert.append("recommend_info", live_info);
		insert.append("recommend_title", live_title);
		insert.append("recommend_infourl",  live_infourl);
		insert.append("recommend_live_starttime", live_starttime);
		insert.append("recommend_live_endtime", live_endtime);
		insert.append("recommend_picurl", live_picurl);
		insert.append("recommend_time", System.currentTimeMillis());
		insert.append("recommend_type", DailyRecommendType.直播.ordinal());
		

		
		long cou = dailypush_recommend().count($$(["_id" : live_id,"live_id" : live_id]));
		if(cou>0)
		{
	
			dailypush_recommend().update($$("_id",live_id),new BasicDBObject($set, insert) );
			
			return getResult("1","更新成功");
		}
		else
		{
			insert.append("is_recommend",0);
		   dailypush_recommend().save( insert);	
		
			return getResult("1","新增成功");
		}
	}
		
		
		/**
		 * 会答首页-每日推送api
		 * @date 2016年3月9日 下午5:42:49
		 * @param @param request
		 */
		@ResponseBody
		@RequestMapping(value = "sync_live_user", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
		@ApiOperation(value = "同步预约情况接口", httpMethod = "POST",  notes = "同步预约情况接口", response = BaseResultVO.class)
		def sync_live_user(
			@ApiParam(required = true, name = "live_id", value = "直播唯一id")@RequestParam(value = "live_id") String live_id,
			@ApiParam(required = true, name = "live_user", value = "直播预约的用户名")@RequestParam(value = "live_user") String live_user,
			@ApiParam(required = true, name = "skey", value = "md5验签  （live_id+约定秘钥）")@RequestParam(value = "skey") String skey,
			HttpServletRequest request
			){
			//TODO 待测试 消息返回结构VO
	
	
			
			String md5str = live_id+AESKEY;
			String MYMD5Result = MsgDigestUtil.MD5.digest2HEX(md5str);
			logger.info("md5result:" + MYMD5Result);
			if (!skey.toLowerCase().equals(MYMD5Result.toLowerCase())) {
		
				return getResult("-1","验签失败");
			}
				
			long cou = live().count($$(["live_id" : live_id,"live_user" : live_user]));
			if(cou>0)
			{
	
				return getResult("1","已经预约");
			}
			else
			{
				BasicDBObject insert = new BasicDBObject();
				
				insert.append("live_id", live_id);
				insert.append("user_name", live_user);
				insert.append("create_time", System.currentTimeMillis());
				
				live().insert(insert);
				return getResult("1","预约成功");
			}
		}
		
    def insertUserList(String userlist,String live_id)
	{
		JSONArray ja = new JSONArray(userlist);
		
		

		 for(int i=0;i<ja.length();i++)
		 {
			 def item  = ja.get(i);
			 
			 BasicDBObject insert = new BasicDBObject();
			 
			 insert.append("live_id", live_id);
			 insert.append("user_name", item);
			 insert.append("create_time", System.currentTimeMillis());
			 
			 live().insert(insert);
			  
		 }
	}
	
	
	
	public static void main(String[] args) throws Exception {
		    
		        String md5str = "test"+"I87HDCcnMgkwcul09GxX";
				String key = MsgDigestUtil.MD5.digest2HEX(md5str);
				System.out.println("cal " + key);
				
		//		 String result = MsgDigestUtil.MD5.MD5ForDotNet("nc13507719687Jey3mwywG9BnLm5IBdCwbw==大区2广西省南宁市JH209020401南宁西大校区10001A51000000004GQCY%^$AF>.12*******");
		//		 String resuls = MsgDigestUtil.MD5.MD5ForDotNet("nc13507719687Jey3mwywG9BnLm5IBdCwbw==大区2广西省南宁市JH209020401南宁西大校区10001A51000000004GQCY%^$AF>.12*******");
		//		 String resul4 = MsgDigestUtil.MD5.digest2HEX("nc13507719687Jey3mwywG9BnLm5IBdCwbw==大区2广西省南宁市JH209020401南宁西大校区10001A51000000004GQCY%^$AF>.12*******");
		//		 System.out.println(result);
		//		 System.out.println(resul4);
				// XingeApp xinge = new XingeApp(2200149294L,
				// "55b252ee247f1c9b2e642ac8cc9eec61");
				// System.out.println( xinge.queryTokensOfAccount("10037953"));
				
				// XingeApp xinge = new XingeApp(2200149294L,
						// "55b252ee247f1c9b2e642ac8cc9eec61");
				// xinge.deleteAllTokensOfAccount("10010943");
			
						// System.out.println( xinge.deleteAllTokensOfAccount("10010943"));
		
				// System.out.println(xinge.deleteAllTokensOfAccount("10037953") );;
				// System.out.println(xinge.deleteAllTokensOfAccount("10013089") );;
		
			}

		
		
		
				
				
				
				
				
				
				
			
	
	
}
