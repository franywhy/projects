package com.izhubo.admin

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.alipay.config.AlipayConfig
import com.izhubo.alipay.util.AlipayCore
import com.izhubo.alipay.util.AlipaySubmit
import com.izhubo.alipay.util.UtilDate
import com.izhubo.alipay.util.details.DetailConfing
import com.izhubo.model.PaymentAlipayState
import com.izhubo.rest.anno.RestWithSession
import com.mysqldb.model.Payment
import com.mysqldb.model.PaymentItem

/**
 * 支付宝批量支付
 * @ClassName: AlipayController 
 * @Description: 支付宝批量支付
 * @author shihongjie
 * @date 2015年8月18日 下午5:16:06 
 *
 */

//@RestWithSession
@RestWithSession
class AlipayBatchController extends BaseController {
	
	@Resource
	private SessionFactory sessionFactory;
	
//	支付宝批量付款异步通知接口
	@Value("#{application['notify.url.batch.alipay']}")
	private String notify_url;
	
	
	def con(){
		return OK();
	}
	
	/**
	 * 批量转账申请
	 * @Description: 批量转账申请
	 * @date 2015年8月18日 下午5:21:19 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	public String apply(int id){
		Session session = sessionFactory.getCurrentSession();
		//申请记录
		//TODO 加入状态判断和审核状态
		Payment payment = (Payment) session.createCriteria(Payment.class)
							.add(Restrictions.eq(Payment.PROP_ID, id))//id
//							.add(Restrictions.eq(Payment.PROP_ALIPAYSTATE, PaymentAlipayState.未申请.ordinal()))//状态
							.add(Restrictions.or(
								Restrictions.eq(Payment.PROP_ALIPAYSTATE, PaymentAlipayState.申请中.ordinal()),
								Restrictions.eq(Payment.PROP_ALIPAYSTATE, PaymentAlipayState.未申请.ordinal())
								))//状态
							.uniqueResult();
		if(payment){
			
			//付款账号
			//必填
			String email = payment.getPayAccountNo();
			
			//付款账户名 
			//必填，个人支付宝账号是真实姓名公司支付宝账号是公司名称
			String account_name = payment.getPayUserName();
			
			//付款当天日期
			//必填，格式：年[4位]月[2位]日[2位]，如：20100801
			String pay_date = UtilDate.getDate();
			
			//批次号
			//必填，格式：当天日期[8位]+序列号[3至16位]，如：201008010000001
			String batch_no = payment.getPayFlowId();

			//付款总金额
			//必填，即参数detail_data的值中所有金额的总和
			String batch_fee = payment.getSumMoney().toString();
			
			//付款笔数
			//必填，即参数detail_data的值中，“|”字符出现的数量加1，最大支持1000笔（即“|”字符出现的数量999个）
			int batch_num = payment.getBatchNo();
			
			//TODO 付款详细数据
			//必填，格式：流水号1^收款方帐号1^真实姓名^付款金额1^备注说明1|流水号2^收款方帐号2^真实姓名^付款金额2^备注说明2....
//			String detail_data = new String(request.getParameter("WIDdetail_data").getBytes("ISO-8859-1"),"UTF-8");
			StringBuffer detail_data = new StringBuffer();
			Set<PaymentItem> paymentItemList = payment.getPaymentItemList();
			if(paymentItemList){
				paymentItemList.each {PaymentItem item ->
					//流水号
					detail_data.append(item.getAlipayCode() + DetailConfing.S1);
					//收款方帐号
					detail_data.append(item.getAlipayAccount() + DetailConfing.S1);
					//真实姓名
					detail_data.append(item.getRealName() + DetailConfing.S1);
					//付款金额
					detail_data.append(item.getApplyMoney().toString() + DetailConfing.S1);
					//备注说明
					detail_data.append(item.getAlipayCode() + DetailConfing.S2);
				}
			}
			
			//////////////////////////////////////////////////////////////////////////////////
			
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "batch_trans_notify");
			sParaTemp.put("partner", AlipayConfig.partner);
			sParaTemp.put("_input_charset", AlipayConfig.input_charset);
			
			
			sParaTemp.put("notify_url", notify_url);
			sParaTemp.put("email", email);
			
			sParaTemp.put("account_name", account_name);
			sParaTemp.put("pay_date", pay_date);
			sParaTemp.put("batch_no", batch_no);
			sParaTemp.put("batch_fee", batch_fee);
			sParaTemp.put("batch_num", batch_num+"");
			
			sParaTemp.put("detail_data", detail_data.substring(0 , detail_data.length() -1));
			
			
			
			println "===========sParaTemp:" + sParaTemp;
			//建立请求
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
			
			println "==========sHtmlText:" + sHtmlText;
			//日志输出
			AlipayCore.logResult("sParaTemp:"+sParaTemp+">>>>>>>>>>>>>>>>>>>>>>>sHtmlText:" +sHtmlText);
			
			//付款单申请中
			payment.setAlipayState(PaymentAlipayState.申请中.ordinal());
			payment.setAlipayPayDate(new java.sql.Timestamp(System.currentTimeMillis()));
			//开启事务
			Transaction tran =session.beginTransaction();
			session.update(payment);
			tran.commit();
			session.flush();
			
			return sHtmlText;
		}
							
	}
}
