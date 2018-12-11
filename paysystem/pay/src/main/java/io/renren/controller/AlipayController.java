package io.renren.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.google.gson.Gson;

import io.renren.entity.NCResultCodeEntity;
import io.renren.entity.PayLogEntity;
import io.renren.entity.PayOrderEntity;
import io.renren.rest.persistent.KGS;
import io.renren.service.PayLogService;
import io.renren.service.PayOrderService;
import io.renren.utils.MD5Util;
import io.renren.utils.R;
import io.renren.utils.alipay.AlipayConfig;
import io.renren.utils.weixin.Configure;
import io.renren.utils.weixin.HttpClientUtil;
import io.renren.utils.weixin.RandomStringGenerator;

/**
 * 支付宝支付
 * 
 * @author alexydz
 */
@Controller
public class AlipayController {
	private static AlipayClient client = null;
	private static String nctestUrl = "";
	private static String returnUrl = "";
	private static String notifyUrl = "";
	private static String commConfig = "";
	private static String wxReturnURL = "";
	protected Logger logger = LoggerFactory.getLogger(getClass());

	// nc测试地址
	@Value("#{application['pom.nc.url']}")
	public void setNctestUrl(String nctestUrl) {
		AlipayController.nctestUrl = nctestUrl;
	}

	// 共同配置
	@Value("#{application['pom.comm.config']}")
	public void setCommConfig(String commConfig) {
		AlipayController.commConfig = commConfig;
	}

	// 支付宝同步回调地址
	@Value("#{application['pom.alipay.return_url']}")
	public void setReturnUrl(String returnUrl) {
		AlipayController.returnUrl = returnUrl;
	}

	// 支付宝异步回调地址
	@Value("#{application['pom.alipay.notify_url']}")
	public void setNotifyUrl(String notifyUrl) {
		AlipayController.notifyUrl = notifyUrl;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static {
		// 调用RSA签名方式
		client = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
				AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
				AlipayConfig.sign_type);
	}
	@Resource
	KGS orderNoKGS;
	private static final String ORDERNO_HEAD = "pay_";
	@Autowired
	private PayOrderService PayOrderService;
	@Autowired
	private PayLogService payLogService;
    //手机网站支付
	@RequestMapping("/clientpay")
	public void clientPay(String orderNo, HttpServletRequest request, HttpServletResponse response) {
		// 商户订单号，商户网站订单系统中唯一订单号，必填
		// String out_trade_no = ORDERNO_HEAD + orderNoKGS.nextId();
		// 根据订单号查询pay_order对象
		Map map = new HashMap();
		map.put("payOrderNo", orderNo);
		PayOrderEntity payOrder = PayOrderService.queryObject(map);
		if (payOrder == null) {
			return;
		}
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
					request.setAttribute("flag", "/");
				} else {
					request.setAttribute("flag", "/");
				}
				// ==========转发到支付成功页面
				try {
					request.getRequestDispatcher("/zfb_pay-success.jsp").forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
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
		alipay_request.setNotifyUrl(notifyUrl);
		// 设置同步地址
		alipay_request.setReturnUrl(returnUrl);

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
	}

	/**
	 * 电脑网站支付
	 */
	@RequestMapping("/webpagepay")
	public void webpagePay(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获得初始化的AlipayClient
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
					AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
					AlipayConfig.sign_type);
			// 设置请求参数
			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

			alipayRequest.setReturnUrl(returnUrl);
			alipayRequest.setNotifyUrl(notifyUrl);
			String out_trade_no = ORDERNO_HEAD + orderNoKGS.nextId();
			String total_amount = "0.01";
			String subject = "自考2.0测试";
			String body = "";
			alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\""
					+ total_amount + "\"," + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body + "\","
					+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

			String result = alipayClient.pageExecute(alipayRequest).getBody();
			response.setContentType("text/html;charset=utf-8");
			PrintWriter pw = response.getWriter();
			pw.write(result);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 支付宝支付成功回调异步（notify_url）接口，只要修改订单信息等。
	 */
	@RequestMapping("/alipay/callback/notify")
	public void alipayNotify(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> params = new HashMap<String, String>();
		String result = null;
		// 保存下订单error信息
		PayLogEntity payLog = new PayLogEntity();
		try {
			// 获取支付宝GET过来反馈信息
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "utf-8");
				params.put(name, valueStr);
			}

			boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key,
					AlipayConfig.charset, AlipayConfig.sign_type); // 调用SDK验证签名

			System.out.println("验证签名：" + signVerified);
			// 判断调用SDK验证签名是否验证成功
			if (signVerified) {
				// 商户订单号
				String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				System.out.println("商户订单号：" + out_trade_no);
				// 支付宝交易号
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				// 付款金额
				String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
				// 交易状态
				String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
				// 判断是否支付成功，支付成功才修改订单信息和同步消息，如果交易状态为关闭状态不修改订单信息
				if (trade_status.equals("TRADE_SUCCESS")) {
					// 判断是否已更新订单信息和保存订单日志
					Map<String, Object> Logmap = new HashMap();
					Logmap.put("payOrderNo", out_trade_no);
					Logmap.put("tradeNo", trade_no);
					int count = payLogService.queryLogExist(Logmap);
					if (count == 0) {
						// 根据订单号查询pay_order对象
						Map map = new HashMap();
						map.put("payOrderNo", out_trade_no);
						PayOrderEntity payOrder = PayOrderService.queryObject(map);
						Date d = new Date();
						String date = sdf.format(d);
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
							updatePayOrder.setTradeNo(trade_no);
							// 商户订单号
							updatePayOrder.setPayOrderNo(payOrder.getPayOrderNo());
							// 支付方式 0：支付宝 1：微信
							updatePayOrder.setPayMode(0);
							PayOrderService.update(updatePayOrder);
							// ================保存日志信息
							payLog.setTradeMoney(payOrder.getTradeMoney().toString());
							// 统一下单订单号
							payLog.setPayOrderNo(out_trade_no);
							// 第三方交易支付订单号
							payLog.setTradeNo(trade_no);
							// 支付回调ip地址
							payLog.setAlipayIp(request.getServerName());
							// 时间
							payLog.setTs(new Date());
							// 支付方式
							payLog.setPayMode(0);
							// 订单号
							payLog.setOrderNo(payOrder.getOrderNo());
							// 交易平台标识
							payLog.setTerrace(payOrder.getTerrace());
							// 创建订单IP
							payLog.setCreateOrderIp(payOrder.getCreateOrderIp());
							// ===================
							// 采用md5加密保证双方支付安全性
							String ciphertext = MD5Util.string2MD5(
									payOrder.getOrderNo() + date + payOrder.getTradeMoney() + trade_no + "hengqijypay");
							// 同步支付成功给nc
							String httpUrl = nctestUrl
									+ "servlet/~hq/nc.impl.hq.webservice.collectionbill.UpdateCollectionBillStatusServlet?";
							String param = "orderNo=" + payOrder.getOrderNo() + "&orderTimestamp=" + date
									+ "&tradeMoney=" + payOrder.getTradeMoney() + "&tradeNo=" + trade_no + "&code=" + 0
									+ "&tradesys=zfb" + "&ciphertext=" + ciphertext;
							result = HttpClientUtil.getInstance().sendHttpPost(httpUrl, param);
							// =============解析nc返回json格式
							Gson gson = new Gson();
							NCResultCodeEntity ncResultCode = gson.fromJson(result, NCResultCodeEntity.class);
							if (ncResultCode != null && ncResultCode.getCode().equals("1")) {
								// 保存nc同步成功到日志表中
								payLog.setErrorLog(payOrder.getTerrace() + ":同步成功");
								// 保存nc同步成功标志
								payLog.setAlipayReturnCode(ncResultCode.getCode());
								payLogService.save(payLog);
								// 一定要返回告诉支付宝回调成功，否则会不断回调notify_url接口
								response.getWriter().print("success");
							} else {
								// 请求nc接口失败保存到日志表中
								payLog.setErrorLog("请求" + payOrder.getTerrace() + "同步失败:" + result);
								payLogService.save(payLog);
								// 一定要返回告诉支付宝回调失败，支付会重复请求notify_url接口
								response.getWriter().print("fail");
							}
						}
					} else {
						response.getWriter().print("success");
					}
				}
			} else {
				System.out.println("验签失败:" + signVerified);
				System.out.println("验签失败支付宝返回参数 :" + params.toString());
			}
		} catch (Exception e) {
			// 保存nc同步失败到日志表中
			payLog.setErrorLog(params.toString());
			payLogService.save(payLog);
			try {
				// 一定要返回告诉支付宝回调失败
				response.getWriter().print("fail");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 支付宝支付成功回调同步（return_url）接口，只要显示交易成功订单信息页面展示给用户看。
	 */
	@RequestMapping("/alipay/callback/return")
	public void alipayReturn(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取支付宝GET过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				/*
				 * valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				 * "utf-8");
				 */
				params.put(name, valueStr);
			}
			// 商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
			boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key,
					AlipayConfig.charset, AlipayConfig.sign_type);
			if (verify_result) {// 验证成功
				// 根据订单号查询pay_order对象
				Map map = new HashMap();
				map.put("payOrderNo", out_trade_no);
				PayOrderEntity payOrder = PayOrderService.queryObject(map);
				Date d = new Date();
				String date = sdf.format(d);
				if (payOrder != null) {
					// 商品订单号
					request.setAttribute("outTradeNo", out_trade_no);
					// 支付交易订单号
					request.setAttribute("tradeNo", trade_no);
					// 交易金额
					request.setAttribute("totalAmount", total_amount);
					// 交易完成时间
					request.setAttribute("date", date);
					// 判断是否已支付
					request.setAttribute("state", "支付成功");
					// 交易商品名称
					request.setAttribute("orderName", payOrder.getOrderName());
					// 标识部署标志
					if (commConfig.equals("pro")) {
						request.setAttribute("flag", "/");
					} else {
						request.setAttribute("flag", "/");
					}
					// ==========转发到支付成功页面
					request.getRequestDispatcher("/zfb_pay-success.jsp").forward(request, response);
				}
			}
			response.getWriter().print("验证成功<br />");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().print("验证失败");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
