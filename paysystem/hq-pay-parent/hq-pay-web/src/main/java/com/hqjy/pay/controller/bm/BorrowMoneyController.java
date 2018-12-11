package com.hqjy.pay.controller.bm;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.utils.DateUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.hqjy.pay.BorrowMoneyEntity;
import com.hqjy.pay.BorrowMoneyService;
import com.hqjy.pay.HDDataEntity;
import com.hqjy.pay.HDJsonEntity;
import com.hqjy.pay.PayConfig;
import com.hqjy.pay.PayConfigService;
import com.hqjy.pay.bmutils.BMConfig;
import com.hqjy.pay.bmutils.HttpUtil;
import com.hqjy.pay.bmutils.SignatureUtils;
import com.hqjy.pay.bmutils.model.HdBizContent;
import com.hqjy.pay.bmutils.model.HdBizLoanInfo;
import com.hqjy.pay.bmutils.model.ReturnObj;
import com.hqjy.pay.utils.MD5Util;

import io.swagger.annotations.ApiOperation;

/**
 * 借款
 * 
 * @author wuyong
 */
@RequestMapping("/gateway")
@Controller
public class BorrowMoneyController {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private BorrowMoneyService borrowMoneyService;
	@Autowired
	private PayConfigService payConfigService;

	/**
	 * 查询借款订单信息
	 * 
	 * @param request
	 * @param response
	 * @param payOrder
	 * @return
	 */
	@ApiOperation("获取学员借款信息")
	@ResponseBody
	@RequestMapping(value = "/queryOrder", method = RequestMethod.POST)
	public Object queryOrder(HttpServletRequest request, HttpServletResponse response, HdBizContent hdBizContent) {
		HDJsonEntity HDJson = null;
		String tradeNo = null;
		String order_no = null;
		String result = null;
		BorrowMoneyEntity borrowMoney = new BorrowMoneyEntity();
		try {
			// 采用MD5加密
			String ciphertext = MD5Util.string2MD5(hdBizContent.getOrderStatus() + "hdBMGatewayQueryOrder");
			// 判断双方交互密钥
			if (!ciphertext.equals(hdBizContent.getCiphertext())) {
				HDJson = new HDJsonEntity();
				HDJson.setCode("500");
				HDJson.setMessage("双方交互密钥不一致");
				return HDJson;
			}
			// 审核状态必传
			if (hdBizContent.getOrderStatus() == null) {
				HDJson = new HDJsonEntity();
				HDJson.setCode("500");
				HDJson.setMessage("审批状态不能为空");
				return HDJson;
			}
			String timestamp = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
			// 1.组装业务参数
			HdBizContent bizContent = new HdBizContent();
			/**
			 * 0 待审批 5000 审批通过 7000 已放款
			 */
			bizContent.setOrderStatus(hdBizContent.getOrderStatus());
			/**
			 * 开始时间
			 */
			bizContent.setStartTime(hdBizContent.getStartTime());
			/**
			 * 结束时间
			 */
			bizContent.setEndTime(hdBizContent.getEndTime());

			// 2.序列化业务参数 首字母排序
			String biz_content = JSON.toJSONString(bizContent);

			// 3.生成签名
			String preSignStr = "app_id=" + BMConfig.appId + "&biz_content=" + biz_content
					+ "&sign_type=RSA2&timestamp=" + timestamp;
			System.out.println("待签名数据：=" + preSignStr);

			String sign = SignatureUtils.rsaSign(preSignStr, BMConfig.biz_hq_private_key, "UTF-8", "RSA2");
			// 4.组装请求结果
			Map<String, String> params = new HashMap<String, String>();
			params.put("app_id", BMConfig.appId);
			params.put("biz_content", biz_content);
			params.put("sign_type", "RSA2");
			params.put("timestamp", timestamp);
			params.put("sign", sign);
			PayConfig config = payConfigService.find();
			// 5.发送请求并接收结果
			result = HttpUtil.postFormResponse(config.getBorrowMoneyUrl(), params);

			ReturnObj returnObj = JSON.parseObject(result, ReturnObj.class);
			// 6.验证签名
			String returnObjStr = "code=" + returnObj.getCode() + "&message=" + returnObj.getMessage();
			if (StringUtils.isEmpty(returnObj.getData())) {
				returnObjStr += "&data=";
			} else {
				List<HdBizLoanInfo> loanInfos = JSON.parseArray(returnObj.getData(), HdBizLoanInfo.class);
				returnObjStr += "&data=" + JSON.toJSONString(loanInfos);
			}
			System.out.println("verfiy=====验证签名结果:" + returnObjStr);
			boolean verfiy = SignatureUtils.rsaCheck(returnObjStr, returnObj.getSign(), BMConfig.hd_public_key, "UTF-8",
					"RSA2");
			System.out.println("verfiy=====验证签名结果:" + verfiy);
			if (verfiy) {
				// 7.处理业务数据
				Gson gson = new Gson();
				HDJson = gson.fromJson(result, HDJsonEntity.class);
				List<HDDataEntity> listData = HDJson.getData();
				if (listData != null && listData.size() > 0) {
					for (HDDataEntity list : listData) {
						/**
						 * 状态:-100 系统繁忙，请稍候再试 0 请求成功 40001 参数必填参数 40002 签名验证失败
						 * 40003 业务处理失败
						 */
						borrowMoney.setCode(HDJson.getCode());
						// 订单号
						tradeNo = list.getTrade_no();
						borrowMoney.setTradeNo(tradeNo);
						/**
						 * 订单状态:0 待审批 -3000 订单取消 -1000 订单拒绝 2000 订单打回 5000 审批通过
						 * 7000 已放款 8000 提前结清 9000 正常结清
						 */
						borrowMoney.setOrderStatus(list.getOrder_status());
						// 订单状态中文描述
						borrowMoney.setOrderStatusDesc(list.getOrder_status_desc());
						// 审核通过时间
						borrowMoney.setApproveTime(list.getApprove_time());
						// 备注
						borrowMoney.setRemark(list.getRemark());
						// 名字
						borrowMoney.setIdName(list.getId_name());
						// 身份证号码
						borrowMoney.setIdNo(list.getId_no());
						// 手机号码
						borrowMoney.setPhoneNo(list.getPhone_no());
						// 贷款机构名称
						borrowMoney.setCapitalName(list.getCapital_name());
						// 申请贷款金额
						BigDecimal applyAmount = new BigDecimal(list.getApply_amount());
						borrowMoney.setApplyAmount(applyAmount);
						// 申请贷款时间
						borrowMoney.setApplyTime(list.getApply_time());
						// 商品内容
						borrowMoney.setCommodity(list.getCommodity());
						// 所属校区
						borrowMoney.setSchoolZone(list.getSchool_zone());
						// 放款订单号
						borrowMoney.setLendingTradeNo(list.getLending_trade_no());
						// 放款时间
						borrowMoney.setLendingTime(list.getLending_time());
						// 放款金额
						if (list.getLending_amount() != null) {
							BigDecimal lendingAmount = new BigDecimal(list.getLending_amount());
							borrowMoney.setLendingAmount(lendingAmount);
						}
						// 返回json格式
						borrowMoney.setBmJson(result);
						// 判断是否已同步过数据保存在数据库
						Map map = new HashMap();
						map.put("tradeNo", list.getTrade_no());
						BorrowMoneyEntity exist = borrowMoneyService.queryObject(map);
						if (exist != null) {
							// 判断订单号和订单状态是否存在，如果不存在就修改操作
							map.put("orderStatus", list.getOrder_status());
							BorrowMoneyEntity update = borrowMoneyService.queryObject(map);
							if (update == null) {
								// 修改时间
								borrowMoney.setModifyTime(new Date());
								borrowMoneyService.update(borrowMoney);
							}
						} else {
							// 创建时间
							borrowMoney.setCreateTime(new Date());
							borrowMoneyService.save(borrowMoney);
						}
					}
				}
			}
		} catch (Exception e) {
			borrowMoney.setError(e.toString());
			borrowMoney.setCreateTime(new Date());
			borrowMoneyService.save(borrowMoney);
			e.printStackTrace();
		} finally {
			return HDJson;
		}
	}
}
