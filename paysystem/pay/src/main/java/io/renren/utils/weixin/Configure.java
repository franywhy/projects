package io.renren.utils.weixin;

public class Configure {
     //这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

	//public static final String KEY = "hengqi01234567899876543210hengqi";
	
	public static final String KEY = "hengqi12345678900987654321hengqi";

	//微信分配的公众号ID（开通公众号之后可以获取到）
	//public static final String APPID = "wx69a9416509e517c5";
	
	public static final String APPID="wx5e02795653121413";
	
	//AppSecret(应用密钥)
	//public static final String APPSECRET = "a6acfb11cca6d7456d5a6c9ea07b3de9";
	public static final String APPSECRET = "0c12ac212119b0a389f48c544ba50352";

	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	//public static final String MCHID = "1344054801";
	public static final String MCHID = "1486886322";
	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	public static final String TRADE_TYPE = "JSAPI";
	//微信签名算法，暂支持MD5
	public static final String SIGNTYPE = "MD5";
	
	/** 微信生成订单URL地址 */
	public static final String CREATE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/** 微信通过code获取appid 地址  详情参见:http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html */
	public static final String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	                                        
    /**回调地址**/
    public static final String NOTIFY_URL = "http://183.63.120.222:8010/pay/test/wxpay/callback/notify";
 
}
