package com.izhubo.web.score;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;

import com.izhubo.rest.persistent.KGS;
import com.izhubo.rest.common.util.QRcode;


import com.duiba.helper.CreditConsumeParams
import com.duiba.helper.CreditConsumeResult
import com.duiba.helper.CreditNotifyParams
import com.duiba.helper.CreditTool
import com.duiba.helper.SignTool
import com.izhubo.web.BaseController;
import com.izhubo.model.AccScoreGainType;
import com.izhubo.model.AccScoreType;














import static com.izhubo.rest.common.util.WebUtils.$$

import java.sql.Timestamp
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap.PrivateEntryIterator;

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Restrictions
import org.hibernate.criterion.Projections
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody









import com.izhubo.model.Code
import com.izhubo.model.DR
import com.izhubo.model.OrderPayStatus
import com.izhubo.model.OrderPayType
import com.izhubo.model.OrderPreferenceScheme
import com.izhubo.model.OrderStatus
import com.izhubo.model.OrdersPayOnLineType
import com.izhubo.model.DuibaCreditsStatus
import com.izhubo.model.DuibaOrderStatus


import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.persistent.KGS
import com.izhubo.web.api.Web
import com.mongodb.DBCollection
import com.mysqldb.model.Discount
import com.mysqldb.model.DiscountUser
import com.mysqldb.model.DuibaOrders
import com.mysqldb.model.ScoreDetail
import com.mysqldb.model.UserScore


import com.rabbitmq.client.AMQP.Basic.Return;
import com.sun.mail.util.logging.MailHandler.DefaultAuthenticator;
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation

/**
 * 积分订单
* @ClassName: ScoreApiController 
* @Description: 订单 
* @author zhaokum
* @date 2016年3月17日 上午11:11:46 
*
 */
@Controller
public class ScoreApiController extends BaseController{

	
	@Resource
	private SessionFactory sessionFactory;
	

	/** 支付宝单号补齐位 */
	@Value("#{application['sorceorders.head']}")
	private String sorceorders_head;
	
	private String duiba_key = "VzURhzdgkYLbcgULHnfHo2eFimT";
	
	private String duiba_appSecret = "SoMK46Nv4ww7BLq6V5mun1Y5awj";
	
	private String autoLoginUrl = "http://www.duiba.com.cn/autoLogin/autologin";
	
	@Resource
	KGS scoreOrderKGS;
	
	

	
	public Map getDuibaResultFail( String errorMessage , double credits){
		
		
		Map map = new HashMap();
		map.put("status", "fail");
		map.put("errorMessage", errorMessage);
		map.put("credits", credits);
		return map;
	}
	
	def duiba_autologin(HttpServletRequest request,HttpServletResponse response)
	{
		CreditTool tool=new CreditTool(duiba_key, duiba_appSecret);
		
	    String access_token = request.getParameter("access_token"); 
		String redirect = request.getParameter("redirect");
		Map params=new HashMap();
		Integer user_id =Integer.valueOf(mainRedis.opsForHash().get("token:"+access_token,"_id") == null?0:mainRedis.opsForHash().get("token:"+access_token,"_id"));
		
		String url=BuildUrl(user_id,redirect);
		
		
		return getResultOK(url);
		
		
	}
	
	//生成兑吧二维码
	def duiba_autologin_qr(HttpServletRequest request,HttpServletResponse response)
	{
		String access_token = request.getParameter("access_token");
		String redirect = request.getParameter("redirect");
		
		Integer user_id =Integer.valueOf(mainRedis.opsForHash().get("token:"+access_token,"_id") == null?0:mainRedis.opsForHash().get("token:"+access_token,"_id"));
		String url=BuildUrl(user_id,redirect);
		
		response.addHeader('Content-Type',"image/png")
		QRcode.PrintQR2Steam(url, response.getOutputStream());
		
		
	}
	
