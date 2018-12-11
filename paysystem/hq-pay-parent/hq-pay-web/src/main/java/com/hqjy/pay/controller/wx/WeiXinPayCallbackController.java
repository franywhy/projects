package com.hqjy.pay.controller.wx;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hqjy.pay.utils.SwitchTrackSupport;
import com.hqjy.pay.weixin.Configure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
import com.hqjy.pay.utils.R;
import com.hqjy.pay.weixin.HttpClientUtil;
import com.hqjy.pay.weixin.Signature;
import com.hqjy.pay.weixin.XMLParser;

import io.swagger.annotations.ApiOperation;

/**
 * 微信支付成功回调接口
 * 
 * @author wuyong
 */
@Controller
public class WeiXinPayCallbackController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PayOrderService PayOrderService;
	@Autowired
	private PayLogService payLogService;
	@Autowired
	private PayConfigService payConfigService;

	/**
	 * 微信支付的通知接口。
	 */
	@SuppressWarnings("unused")
	@ApiOperation("微信支付的回调接口")
	@RequestMapping(value = "/wxpay/callback/notify")
	public ModelAndView notify(HttpServletRequest request, HttpServletResponse response) {
	    String result = null;
		PayOrderEntity order = null;
		String trade_no = null;
		BigDecimal total_fee = null;
		String transaction_id = null;
		PayOrderEntity payOrder = null;
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
			result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			Map<String, Object> map = XMLParser.getMapFromXML(result);

			trade_no = map.get("out_trade_no").toString();// 商户单号
			if (trade_no == null) {
				logger.error("交易单号不能为空：{}", trade_no);
				return null;
			}
			// 根据微信回调订单号查询pay_order对象
			Map payOrderMap = new HashMap();
			payOrderMap.put("payOrderNo", trade_no);
			payOrder = PayOrderService.queryObject(payOrderMap);
			Boolean check = Signature.checkIsSignValidFromResponseString(map, SwitchTrackSupport.WeChatPay(payOrder.getTerrace()).getKey());
			// ===========获取微信回调参数

			String time_end = map.get("time_end").toString();// 支付完成时间
			// 订单金额
			total_fee = new BigDecimal(map.get("total_fee").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 订单金额
			transaction_id = map.get("transaction_id").toString();// 交易单号
			logger.info("微信商户单号：{}", transaction_id);
			logger.info("微信交易单号：{}", trade_no);
			if (transaction_id == null) {
				logger.error("商户单号不能为空：{}", transaction_id);
				return null;
			} else if (trade_no == null) {
				logger.error("交易单号不能为空：{}", trade_no);
				return null;
			} else if (total_fee == null) {
				logger.error("订单金额不能为空：{}", total_fee);
				return null;
			} else if (!check) {
				order = new PayOrderEntity(payOrder.getOrderNo(), payOrder.getTradeMoney(), payOrder.getTerrace(), 1,
						request.getServerName(), transaction_id, result, "API返回的数据签名数据不存在，有可能被第三方篡改!!!" + result,
						trade_no);
				// 保存日志信息
				saveLogInfo(order);
				logger.error("API返回的数据签名数据不存在，有可能被第三方篡改!!!：商户单号：{}", transaction_id);
				return null;
			} else {
				// 这里需要调试订单已关闭状态是否需要判断
				if ("SUCCESS".equals(map.get("result_code"))) {
					// ===========判断是否支付成功，防止微信再次回调
					boolean  bool=payLogService.queryTotal(transaction_id);
					if (!bool) {
						if (payOrder != null) {
							// 判断订单金额是否跟回调参数金额是否一致
							BigDecimal money = total_fee.divide(new BigDecimal(100)).setScale(2,
									BigDecimal.ROUND_HALF_UP);
							if (!payOrder.getTradeMoney().equals(money)) {
								order = new PayOrderEntity(payOrder.getOrderNo(), total_fee, sdf.format(new Date()),
										payOrder.getTerrace(), null, transaction_id,
										"订单金额有误==" + "订单金额：" + payOrder.getTradeMoney() + "微信回调金额：" + total_fee);
								// 保存日志信息
								saveLogInfo(order);
								return null;
							}
							// ===============修改数据库支付成功状态=============
							// transaction_id=4200000077201803106227886173
							order = new PayOrderEntity(transaction_id, trade_no, sdf.format(new Date()));
							PayOrderService.update(order);
							// 同步nc接口
							notifyCall(request, response, result, payOrder, trade_no, "wx", 1);
						}
					} else {
						return new ModelAndView("success");
					}
				}
			}
		} catch (Exception e) {
			order = new PayOrderEntity(payOrder.getOrderNo(), total_fee, payOrder.getTerrace(), 1,
					request.getServerName(), trade_no, result, payOrder.getTerrace() + "微信报错信息：" + e.getMessage(),
					transaction_id);
			// 保存日志信息
			saveLogInfo(order);
			logger.error("订单单号：{}，商户单号：{}，交易单号：{}，，报错信息：{}", payOrder.getOrderNo(), trade_no, transaction_id,
					e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
