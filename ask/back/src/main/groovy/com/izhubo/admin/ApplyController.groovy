package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.common.util.HttpUtils
import com.izhubo.model.ApplyState
import com.izhubo.rest.anno.RestWithSession
import com.mysqldb.model.Apply
import com.mysqldb.model.UserFinance

@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class ApplyController extends BaseController {
	@Resource
	private SessionFactory sessionFactory;
	
	private static Logger logger = LoggerFactory.getLogger(ApplyController.class);
	
	def list(HttpServletRequest request){
		//账单状态
		Integer applyState = ServletRequestUtils.getIntParameter(request, "applyState");
		//申请人id
		Integer userId = ServletRequestUtils.getIntParameter(request, "userId");
		//昵称
		String nickName = request[ "nickName"];
		//真实姓名
		String realName = request[ "realName"];
		
		String alipayAccount = request[ "alipayAccount"];
		
		//修改时间
		Date update_stime = Web.getTime(request , "update_stime");
		Date update_etime = Web.getTime(request , "update_etime");
		//创建时间
		Date stime = Web.getStime(request);
		Date etime = Web.getEtime(request);
		
		//翻页查询
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		//List数据
		Criteria criterion = sessionFactory.getCurrentSession().createCriteria(Apply.class);
		//总条数-分页
		Criteria criterion_count = sessionFactory.getCurrentSession().createCriteria(Apply.class).setProjection(Projections.count(Apply.PROP_ID));
		//总金额-汇总
		Criteria criterion_sum = sessionFactory.getCurrentSession().createCriteria(Apply.class).setProjection(Projections.sum(Apply.PROP_APPLYMONEY));
		//拼接条件
		
		if(userId != null){
			criterion.add(Restrictions.eq(Apply.PROP_USERID , userId));
			criterion_count.add(Restrictions.eq(Apply.PROP_USERID , userId));
			criterion_sum.add(Restrictions.eq(Apply.PROP_USERID , userId));
		}
		
		if(applyState !=null){
			criterion.add(Restrictions.eq(Apply.PROP_APPLYSTATE , applyState));
			criterion_count.add(Restrictions.eq(Apply.PROP_APPLYSTATE , applyState));
			criterion_sum.add(Restrictions.eq(Apply.PROP_APPLYSTATE , applyState));
		}
		
		if(StringUtils.isNotBlank(nickName)){
			criterion.add(Restrictions.eq(Apply.PROP_NICKNAME , nickName));
			criterion_count.add(Restrictions.eq(Apply.PROP_NICKNAME , nickName));
			criterion_sum.add(Restrictions.eq(Apply.PROP_NICKNAME , nickName));
		}
		if(StringUtils.isNotBlank(realName)){
			criterion.add(Restrictions.eq(Apply.PROP_REALNAME , realName));
			criterion_count.add(Restrictions.eq(Apply.PROP_REALNAME , realName));
			criterion_sum.add(Restrictions.eq(Apply.PROP_REALNAME , realName));
		}
		if(StringUtils.isNotBlank(alipayAccount)){
			criterion.add(Restrictions.eq(Apply.PROP_ALIPAYACCOUNT , alipayAccount));
			criterion_count.add(Restrictions.eq(Apply.PROP_ALIPAYACCOUNT , alipayAccount));
			criterion_sum.add(Restrictions.eq(Apply.PROP_ALIPAYACCOUNT , alipayAccount));
		}
		if(stime != null){
			criterion.add(Restrictions.ge(Apply.PROP_CREATETIME , stime));
			criterion_count.add(Restrictions.ge(Apply.PROP_CREATETIME , stime));
			criterion_sum.add(Restrictions.ge(Apply.PROP_CREATETIME , stime));
		}
		if(etime != null){
			criterion.add(Restrictions.le(Apply.PROP_CREATETIME , etime));
			criterion_count.add(Restrictions.le(Apply.PROP_CREATETIME , etime));
			criterion_sum.add(Restrictions.le(Apply.PROP_CREATETIME , etime));
		}
		if(update_stime != null){
			criterion.add(Restrictions.ge(Apply.PROP_UPDATETIME , update_stime));
			criterion_count.add(Restrictions.ge(Apply.PROP_UPDATETIME , update_stime));
			criterion_sum.add(Restrictions.ge(Apply.PROP_UPDATETIME , update_stime));
		}
		if(update_etime != null){
			criterion.add(Restrictions.le(Apply.PROP_UPDATETIME , update_etime));
			criterion_count.add(Restrictions.le(Apply.PROP_UPDATETIME , update_etime));
			criterion_sum.add(Restrictions.le(Apply.PROP_UPDATETIME , update_etime));
		}


		def applyList = criterion.addOrder(Order.desc(Apply.PROP_CREATETIME)).setFirstResult((page - 1) * size).setMaxResults(size).list();


		int apply_count = (Integer) criterion_count.uniqueResult();

		int allpage = apply_count / size + apply_count% size >0 ? 1 : 0;
		
		//当前页合计
		Double page_sum_money = 0.00d;
		if(applyList){
			applyList.each {Apply apply_item->
				page_sum_money = sum(page_sum_money, apply_item.getApplyMoney());
			}
		}
		//TODO
		Double sum_money = (Double) criterion_sum.uniqueResult();
		
		if(sum_money == null){
			sum_money = 0.00d;
		}
		
		
		Map map = new HashMap();
		map["code"]  = 1;
		map["data"]  = applyList;
		map["all_page"]  = allpage;
		map["count"]  = apply_count;
		//当前页合计
		map["page_sum_money"]  = page_sum_money + "";
		//总合计
		map["sum_money"]  = sum_money  + "";
		return map;
	}
	
	def cancle(HttpServletRequest request){
		int id = ServletRequestUtils.getIntParameter(request, "id", -1);
		if(id > 0){
			//开启事务
			Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();
			Session session = sessionFactory.getCurrentSession();
			//申请单
//			Apply apply = (Apply) session.get(Apply.class, id);
			Apply apply = (Apply) session.createCriteria(Apply)
						.add(Restrictions.eq(Apply.PROP_ID, id))
						.add(Restrictions.eq(Apply.PROP_APPLYSTATE, ApplyState.已申请.ordinal()))
						.uniqueResult();
			if(apply){
				//失败
				apply.setApplyState(ApplyState.已作废.ordinal());
				//操作人信息
				Map user = (Map)request.getSession().getAttribute("user");
				//失败原因
				apply.setErrMemo("申请已撤销![" + new Date().toString()+ "-" +  user["_id"] + "-" + user["name"]+"]" )
				//申请人id
				Integer apply_user_id =  apply.getUserId();
				//申请人余额
				UserFinance uf = (UserFinance) session.createCriteria(UserFinance)
						.add(Restrictions.eq(UserFinance.PROP_USERID, apply.getUserId()))
						.uniqueResult();
				
				if(uf){
					def ahurl = mainMongo.getCollection("ah_url").findOne($$("_id" : 1025))
					float ratio
					if(ahurl) {
						ratio = ahurl.get("url") as float
					}
					//申请金额
					String applyMoney = ((apply.getApplyMoney()) / (1-ratio)).toString();
					//声请人账户余额
					uf.setUserMoney(uf.getUserMoney().add(new BigDecimal(applyMoney)));
					
					//更新申请表
					session.update(apply);
					//更新申请人账户余额
					session.update(uf);
					
					//获取申请人手机号码
					String tuid = mainMongo.getCollection("users").findOne($$("_id" : apply_user_id) , $$("tuid" : 1))?.get("tuid");
					String mobile = qquserMongo.getCollection("qQUser").findOne($$("tuid" : tuid) , $$("username" : 1)).get("username");
					
					//短信内容
					String content = "【温馨提示】尊敬的恒企老师，由于您在会答APP提现时填写的支付宝账号有误，导致提现失败，申请提现的红包已打回会答账户，请核对后再提现！";
					//发送短信
					Map<String , String> parameter = new HashMap<String , String>();
					parameter.put("username", "hengqi_kuaid");
					parameter.put("userpwd", "686868");
					parameter.put("mobiles", mobile);
					parameter.put("content", content);
					parameter.put("mobilecount", "1");
					String sendRes = HttpUtils.sendGet("http://sms.ue35.net/sms/interface/sendmess.htm", parameter);
					
					logger.info("apply cancle id:{} , apply_user_id:{} ,applyMoney:{}, user:{}" , id ,apply_user_id,applyMoney, user);
				}
								
			}
			//事务提交
			transaction.commit();
			sessionFactory.getCurrentSession().flush();
			
		}
		return OK();
	}
	
	/**
	 * java精度运算double 相加
	 * @param d1
	 * @param d2
	 * @return
	 */
	public double sum(double d1,Float d2){
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Float.toString(d2));
		return bd1.add(bd2).doubleValue();
	}
	
	
	
}
