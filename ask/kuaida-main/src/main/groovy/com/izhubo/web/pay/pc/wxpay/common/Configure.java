package com.izhubo.web.pay.pc.wxpay.common;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:40
 * 这里放置各种配置数据
 */
public class Configure {
	//这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
		// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
		// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

		public static final String KEY = "hengqi01234567899876543210hengqi";

		//微信分配的公众号ID（开通公众号之后可以获取到）
		public static final String APPID = "wx0d3b55b2b0d0254e";
		
		//AppSecret(应用密钥)
		public static final String APPSECRET = "c25b74f6c162ecda5164b95cf13faef8";

		//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
		public static final String MCHID = "1388643502";
		
		//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
		public static final String TRADE_TYPE = "JSAPI";
		
		public static final String TRADE_TYPE_APP = "APP";
		
		//微信签名算法，暂支持MD5
		public static final String SIGNTYPE = "MD5";
		
		/** 微信生成订单URL地址 */
		public static final String CREATE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		/** 微信通过code获取appid 地址  详情参见:http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html */
		public static final String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
		
}
