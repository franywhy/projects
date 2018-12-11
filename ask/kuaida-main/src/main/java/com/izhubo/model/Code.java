package com.izhubo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * date: 13-8-5 下午1:56
 * 
 * @author: wubinjie@ak.cc
 */
public interface Code {

	Integer OK = 1;
	String OKS = "1";
	String OK_S = "";

	String CODE_KEY = "code";
	String MSG_KEY = "msg";
	String MESSAGE_KEY = "message";
	String DATA_KEY = "data";

	Integer 系统异常稍后再试 = 99;
	String 系统异常稍后再试S = "99";
	String 系统异常稍后再试_S = "系统异常稍后再试";

	Integer 余额不足 = 30412;
	String 余额不足_S = "余额不足";

	Integer 权限不足 = 30413;
	String 权限不足_S = "权限不足";

	Integer 验证码验证失败 = 30419;
	String 验证码验证失败_S = "验证码验证失败";

	Integer 参数无效 = 30406;
	String 参数无效S = "30406";
	String 参数无效_S = "参数无效";

	Integer 提问保存错误 = 40000;
	String 提问保存错误_S = "提问保存错误";

	Integer 已被抢答 = 40001;
	String 已被抢答S = "40001";
	String 已被抢答_S = "已被抢答";
	Integer 红包已打开 = 40002;
	String 红包已打开S = "40002";
	String 红包已打开_S = "红包已打开";

	Integer 申请金额超出总金额 = 60001;
	String 申请金额超出总金额S = "60001";
	String 申请金额超出总金额_S = "申请金额超出总金额";

	Integer 密码不正确 = 60002;
	String 密码不正确S = "60002";
	String 密码不正确_S = "密码不正确";

	Integer 学员未设置关注标签 = 60100;
	String 学员未设置关注标签_S = "未设置关注标签";
	Integer 学员设置关注标签数量超出限制 = 60101;
	String 学员设置关注标签数量超出限制_S = "标签数量超出限制";
	Integer 学员设置关注标签数量超出权限 = 60101;
	String 学员设置关注标签数量超出权限_S = "您没有权限设置此标签";
	Integer 学员未提交标签 = 60102;
	String 学员未提交标签_S = "请选择您的标签";
	Integer 教师账号未正确关联校区 = 60111;
	String 教师账号未正确关联校区_S = "教师账号未正确关联校区";
	Integer 校区未上传版本号 = 60113;
	String 校区未上传版本号_S = "数据未上传";
	Integer 问题已收藏 = 60114;
	String 问题已收藏_S = "问题已收藏";
	Integer 招生权限 = 61211;
	String 招生权限_S = "没有招生权限!";

	Integer 教务权限 = 61212;
	String 教务权限_S = "没有教务权限!";

	
	public static class TOPICS {
		public static final Integer 学生问题时限_CODE = 40101;
		public static final String 学生问题时限_MSG = "您刚刚提问过啦，请1分钟后再来~";

		public static Map 学生问题时限() {
			Map m = new HashMap();
			m.put(CODE_KEY, 学生问题时限_CODE);
			m.put(MESSAGE_KEY, 学生问题时限_MSG);
			m.put(DATA_KEY, 学生问题时限_MSG);
			return m;
		}

		public static final Integer 教师抢答时限_CODE = 40102;
		public static final String 教师抢答时限_MSG = "您刚刚成功抢答了1题，请1分钟后再来~";

		public static Map 教师抢答时限() {
			Map m = new HashMap();
			m.put(CODE_KEY, 教师抢答时限_CODE);
			m.put(MSG_KEY, 教师抢答时限_MSG);
			m.put(DATA_KEY, 教师抢答时限_MSG);
			return m;
		}

		public static final Integer 免费提问限制_CODE = 40103;
		public static final String 免费提问限制_MSG = "您当日已经没有免费权限了!";

		public static Map 免费提问限制() {
			Map m = new HashMap();
			m.put(CODE_KEY, 免费提问限制_CODE);
			m.put(MSG_KEY, 免费提问限制_MSG);
			m.put(DATA_KEY, 免费提问限制_MSG);
			return m;
		}

		public static final Integer 提问标签限制_CODE = 40104;
		public static final String 提问标签限制_MSG = "您没有此标签的提问权限~";

		public static Map 提问标签限制() {
			Map m = new HashMap();
			m.put(CODE_KEY, 提问标签限制_CODE);
			m.put(MSG_KEY, 提问标签限制_MSG);
			m.put(DATA_KEY, 提问标签限制_MSG);
			return m;
		}
	}

