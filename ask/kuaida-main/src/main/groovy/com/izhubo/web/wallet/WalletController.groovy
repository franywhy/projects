package com.izhubo.web.wallet;


import static com.izhubo.rest.common.util.WebUtils.$$

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.hibernate.Query
import org.hibernate.SessionFactory
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody



import com.izhubo.web.api.Web
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.Double;
import com.izhubo.model.Code;
import com.izhubo.model.DiscountIsUser;
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.data.Map2View;
import com.izhubo.rest.web.spring.InterceptorAnnotationAwareClassNameHandlerMapping;
import com.izhubo.util.JSONUtil;
import com.izhubo.web.BaseController
import com.izhubo.web.vo.WalletInfoVO
import com.izhubo.web.vo.WalletInfoVO.WalletInfoVOData
import com.izhubo.web.vo.UserWalletDetailListVO


import com.mongodb.DBCollection
import com.mysql.jdbc.BalanceStrategy;
import com.mysqldb.model.UserFinanceDetail
import com.mysqldb.model.UserFinance
import com.mysqldb.model.UserScore;
import com.mysqldb.model.DiscountUser;
import com.mysqldb.model.UserWallet
import com.mysqldb.model.UserWalletDetail
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation


/**
 * 钱包api
* @ClassName: UserController
* @Description: TODO(这里用一句话描述这个类的作用)
* @author 赵琨
* @date 2016年4月12日 上午9:47:16
*
 */
@RestWithSession
@RequestMapping("/wallet")
class WalletController extends BaseController {


	
	@Resource
	private SessionFactory sessionFactory;

	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String formate(){
		return sdf.format(new Date());
	}
	private Date Dateformate(){
		
		Date date = sdf.parse(formate());
		return   date ;
	}
	private java.sql.Date SqlDateformate(){
		
		java.sql.Date d = new java.sql.Date(System.currentTimeMillis());
		return d ;
	}
	
	public Map getResultOKDefine(Object data , Integer all_page , Long count , Integer page ,Integer size,double allmoney){
		Map map = new HashMap();
		map.put("code", Code.OK);
		map.put("msg", Code.OK_S);
		map.put("data", data);
		map.put("all_page", all_page);
		map.put("count", count);
		map.put("page", page);
		map.put("size", size);
		map.put("allmoney", allmoney ==null?0:allmoney);
		return map;
	}
	
	/**
	 * 我的订单
	 * @Description: 我的订单
	 * @date 2016年3月16日 上午10:48:22
	 */
	
	@ResponseBody
	@RequestMapping(value = "walletinfo/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "我的钱包信息", httpMethod = "GET",  notes = "我的钱包信息",response = WalletInfoVOData.class)
	@TypeChecked(TypeCheckingMode.SKIP)
	def walletinfo(
		HttpServletRequest request){
				  Integer user_id = Web.getCurrentUserId();

		        com.izhubo.web.vo.WalletInfoVO.WalletInfoVOData w  = new WalletInfoVOData();

		//返現餘額
		UserFinance ufdetail = sessionFactory
					.getCurrentSession().createCriteria(UserFinance.class)
					.add(Restrictions.eq(UserFinance.PROP_USERID , user_id))
					.uniqueResult();
					
					
					
					
		UserWallet userWallet  = sessionFactory
					.getCurrentSession().createCriteria(UserWallet.class)
					.add(Restrictions.eq(UserFinanceDetail.PROP_USERID, user_id))
					.uniqueResult();
					
					
					//Integer user_id = Web.getCurrentUserId();
					//用户基本信息
					def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "tuid" : 1));
					
					String username = qquserMongo.getCollection("qQUser").findOne($$("tuid" : user.get("tuid"))).get("username");
					
					String queryString = " select count(duser) from DiscountUser duser   where duser.isUse =  ? and duser.phone=? and ? between duser.discountStartTime and duser.discountEndTime";
					
					//int count = (int)).createCriteria(DiscountUser.class)
					
					
					int count = (int)sessionFactory
					.getCurrentSession().createQuery(queryString)
					.setParameter(0, DiscountIsUser.未使用.ordinal())
					.setParameter(1, username)
//					.setParameter(2, formate())
					.setParameter(2, SqlDateformate())
					.uniqueResult();
					
				
					double cash = (ufdetail == null?0:(double)ufdetail.getUserMoney());
					double balance = (userWallet == null?0:(double)userWallet.getUserBalance());
	
			
					Map map = new HashMap();
					map.put("wallet_balance", balance);
					map.put("wallet_total", cash);
					map.put("coupon_count", count);
		
				
					
