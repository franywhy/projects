package com.izhubo.web.pay.pc.alipay

import static com.izhubo.rest.common.util.WebUtils.$$

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.alipay.config.AlipayConfig
import com.izhubo.alipay.util.AlipaySubmit
import com.izhubo.model.DR
import com.izhubo.rest.anno.Rest
import com.izhubo.web.pay.PayBaseController
import com.mysqldb.model.Orders

/**
 * 支付宝支付
 * @author shihongjie
 *
 */
//@Controller
//@RequestMapping("/alipay")
@Rest
class AlipayController extends PayBaseController{
	
	@Resource
	private SessionFactory sessionFactory;
	
	@Value("#{application['notify.alipay.url']}")
	private String notify_url;
	@Value("#{application['return.alipay.url']}")
	private String return_url;
	
	@Override
	public Object factoryPayHtml(Orders order) {
		//返回访问地址
		String sHtmlText = null;
		////////////////////////////////////请求参数//////////////////////////////////////
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = order.getOrderno();

		//订单名称，必填
		String subject = order.getOrderName();

		//付款金额，必填
		String total_fee = order.getPayMoney().toString();

		//商品描述，可空
		String body = order.getOrderDescribe();

		//////////////////////////////////////////////////////////////////////////////////
				
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		//信息确认
		sParaTemp.put("service", AlipayConfig.service);
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("seller_id", AlipayConfig.seller_id);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		
		//服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
		sParaTemp.put("notify_url", notify_url);//后台接口
		//支付完成后跳转的页面
		sParaTemp.put("return_url",return_url);//我的订单列表-对应商品
		
		//防钓鱼
		sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
		
		//业务参数
		//订单号
		sParaTemp.put("out_trade_no", out_trade_no);
		//商品名称
		sParaTemp.put("subject", subject);
		//支付金额
		sParaTemp.put("total_fee", total_fee);
		//商品描述
		sParaTemp.put("body", body);
		
		//非必填业务参数
		//商品单价
//				sParaTemp.put("price", total_fee);
//				//购买数量
//				sParaTemp.put("quantity", "1");
		//商品展示网址
//				sParaTemp.put("show_url", 1);
		
		//其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
		//如sParaTemp.put("参数名","参数值");
		
		
		//建立请求
		sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
		return sHtmlText;
	}

	@Override
	public String payName() {
		return "alipay_PC";
	}
	
}
