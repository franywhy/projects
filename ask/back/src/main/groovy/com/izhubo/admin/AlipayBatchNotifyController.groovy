
package com.izhubo.admin


import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Restrictions
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.hqonline.model.UserIncomType
import com.izhubo.alipay.util.AlipayCore
import com.izhubo.alipay.util.AlipayNotify
import com.izhubo.alipay.util.details.DetailVo
import com.izhubo.alipay.util.details.DetailsUtils
import com.izhubo.model.ApplyState
import com.izhubo.model.PaymentAlipayState
import com.izhubo.rest.anno.Rest
import com.mysqldb.model.Apply
import com.mysqldb.model.Payment
import com.mysqldb.model.PaymentItem
import com.mysqldb.model.UserFinance

import static com.izhubo.rest.common.util.WebUtils.$$

/**
 * 支付宝异步通知
 * @ClassName: AlipayController
 * @Description: 支付宝批量支付
 * @author shihongjie
 * @date 2015年8月18日 下午5:16:06
 *
 */

@Rest
class AlipayBatchNotifyController extends BaseController {

	@Resource
	private SessionFactory sessionFactory;
	@Resource
	private UserIncomController userincomController;
	//锁
	private Lock lock = new ReentrantLock();
	
	/** 支付宝用户名和账号不匹配 */
	private static final String ALIPAY_ACCOUN_NAME_NOT_MATCH = "ACCOUN_NAME_NOT_MATCH";
	/** 支付宝账号不存在 */
	private static final String ALIPAY_RECEIVE_USER_NOT_EXIST = "RECEIVE_USER_NOT_EXIST";
	/** 支付宝账号错误信息 */
	private static final String ALIPAY_RECEIVE_ERROR_MSG = "转账失败。支付宝账号或真实姓名错误!";
	/** 支付宝账号余额不足 */
	private static final String ALIPAY_RECEIVE_TRANSFER_AMOUNT_NOT_ENOUGH = "TRANSFER_AMOUNT_NOT_ENOUGH";
	/** 支付宝账号错误信息 */
	private static final String ALIPAY_RECEIVE__AMOUNT_NOT_ENOUGH_MSG = "付款余额宝账户金额不足!";
	
	//http://ttadmin.app1101168695.twsapp.com/alipaybatchnotify.json
	@RequestMapping(value = "/alipaybatchnotify", method = RequestMethod.POST)
	@ResponseBody
	def alipayNotify(HttpServletRequest request){
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}

		//校验是否是支付宝发送过来的通知
		if(!AlipayNotify.verify(params)){
			AlipayCore.logResult("非支付宝请求！IP:"+getIpAddr(request));
			return "error";
		}
		//批次号  转账批次号。 20100101001
		String batch_no = request["batch_no"];
		println "请求的batch_no:" + batch_no;
		//锁
		lock.lock();
		try {
			Session session = sessionFactory.getCurrentSession();
			Payment payment = (Payment) session.createCriteria(Payment.class)
					.add(Restrictions.eq(Payment.PROP_PAYFLOWID, batch_no))//批次号
					.add(Restrictions.or(
					Restrictions.eq(Payment.PROP_ALIPAYSTATE, PaymentAlipayState.申请中.ordinal()),
					Restrictions.eq(Payment.PROP_ALIPAYSTATE, PaymentAlipayState.未申请.ordinal())
					))//状态
					.uniqueResult();
			if(payment){
				//开启事务
				Transaction tran = session.beginTransaction();
	
				//update
				payment.setAlipayState(PaymentAlipayState.申请成功.ordinal());
				java.sql.Timestamp payTime = new java.sql.Timestamp(System.currentTimeMillis());
				payment.setAlipayPayDate(payTime);
	
				//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
				//批量付款数据中转账成功的详细信息
				
				String or_success_details = request.getParameter("success_details");
				if(StringUtils.isBlank(or_success_details)){
					or_success_details = "";
				}
				
				String success_details = or_success_details;
//				String success_details = new String(or_success_details.getBytes("ISO-8859-1"),"UTF-8");
//				String success_details = new String(request.getParameter("success_details").getBytes("ISO-8859-1"),"UTF-8");
				
				//批量付款数据中转账失败的详细信息
				String or_fail_details = request.getParameter("fail_details");
				if(StringUtils.isBlank(or_fail_details)){
					or_fail_details = "";
				}
//				String fail_details = new String(or_fail_details.getBytes("ISO-8859-1"),"UTF-8");
				String fail_details = or_fail_details;
//				String fail_details = new String(request.getParameter("fail_details").getBytes("ISO-8859-1"),"UTF-8");
				
				println "success_details:" + success_details;
				println "fail_details:" + fail_details;
				
				//批量成功
				List<DetailVo> succesList = DetailsUtils.strToDetailVo(success_details);
				//批量失败
				List<DetailVo> failList = DetailsUtils.strToDetailVo(fail_details);
				//原始结果集
				Set<PaymentItem> paymentItemList = payment.getPaymentItemList();
	
				//实际付款总金额
				BigDecimal actual_sum_money = new BigDecimal(0);
				//实际支付总笔数
				Integer actual_batch_no = succesList.size();
	
				//成功结果集
				succesList.each { DetailVo vo ->
					for(PaymentItem item : paymentItemList){
						if(item.getAlipayCode().equals(vo.getNo())){
							//支付状态
							item.setReturnState(1);
							//完成时间
							item.setAliFinishTime(vo.getDate());
							//支付宝内部流水号
							item.setAlipayCode(vo.getAlipayNo());
							//代码
							item.setErrCode(vo.getTag());
							//实际付款金额总和
							actual_sum_money = actual_sum_money.add(item.getApplyMoney());
							
							//批量支付成功的业务逻辑处理
							alipayItemSuccessApply(item , session , payTime);
							break;
						}
					}
				}
	
				//失败结果集
				failList.each { DetailVo vo ->
					for(PaymentItem item : paymentItemList){
						if(item.getAlipayCode().equals(vo.getNo())){
							//支付状态
							item.setReturnState(0);
							//完成时间
							item.setAliFinishTime(vo.getDate());
							//支付宝内部流水号
							item.setAlipayCode(vo.getAlipayNo());
							//错误原因
							item.setErrMemo(vo.getMsg());
							//错误代码
							item.setErrCode(vo.getTag());
	
							//批量支付后失败记录处理
							alipayItemFailApply(item , session , payTime);
							break;
						}
					}
				}
				//1.修改付款单总笔数 实际付款总金额
				payment.setActualBatchNo(actual_batch_no);
				payment.setActualSumMoney(actual_sum_money);
	
				//		//开启事务
				//		Transaction tran = session.beginTransaction();
				session.update(payment);
				tran.commit();
				session.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//显示释放锁
			lock.unlock();
		}

		return "success";
	}

