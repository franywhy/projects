package com.izhubo.web.pay.h5.alipay

import javax.servlet.http.HttpServletRequest

import org.hibernate.Session
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.model.DR
import com.izhubo.rest.anno.Rest;
import com.izhubo.web.pay.PayBaseController
import com.izhubo.web.pay.h5.alipay.config.AlipayConfig
import com.izhubo.web.pay.h5.alipay.util.AlipaySubmit
import com.mysqldb.model.Orders

@Rest
class AlipaywapdirectController extends PayBaseController {


	@Value("#{application['notify.wapdirect.url']}")
	private String notify_url;
	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	@Value("#{application['notify.return_h5.url']}")
	public String return_url;
	//收银台页面上，商品展示的超链接，必填
	private String show_url = "";

	@Override
	public Object factoryPayHtml(Orders order) {
		////////////////////////////////////请求参数//////////////////////////////////////
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = order.getOrderno();

		//订单名称，必填
		String subject = order.getOrderName();

		//付款金额，必填
		String total_fee = order.getPayMoney();

		//收银台页面上，商品展示的超链接，必填
		String show_url = show_url;

		//商品描述，可空
		String body = order.getOrderDescribe();
		
		//////////////////////////////////////////////////////////////////////////////////

		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.service);
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("seller_id", AlipayConfig.seller_id);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url + getToken(order.getUserId()));
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("body", body);
		//其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.2Z6TSk&treeId=60&articleId=103693&docType=1
		//如sParaTemp.put("参数名","参数值");

		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
		return sHtmlText;
	}

	
	@Override
	public String payName() {
		return "Alipay_H5";
	}
}