	public static class COMMODITYS {

		public static final Integer 重复购买_CODE = 80001;
		public static final String 重复购买_MSG = "重复购买";

		public static Map 重复购买() {
			Map m = new HashMap();
			m.put(CODE_KEY, 重复购买_CODE);
			m.put(MSG_KEY, 重复购买_MSG);
			m.put(DATA_KEY, 重复购买_MSG);
			return m;
		}

		public static final Integer 商品已下架_CODE = 80002;
		public static final String 商品已下架_MSG = "商品已下架";

		public static Map 商品已下架() {
			Map m = new HashMap();
			m.put(CODE_KEY, 商品已下架_CODE);
			m.put(MSG_KEY, 商品已下架_MSG);
			m.put(DATA_KEY, 商品已下架_MSG);
			return m;
		}

		public static final Integer 商品不存在_CODE = 80003;
		public static final String 商品不存在_MSG = "商品不存在";

		public static Map 商品不存在() {
			Map m = new HashMap();
			m.put(CODE_KEY, 商品不存在_CODE);
			m.put(MSG_KEY, 商品不存在_MSG);
			m.put(DATA_KEY, 商品不存在_MSG);
			return m;
		}

	}

	public static class DISCOUNT {
		public static final Integer 优惠券已领取过_CODE = 70201;
		public static final String 优惠券已领取过_MSG = "优惠券已领取过";

		public static final Integer 当天优惠券数量不足_CODE = 70202;
		public static final String 当天优惠券数量不足_MSG = "当天优惠券数量不足";

		public static Map 当天优惠券数量不足() {
			Map m = new HashMap();
			m.put(CODE_KEY, 当天优惠券数量不足_CODE);
			m.put(MSG_KEY, 当天优惠券数量不足_MSG);
			m.put(DATA_KEY, 当天优惠券数量不足_MSG);
			return m;
		}

		public static Map 优惠券已领取过() {
			Map m = new HashMap();
			m.put(CODE_KEY, 优惠券已领取过_CODE);
			m.put(MSG_KEY, 优惠券已领取过_MSG);
			m.put(DATA_KEY, 优惠券已领取过_MSG);
			return m;
		}
	}

	public static class ORDERS {
		public static final Integer 订单无效_CODE = 70101;
		public static final String 订单无效_MSG = "订单已支付成功";

		public static Map 订单无效() {
			Map m = new HashMap();
			m.put(CODE_KEY, 订单无效_CODE);
			m.put(MSG_KEY, 订单无效_MSG);
			m.put(DATA_KEY, 订单无效_MSG);
			return m;
		}

		public static final Integer 优惠券使用错误_CODE = 70102;
		public static final String 优惠券使用错误_MSG = "优惠券使用错误";

		public static Map 优惠券使用错误() {
			Map m = new HashMap();
			m.put(CODE_KEY, 优惠券使用错误_CODE);
			m.put(MSG_KEY, 优惠券使用错误_MSG);
			m.put(DATA_KEY, 优惠券使用错误_MSG);
			return m;
		}

		public static final Integer 订单支付金额错误_CODE = 70103;
		public static final String 订单支付金额错误_MSG = "订单支付金额错误";

		public static Map 订单支付金额错误() {
			Map m = new HashMap();
			m.put(CODE_KEY, 订单支付金额错误_CODE);
			m.put(MSG_KEY, 订单支付金额错误_MSG);
			m.put(DATA_KEY, 订单支付金额错误_MSG);
			return m;
		}

		public static final Integer 订单已支付成功_CODE = 70201;
		public static final String 订单已支付成功_MSG = "订单已支付成功";

		public static Map 订单已支付成功() {
			Map m = new HashMap();
			m.put(CODE_KEY, 订单已支付成功_CODE);
			m.put(MSG_KEY, 订单已支付成功_MSG);
			m.put(DATA_KEY, 订单已支付成功_MSG);
			return m;
		}

		public static final Integer 订单已支付失败_CODE = 70202;
		public static final String 订单已支付失败_MSG = "订单已支付成功";

		public static Map 订单已支付失败() {
			Map m = new HashMap();
			m.put(CODE_KEY, 订单已支付失败_CODE);
			m.put(MSG_KEY, 订单已支付失败_MSG);
			m.put(DATA_KEY, 订单已支付失败_MSG);
			return m;
		}
	}

}