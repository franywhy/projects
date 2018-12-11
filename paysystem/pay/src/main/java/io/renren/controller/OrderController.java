package io.renren.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.entity.PayLogEntity;
import io.renren.entity.PayOrderEntity;
import io.renren.service.PayLogService;
import io.renren.service.PayOrderService;
import io.renren.utils.MD5Util;
import io.renren.utils.R;
import io.renren.utils.weixin.RandomStringGenerator;

/**
 * 统一下订单
 */
@Controller
public class OrderController {
	private static String commConfig = "";

	// 共同配置
	@Value("#{application['pom.comm.config']}")
	public void setCommConfig(String commConfig) {
		OrderController.commConfig = commConfig;
	}

	@Autowired
	private PayLogService payLogService;
	@Autowired
	private PayOrderService PayOrderService;

	/**
	 * nc保存订单
	 * 
	 * @param request
	 * @param response
	 * @param payOrder
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveorder", method = RequestMethod.POST)
	public R ncSaveOrder(HttpServletRequest request, HttpServletResponse response, PayOrderEntity payOrder) {
		String ciphertext = MD5Util.string2MD5(payOrder.getOrderNo() + payOrder.getOrderTimestamp()
				+ request.getParameter("tradeMoney") + "hengqijypay");
		String order_no = null;
		// 判断双方交互密钥
		if (!ciphertext.equals(payOrder.getCiphertext())) {
			return R.error("双方交互密钥不一致");
		}
		try {
			if (payOrder.getOrderNo() == null) {
				return R.error("订单号不能为空");
			} else if (payOrder.getOrderName() == null) {
				return R.error("订单名称不能为空");
			} else if (payOrder.getTradeMoney() == null) {
				return R.error("交易金额不能为空");
			} else if (payOrder.getOrderTimestamp() == null) {
				return R.error("时间戳不能为空");
			} else if (payOrder.getTerrace() == null) {
				return R.error("平台标志不能为空");
			} else if (payOrder.getCiphertext() == null) {
				return R.error("密钥不能为空");
			}
			// 下订单号
			order_no = RandomStringGenerator.getRandomStringByLength(20);
			if (payOrder.getTerrace().equals("nc")) {
				order_no = "NC" + order_no;
			}
			// 创建订单IP
			payOrder.setCreateOrderIp(request.getServerName());
			// 订单号
			payOrder.setPayOrderNo(order_no);
			// 订单名称
			payOrder.setOrderName(payOrder.getOrderName());
			PayOrderService.save(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
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
			return R.error("系统下单失败");
		}
		String serverName = request.getServerName();
		Integer port = request.getServerPort();
		String po = port.toString();
		// 区分测试线和正式线端口设置
		if (po == null) {
			po = "";
		} else {
			po = ":" + po;
		}
		return R.ok().put("http://" + serverName + po + "/qrcode.html?order_no=" + order_no + "^&flag=" + commConfig);
	}
}
