package io.renren.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.google.gson.Gson;

import io.renren.entity.NCResultCodeEntity;
import io.renren.entity.PayLogEntity;
import io.renren.entity.PayOrderEntity;
import io.renren.rest.persistent.KGS;
import io.renren.service.PayLogService;
import io.renren.service.PayOrderService;
import io.renren.utils.MD5Util;
import io.renren.utils.weixin.Configure;
import io.renren.utils.weixin.HttpClientUtil;
import io.renren.utils.weixin.JsonUtil;
import io.renren.utils.weixin.PayReqData;
import io.renren.utils.weixin.PayResData;
import io.renren.utils.weixin.RandomStringGenerator;
import io.renren.utils.weixin.Signature;
import io.renren.utils.weixin.Snippet;
import io.renren.utils.weixin.Util;
import io.renren.utils.weixin.XMLParser;

/**
 * 微信支付相关
 * 
 * @author alexydz
 */
@Controller
public class WeixinPayController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static final String SUCCESS = "SUCCESS"; // 微信处理结果：成功
	private static final String FAIL = "FAIL";
	@Autowired
	private PayOrderService PayOrderService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private PayLogService payLogService;
	private static String nctestUrl = "";
	private static String wxReturnURL = "";
	private static String commConfig = "";

	// nc测试地址
	@Value("#{application['pom.nc.url']}")
	public void setNctestUrl(String nctestUrl) {
		WeixinPayController.nctestUrl = nctestUrl;
	}

	// 微信回调地址
	@Value("#{application['pom.wxpay.return_url']}")
	public void setWxReturnURL(String wxReturnURL) {
		WeixinPayController.wxReturnURL = wxReturnURL;
	}

	// 共同配置
	@Value("#{application['pom.comm.config']}")
	public void setCommConfig(String commConfig) {
		WeixinPayController.commConfig = commConfig;
	}

	/**
	 * 重定向到微信支付js
	 * 
	 * @param orderNo
	 * @param request
	 * @param response
	 */
	@RequestMapping("/redirectUrl")
	public void redirectUrl(String orderNo, HttpServletRequest request, HttpServletResponse response) {
		try {
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=wx5e02795653121413&redirect_uri=" + "http://weixin.hqjy.com/wechat/zk2/weixin_"
					+ commConfig + ".html?orderNo=" + orderNo + "&" + "response_type=code&scope=snsapi_base&state=123&"
					+ "connect_redirect=1#wechat_redirect";

			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/paid")
	public void paid(String payOrderNo, HttpServletRequest request, HttpServletResponse response) {
		Map map = new HashMap();
		map.put("payOrderNo", payOrderNo);
		PayOrderEntity payOrder = PayOrderService.queryObject(map);
		// 判断该订单是否支付成功
		PayOrderEntity exist = PayOrderService.judgeOrderPaySucceed(payOrder);
		// 如果该订单已支付，不需要再进行支付
		if (exist != null) {
			if (exist.getState() == 0) {
				// 商品订单号
				request.setAttribute("outTradeNo", exist.getOrderNo());
				// 支付交易订单号
				request.setAttribute("tradeNo", exist.getTradeNo());
				// 交易金额
				request.setAttribute("totalAmount", exist.getTradeMoney());
				// 交易完成时间
				request.setAttribute("date", sdf.format(exist.getOrderPaySucceedTime()));
				// 判断是否已支付
				request.setAttribute("state", "该订单已支付");
				// 交易商品名称
				request.setAttribute("orderName", exist.getOrderName());
				// 标识部署标志
				if (commConfig.equals("pro")) {
					request.setAttribute("flag", "/pay/");
				} else {
					request.setAttribute("flag", "/hqajax/pay/" + commConfig + "/");
				}
				// ==========转发到支付成功页面
				try {
					request.getRequestDispatcher("/wx_pay-success.jsp").forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}

	// 微信支付。
	@RequestMapping("/wxpay")
	@ResponseBody
	public Map wxpay(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
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

		String json;
		String openid = null;
		try {
			// 获取微信返回code值
			String code = ServletRequestUtils.getStringParameter(request, "code", "");
			String sUrl = Configure.OAUTH2_URL + "?appid=" + Configure.APPID + "&secret=" + Configure.APPSECRET
					+ "&code=" + code + "&grant_type=authorization_code";
			Charset forceCharset = Charset.forName("utf-8");
			json = HttpClientUtil.get(sUrl, null, forceCharset);
			// 获取openid
			Map<String, Object> m = JsonUtil.jsonToMap(json);
			openid = (String) m.get("openid");

			double fee = payOrder.getTradeMoney() * 100;
			int total_fee = (int) Math.rint(fee);

			String out_trade_no = payOrder.getPayOrderNo();
			// ==========统一下订单================
			String body = payOrder.getOrderName();
			String nonce_str = RandomStringGenerator.getRandomStringByLength(32);
			String spbill_create_ip = "183.63.120.222";
			// 支付data
			PayReqData data = new PayReqData(openid, Configure.APPID, Configure.MCHID, nonce_str, body, out_trade_no,
					total_fee, spbill_create_ip, wxReturnURL, Configure.TRADE_TYPE);

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
			Map<String, Object> paySignMap = new HashMap<>();
			paySignMap.put("appId", Configure.APPID);
			paySignMap.put("timeStamp", Util.create_timestamp());
			paySignMap.put("nonceStr", noceStr);
			paySignMap.put("signType", Configure.SIGNTYPE);
			paySignMap.put("package", "prepay_id=" + prepay_id);
			String paySign = Signature.getSign(paySignMap);
			map.put("paySign", paySign);
		} catch (IOException e) {
			// 保存下订单error信息
			PayLogEntity payLog = new PayLogEntity();
			// 订单号
			payLog.setOrderNo(payOrder.getOrderNo());
			// 交易平台标识
			payLog.setTerrace(payOrder.getTerrace());
			// 创建订单IP
			payLog.setCreateOrderIp(payOrder.getCreateOrderIp());
			// 下订单报错日志
			payLog.setErrorLog(e.getMessage());
			payLogService.save(payLog);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 微信支付的通知接口。
	 */
	@RequestMapping("/wxpay/callback/notify")
	@ResponseBody
	public String notify(HttpServletRequest request, HttpServletResponse response) {
		logger.info("/wxpay/callback/notify", new Date());
		String ncResult = null;
		// 保存下订单error信息
		PayLogEntity payLog = new PayLogEntity();
		try {
			// 接收微信返回参数
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			Map<String, Object> map = XMLParser.getMapFromXML(result);

			Boolean check = Signature.checkIsSignValidFromResponseString(map);
			if (!check) {
				return this.buildResponse(FAIL, FAIL);
			}
			if ("SUCCESS".equals(map.get("result_code"))) {
				String trade_no = map.get("out_trade_no").toString();// 订单号
				String time_end = map.get("time_end").toString();// 支付完成时间
				float total_fee = Float.parseFloat(map.get("total_fee").toString());// 订单金额
				String transaction_id = map.get("transaction_id").toString();// 微信支付订单号
				// ===========判断是否支付成功，防止微信再次回调
				PayOrderEntity pay = new PayOrderEntity();
				pay.setPayOrderNo(trade_no);
				pay.setTradeNo(transaction_id);
				int count = PayOrderService.judgeWeiXinOrderPaySucceed(pay);
				if (count == 0) {
					// 根据微信回调订单号查询pay_order对象
					Map payOrderMap = new HashMap();
					payOrderMap.put("payOrderNo", trade_no);
					PayOrderEntity payOrder = PayOrderService.queryObject(payOrderMap);
					if (payOrder != null) {
						// 修改数据库支付成功状态
						PayOrderEntity updatePayOrder = new PayOrderEntity();
						// 修改支付成功状态 ：0 ：成功 1：失败
						updatePayOrder.setState(0);
						// 支付成功创建时间
						updatePayOrder.setOrderPaySucceedTime(new Date());
						// 支付成功回调IP地址
						updatePayOrder.setAlipayIp(request.getServerName());
						// 支付宝交易号
						updatePayOrder.setTradeNo(transaction_id);
						// 商户订单号
						updatePayOrder.setPayOrderNo(payOrder.getPayOrderNo());
						// 支付方式 0：支付宝 1：微信
						updatePayOrder.setPayMode(1);
						PayOrderService.update(updatePayOrder);
						Date d = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String date = sdf.format(d);
						// ================保存日志信息
						// 交易金额
						payLog.setTradeMoney(payOrder.getTradeMoney().toString());
						// 统一下单订单号
						payLog.setPayOrderNo(trade_no);
						// 支付方式
						payLog.setPayMode(1);
						// 时间
						payLog.setTs(new Date());
						// 支付回调ip地址
						payLog.setAlipayIp(request.getServerName());
						// 第三方交易支付订单号
						payLog.setTradeNo(transaction_id);
						// 订单号
						payLog.setOrderNo(payOrder.getOrderNo());
						// 交易平台标识
						payLog.setTerrace(payOrder.getTerrace());
						// 创建订单IP
						payLog.setCreateOrderIp(payOrder.getCreateOrderIp());
						// =============================================================
						// 采用md5加密保证双方支付安全性
						String ciphertext = MD5Util.string2MD5(payOrder.getOrderNo() + date + payOrder.getTradeMoney()
								+ transaction_id + "hengqijypay");
						// 同步支付成功给nc
						String httpUrl = nctestUrl
								+ "servlet/~hq/nc.impl.hq.webservice.collectionbill.UpdateCollectionBillStatusServlet?";
						String param = "orderNo=" + payOrder.getOrderNo() + "&orderTimestamp=" + date + "&tradeMoney="
								+ payOrder.getTradeMoney() + "&tradeNo=" + transaction_id + "&code=" + 0
								+ "&tradesys=wx" + "&ciphertext=" + ciphertext;
						ncResult = HttpClientUtil.getInstance().sendHttpPost(httpUrl, param);
						// =============解析nc返回json格式
						Gson gson = new Gson();
						NCResultCodeEntity ncResultCode = gson.fromJson(ncResult, NCResultCodeEntity.class);
						if (ncResultCode != null) {
							if (ncResultCode.getCode().equals("1")) {
								// 保存nc同步成功到日志表中
								payLog.setErrorLog(payOrder.getTerrace() + ":同步成功");
								payLogService.save(payLog);
							} else {
								// 保存nc同步失败到日志表中
								payLog.setErrorLog(payOrder.getTerrace() + ":同步失败:" + ncResult);
								payLogService.save(payLog);
							}
						} else {
							// 请求nc接口失败保存到日志表中
							payLog.setErrorLog("请求" + payOrder.getTerrace() + "接口失败:" + ncResult);
							payLogService.save(payLog);
						}

					}
				}
				return this.buildResponse(SUCCESS, SUCCESS);
			}
		} catch (Exception e) {
			// 保存nc同步成功到日志表中
			payLog.setErrorLog(e.toString() + ncResult);
			payLogService.save(payLog);
			e.printStackTrace();
		}
		return this.buildResponse(FAIL, FAIL);
	}

	// 商户处理后同步返回给微信参数：
	private String buildResponse(String returnCode, String returnMsg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<return_code><![CDATA[").append(returnCode).append("]]></return_code>");
		sb.append("<return_msg><![CDATA[").append(returnMsg).append("]]></return_msg>");
		sb.append("</xml>");
		return sb.toString();
	}
}
