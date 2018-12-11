package com.izhubo.admin
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat

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
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.persistent.KGS
import com.mysqldb.model.Apply
import com.mysqldb.model.Payment
import com.mysqldb.model.PaymentItem

@RestWithSession
class PaymentController extends BaseController {
	@Resource
	private SessionFactory sessionFactory;
	@Resource
	KGS paymentKGS;
	@Resource
	private AlipayBatchController alipaybatchController;
	/** 支付宝单号补齐位 */
	@Value("#{application['notify.pay_flow_id_head']}")
	private String notify_pay_flow_id_head;
	//添加付款单查询
	def applylist(HttpServletRequest request){
		
		//翻页查询
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		
		Criteria criterion = sessionFactory.getCurrentSession().createCriteria(Apply.class)
		criterion.add(Restrictions.isNull(Apply.PROP_PAYMENTFLOWID)); //判断为NULL
		criterion.add(Restrictions.eq(Apply.PROP_APPLYSTATE,0)); //判断值为0
		
		def applyList = criterion.addOrder(Order.desc(Apply.PROP_CREATETIME)).addOrder(Order.desc(Apply.PROP_ID)).setFirstResult((page - 1) * size).setMaxResults(size).list();
		
		int apply_count = (Integer) sessionFactory.getCurrentSession()
				.createCriteria(Apply.class)
				.setProjection(Projections.count(Apply.PROP_ID))
				.add(Restrictions.isNull(Apply.PROP_PAYMENTFLOWID))
				.add(Restrictions.eq(Apply.PROP_APPLYSTATE,0)) //判断值为0
				.uniqueResult();

		int allpage = apply_count / size + apply_count% size >0 ? 1 : 0;
		Map map = new HashMap();
		map["code"]  = 1;
		map["data"]  = applyList;
		map["all_page"]  = allpage;
		map["count"]  = apply_count;
		return map;
	}
	
	
	//付款单查看 
	def payment_flowlist(HttpServletRequest request){
		
		//翻页查询
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int paymentId = ServletRequestUtils.getIntParameter(request, "paymentId", 0);

		if(paymentId > 0){
			Criteria criterion = sessionFactory.getCurrentSession().createCriteria(PaymentItem.class)
			criterion.add(Restrictions.eq(PaymentItem.PROP_PAYMENTID , paymentId));
//			criterion.add(Restrictions.eq(PaymentItem.PROP_PAYMENTID , String.valueOf(paymentId)));
//			criterion.add(Restrictions.eq(PaymentItem.PROP_PAYFLOWID , payFlowId));

			def payment_flowlist = criterion.addOrder(Order.desc(PaymentItem.PROP_PAYMENTID)).addOrder(Order.desc(PaymentItem.PROP_ID)).setFirstResult((page - 1) * size).setMaxResults(size).list();

			int PaymentItem_count = (Integer) sessionFactory.getCurrentSession()
					.createCriteria(PaymentItem.class)
					.setProjection(Projections.count(PaymentItem.PROP_ID))
					.add(Restrictions.eq(PaymentItem.PROP_PAYMENTID ,paymentId ))
					.uniqueResult();

			int allpage = PaymentItem_count / size + PaymentItem_count% size >0 ? 1 : 0;
			Map map = new HashMap();
			map["code"]  = 1;
			map["data"]  = payment_flowlist;
			map["all_page"]  = allpage;
			map["count"]  = PaymentItem_count;
			return map;
		}
	}
	

	
	//主界面查询
	def list(HttpServletRequest request){

		String batchNo = request[ "batchNo"];
//		int payFlowId = ServletRequestUtils.getIntParameter(request, "payFlowId", 0);;
		String payFlowId = request[ "payFlowId"];
		String memo = request[ "memo"];
		String payAccountNo = request[ "payAccountNo"];
		String payUserId = request[ "payUserId"];
		String payUserName = request[ "payUserName"];
		
		//提交状态
		String upload_flag = request[ "upload_flag"];
//		审核状态
		String audit_flag = request[ "audit_flag"];
//		支付状态
		String alipay_state = request[ "alipay_state"];

		//单据创建日期
		Date begin_date = Web.getTime(request, "begin_date");
		Date end_date = Web.getTime(request, "end_date");
		//支付日期
		Date pay_begin_date = Web.getTime(request, "pay_begin_date");
		Date pay_end_date = Web.getTime(request, "pay_end_date");
		
		
		//翻页查询
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		Criteria criterion = sessionFactory.getCurrentSession().createCriteria(Payment.class) ;
		Criteria criterion_count = sessionFactory.getCurrentSession().createCriteria(Payment.class) ;
		//拼接条件
		
		if(StringUtils.isNotBlank(payFlowId)){
			criterion.add(Restrictions.eq(Payment.PROP_PAYFLOWID , payFlowId));
			criterion_count.add(Restrictions.eq(Payment.PROP_PAYFLOWID , payFlowId));
		}
		//提交状态
		if(StringUtils.isNotBlank(upload_flag)){
			criterion.add(Restrictions.eq(Payment.PROP_UPLOADFLAG , Integer.valueOf(upload_flag)));
			criterion_count.add(Restrictions.eq(Payment.PROP_UPLOADFLAG , Integer.valueOf(upload_flag)));
		}
		//审核状态
		if(StringUtils.isNotBlank(audit_flag)){
			criterion.add(Restrictions.eq(Payment.PROP_AUDITFLAG , Integer.valueOf(audit_flag)));
			criterion_count.add(Restrictions.eq(Payment.PROP_AUDITFLAG , Integer.valueOf(audit_flag)));
		}
		
		//支付状态
		if(StringUtils.isNotBlank(alipay_state)){
			criterion.add(Restrictions.eq(Payment.PROP_ALIPAYSTATE , Integer.valueOf(alipay_state)));
			criterion_count.add(Restrictions.eq(Payment.PROP_ALIPAYSTATE , Integer.valueOf(alipay_state)));
		}
		
		//单据创建日期
		if(begin_date){
			criterion.add(Restrictions.ge(Payment.PROP_TIMESTAMP,new Timestamp(begin_date.getTime())));
			criterion_count.add(Restrictions.ge(Payment.PROP_TIMESTAMP,new Timestamp(begin_date.getTime())));
		}
		if(end_date){
			criterion.add(Restrictions.le(Payment.PROP_TIMESTAMP,new Timestamp(end_date.getTime())));
			criterion_count.add(Restrictions.le(Payment.PROP_TIMESTAMP,new Timestamp(end_date.getTime())));
		}
		
		//支付日期
		if(pay_begin_date){
			criterion.add(Restrictions.ge(Payment.PROP_ALIPAYPAYDATE,new Timestamp(pay_begin_date.getTime())));
			criterion_count.add(Restrictions.ge(Payment.PROP_ALIPAYPAYDATE,new Timestamp(pay_begin_date.getTime())));
		}
		if(pay_end_date){
			criterion.add(Restrictions.le(Payment.PROP_ALIPAYPAYDATE,new Timestamp(pay_end_date.getTime())));
			criterion_count.add(Restrictions.le(Payment.PROP_ALIPAYPAYDATE,new Timestamp(pay_end_date.getTime())));
		}
		
		criterion.addOrder(Order.desc(Payment.PROP_ID));//降序排列
		def paymentList = criterion.setFirstResult((page - 1) * size).setMaxResults(size)?.list();
		//		def paymentList = criterion.addOrder(Order.desc(Payment.PROP_PAYMENTMONTH)).setFirstResult((page - 1) * size).setMaxResults(size).list();
		int payment_count = 0;
		if(paymentList){
			payment_count = (int)criterion_count.setProjection(Projections.count(Payment.PROP_ID)).uniqueResult()?:0;
//			payment_count = (int)sessionFactory.getCurrentSession()
//					.createCriteria(Payment.class)
//					.setProjection(Projections.count(Payment.PROP_ID))
//					.uniqueResult()?:0;
		}
		
		/*int payment_count = (int)sessionFactory.getCurrentSession()
				.createCriteria(Payment.class)
				.setProjection(Projections.count(Payment.PROP_ID))
				.uniqueResult()?:0;*/
		//	int payment_count = (Integer)	(payment_re == null ? payment_re : 0);
		int allpage = payment_count / size + payment_count% size >0 ? 1 : 0;
		Map map = new HashMap();
		map["code"]  = 1;
		map["data"]  = paymentList;
		map["all_page"]  = allpage;
		map["count"]  = payment_count;
		map["abc"]  = 1;
		return map;
	}
	