	def con(){
		return OK();
	}

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 支付宝批量支付成功后申请单回写状态
	 * @Description: 支付宝批量支付成功后申请单回写状态
	 * @date 2015年9月24日 上午11:40:51
	 * @param @param item
	 * @param @param session
	 * @param @return
	 * @throws
	 */
	def alipayItemSuccessApply(PaymentItem item , Session session ,java.sql.Timestamp payTime){
		//申请单流水号
		Apply apply = (Apply) session.createCriteria(Apply.class).add(Restrictions.eq(Apply.PROP_APPLYFLOWID, item.getApplyFlowId())).uniqueResult();
		if(apply){
			//状态
			apply.setApplyState(ApplyState.已支付.ordinal());
			apply.setUpdateTime(payTime);
			//更新用户申请表
			session.update(apply);
		}
	}
	
	
	/**
	 * 用户账号或姓名错误
	 *  1.申请单错误提醒
	 *  2.账户余额加入
	 * @Description:  用户账号或姓名错误
	 * @date 2015年9月24日 上午10:46:10
	 * @param @param item 支付单笔信息
	 * @param @param session 事务
	 */
	def alipayItemFailApply(PaymentItem item , Session session,java.sql.Timestamp payTime){
		////2.申请单 错误原因
		//申请单流水号
		Apply apply = (Apply) session.createCriteria(Apply.class).add(Restrictions.eq(Apply.PROP_APPLYFLOWID, item.getApplyFlowId())).uniqueResult();
		if(apply){
			//错误代码
			apply.setErrCode(item.getErrCode());
			//状态
			apply.setApplyState(ApplyState.已作废.ordinal());
			//错误提示信息
			apply.setErrMemo(item.getErrMemo());
			String errorCode = item.getErrCode();
			String errMemo = item.getErrMemo();

			//TODO 错误代码判断
			//用户支付宝账号或真实姓名错误
//			if(StringUtils.isNotBlank(errMemo)){
				if(ALIPAY_ACCOUN_NAME_NOT_MATCH.equals(errMemo) || ALIPAY_RECEIVE_USER_NOT_EXIST.equals(errMemo)){
					apply.setErrMemo(ALIPAY_RECEIVE_ERROR_MSG);
				}else if(ALIPAY_RECEIVE_TRANSFER_AMOUNT_NOT_ENOUGH.equals(errMemo)){
					apply.setErrMemo(ALIPAY_RECEIVE__AMOUNT_NOT_ENOUGH_MSG);
				}else{
					apply.setErrMemo(errMemo);
				}
				//错误提示信息
//				apply.setErrMemo(ALIPAY_RECEIVE_ERROR_MSG);
				apply.setUpdateTime(payTime);

				def ahurl = mainMongo.getCollection("ah_url").findOne($$("_id" : 1025))
				float ratio
				if(ahurl) {
					ratio = ahurl.get("url") as float
				}
				//申请金额
				String applyMoney = ((apply.getApplyMoney()) / (1-ratio)).toString()
				//3.账户余额表
				UserFinance userFinance = (UserFinance) session.createCriteria(UserFinance.class).add(Restrictions.eq(UserFinance.PROP_USERID, item.getUserId())).uniqueResult();
				userFinance.setUserMoney(userFinance.getUserMoney().add(new BigDecimal(applyMoney)));
				//更新用户余额表
				session.update(userFinance);
//			}

			//更新用户申请表
			session.update(apply);
			
			userincomController.save(apply.getUserId(), Double.valueOf(apply.getApplyMoney()), UserIncomType.提现失败.ordinal(), null, payTime.getTime(), apply.getId());
		}
	}
}
