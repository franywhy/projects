package com.izhubo.web.potentialstudent


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;


import java.text.SimpleDateFormat
import java.util.HashMap;
import java.util.Map;
import java.util.Formatter.DateTime;
import java.util.regex.Pattern.End;

import com.izhubo.web.BaseController;
import com.izhubo.web.CommoditysController
import com.izhubo.web.DiscountController
import com.mongodb.DBCollection;







import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody;







import org.apache.commons.lang3.StringUtils
import org.hibernate.SessionFactory;
import org.springframework.aop.TrueClassFilter;
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.web.model.CommodityType
import com.izhubo.web.mq.MessageProductor
import com.izhubo.web.score.ScoreBase;
import com.izhubo.web.vo.DailyRecommandVO
import com.izhubo.web.vo.DailyRecommandListVO
import com.izhubo.web.vo.BaseResultVO
import com.izhubo.web.vo.AHCommoditysCommodityConfirmPriceVO.City;
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mysqldb.model.IbSchool
import com.rabbitmq.client.AMQP.Connection.Start;
import com.sun.mail.util.logging.MailHandler.DefaultAuthenticator;
import com.izhubo.common.util.KeyUtils.LIVE;
import com.izhubo.model.CheckStatus;
import com.izhubo.model.Code;
import com.izhubo.model.BusinessOpportunityType;
import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiParam
import com.izhubo.model.AccScoreGainType;
import com.izhubo.web.score.ScoreBase;
import com.izhubo.rest.common.util.RandomUtil
import com.izhubo.rest.web.data.Map2View;

import org.hibernate.SessionFactory
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.json.JSONObject
import com.wordnik.swagger.annotations.ApiOperation







import com.wordnik.swagger.annotations.ApiParam
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation

import static com.izhubo.rest.common.util.WebUtils.$$


@Controller
@RequestMapping("/businessopportunity")
public class BusinessOpportunityController extends BaseController {
	
	@Resource
	private SessionFactory sessionFactory;
	
	@Resource
	private DiscountController discountController;
	
	private String defaultshoolcode = "0001A510000000000KY0";
	
	
	private String trylistenUrl = "http://p.bokecc.com/flash/player.swf?vid=00A6B9D6EED7067F9C33DC5901307461&siteid=FE7A65E6BE2EA539&playerid=CED4B0511C5D4992&playertype=1&autoStart=auto";
	

	/** 短信验证码题头 */
	private static final String MESSAGE_KEY = "message:";

	/** 短信验证码存放时间 */
	private static final String SECURITY_KEY = "security:";
	
	public static String getSecurityKey(String key){
		return MESSAGE_KEY + SECURITY_KEY + key;
	}
	
	private DBCollection business_opportunity_logs(){
		return mainMongo.getCollection("business_opportunity_logs");
	}
	

	//Preferential
	
	
	
