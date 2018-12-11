package com.hqjy.pay.controller.base;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.hqjy.pay.NCResultCodeEntity;
import com.hqjy.pay.PayConfig;
import com.hqjy.pay.PayConfigService;
import com.hqjy.pay.PayLogEntity;
import com.hqjy.pay.PayLogService;
import com.hqjy.pay.PayOrderEntity;
import com.hqjy.pay.utils.MD5Util;
import com.hqjy.pay.weixin.HttpClientUtil;

/**
 * 控制器的抽象基类。
 * 
 * @author wuyong
 */
public abstract class BaseController {
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private PayLogService payLogService;
	@Autowired
	private PayConfigService payConfigService;
	//支付宝同步通知URL
	@Value("${paycofig.zhfReturnUrl}")
	protected String zhfReturnUrl;
	//支付宝异步通知URL
	@Value("${paycofig.zhfNotifyUrl}")
	protected String zhfNotifyUrl;
	//区分部署：pro，test，dev
	@Value("${paycofig.config}")
	protected String config;
	//微信同步通知URL
	@Value("${paycofig.wxReturnUrl}")
	protected String wxReturnUrl;
	//项目URL
	@Value("${paycofig.url}")
	protected String url;
	// 保存日志信息
	public void saveLogInfo(PayOrderEntity payOrder) {
		// 保存下订单error信息
		PayLogEntity payLog = new PayLogEntity();
		// 订单号
		payLog.setOrderNo(payOrder.getOrderNo());
		// 交易金额
		payLog.setTradeMoney(payOrder.getTradeMoney());
		// 支付方式 0：支付宝 1：微信
		payLog.setPayMode(payOrder.getPayMode());
		// 创建订单IP
		payLog.setCreateOrderIp(payOrder.getCreateOrderIp());
		// 支付成功回调IP
		payLog.setPayCallbackIp(payOrder.getPayCallbackIp());
		// 商户单号
		payLog.setPayOrderNo(payOrder.getPayOrderNo());
		// 第三方支付成功回调信息
		payLog.setPayCallbackInfo(payOrder.getPayCallbackInfo());
		// 订单日志
		payLog.setPayLog(payOrder.getPayLog());
		// 创建时间
		payLog.setCreateTime(sdf.format(new Date()));
		// 交易单号
		payLog.setTradeNo(payOrder.getTradeNo());
		// 交易平台标识
		payLog.setTerrace(payOrder.getTerrace());
		// 下订单报错日志
		payLogService.save(payLog);
	}

	// 支付成功页面
	protected void redirectPage(PayOrderEntity payOrder, ModelMap modelMap, String config, int state, int payMode) {
		// 商户订单号
		modelMap.addAttribute("outTradeNo", payOrder.getOrderNo());
		// 交易订单号
		modelMap.addAttribute("tradeNo", payOrder.getTradeNo());
		// 交易金额
		modelMap.addAttribute("totalAmount", payOrder.getTradeMoney());
		// 交易完成时间
		modelMap.addAttribute("date", sdf.format(new Date()));
		// 判断是否已支付
		if (state == 0) {
			modelMap.addAttribute("state", "支付成功");
		} else {
			modelMap.addAttribute("state", "该订单已支付");
		}
		// 交易商品名称
		modelMap.addAttribute("orderName", payOrder.getOrderName());
		if (config.equals("pro")) {
			if (payMode == 0) {
				modelMap.addAttribute("flag", "/");
			} else {
				modelMap.addAttribute("flag", "/pay/");
			}
		} else {
			if (payMode == 0) {
				modelMap.addAttribute("flag", "/");
			} else {
				modelMap.addAttribute("flag", "/hqajax/");
			}
		}
	}

	protected void notifyCall(HttpServletRequest request, HttpServletResponse response, String params, PayOrderEntity payOrder, String trade_no, String type, Integer payMode)
			throws IOException {
		String Result = "";
		switch (payOrder.getTerrace()){
			case "bw":
				Result = BwSynchronization(payOrder, trade_no, request, params,
						type, payMode);
				System.out.println("同步bw接口:" + Result);
				break;
			default:
				Result = NcSynchronization(payOrder, trade_no, request, params,
						type, payMode);
				System.out.println("同步nc接口:" + Result);
				break;
		}
		Gson gson = new Gson();
		NCResultCodeEntity ncResultCode = gson.fromJson(Result, NCResultCodeEntity.class);
		if (ncResultCode != null && ncResultCode.getCode().equals("1")) {
			response.getWriter().print("success");
		}
	}

