package com.hqjy.pay.controller.wx;

import com.hqjy.pay.PayLogService;
import com.hqjy.pay.PayOrderEntity;
import com.hqjy.pay.PayOrderService;
import com.hqjy.pay.controller.base.BaseController;
import com.hqjy.pay.utils.SwitchTrackSupport;
import com.hqjy.pay.weixin.Configure;
import com.hqjy.pay.weixin.HttpClientUtil;
import com.hqjy.pay.weixin.JsonUtil;
import com.hqjy.pay.weixin.PayReqData;
import com.hqjy.pay.weixin.PayResData;
import com.hqjy.pay.weixin.RandomStringGenerator;
import com.hqjy.pay.weixin.Signature;
import com.hqjy.pay.weixin.Snippet;
import com.hqjy.pay.weixin.Util;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信支付相关
 * 
 * @author wuyong
 */
@Controller
public class WeixinPayController extends BaseController {
	@Autowired
	private PayOrderService PayOrderService;
	@Autowired
	private PayLogService payLogService;

	/**
	 * 重定向到微信支付js
	 * 
	 * @param orderNo
	 * @param request
	 * @param response
	 */
	@ApiOperation(" 重定向到微信支付js")
	@ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "query")
	@RequestMapping(value = "/redirectUrl", method = RequestMethod.GET)
	public void redirectUrl(String orderNo, HttpServletRequest request, HttpServletResponse response) {
		Map payOrderMap = new HashMap();
		payOrderMap.put("payOrderNo", orderNo);
		PayOrderEntity payOrder = PayOrderService.queryObject(payOrderMap);
		try {
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=" + SwitchTrackSupport.WeChatPay(payOrder.getTerrace()).getAppId() + "&redirect_uri=" +
					"http://weixin.hqjy.com/wechat/zk2/weixin_"
					+ config+ ".html?orderNo=" + orderNo + "&"
					+ "response_type=code&scope=snsapi_base&state=123&" + "connect_redirect=1#wechat_redirect";

			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ApiOperation("微信js回调判断是否支付成功")
	@ApiImplicitParam(name = "payOrderNo", value = "订单号", required = true, dataType = "String", paramType = "query")
	@RequestMapping(value = "/paid", method = RequestMethod.GET)
	public String paid(String payOrderNo, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		Map payOrderMap = new HashMap();
		payOrderMap.put("payOrderNo", payOrderNo);
		PayOrderEntity payOrder = PayOrderService.queryObject(payOrderMap);
		// 判断该订单是否支付成功
		PayOrderEntity exist = PayOrderService.judgeOrderPaySucceed(payOrder);
		// 如果该订单已支付，不需要再进行支付
		if (exist != null) {
			if (exist.getState() == 0) {
				redirectPage(payOrder,modelMap,config,1,1);
				return "pay-success";
			}
		}
		return null;
	}

	// 微信支付。
	@ApiOperation("微信js调支付页面")
	@RequestMapping(value = "/wxpay", method = RequestMethod.POST)
	@ResponseBody
	public Map wxpay(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap();
		String json;
		String openid = null;
		// 获取订单号
		String payOrderNo = ServletRequestUtils.getStringParameter(request, "orderNo", "");
		// 根据订单号查询pay_order对象
		Map orderMap = new HashMap();
		orderMap.put("payOrderNo", payOrderNo);
		PayOrderEntity payOrder = PayOrderService.queryObject(orderMap);
		if (payOrder == null) {
			return null;
		}
		// 判断该订单是否支付成功
		PayOrderEntity exist = PayOrderService.judgeOrderPaySucceed(payOrder);
		// 如果该订单已支付，不需要再进行支付
		if (exist != null) {
			if (exist.getState() == 0) {
				map.put("code", "1");
				map.put("payOrderNo", payOrderNo);
				return map;
			}
		}
		try {
			// 获取微信返回code值
			String code = ServletRequestUtils.getStringParameter(request, "code", "");
			String sUrl = Configure.OAUTH2_URL + "?appid=" + SwitchTrackSupport.WeChatPay(payOrder.getTerrace()).getAppId() + "&secret=" +
					SwitchTrackSupport.WeChatPay(payOrder.getTerrace()).getAppSecret()
						+ "&code=" + code + "&grant_type=authorization_code";
			Charset forceCharset = Charset.forName("utf-8");
			json = HttpClientUtil.get(sUrl, null, forceCharset);
			// 获取openid
			Map<String, Object> m = JsonUtil.jsonToMap(json);
			openid = (String) m.get("openid");
			//测试注意
			int fee = new BigDecimal(100).multiply(payOrder.getTradeMoney()).setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
			// int total_fee = (int) Math.rint(fee);
			String out_trade_no = payOrder.getPayOrderNo();
			// ==========统一下订单================
			String body = payOrder.getOrderName();
			String nonce_str = RandomStringGenerator.getRandomStringByLength(32);
			String spbill_create_ip ="183.63.120.222";
			// 支付data
			PayReqData data = new PayReqData(openid, SwitchTrackSupport.WeChatPay(payOrder.getTerrace()).getAppId(), SwitchTrackSupport.WeChatPay(payOrder.getTerrace()).getMchId(), nonce_str, body, out_trade_no,
						fee, spbill_create_ip,wxReturnUrl, Configure.TRADE_TYPE, SwitchTrackSupport.WeChatPay(payOrder.getTerrace()).getKey());

			String prepay_id = null;
			// xml
			String xml = Snippet.convertToXml(data);
			// 头部
			Map<String, String> headers = new HashMap<String, String>();
			// 提交订单 返回结果xml
			String result = HttpClientUtil.getInstance().postXML(Configure.CREATE_ORDER_URL, headers, xml);
			// 返回结果格式化
			result = new String(result.getBytes("gbk"), "utf-8");

			// =====================返回结果的结构体
			PayResData resData = Snippet.converyToJavaBean(result, PayResData.class);
			prepay_id = resData.getPrepay_id();
			// =====================启动微信支付
			String timestamp = System.currentTimeMillis() + "";
			timestamp = timestamp.substring(0, 10);
			map.put("code", "0");
			map.put("appId", resData.getAppid());
			map.put("timeStamp", Util.create_timestamp());
			String noceStr = RandomStringGenerator.getRandomStringByLength(16);
			map.put("nonceStr", noceStr);
			map.put("packageStr", "prepay_id=" + prepay_id);
			map.put("signType", "MD5");
			// ============================签名
			Map<String, Object> paySignMap = new HashMap();
			paySignMap.put("appId", SwitchTrackSupport.WeChatPay(payOrder.getTerrace()).getAppId());
			paySignMap.put("timeStamp", Util.create_timestamp());
			paySignMap.put("nonceStr", noceStr);
			paySignMap.put("signType", Configure.SIGNTYPE);
			paySignMap.put("package", "prepay_id=" + prepay_id);
			String paySign = Signature.getSign(paySignMap, SwitchTrackSupport.WeChatPay(payOrder.getTerrace()).getKey());
			map.put("paySign", paySign);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
}
