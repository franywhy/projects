package com.izhubo.web.pay.app.alipay

import static com.izhubo.rest.common.util.WebUtils.$$

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.alipay.util.AlipayNotify
import com.izhubo.rest.anno.Rest
import com.izhubo.web.pay.PayNotityBaseController

/**
 * 支付宝支付
 * @author shihongjie
 *	创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
 该页面调试工具请使用写文本函数logResult，该函数在com.alipay.util文件夹的AlipayNotify.java类文件中
 如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
 */
@Rest
@RequestMapping("/alipaynotify_app")
class AlipayNotifyAppController extends PayNotityBaseController{

	@Override
	@RequestMapping(value = "/nodity")
	@ResponseBody
	public String nodity(HttpServletRequest request , HttpServletResponse response) {
		return super.nodity(request , response);
	}
	@Override
	public boolean nodityMethod(HttpServletRequest request , HttpServletResponse response) {
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
		
		String ip = getIpAddr(request);
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		
		super.out_trade_no = out_trade_no;
		super.trade_no = trade_no;
		super.payCallBlackRemark = params.toString();
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
		if(AlipayNotify.verify(params)){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			
//			WAIT_BUYER_PAY	交易创建，等待买家付款。
//			TRADE_CLOSED	在指定时间段内未支付时关闭的交易；
//			在交易完成全额退款成功时关闭的交易。
//			TRADE_SUCCESS	交易成功，且可对该交易做操作，如：多级分润、退款等。
//			TRADE_PENDING	等待卖家收款（买家付款后，如果卖家账号被冻结）。
//			TRADE_FINISHED	交易成功且结束，即不可再做任何操作。
			
				
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				//如果有做过处理，不执行商户的业务程序
	//					pay_logs().save($$("_id" : UUID.randomUUID().toString() , "timestamp" : System.currentTimeMillis() , "type1" : "alipay_notify" , "type2" : "TRADE_FINISHED" , "remark" : "TRADE_FINISHED" , "P1" : params.toString()));
				return false;
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
			} else if (trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				//如果有做过处理，不执行商户的业务程序
				
	//					paySuccessNodity(sessionFactory.getCurrentSession(), out_trade_no, trade_no, "AlipayNotifyController-" + params.toString() );
				return true;
				
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
			}
		}
		
		return false;
	}


	@Override
	public String successString() {
		return "success";
	}


	@Override
	public String failString() {
		return "fail";
	}


	@Override
	public String nodityName() {
		return "支付宝PC-异步回调";
	}
	
	
}