	//查看子表
	def getdetaillist(HttpServletRequest req) {
		
				Map<String, Object> map = new HashMap<String, Object>();
				int size = ServletRequestUtils.getIntParameter(req , "size" , 20);
				int page = ServletRequestUtils.getIntParameter(req , "page" , 1);
		
				String id =req.getParameter("payFlowId").toString();
//				int id =Integer.valueOf( req.getParameter("payFlowId").toString());
				
				int count = (int)sessionFactory
						.getCurrentSession().createCriteria(PaymentItem.class)
						.setProjection(Projections.count(PaymentItem.PROP_PAYMENTID))
						.add(Restrictions.eq(PaymentItem.PROP_PAYMENTID, id))
						.uniqueResult();
						
				List incomelist = sessionFactory
						.getCurrentSession().createCriteria(PaymentItem.class)
						.add(Restrictions.eq(PaymentItem.PROP_PAYMENTID, id))
						.setFirstResult((page-1)*size)
						.setMaxResults(size)
						.list();
		
						return GetResultOKForList(incomelist,count);
			}

	//新增付款单
	@TypeChecked(TypeCheckingMode.SKIP)
	def add(HttpServletRequest request){


		int userid = Web.currentUserId;

		Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();
		String memo = request[ "memo"];
		String payAccountNo = request[ "payAccountNo"];
		int payUserId = request[ "payUserId"] as int;
		String payUserName = request[ "payUserName"];
		
		if(StringUtils.isBlank(memo) || StringUtils.isBlank(payAccountNo) || StringUtils.isBlank(payUserName)){
			return [code : 0  , msg : "参数异常"];
		}
		

		Payment bt = new Payment ();
		bt.setMemo(memo.toString())
		bt.setPayAccountNo(payAccountNo.toString());
		bt.setPayUserId(payUserId);
		bt.setPayUserName(payUserName.toString());
		bt.setPayFlowId(notify_pay_flow_id_head + String.valueOf(paymentKGS.nextId()));
//		bt.setPayFlowId("100" + String.valueOf(paymentKGS.nextId()));
		bt.setPaymentMonth(dateToString(new Date()));
		
		bt.paymentItemList =   new ArrayList();
		
		def listjson =  request.getParameter("listjson");
		
		int sum = 0;
//		double mon = 0;
		//统计字表总金额
		BigDecimal mon = new BigDecimal(0);
		//非空校验
		if(listjson){
			List<String> applyFlowIdArray = JSONUtil.jsonToBean(listjson, ArrayList.class);
			if(applyFlowIdArray && applyFlowIdArray.size() < 1000){//非空校验
				
				for(int i = 0 ; i < applyFlowIdArray.size() ; i++) {
					
					String apply_flowId = applyFlowIdArray.get(i);
					
					Apply apply = (Apply) sessionFactory.getCurrentSession().createCriteria(Apply.class)
														.add(Restrictions.isNull(Apply.PROP_PAYMENTFLOWID)) //判断不为NULL
														.add(Restrictions.eq(Apply.PROP_APPLYSTATE , 0))//判断值为0
														.add(Restrictions.eq(Apply.PROP_APPLYFLOWID , apply_flowId))//applyFlowId
														.uniqueResult();
					
					if(apply == null){
						return [code : 0  , msg : "本次操作异常，申请单流水号【" + apply_flowId +"】已提交!"];
					}
														
//					double applyMoney =apply.getApplyMoney();
					Integer userId =apply.getUserId();
					String realName = apply.getRealName();
					String alipayAccount = apply.getAlipayAccount();
					String applyFlowId = apply.getApplyFlowId();
					String applyYearMonth =apply.getApplyYearMonth();
					
		
					
		
//					mon = mon + applyMoney;
					mon = mon.add(apply.getApplyMoney());
					
					
					sum ++;
		
		
					PaymentItem btype = new PaymentItem();
		
					
					btype.setApplyFlowId(applyFlowId);
					btype.setRealName(realName);
					btype.setAlipayAccount(alipayAccount);
					btype.setUserId(userId);
					btype.setApplyMoney(apply.getApplyMoney());
					btype.setPayment(bt);
					btype.setApplyYearMonth(applyYearMonth);
					//支付宝流水号
					btype.setAlipayCode(bt.getPayFlowId() + getNextUserId(i));
					btype.setPayFlowId(bt.getPayFlowId());
		
					bt.paymentItemList.add(btype);
					//sessionFactory.getCurrentSession().save(btype);
					
					//查询applyFlowId并赋值
					Apply app = (Apply) sessionFactory.getCurrentSession()
					.createCriteria(Apply.class)
					.add(Restrictions.eq(Apply.PROP_APPLYFLOWID, applyFlowId))
					.uniqueResult();
					
					app.setPaymentFlowId(bt.getPayFlowId());
		//			app.setPaymentFlowId(bt.getPayFlowId());
					sessionFactory.getCurrentSession().update(app); //保存值
				}
				bt.setBatchNo(sum);
				bt.setSumMoney(mon);
				bt.setTimestamp(new Timestamp(System.currentTimeMillis()));
				bt.setCreateUserId(userid);
				bt.setAlipayState(0);
		
		
		
		
				sessionFactory.getCurrentSession().save(bt);
				transaction.commit(); // 提交事务
				sessionFactory.getCurrentSession().flush();
				return OK();
				
				
			}
		}
		return [code : 0  , msg : "参数异常"];
	}
	