	private String BuildUrl(Integer user_id,String redirect)
	{
		CreditTool tool=new CreditTool(duiba_key, duiba_appSecret);
		Map params=new HashMap();
		if(user_id>0)
		{
			UserScore uscore = (UserScore)sessionFactory.getCurrentSession().createCriteria(UserScore.class)
			.add(Restrictions.eq(UserScore.PROP_USERID, user_id))	//用户id
			.uniqueResult();
			
			if(uscore!=null)
			{
				params.put("uid",user_id.toString());
				params.put("credits",uscore.getUserScoreRemain().intValue().toString());
			}
			else
			{
				params.put("uid",user_id.toString());
				params.put("credits","0");
			}
			
		 
		}
		else
		{
			params.put("uid","not_login");
			params.put("credits","0");
		}
		
		if(redirect!=null){
			//redirect是目标页面地址，默认积分商城首页是：http://www.duiba.com.cn/chome/index
			//此处请设置成一个外部传进来的参数，方便运营灵活配置
			params.put("redirect",redirect);
		}
		String url=tool.buildUrlWithSign("http://www.duiba.com.cn/autoLogin/autologin?",params);
		
		return url;
	}
	
	
	def async_score(HttpServletRequest request,HttpServletResponse response)
	{
		CreditTool tool=new CreditTool(duiba_key, duiba_appSecret);
		
		try {
			
			
			CreditNotifyParams params= tool.parseCreditNotify(request);//利用tool来解析这个请求
			String duiba_orderNum = params.getOrderNum();
			
			
			if(!duiba_key.equals(params.getAppKey())){
				throw new Exception("appKey not match");
			}
			if(params.getTimestamp() == null ){
				throw new Exception("timestamp can't be null");
			}
			
			SignTool signTool = new SignTool();
			
			Boolean verify = signTool.signVerify(duiba_appSecret, request)
			
			if(!verify){
				throw new Exception("sign verify fail");
			 }
			
			
			DuibaOrders duibaorder = (DuibaOrders)sessionFactory.getCurrentSession().createCriteria(DuibaOrders.class)
			.add(Restrictions.eq(DuibaOrders.PROP_DUIBAORDERNUM, duiba_orderNum))	//用户id
			.uniqueResult();
			
			if(duibaorder!=null)
			{
				if(duibaorder.orderStatus == DuibaOrderStatus.创建.ordinal())
				{
				  
					if(params.isSuccess()){
						
						//只有创建的订单，才是能够确认的，否则，当重复订单处理。
						duibaorder.orderStatus = DuibaOrderStatus.成功.ordinal();
						duibaorder.setCreditsStatus(DuibaCreditsStatus.成功.ordinal());
						sessionFactory.getCurrentSession().update(duibaorder);
						sessionFactory.getCurrentSession().flush();
						
					//兑换成功
					
					}else{
					   
					sessionFactory.getCurrentSession().beginTransaction();
					
					
					duibaorder.orderStatus = DuibaOrderStatus.失败.ordinal();
					duibaorder.setCreditsStatus(DuibaCreditsStatus.返还.ordinal());
					sessionFactory.getCurrentSession().update(duibaorder);
					
					ScoreDetail scoredetail = new ScoreDetail();
					
					Double result = duibaorder.getCredits()/100;
					
					java.sql.Timestamp dateTimesql = new java.sql.Timestamp(new Date());
		
					
					scoredetail.setScoreDetail(AccScoreGainType.兑吧消费失败积分退还.name());
					scoredetail.setScoreType(AccScoreType.默认无限次赠送.ordinal());
					scoredetail.setScoreGainType(AccScoreGainType.兑吧消费失败积分退还.ordinal());
					scoredetail.setCreateTime(dateTimesql);
					scoredetail.setUserId(duibaorder.getUserId());
					scoredetail.setUserNickname(duibaorder.getUserId());
					scoredetail.setScore(new java.math.BigDecimal(result));
					
					sessionFactory.getCurrentSession().save(scoredetail);
					
					
					UserScore uscore = (UserScore)sessionFactory.getCurrentSession().createCriteria(UserScore.class)
					.add(Restrictions.eq(UserScore.PROP_USERID, duibaorder.getUserId()))	//用户id
					.uniqueResult();
					
					uscore.setUserScoreRemain(uscore.getUserScoreRemain() +result )
					sessionFactory.getCurrentSession().update(uscore);
					sessionFactory.getCurrentSession().getTransaction().commit();
					sessionFactory.getCurrentSession().flush();
					
					
					}
				}
				//重复订单处理。
				else
				{
					
					//啥也不做
				}
			}
			
		
			
			
			response.getWriter().write("ok");
		}
	     catch (Exception e) {
	// TODO Auto-generated catch block
	    return getDuibaResultFail("系统异常，扣减失败，请联系客服",0);
     }
		 
		 
	}
	
	
	
	
	def build_score_order(HttpServletRequest request,HttpServletResponse response){
	
	
	
		

		
		
		CreditTool tool=new CreditTool(duiba_key, duiba_appSecret);
		
		try {
			
			CreditConsumeParams params= tool.parseCreditConsume(request);//利用tool来解析这个请求
			
			Integer user_id = Integer.valueOf(params.getUid());//用户id
			double credits=params.getCredits();
			String type=params.getType();//获取兑换类型
			String alipay=params.getAlipay();//获取支付宝账号
			String duiba_orderNum = params.getOrderNum();
			
			println params.getAppKey();
		
			if(duiba_key !=params.getAppKey()){
				throw new Exception("appKey not match");
			}
			if(params.getTimestamp() == null ){
				throw new Exception("timestamp can't be null");
			}
			
			SignTool signTool = new SignTool();
			
			Boolean verify = signTool.signVerify(duiba_appSecret, request)
			
			if(!verify){
				throw new Exception("sign verify fail");
			 }
			
		

			
			

			//其他参数参见 params的属性字段
		
			//TODO 开发者系统对uid用户扣除credits个积分
		
			String bizId= sorceorders_head + String.valueOf(scoreOrderKGS.nextId());//返回开发者系统中的业务订单id
			
			
			Double userScore = 0;
			
			// 如果订单存在，则禁止这次交易
			
			int count = (int)sessionFactory
			.getCurrentSession().createCriteria(DuibaOrders.class)
			.setProjection(Projections.count(DuibaOrders.PROP_ID))
			.add(Restrictions.eq(DuibaOrders.PROP_DUIBAORDERNUM, duiba_orderNum))
			.uniqueResult();
			
			
			UserScore ufe = (UserScore)sessionFactory.getCurrentSession().createCriteria(UserScore.class)
			.add(Restrictions.eq(UserScore.PROP_USERID, user_id))	//用户id
			.uniqueResult();
				
			 if(ufe != null){
				  userScore = ufe.getUserScoreRemain();
			 }
			 else
			 {
				 userScore = 0;
//				 UserScore newUS = new UserScore();
//				 newUS.setUserId(user_id);
//				 newUS.setUserScoreRemain(0);
//				 sessionFactory.getCurrentSession().save(newUS);
				 
			 }
			if(count>0)
			{
				return getDuibaResultFail("兑吧订单号存在，无法完成交易,请联系客服",userScore);
			}
			if( credits>userScore)
			{
				return getDuibaResultFail("积分余额不足",userScore);
			}
			
			
			
//			app_id： 需同时维护多个应用时作为区分。
//			order_status：订单状态（创建、成功、失败）。
//			credits_status：积分状态（预扣、成功、返还）。
			
			
			try {
				sessionFactory.getCurrentSession().beginTransaction();
				//如果订单存在，则开始进行交易
				DuibaOrders duibaOrders = new DuibaOrders();
				duibaOrders.setActualPrice(params.getActualPrice());
				duibaOrders.setAppId(100000);
				duibaOrders.setCredits(params.getCredits());
				duibaOrders.setCreditsStatus(DuibaCreditsStatus.预扣.ordinal());
				duibaOrders.setDescription(params.getDescription());
				duibaOrders.setDuibaOrderNum(params.getOrderNum());
				duibaOrders.setOrderNum(bizId);
				
				java.sql.Timestamp dateTimesqlduiba = new java.sql.Timestamp(params.getTimestamp().getTime());
				duibaOrders.setGmtCreate(dateTimesqlduiba);
				
				java.sql.Timestamp dateTimesql_duiba_update = new java.sql.Timestamp(System.currentTimeMillis());
				duibaOrders.setGmtModified(dateTimesql_duiba_update);
				duibaOrders.setOrderStatus(DuibaOrderStatus.创建.ordinal());
				duibaOrders.setType(params.getType());
				duibaOrders.setUserId(user_id);
				
				sessionFactory.getCurrentSession().save(duibaOrders);
				
				
				
				ScoreDetail scoredetail = new ScoreDetail();
				
				java.sql.Timestamp dateTimesql = new java.sql.Timestamp(new Date().time);
	
				
				scoredetail.setScoreDetail(AccScoreGainType.在兑吧消费.name());
				scoredetail.setScoreType(AccScoreType.消耗积分.ordinal());
				scoredetail.setScoreGainType(AccScoreGainType.在兑吧消费.ordinal());
				scoredetail.setCreateTime(dateTimesql);
				scoredetail.setUserId(user_id);
				scoredetail.setUserNickname(user_id.toString());
				scoredetail.setScore(new java.math.BigDecimal(credits));
				
				sessionFactory.getCurrentSession().save(scoredetail);
				
				
				UserScore uscore = (UserScore)sessionFactory.getCurrentSession().createCriteria(UserScore.class)
				.add(Restrictions.eq(UserScore.PROP_USERID, user_id))	//用户id
				.uniqueResult();
				
				uscore.setUserScoreRemain(uscore.getUserScoreRemain() -credits )
				sessionFactory.getCurrentSession().update(uscore);
				sessionFactory.getCurrentSession().getTransaction().commit();
				sessionFactory.getCurrentSession().flush();
				
				
				CreditConsumeResult result=new CreditConsumeResult(true);
				
				result.setBizId(bizId);
				
				
				response.getWriter().write(result.toString());
				
				
			} catch (Exception e) {

				sessionFactory.getCurrentSession().getTransaction().rollback();

				return getDuibaResultFail("系统异常，扣减失败，请联系客服",userScore);
			}

			
				
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
	     	return getDuibaResultFail("系统异常，扣减失败，请联系客服",0);
		}
		
		
		
	
	}
	
}