	@ResponseBody
	@RequestMapping(value = "reviews", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "性格评测", httpMethod = "POST",response = BaseResultVO.class,  notes = "获取评测结果")
	@ApiImplicitParams([@ApiImplicitParam(name = "yes_count", value = "问题回答是的数量", required = false, dataType = "int", paramType = "query"),
						@ApiImplicitParam(name = "phone", value = "手机号", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "security", value = "验证码", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "channel", value = "推广渠道来源", required = false, dataType = "String", paramType = "query")])
	def reviews(HttpServletRequest request){

		int yes_count  = ServletRequestUtils.getIntParameter(request, "yes_count", 0);
		String phone = ServletRequestUtils.getStringParameter(request, "phone");
		String security =  ServletRequestUtils.getStringParameter(request, "security");
		String channel =  ServletRequestUtils.getStringParameter(request, "channel");
		
		if(channel==null)
		{
			channel = "0";
		}
		
	

		
		//先判断验证码
		String result = "";
		
		boolean res = false;
		if (StringUtils.isNotBlank(security) && StringUtils.isNotBlank(phone)) {
			String rs = mainRedis.opsForValue().get(
					getSecurityKey(phone));
			res = security.equals(rs);
		}
	
		
		if(true){
			
			if(yes_count>=0&&yes_count<=2)
			{
				result = "感谢您的参与！本次结果：您在财会领域的发展潜力有很大的挖掘空间，也许目前有一点不确定，但是多了解会.计专业会激发您的兴趣，加深认识您加入会.计领域发展的意愿也会越来越强烈，希望在这个行业获得安定的生活和良好的发展前景";
			}
			else if(yes_count>=3&&yes_count<=5)
			{
				result = "感谢您的参与！本次结果：您在财会领域有一定的发展潜力，但还需要系统的训练与积累。您有在会.计领域发展的主观意愿，希望在这个行业获得安定的生活与良好的发展前景，如果能注重性格上的磨练，会更有利于会.计职业的发展";
			}
			else if(yes_count>=6&&yes_count<=8)
			{
				result = "感谢您的参与！本次结果：您在财会领域有很大的发展潜力，只要努力与坚持，您定会成为一名优秀的会.计。您对待工作严谨认真，情绪稳定而成熟，具有较强的自恃其力，通过不懈的努力，您定能在财会领域崭露头角";
			}
			else if(yes_count>=9&&yes_count<=11)
			{
				result = "感谢您的参与！本次结果：您天生就是做会.计的料。您温和、内敛、严谨又敏感，您不张扬，不夸张，能着重面对现实，情绪稳定而成熟，追求细节的完美，对数字天生敏感，善于分析数据。从事财会工作能很快崭露头角，并能在财会领域做出一番事业";
			}
			else
			{
				result = "测试数据不足，请重新测试";
			}
			
			
			
			////记录商机
			
			def query = $$("yes_count":yes_count);
			query.append("phone", phone);
			query.append("channel", channel);
			query.append("test_result", result);
			query.append("type", BusinessOpportunityType.性格评测.ordinal());
			query.append("type_name", BusinessOpportunityType.性格评测.name());
			query.append("create_time", System.currentTimeMillis());
			
			
			JSONObject jsonrequest = new JSONObject();
			
			
			jsonrequest.put("UserId", RandomUtil.getTuid());
			jsonrequest.put("ShoolCode", defaultshoolcode);
			jsonrequest.put("Name", "未知");
			jsonrequest.put("Phone", phone);
			jsonrequest.put("QQ", "未知");
			jsonrequest.put("CityCode", "");
			jsonrequest.put("Email", "");
			jsonrequest.put("topchannels", channel);
			jsonrequest.put("zsqd", "恒企在线官网");
			jsonrequest.put("hslx", BusinessOpportunityType.性格评测.name());
			
	
	
			//messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_students", jsonrequest.toString());
	
			business_opportunity_logs().save(query);
			
			 return getResultOK(result);
			
			
			
		}
		else
		{
			return getResult(0,"验证码不正确");
		}
		
		

	
		
	}
	
	

	
	