	private static String getNextUserId(int num){
		int nextid= num;
		DecimalFormat decformat=new DecimalFormat("0000");
		return decformat.format(nextid);
	}

	public static String dateToString(Date time){
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM");
		String ctime = formatter.format(time);

		return ctime;
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	
	//改
	def edit(HttpServletRequest req){
		def memo = req.getParameter("memo");
		def payAccountNo = req.getParameter("payAccountNo");
		def payUserId = req.getParameter("payUserId");
		def payUserName = req.getParameter("payUserName");
		//1,2,3,4
		def item_remove_string = req.getParameter("item_remove_list");
		def item_remove_list = null;
		if(StringUtils.isNotBlank(item_remove_string)){
			item_remove_list = item_remove_string.split(",");
		}
		
		int _id = Web.currentUserId;
		int id =Integer.valueOf( req.getParameter("_id").toString());
		
		Session session = sessionFactory.getCurrentSession();
		Transaction  transaction  = session.beginTransaction();
		
		//主表
		Payment bt = (Payment) session.get(Payment.class, id);
		bt.setMemo(memo);
		bt.setPayAccountNo(payAccountNo);
		bt.setPayUserId(Integer.valueOf(payUserId));
		bt.setPayUserName(payUserName);
		
		if(item_remove_list){
			Set<PaymentItem> paymentItemList = bt.getPaymentItemList();
			//要删除的子对象
			List removePaymentList = new ArrayList();
			//删除字表
			item_remove_list.each { def item->
				for(PaymentItem pitem : paymentItemList){
					if(pitem.get_id() == Integer.valueOf(item)){
						paymentItemList.remove(pitem);
						removePaymentList.add(pitem)
						break;
					}
				}
			}	//end删除字表
			
			//统计字表总金额
			BigDecimal sumMoney = new BigDecimal(0);
			paymentItemList.each {PaymentItem item->
				sumMoney = sumMoney.add(item.getApplyMoney());
			}
			bt.setSumMoney(sumMoney);
			//修改总笔数
			bt.setBatchNo(paymentItemList.size());
			
			//修改apply表
			removePaymentList.each {PaymentItem item ->
				Apply apply = session.createCriteria(Apply.class).add(Restrictions.eq(Apply.PROP_APPLYFLOWID ,item.getApplyFlowId())).uniqueResult();
				apply.setPaymentFlowId(null);
				//修改apply表中的 付款单号
				session.update(apply);
				//删除PaymentItem
				session.delete(item);
				
			}
		}
		//保存主表
		session.update(bt); //暂存缓存
		
		transaction.commit();//更新
		session.flush(); //保存
		
		return OK();
	}
		
	def submit(HttpServletRequest request){

		int userid = Web.currentUserId;
		int id =Integer.valueOf(request.getParameter("_id").toString());
		Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();
		Payment bt = (Payment) sessionFactory.getCurrentSession().get(Payment.class, id);
		bt.setUploadFlag(1);
		bt.setUploadUserId(userid);
		bt.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		bt.setUpdateUserId(userid);
		sessionFactory.getCurrentSession().update(bt);
		transaction.commit();
		sessionFactory.getCurrentSession().flush();

		
		
		//TODO 支付宝
//		String htmlString = alipaybatchController.apply(id);
		
		return ["code" : 1 , "data" : id];
		
//		return OK();
	}
	
	
	
	def del(HttpServletRequest req){
		Integer id = req.getParameter("_id") as Integer;

		Payment bt = (Payment) sessionFactory.getCurrentSession().get(Payment.class, id);
		Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();
		
		//查询PayFlowId集合值
		List applyList = sessionFactory.getCurrentSession().createCriteria(Apply.class)
								.add(Restrictions.eq(Apply.PROP_PAYMENTFLOWID , bt.getPayFlowId()))
//								.add(Restrictions.eq(Apply.PROP_PAYMENTFLOWID , Integer.valueOf(bt.getPayFlowId())))
								.list();

		//delete
		sessionFactory.getCurrentSession().delete(bt);
		
		if(applyList){
			for(int i = 0 ; i < applyList.size(); i++){
				Apply apply = (Apply)applyList.get(i);
				apply.setPaymentFlowId(null);
				sessionFactory.getCurrentSession().update(apply);
			}
		}
		transaction.commit();
		sessionFactory.getCurrentSession().flush();
		return OK();
	}

	def rollbackSubmit(HttpServletRequest request){

		int userid = Web.currentUserId;
		int id =Integer.valueOf( request.getParameter("_id").toString());
		Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();
		Payment bt = (Payment) sessionFactory.getCurrentSession().get(Payment.class, id);
		bt.setUploadFlag(0);
		bt.setUploadUserId(0);
		bt.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		bt.setUpdateUserId(userid);
		sessionFactory.getCurrentSession().update(bt);
		transaction.commit();
		sessionFactory.getCurrentSession().flush();
		return OK();
	}

	private void Audit(int id) {
		int userid = Web.currentUserId;

		Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();

		Payment bt = (Payment) sessionFactory.getCurrentSession().get(Payment.class, id);

		//修改状态
		bt.setAuditFlag(1);
		bt.setAuditUserId(userid);
		bt.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		bt.setUpdateUserId(userid);



		//在奖金池总额上增加

		sessionFactory.getCurrentSession().update(bt);


		transaction.commit();
		sessionFactory.getCurrentSession().flush();
	}
	
	/**
	 * 审核操作
	 * @param request
	 * @return
	 */
	def audit(HttpServletRequest request){
		int id =Integer.valueOf( request.getParameter("_id").toString());
		Audit(id);
		
//		//支付宝
//		String htmlString = alipaybatchController.apply(id);
		
		return ["code" : 1 , "data" : id];
	}
	
	
	
	def applyHtml(HttpServletRequest request){
		int id = ServletRequestUtils.getIntParameter(request , "id" , 0);
		if(id > 0){
			return [code : 1 , data : alipaybatchController.apply(id)];
		}
		
		return [code : 0  , msg : "参数异常!" , data : "参数异常!"];
	}
	
//	def rollbackAudit(HttpServletRequest request){
//		rollbackAudit1(request);
//	}
}
