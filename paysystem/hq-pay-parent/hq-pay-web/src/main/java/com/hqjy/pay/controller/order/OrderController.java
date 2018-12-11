package com.hqjy.pay.controller.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hqjy.pay.PayOrderService;
import com.hqjy.pay.controller.base.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hqjy.pay.PayLogService;
import com.hqjy.pay.PayOrderEntity;
import com.hqjy.pay.utils.MD5Util;
import com.hqjy.pay.utils.PMapUtils;
import com.hqjy.pay.utils.R;
import com.hqjy.pay.weixin.RandomStringGenerator;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 统一下订单
 * 
 * @author wuyong
 */
@RestController
public class OrderController extends BaseController {
	@Autowired
	private PayLogService payLogService;
	@Autowired
	private PayOrderService PayOrderService;

	/**
	 * nc保存订单
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation("支付下订单接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "orderName", value = "订单名称", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "tradeMoney", value = "交易金额", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "orderTimestamp", value = "下单时间戳", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "terrace", value = "平台标志", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "ciphertext", value = "双方相互密文", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "coursemajor", value = "课程专业", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "educlevel", value = "学历层次", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "classtypename", value = "班型名称", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "courseprice", value = "课程原价", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "discount", value = "优惠折扣", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "currentpaymoney", value = "本次需支付", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "overpaymoney", value = "剩余需支付", required = true, dataType = "String", paramType = "query"),
	})
	@RequestMapping(value = "/saveorder", method = RequestMethod.POST)
	public R ncSaveOrder(@RequestParam @ApiIgnore Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		return saveOrder(params, request);
	}

	public R saveOrder(Map<String, Object> params, HttpServletRequest request) {
		String payOrderNo = null;
		PayOrderEntity order = null;
		// 订单号
		String orderNo = PMapUtils.getString(params, "orderNo");
		// 订单名称
		String orderName = PMapUtils.getString(params, "orderName");
		// 交易金额
		String tradeMoney = PMapUtils.getString(params, "tradeMoney");
		//String tradeMoney = "0.01";
		// 设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
		BigDecimal bd = new BigDecimal(tradeMoney);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		// 订单时间戳
		String orderTimestamp = PMapUtils.getString(params, "orderTimestamp");
		// 平台标识
		String terrace = PMapUtils.getString(params, "terrace");
		// nc密文
		String ncCiphertext = PMapUtils.getString(params, "ciphertext");
		// 加密密文
		String ciphertext = MD5Util.string2MD5(orderNo + orderTimestamp + tradeMoney + "hengqijypay");
		String createOrderIp = request.getServerName();
		// 判断双方交互密钥
		try {
			if (orderNo == null) {
				return R.error("订单号不能为空");
			} else if (orderName == null) {
				return R.error("订单名称不能为空");
			} else if (tradeMoney == null) {
				return R.error("交易金额不能为空");
			} else if (orderTimestamp == null) {
				return R.error("时间戳不能为空");
			} else if (terrace == null) {
				return R.error("平台标志不能为空");
			} else if (ncCiphertext == null) {
				return R.error("密钥不能为空");
			} else if (!ciphertext.equals(ncCiphertext)) {
				return R.error("双方交互密钥不一致");
			} else {
				// 商户单号
				payOrderNo = terrace.toUpperCase() + RandomStringGenerator.getRandomStringByLength(26);
				/*if ("nc".equals(terrace)) {
					payOrderNo = "NC" + payOrderNo;
				} else if ("zk".equals(terrace)){
					payOrderNo = "ZK" + payOrderNo;
				}*/
				PayOrderEntity payOrder = null;
				if(!"zk".equals(terrace)){//如果不是自考订单则需要判断订单是否重复支付
					payOrder = new PayOrderEntity(orderNo, orderName, bd, createOrderIp, orderTimestamp,
							payOrderNo, ciphertext, terrace);
				}else{//如果是自考订单则需要构造自考订单实体
					String coursemajor = PMapUtils.getString(params, "coursemajor");
					String educlevel = PMapUtils.getString(params, "educlevel");
					String classtypename = PMapUtils.getString(params, "classtypename");
					String courseprice = PMapUtils.getString(params, "courseprice");
					String discount = PMapUtils.getString(params, "discount");
					String currentpaymoney = PMapUtils.getString(params, "currentpaymoney");
					String overpaymoney = PMapUtils.getString(params, "overpaymoney");
					if(StringUtils.isEmpty(coursemajor)){
						return R.error("课程专业不能为空");
					}else if (StringUtils.isEmpty(educlevel)){
						return R.error("学历层次不能为空");
					}else if (StringUtils.isEmpty(classtypename)){
						return R.error("班型名称不能为空");
					}else if (StringUtils.isEmpty(courseprice)){
						return R.error("课程原价不能为空");
					}else if (StringUtils.isEmpty(discount)){
						return R.error("优惠折扣不能为空");
					}else if (StringUtils.isEmpty(currentpaymoney)){
						return R.error("本次需支付不能为空");
					}else if (StringUtils.isEmpty(overpaymoney)){
						return R.error("剩余需支付不能为空");
					}
					BigDecimal bigcourseprice = toBigDecimal(courseprice);
					BigDecimal bigdiscount = toBigDecimal(discount);
					BigDecimal bigcurrentpaymoney = toBigDecimal(currentpaymoney);
					BigDecimal bigoverpaymoney = toBigDecimal(overpaymoney);
					payOrder = new PayOrderEntity(orderNo, orderName, bd, createOrderIp, orderTimestamp,
							payOrderNo, ciphertext, terrace, coursemajor, educlevel, classtypename, bigcourseprice, bigdiscount, bigcurrentpaymoney, bigoverpaymoney);
				}
				Map orderMap = new HashMap();
				orderMap.put("orderNo", orderNo);
				orderMap.put("state", 0);
				order = PayOrderService.queryOrderNo(orderMap);
				if (order != null) {
					return R.error(500, "该订单单号：" + orderNo + "已支付");
				}
				Map mapPlay = new HashMap();
				mapPlay.put("orderNo", orderNo);
				mapPlay.put("state", 1);
				// 判断是否存在订单号
				order = PayOrderService.queryOrderNo(mapPlay);
				if (order != null) {
					// 修改订单
					PayOrderService.updatePayOrderNo(payOrder);
				} else {
					// 保存订单
					PayOrderService.save(payOrder);
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
				/*if("zk".equals(terrace)){
					return R.ok().put("http://" + serverName + po + "/zkqrcode?payOrderNo=" + payOrderNo);
				}else if("bw".equals(terrace)){
					return R.ok().put("http://" + serverName + po + "/bwqrcode?payOrderNo=" + payOrderNo);
				}else{
					return R.ok().put("http://" + serverName + po + "/qrcode?payOrderNo=" + payOrderNo);
				}*/
				return R.ok().put("http://" + serverName + po + "/" + terrace + "qrcode?payOrderNo=" + payOrderNo);
			}
		} catch (Exception e) {
			PayOrderEntity payOrder = new PayOrderEntity(orderNo, bd, sdf.format(new Date()), terrace, createOrderIp,
					payOrderNo, "下订单失败：" + e.getMessage());
			// 保存日志信息
			saveLogInfo(payOrder);
			e.printStackTrace();
			return R.error("系统下单失败");
		}
	}

	/**
	 * 保留两位小数,四舍五入
	 * @param value
	 * @return
	 */
	private BigDecimal toBigDecimal(String value){
		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bigDecimal;
	}

}
