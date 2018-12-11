package com.hqjy.pay.weixin;

public class Configure {

    public String getKey() {
        return null;
    }

    public String getAppId() {
        return null;
    }

    public String getAppSecret() {
        return null;
    }

    public String getMchId() {
        return null;
    }

    /**
     * 学来学往
     */
    public static class Account extends Configure {

        public String key = "hengqi12345678900987654321hengqi";

        //微信分配的公众号ID（开通公众号之后可以获取到）
        //public static final String APPID = "wx69a9416509e517c5";
        public String appId = "wx5e02795653121413";

        //AppSecret(应用密钥)
        //public static final String APPSECRET = "a6acfb11cca6d7456d5a6c9ea07b3de9";
        public String appSecret = "0c12ac212119b0a389f48c544ba50352";

        //微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
        //public static final String MCHID = "1344054801";
        public String mchId = "1486886322";

        private static final Account account = new Account();

        private Account(){}

        public static Account getInstance(){
            return account;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getAppId() {
            return appId;
        }

        @Override
        public String getAppSecret() {
            return appSecret;
        }

        @Override
        public String getMchId() {
            return mchId;
        }

    }

    /**
     * 自考
     */
    public static class Exam extends Configure {

        public String key = "qwertyuiopqazwsxedcrfvtgbyhnujmi";

        //微信分配的公众号ID（开通公众号之后可以获取到）
        //public static final String APPID = "wx69a9416509e517c5";
        public String appId = "wxfea6a234ae91c6c0";

        //AppSecret(应用密钥)
        //public static final String APPSECRET = "a6acfb11cca6d7456d5a6c9ea07b3de9";
        public String appSecret = "432306eef73d0a25e1f4bc26b9faf751";

        //微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
        //public static final String MCHID = "1344054801";
        public String mchId = "1505810181";

        private static final Exam exam = new Exam();

        private Exam(){}

        public static Exam getInstance(){
            return exam;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getAppId() {
            return appId;
        }

        @Override
        public String getAppSecret() {
            return appSecret;
        }

        @Override
        public String getMchId() {
            return mchId;
        }

    }

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
