package com.izhubo.web.server

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import com.izhubo.web.api.Web
import org.hibernate.SessionFactory
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.web.BaseController
import com.izhubo.web.vo.MyScoreDetailListVO
import com.mongodb.DBCollection
import com.mysqldb.model.ScoreDetail
import com.mysqldb.model.UserScore
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation


/**
 * 用户信息
* @ClassName: UserController
* @Description: TODO(这里用一句话描述这个类的作用)
* @author shihongjie
* @date 2016年3月16日 上午9:47:16
*
 */
@RestWithSession 
@RequestMapping("/score")
class ScoreController extends BaseController {


	
	@Resource
	private SessionFactory sessionFactory;
	
	/**
	 * 获取用户积分余额
	 * @param user_id
	 * @return
	 */
	public Double myScore(Integer user_id){
		Double rd = 0;
		UserScore uScore = (UserScore) sessionFactory.getCurrentSession()
							.createCriteria(UserScore.class)
							.add(Restrictions.eq(UserScore.PROP_USERID , user_id))
							.uniqueResult();
		if(uScore){
			rd = uScore.getUserScoreRemain().toDouble();
		}
		return rd;
	}
	
	/**
	 * 我的订单
	 * @Description: 我的订单
	 * @date 2016年3月16日 上午10:48:22
	 */
	
	@ResponseBody
	@RequestMapping(value = "scorelist/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "我的积分", httpMethod = "GET",  notes = "积分明细",response = MyScoreDetailListVO.class)
	@ApiImplicitParams([@ApiImplicitParam(name = "size", value = "User's email", required = false, dataType = "long", paramType = "query"),
		               @ApiImplicitParam(name = "page", value = "User's email", required = false, dataType = "long", paramType = "query")])
	@TypeChecked(TypeCheckingMode.SKIP)
	def scorelist(
		HttpServletRequest request){
          		Integer user_id = Web.getCurrentUserId();

				  int size = ServletRequestUtils.getIntParameter(request , "size" , 15);
				  int page = ServletRequestUtils.getIntParameter(request , "page" , 1);
		//我的积分列表
		List orderList = sessionFactory
					.getCurrentSession().createCriteria(ScoreDetail.class)
					.add(Restrictions.eq(ScoreDetail.PROP_USERID , user_id))
					.addOrder(Order.desc(ScoreDetail.PROP_CREATETIME))
					.setFirstResult((page-1)*size)
					.setMaxResults(size)
					.list();
					
					
					
					int count = (int)sessionFactory
					.getCurrentSession().createCriteria(ScoreDetail.class)
					.setProjection(Projections.count(ScoreDetail.PROP_ID))
					.add(Restrictions.eq(ScoreDetail.PROP_USERID, user_id))
					.uniqueResult();
	
					
					int allpage = count / size + count% size >0 ? 1 : 0;
					
					return getResultOK(orderList, allpage, count , page , size);
					//return getResultOK();
				
	
	}
		
    
		
   
		
		

}