	/**
	 * bw接口
	 * @param payOrder
	 * @param transaction_id
	 * @param request
	 * @param result
	 * @param tradesys
	 * @param payMode
	 * @return
	 */
	public String BwSynchronization(PayOrderEntity payOrder, String transaction_id, HttpServletRequest request,
					  String result, String tradesys, int payMode){
		PayOrderEntity order = null;
		PayConfig config = payConfigService.find();
		String ciphertext = MD5Util
				.string2MD5(payOrder.getTradeMoney() + payOrder.getOrderNo() + tradesys + "hengqijypay");
		String httpUrl = config.getBwUrl()
				+ "order/paysuccess?orderNo=" + payOrder.getOrderNo() + "&payType=" + tradesys + "&payMoney=" + payOrder.getTradeMoney() + "&ciphertext=" + ciphertext;

		String ncResult = HttpClientUtil.getInstance().sendHttpPost(httpUrl);
		return jxCallResult(payOrder, transaction_id, request, result, payMode, order, ncResult);
	}

	/**
	 * 
	 * @param payOrder
	 *            订单单号
	 * @param transaction_id
	 *            交易单号
	 * @param request
	 * @param result
	 *            微信或支付宝回调参数
	 * @param tradesys
	 *            wx/zfb
	 * @param payMode
	 *            0：支付宝 1：微信
	 * @return nc返回json格式数据
	 */
	// nc接口
	public String NcSynchronization(PayOrderEntity payOrder, String transaction_id, HttpServletRequest request,
			String result, String tradesys, int payMode) {
		PayOrderEntity order = null;
		Date d = new Date();
		String date = sdf.format(d);
		String ciphertext = MD5Util
				.string2MD5(payOrder.getOrderNo() + date + payOrder.getTradeMoney() + transaction_id + "hengqijypay");
		PayConfig config = payConfigService.find();
		String httpUrl = config.getNcUrl()
				+ "servlet/~hq/nc.impl.hq.webservice.collectionbill.UpdateCollectionBillStatusServlet?";
		String param = "orderNo=" + payOrder.getOrderNo() + "&orderTimestamp=" + date + "&tradeMoney="
				+ payOrder.getTradeMoney() + "&tradeNo=" + transaction_id + "&code=" + 0 + "&tradesys=" + tradesys
				+ "&ciphertext=" + ciphertext + "&terrace=" + payOrder.getTerrace();
		String ncResult = HttpClientUtil.getInstance().sendHttpPost(httpUrl, param);
		return jxCallResult(payOrder, transaction_id, request, result, payMode, order, ncResult);
	}


	private String jxCallResult(PayOrderEntity payOrder, String transaction_id, HttpServletRequest request, String result, int payMode, PayOrderEntity order, String ncResult) {
		Gson gson = new Gson();
		NCResultCodeEntity ncResultCode = gson.fromJson(ncResult, NCResultCodeEntity.class);
		if (ncResultCode != null && (ncResultCode.getCode().equals("1") || ncResultCode.getCode().equals("200"))) {
			order = new PayOrderEntity(payOrder.getOrderNo(), payOrder.getTradeMoney(), payOrder.getTerrace(), payMode,
					request.getServerName(), payOrder.getPayOrderNo(), result, payOrder.getTerrace() + ":同步成功",
					transaction_id);
		} else if (ncResultCode != null && ncResultCode.getCode().equals("0")) {
			order = new PayOrderEntity(payOrder.getOrderNo(), payOrder.getTradeMoney(), payOrder.getTerrace(), payMode,
					request.getServerName(), payOrder.getPayOrderNo(), result,
					payOrder.getTerrace() + "同步失败:" + ncResult, transaction_id);
		}
		saveLogInfo(order);
		return ncResult;
	}

}
