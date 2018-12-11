package com.hqjy.pay.controller.zfb;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hqjy.pay.PayLogService;
import com.hqjy.pay.PayOrderEntity;
import com.hqjy.pay.PayOrderService;
import com.hqjy.pay.controller.base.BaseController;
import com.hqjy.pay.controller.order.OrderController;
import com.hqjy.pay.utils.R;
import com.hqjy.pay.utils.SwitchTrackSupport;
import com.hqjy.pay.zfuconfig.AlipayConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 支付宝支付
 * @author wuyong
 */
@Controller
public class ZFBPayController extends BaseController{
	/*private static AlipayClient client = null;
	static {
		// 调用RSA签名方式
		client = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.Account.app_id,
				AlipayConfig.Account.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.Account.alipay_public_key,
				AlipayConfig.sign_type);
	}*/
	@Autowired
	private PayOrderService PayOrderService;
	@Autowired
	private PayLogService payLogService;

	@Autowired
	private OrderController orderController;

	// 手机网站支付
	@ApiOperation("启动支付宝支付页面")
	@ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "query")
	@RequestMapping(value = "/clientpay", method = RequestMethod.GET)
	public String clientPay(String orderNo, HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {


		// 商户订单号，商户网站订单系统中唯一订单号，必填
		// String out_trade_no = ORDERNO_HEAD + orderNoKGS.nextId();
		// 根据订单号查询pay_order对象
		Map map = new HashMap();
		map.put("payOrderNo", orderNo);
		PayOrderEntity payOrder = PayOrderService.queryObject(map);
		if (payOrder == null) {
			return "";
		}
		AlipayClient client =  new DefaultAlipayClient(AlipayConfig.gatewayUrl, SwitchTrackSupport.AliPay(payOrder.getTerrace()).getAppId(),
				SwitchTrackSupport.AliPay(payOrder.getTerrace()).getPrivateKey(), "json", AlipayConfig.charset,
				SwitchTrackSupport.AliPay(payOrder.getTerrace()).getPublicKey(),
				AlipayConfig.sign_type);
		String total_amount = payOrder.getTradeMoney().toString();
		String subject = payOrder.getOrderName();

		// 商品描述，可空
		String body = "";
		// 超时时间 可空
		String timeout_express = "2m";
		// 销售产品码 必填
		String product_code = "QUICK_WAP_PAY";
		// **********************//
		// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签

		AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

		// 封装请求支付信息
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		// 判断该订单是否支付成功
		PayOrderEntity exist = PayOrderService.judgeOrderPaySucceed(payOrder);
		// 如果该订单已支付，不需要再进行支付
		if (exist != null) {
			if (exist.getState() == 0) {
				redirectPage(payOrder,modelMap,config,1,0);
				return "pay-success";
			}
		}
		model.setOutTradeNo(payOrder.getPayOrderNo());
		model.setSubject(subject);
		model.setTotalAmount(total_amount);
		model.setBody(body);
		model.setTimeoutExpress(timeout_express);
		model.setProductCode(product_code);
		alipay_request.setBizModel(model);
		// 设置异步通知地址
		alipay_request.setNotifyUrl(zhfNotifyUrl);
		// 设置同步地址
		alipay_request.setReturnUrl(zhfReturnUrl);

		// form表单生产
		String form = "";
		try {
			// 调用SDK生成表单
			form = client.pageExecute(alipay_request).getBody();
			response.setContentType("text/html;charset=" + AlipayConfig.charset);
			response.getWriter().write(form);
			response.getWriter().flush();
			response.getWriter().close();
			// 直接将完整的表单html输出到页面
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 电脑网站支付
	 */
	@ApiOperation("支付宝pc端支付")
	@RequestMapping(value = "/webpagepay", method = RequestMethod.POST)
	@ResponseBody
	public Object webpagePay(@RequestParam @ApiIgnore Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获得初始化的AlipayClient
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, SwitchTrackSupport.AliPay(params.get("terrace").toString()).getAppId(),
					SwitchTrackSupport.AliPay(params.get("terrace").toString()).getPrivateKey(), "json", AlipayConfig.charset,
					SwitchTrackSupport.AliPay(params.get("terrace").toString()).getPublicKey(),
					AlipayConfig.sign_type);
			// 设置请求参数
			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

			alipayRequest.setReturnUrl(zhfReturnUrl);
			alipayRequest.setNotifyUrl(zhfNotifyUrl);


			R result1 = orderController.saveOrder(params, request);
			int code = (int)result1 .get("code");
			if(code!=0){ //保存失败
				return result1;
			}
			Map orderMap = new HashMap();
			orderMap.put("orderNo", params.get("orderNo").toString());
			orderMap.put("state", 1);
			PayOrderEntity payOrderEntity = PayOrderService.queryOrderNo(orderMap);

			String out_trade_no = payOrderEntity.getPayOrderNo();
			String total_amount = params.get("tradeMoney").toString();
			String subject = params.get("orderName").toString();
			String body = "";
			alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\""
					+ total_amount + "\"," + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body + "\","
					+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

			String result = alipayClient.pageExecute(alipayRequest).getBody();
			result1.put("data", result);
			return result1;
			//response.setContentType("text/html;charset=utf-8");
			//PrintWriter pw = response.getWriter();
			//pw.write(result);
			//pw.flush();
			//pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("操作失败");
		}
	}
}