	@ResponseBody
	@RequestMapping(value = "enroll", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "报名商机入口 ", httpMethod = "POST",response = BaseResultVO.class,  notes = "报名商机入口")
    @ApiImplicitParams([
						@ApiImplicitParam(name = "schoolcode", value = "校区编码", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "phone", value = "手机", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "qq", value = "qq", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "sex", value = "性别 0男 1女", required = false, dataType = "int", paramType = "query"),
						@ApiImplicitParam(name = "security", value = "验证码", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "channel", value = "推广渠道来源", required = false, dataType = "String", paramType = "query")])
	def enroll(HttpServletRequest request){
		
		
		String schoolcode = ServletRequestUtils.getStringParameter(request, "schoolcode");
		String name = ServletRequestUtils.getStringParameter(request, "name");
		String phone = ServletRequestUtils.getStringParameter(request, "phone");
		int sex  = ServletRequestUtils.getIntParameter(request, "sex", 0);
		String qq  = ServletRequestUtils.getStringParameter(request, "qq");
		String security =  ServletRequestUtils.getStringParameter(request, "security");
		String channel =  ServletRequestUtils.getStringParameter(request, "channel");
		
		if(channel==null)
		{
			channel = "0";
		}
		
		
		////记录商机
		
	
		
//		{
//			"UserId": "20位字母数字",
//			"Phone": "radni@163.com",
//			"Name": "杨瑞年",
//			"ShoolCode": "pk",
//			"CityCode": "黄石市",
//			"QQ": "13826095803",
//			"Email": "radni@163.com",
//			"topchannels":"",
//			"zsqd":"恒企在线"，
//			"hslx":"恒企在线注册"
//		
//		}
		
		
		
		boolean res = false;
		if (StringUtils.isNotBlank(security) && StringUtils.isNotBlank(phone)) {
			String rs = mainRedis.opsForValue().get(
					getSecurityKey(phone));
			res = security.equals(rs);
		}
		
		if(true)
		{
			def query = $$("schoolcode":schoolcode);
			query.append("name", name);
			query.append("phone", phone);
			query.append("qq", qq);
			query.append("sex", sex);
			query.append("channel", channel);
			query.append("type", BusinessOpportunityType.报名.ordinal());
			query.append("type_name", BusinessOpportunityType.报名.name());
			query.append("create_time", System.currentTimeMillis());
			
			
			JSONObject jsonrequest = new JSONObject();
			jsonrequest.put("UserId", RandomUtil.getTuid());
			jsonrequest.put("ShoolCode", schoolcode);
			jsonrequest.put("Name", name);
			jsonrequest.put("Phone", phone);
			jsonrequest.put("QQ", qq);	
			jsonrequest.put("CityCode", "");
			jsonrequest.put("Email", "");
			jsonrequest.put("topchannels", channel);
			jsonrequest.put("zsqd", "恒企在线官网");
			jsonrequest.put("hslx", BusinessOpportunityType.报名.name());
	
			//messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_students", jsonrequest.toString());
	
			business_opportunity_logs().save(query);
			
			return getResultOK();
		}

	
		
	return getResult(0,"验证码不正确");
					
					
					

	
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "card", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "优惠券商机入口 ", httpMethod = "POST",response = BaseResultVO.class,  notes = "优惠券入口")
	@ApiImplicitParams([
						@ApiImplicitParam(name = "schoolcode", value = "校区编码", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "phone", value = "手机", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "security", value = "验证码", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "channel", value = "推广渠道来源", required = false, dataType = "String", paramType = "query")])
	def card(HttpServletRequest request){
		
		
		String schoolcode = ServletRequestUtils.getStringParameter(request, "schoolcode");
		String name = ServletRequestUtils.getStringParameter(request, "name");
		String phone = ServletRequestUtils.getStringParameter(request, "phone");
		String security =  ServletRequestUtils.getStringParameter(request, "security");
		String channel =  ServletRequestUtils.getStringParameter(request, "channel");
		
		if(channel==null)
		{
			channel = "0";
		}
		
		

		
		
		
		boolean res = false;
		if (StringUtils.isNotBlank(security) && StringUtils.isNotBlank(phone)) {
			String rs = mainRedis.opsForValue().get(
					getSecurityKey(phone));
			res = security.equals(rs);
		}
		
		if(true)
		{
			def query = $$("schoolcode":defaultshoolcode);
			query.append("name", name);
			query.append("phone", phone);
			query.append("qq", "");
			query.append("sex", 0);
			query.append("channel", channel);
			query.append("type", BusinessOpportunityType.获取优惠券.ordinal());
			query.append("type_name", BusinessOpportunityType.获取优惠券.name());
			query.append("create_time", System.currentTimeMillis());
			
			
			JSONObject jsonrequest = new JSONObject();
			jsonrequest.put("UserId", RandomUtil.getTuid());
			jsonrequest.put("ShoolCode", defaultshoolcode);
			jsonrequest.put("Name", name);
			jsonrequest.put("Phone", phone);
			jsonrequest.put("QQ", "");
			jsonrequest.put("CityCode", "");
			jsonrequest.put("Email", "");
			jsonrequest.put("topchannels", channel);
			jsonrequest.put("zsqd", "恒企在线官网");
			jsonrequest.put("hslx", BusinessOpportunityType.获取优惠券.name());
	
			//messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_students", jsonrequest.toString());
			
			
			//这里获取下优惠券
			
			Integer discount_id = discountController.getSysDiscountId("A0002");
			
			Map reulst = discountController.getDiscountDaysLimit(phone, discount_id);
	
			business_opportunity_logs().save(query);
			
			return reulst;
		}

	
		
	return getResult(0,"验证码不正确");
					
					
					

	
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "callme", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "来电商机入口 ", httpMethod = "POST",response = BaseResultVO.class,  notes = "优惠券入口")
	@ApiImplicitParams([
						@ApiImplicitParam(name = "schoolcode", value = "校区编码", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "phone", value = "手机", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "text", value = "方便接听电话的时间", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "channel", value = "推广渠道来源", required = false, dataType = "String", paramType = "query")])
	def callme(HttpServletRequest request){
		
		
		String schoolcode = ServletRequestUtils.getStringParameter(request, "schoolcode");
		String name = ServletRequestUtils.getStringParameter(request, "name");
		String phone = ServletRequestUtils.getStringParameter(request, "phone");
		String text =  ServletRequestUtils.getStringParameter(request, "text");
		String channel =  ServletRequestUtils.getStringParameter(request, "channel");
		
		if(channel==null)
		{
			channel = "0";
		}
		
		

		
		
	
		if(true)
		{
			def query = $$("schoolcode":schoolcode);
			query.append("name", name);
			query.append("phone", phone);
			query.append("qq", "");
			query.append("sex", 0);
			query.append("text", text);
			query.append("channel", channel);
			query.append("type", BusinessOpportunityType.来电.ordinal());
			query.append("type_name", BusinessOpportunityType.来电.name());
			query.append("create_time", System.currentTimeMillis());
			
			
			JSONObject jsonrequest = new JSONObject();
			jsonrequest.put("UserId", RandomUtil.getTuid());
			jsonrequest.put("ShoolCode", schoolcode);
			jsonrequest.put("Name", name);
			jsonrequest.put("Phone", phone);
			jsonrequest.put("QQ", "");
			jsonrequest.put("CityCode", "");
			jsonrequest.put("Email", "");
			jsonrequest.put("topchannels", channel);
			jsonrequest.put("zsqd", "恒企在线官网");
			jsonrequest.put("remarks", text);
			jsonrequest.put("hslx", BusinessOpportunityType.来电.name());
	
			//messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_students", jsonrequest.toString());
			
			
			//这里获取下优惠券
			return getResultOK();
		
		}

	
		
	return getResult(0,"验证码不正确");
					
					
					

	
		
	}
	