					return getResultOK(map);
	
	}
		
	
		/**
		 * 我的钱包明细
		 * @Description: 我的订单
		 * @date 2016年3月16日 上午10:48:22
		 */
		
		@ResponseBody
		@RequestMapping(value = "balance_list/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
		@ApiOperation(value = "我的钱包明细", httpMethod = "GET",  notes = "我的钱包明细",response =UserWalletDetailListVO.class)
		@ApiImplicitParams([@ApiImplicitParam(name = "size", value = "页大小", required = false, dataType = "long", paramType = "query"),
			                @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "long", paramType = "query"),
							@ApiImplicitParam(name = "type", value = "0:收入 1:支出", required = false, dataType = "int", paramType = "query")])
		@TypeChecked(TypeCheckingMode.SKIP)
		def balance_list(
			HttpServletRequest request){
					  Integer user_id = Web.getCurrentUserId();
					  
					  
				      int type = ServletRequestUtils.getIntParameter(request , "type" , 0);
					  int size = ServletRequestUtils.getIntParameter(request , "size" , 15);
					  int page = ServletRequestUtils.getIntParameter(request , "page" , 1);
					  
					BigDecimal sum = 0;
					
					
					 if(type ==0)
					 {
					
			    	   sum=	(BigDecimal)sessionFactory
							.getCurrentSession().createCriteria(UserWalletDetail.class)
							.setProjection(Projections.sum(UserWalletDetail.PROP_MMONEY))
							.add(Restrictions.eq(UserWalletDetail.PROP_USERID, user_id))
							.add(Restrictions.gt(UserWalletDetail.PROP_MMONEY, 0))
							.uniqueResult();
					 }
					 else
					 {
						 sum=	(BigDecimal)sessionFactory
						 .getCurrentSession().createCriteria(UserWalletDetail.class)
						 .setProjection(Projections.sum(UserWalletDetail.PROP_MMONEY))
						 .add(Restrictions.eq(UserWalletDetail.PROP_USERID, user_id))
						 .add(Restrictions.lt(UserWalletDetail.PROP_MMONEY, 0))
						 .uniqueResult();
					 }
							
					if(sum==null)
					{
						sum = 0;
					}					
					  if(type ==0)
					  {
						
						List userFinanceList = sessionFactory
						.getCurrentSession().createCriteria(UserWalletDetail.class)
						.add(Restrictions.eq(UserWalletDetail.PROP_USERID , user_id))
						.add(Restrictions.gt(UserWalletDetail.PROP_MMONEY , new java.math.BigDecimal(0)))
						.setFirstResult((page-1)*size)
						.setMaxResults(size)
						.list();
						
						int count = (int)sessionFactory
						.getCurrentSession().createCriteria(UserWalletDetail.class)
						.setProjection(Projections.count(UserWalletDetail.PROP_ID))
						.add(Restrictions.eq(UserWalletDetail.PROP_USERID, user_id))
						.add(Restrictions.gt(UserWalletDetail.PROP_MMONEY , new java.math.BigDecimal(0)))
						.uniqueResult();
						
						int allpage = count / size + count% size >0 ? 1 : 0;
						
						return getResultOKDefine(userFinanceList, allpage, count , page , size,sum);
					  }
					  else
					  {
						  List userFinanceList = sessionFactory
						  .getCurrentSession().createCriteria(UserWalletDetail.class)
						  .add(Restrictions.eq(UserWalletDetail.PROP_USERID , user_id))
						  .add(Restrictions.lt(UserWalletDetail.PROP_MMONEY , new java.math.BigDecimal(0)))
						  .setFirstResult((page-1)*size)
						  .setMaxResults(size)
						  .list();
						  
						  int count = (int)sessionFactory
						  .getCurrentSession().createCriteria(UserWalletDetail.class)
						  .setProjection(Projections.count(UserWalletDetail.PROP_ID))
						  .add(Restrictions.eq(UserWalletDetail.PROP_USERID, user_id))
						  .add(Restrictions.lt(UserWalletDetail.PROP_MMONEY , new java.math.BigDecimal(0)))
						  .uniqueResult();
						  
						  int allpage = count / size + count% size >0 ? 1 : 0;
						  
						  return getResultOKDefine(userFinanceList, allpage, count , page , size,sum);
					  }
						
				
		
		}
			
			/**
			 * 我的收益明细
			 * @Description: 我的订单
			 * @date 2016年3月16日 上午10:48:22
			 */
			
			@ResponseBody
			@RequestMapping(value = "cash_list/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
			@ApiOperation(value = "我的返现列表", httpMethod = "GET",  notes = "我的收益列表",response =UserWalletDetailListVO.class)
			@ApiImplicitParams([@ApiImplicitParam(name = "size", value = "页大小", required = false, dataType = "long", paramType = "query"),
				                @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "long", paramType = "query")])
			@TypeChecked(TypeCheckingMode.SKIP)
			def cash_list(
				HttpServletRequest request){
						  Integer user_id = Web.getCurrentUserId();
						  
						  
					
						  int size = ServletRequestUtils.getIntParameter(request , "size" , 15);
						  int page = ServletRequestUtils.getIntParameter(request , "page" , 1);
						  
						  
						  UserFinance ufdetail = sessionFactory
						  .getCurrentSession().createCriteria(UserFinance.class)
						  .add(Restrictions.eq(UserFinance.PROP_USERID , user_id))
						  .uniqueResult();
							
							List userFinanceList = sessionFactory
							.getCurrentSession().createCriteria(UserFinanceDetail.class)
							.add(Restrictions.eq(UserFinanceDetail.PROP_USERID , user_id))
							.setFirstResult((page-1)*size)
							.setMaxResults(size)
							.list();
							
							int count = (int)sessionFactory
							.getCurrentSession().createCriteria(UserFinanceDetail.class)
							.setProjection(Projections.count(UserFinanceDetail.PROP_ID))
							.add(Restrictions.eq(UserFinanceDetail.PROP_USERID, user_id))
							.uniqueResult();
							
							BigDecimal sum = (BigDecimal)sessionFactory
							.getCurrentSession().createCriteria(UserFinanceDetail.class)
							.setProjection(Projections.sum(UserFinanceDetail.PROP_MMONEY))
							.add(Restrictions.eq(UserFinanceDetail.PROP_USERID, user_id))
							.uniqueResult();
							
							if(sum==null)
							{
								sum = 0;
							}
							
							int allpage = count / size + count% size >0 ? 1 : 0;
							
							return getResultOKDefine(userFinanceList, allpage, count , page , size,sum);
							
					
			
			}
		
   
		
		

}
