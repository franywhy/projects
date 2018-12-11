package com.hqjy.pay.controller.zfb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hqjy.pay.utils.SwitchTrackSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.gson.Gson;
import com.hqjy.pay.NCResultCodeEntity;
import com.hqjy.pay.PayConfig;
import com.hqjy.pay.PayConfigService;
import com.hqjy.pay.PayLogEntity;
import com.hqjy.pay.PayLogService;
import com.hqjy.pay.PayOrderEntity;
import com.hqjy.pay.PayOrderService;
import com.hqjy.pay.controller.base.BaseController;
import com.hqjy.pay.utils.MD5Util;
import com.hqjy.pay.weixin.HttpClientUtil;
import com.hqjy.pay.zfuconfig.AlipayConfig;

import io.swagger.annotations.ApiOperation;

/**
 * 支付宝支付成功同步和异步回调接口
 * 
 * @author wuyong
 */
@Controller
public class ZFBPayCallbackController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PayOrderService PayOrderService;
	@Autowired
	private PayLogService payLogService;
	@Autowired
	private PayConfigService payConfigService;

	/**
	 * 【nc】 支付宝支付成功回调异步（notify_url）接口，只要修改订单信息等。
	 */
	@SuppressWarnings("unused")
	@ApiOperation("支付宝支付成功回调异步（notify_url）接口")
	@RequestMapping(value = "/alipay/callback/notify", method = RequestMethod.POST)
	public void alipayNotify(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> params = new HashMap<String, String>();
		String result = null;
		PayOrderEntity order = null;
		PayOrderEntity payOrder = null;
		BigDecimal total_amount = null;
		String out_trade_no = null;
		String trade_no = null;
		// 保存下订单error信息
		PayLogEntity payLog = new PayLogEntity();
		// 商户单号
		try {
			out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			logger.info("支付宝商户单号：{}", out_trade_no);
		} catch (UnsupportedEncodingException e) {
		}
		if (out_trade_no == null) {
			logger.error("商户单号不能为空：{}", out_trade_no);
			return;
		}
		// 根据订单号查询pay_order对象
		Map map = new HashMap();
		map.put("payOrderNo", out_trade_no);
		payOrder = PayOrderService.queryObject(map);
		if(payOrder==null){
			logger.error("订单号为 {} 的订单为空", out_trade_no);
			return;
		}
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
			boolean signVerified = AlipaySignature.rsaCheckV1(params, SwitchTrackSupport.AliPay(payOrder.getTerrace()).getPublicKey(),
					AlipayConfig.charset, AlipayConfig.sign_type); // 调用SDK验证签名
			System.out.println("验证签名：" + signVerified);
			// 判断调用SDK验证签名是否验证成功
			if (signVerified) {
				/*// 商户单号
				out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				logger.info("支付宝商户单号：{}", out_trade_no);*/
				// 交易单号
				trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				// 付款金额
				total_amount = new BigDecimal(
						new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8"));
				// 交易状态
				String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
				if (trade_no == null) {
					logger.error("交易单号不能为空：{}", trade_no);
					return;
				} else if (total_amount == null) {
					logger.error("订单金额不能为空：{}", total_amount);
					return;
				} else {
					// 判断是否支付成功，支付成功才修改订单信息和同步消息，如果交易状态为关闭状态不修改订单信息
					if (trade_status.equals("TRADE_SUCCESS")) {
						// 判断是否已更新订单信息和保存订单日志
						boolean  bool=payLogService.queryTotal(trade_no);
						if (!bool) {
							/*// 根据订单号查询pay_order对象
							Map map = new HashMap();
							map.put("payOrderNo", out_trade_no);
							payOrder = PayOrderService.queryObject(map);*/
							Date d = new Date();
							String date = sdf.format(d);
							if (payOrder != null) {
								// 判断订单金额是否跟回调参数金额是否一致
								if (!total_amount.equals(payOrder.getTradeMoney())) {
									order = new PayOrderEntity(payOrder.getOrderNo(), total_amount,
											sdf.format(new Date()), payOrder.getTerrace(), null, out_trade_no,
											"订单金额有误==" + "订单金额：" + payOrder.getTradeMoney() + "支付宝回调金额："
													+ total_amount);
									// 保存日志信息
									saveLogInfo(order);
									response.getWriter().print("fail");
									return;
								}
								// ===============修改数据库支付成功状态=============
								order = new PayOrderEntity(trade_no, new Date(), out_trade_no);
								PayOrderService.update(order);
								// 同步
								notifyCall(request, response, params.toString(), payOrder, trade_no, "zfb", 0);
							}
						} else {
							response.getWriter().print("success");
						}
					} else {
						logger.info("订单超时支付，{}该订单,商户单号为：{}", trade_status, out_trade_no);
					}
				}
			} else {
				order = new PayOrderEntity(payOrder.getOrderNo(), payOrder.getTradeMoney(), payOrder.getTerrace(), 1,
						request.getServerName(), out_trade_no, result,
						"API返回的数据签名数据不存在，有可能被第三方篡改!!!" + params.toString(), trade_no);
				// 保存日志信息
				saveLogInfo(order);
				response.getWriter().print("fail");
			}
		} catch (Exception e) {
			try {
				logger.error("订单单号：{}，商户单号：{}，交易单号：{}，，报错信息：{}", payOrder.getOrderNo(), out_trade_no, trade_no,
						e.getMessage());
				order = new PayOrderEntity(payOrder.getOrderNo(), total_amount, payOrder.getTerrace(), 1,
						request.getServerName(), trade_no, null, payOrder.getTerrace() + "支付宝报错信息：" + e.getMessage(),
						trade_no);
				// 保存日志信息
				saveLogInfo(order);
				// 通知支付宝回调失败
				response.getWriter().print("fail");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 【nc】支付宝支付成功回调同步（return_url）接口，只要显示交易成功订单信息页面展示给用户看。
	 */
	@ApiOperation("支付宝支付成功回调同步（return_url）接口")
	@RequestMapping(value = "/alipay/callback/return", method = RequestMethod.GET)
	public String alipayReturn(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		response.setContentType("text/html;charset=utf-8");
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
			// 根据订单号查询pay_order对象
			Map map = new HashMap();
			map.put("payOrderNo", out_trade_no);
			PayOrderEntity payOrder = PayOrderService.queryObject(map);
			boolean verify_result = AlipaySignature.rsaCheckV1(params, SwitchTrackSupport.AliPay(payOrder.getTerrace()).getPublicKey(),
					AlipayConfig.charset, AlipayConfig.sign_type);
			if (verify_result) {// 验证成功
				/*// 根据订单号查询pay_order对象
				Map map = new HashMap();
				map.put("payOrderNo", out_trade_no);
				PayOrderEntity payOrder = PayOrderService.queryObject(map);*/
				Date d = new Date();
				String date = sdf.format(d);
				if (payOrder != null) {
					//redirectPage(payOrder, modelMap,config, 0, 0);
					//return "pay-success";
					response.sendRedirect(payConfigService.find().getZfbCallBackUrl());
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
		return null;
	}
}
