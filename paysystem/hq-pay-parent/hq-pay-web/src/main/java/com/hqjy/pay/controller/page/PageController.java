package com.hqjy.pay.controller.page;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hqjy.pay.PayLogService;
import com.hqjy.pay.PayOrderEntity;
import com.hqjy.pay.PayOrderService;
import com.hqjy.pay.controller.base.BaseController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
public class PageController extends BaseController{
	@Autowired
	private PayOrderService PayOrderService;

	@ApiOperation("下订单成功跳转生成二维码页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_no", value = "商户单号", paramType = "query", required = true, dataType = "String"), })
	@RequestMapping(value = "/bwqrcode", method = RequestMethod.GET)
	public String bwqrcode(String payOrderNo, String flag, ModelMap modelMap) {
		Map map = new HashMap();
		map.put("payOrderNo", payOrderNo);
		PayOrderEntity payOrder = PayOrderService.queryObject(map);
		if (payOrder == null) {
			return null;
		}
		modelMap.addAttribute("payOrderNo", payOrder.getPayOrderNo());
		modelMap.addAttribute("orderNo", payOrder.getOrderNo());
		modelMap.addAttribute("tradeMoney", payOrder.getTradeMoney());
		modelMap.addAttribute("orderName", payOrder.getOrderName());
		modelMap.addAttribute("flag",config);
		modelMap.addAttribute("payUrl",url);
		return "bwqrcode";
	}

	@ApiOperation("下订单成功跳转生成二维码页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_no", value = "商户单号", paramType = "query", required = true, dataType = "String"), })
	@RequestMapping(value = "/ncqrcode", method = RequestMethod.GET)
	public String qrcode(String payOrderNo, String flag, ModelMap modelMap) {
		Map map = new HashMap();
		map.put("payOrderNo", payOrderNo);
		PayOrderEntity payOrder = PayOrderService.queryObject(map);
		if (payOrder == null) {
			return null;
		}
		modelMap.addAttribute("payOrderNo", payOrder.getPayOrderNo());
		modelMap.addAttribute("orderNo", payOrder.getOrderNo());
		modelMap.addAttribute("tradeMoney", payOrder.getTradeMoney());
		modelMap.addAttribute("orderName", payOrder.getOrderName());
		modelMap.addAttribute("flag",config);
		modelMap.addAttribute("payUrl",url);
		return "ncqrcode";
	}

	@ApiOperation("下自考订单成功跳转生成二维码页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_no", value = "商户单号", paramType = "query", required = true, dataType = "String"), })
	@RequestMapping(value = "/zkqrcode", method = RequestMethod.GET)
	public String zkqrcode(String payOrderNo, String flag, ModelMap modelMap) {
		Map map = new HashMap();
		map.put("payOrderNo", payOrderNo);
		PayOrderEntity payOrder = PayOrderService.queryObject(map);
		if (payOrder == null) {
			return null;
		}
		modelMap.addAttribute("payOrderNo", payOrder.getPayOrderNo());
		modelMap.addAttribute("orderNo", payOrder.getOrderNo());
		modelMap.addAttribute("tradeMoney", payOrder.getTradeMoney());
		modelMap.addAttribute("orderName", payOrder.getOrderName());
		modelMap.addAttribute("coursemajor", payOrder.getCourseMajor());
		modelMap.addAttribute("educlevel", payOrder.getEduLevel());
		modelMap.addAttribute("classtypename", payOrder.getClassTypeName());
		modelMap.addAttribute("courseprice", payOrder.getCoursePrice());
		modelMap.addAttribute("discount", payOrder.getDiscount());
		modelMap.addAttribute("currentpaymoney", payOrder.getCurrentPayMoney());
		modelMap.addAttribute("overpaymoney", payOrder.getOverPayMoney());
		modelMap.addAttribute("flag",config);
		modelMap.addAttribute("payUrl",url);
		return "zkqrcode";
	}

	@ApiOperation("扫描二维码进入支付js页面")
	@ApiImplicitParam(name = "o", value = "商户单号", paramType = "query", required = true, dataType = "String")
	@RequestMapping(value = "/p", method = RequestMethod.GET)
	public String p(String o, ModelMap modelMap) {
		modelMap.addAttribute("payOrderNo", o);
		modelMap.addAttribute("flag",config);
		modelMap.addAttribute("payUrl",url);
		return "pay";
	}
}