	@ResponseBody
	@RequestMapping(value = "try_listen", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "试听入口 ", httpMethod = "POST",response = BaseResultVO.class,  notes = "试听入口")
	@ApiImplicitParams([@ApiImplicitParam(name = "schoolcode", value = "校区编码", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "listen_type", value = "试听类型 0线下 1线上", required = false, dataType = "int", paramType = "query"),
						@ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "phone", value = "手机", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "channel", value = "推广渠道来源", required = false, dataType = "String", paramType = "query"),
						@ApiImplicitParam(name = "security", value = "验证码", required = false, dataType = "String", paramType = "query")])
	def try_listen(HttpServletRequest request){
		
		
		String schoolcode = ServletRequestUtils.getStringParameter(request, "schoolcode");
		String name = ServletRequestUtils.getStringParameter(request, "name");
		String phone = ServletRequestUtils.getStringParameter(request, "phone");
		int listen_type  = ServletRequestUtils.getIntParameter(request, "listen_type", 0);
		String security =  ServletRequestUtils.getStringParameter(request, "security");
		String channel =  ServletRequestUtils.getStringParameter(request, "channel");
		
		if(channel==null)
		{
			channel = "0";
		}
		
		
		////记录商机
		
	
		
		
//		
//		boolean res = false;
//		if (StringUtils.isNotBlank(security) && StringUtils.isNotBlank(phone)) {
//			String rs = mainRedis.opsForValue().get(
//					getSecurityKey(phone));
//			res = security.equals(rs);
//		}
		
		if(true)
		{
			def query = $$("province":schoolcode);
			query.append("name", name);
			query.append("phone", phone);
			query.append("channel", channel);
			query.append("listen_type", listen_type);
			query.append("type", BusinessOpportunityType.试听.ordinal());
			query.append("type_name", BusinessOpportunityType.试听.name());
			query.append("create_time", System.currentTimeMillis());
			
			JSONObject jsonrequest = new JSONObject();

			
			jsonrequest.put("UserId", RandomUtil.getTuid());
			jsonrequest.put("ShoolCode", schoolcode);
			jsonrequest.put("Name", name);
			jsonrequest.put("Phone", phone);
			jsonrequest.put("QQ", "");
			jsonrequest.put("CityCode", "");
			jsonrequest.put("Email", "");
			jsonrequest.put("topchannels", channel);
			jsonrequest.put("zsqd", "恒企在线官网");
			jsonrequest.put("hslx", BusinessOpportunityType.试听.name());
	
			//messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_students", jsonrequest.toString());
			
			business_opportunity_logs().save(query);
			
			return getResultOK(trylistenUrl);
		}

	
		
	return getResult(0,"验证码不正确");
					
					
					

	
		
	}
	
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		
			
	}
}

