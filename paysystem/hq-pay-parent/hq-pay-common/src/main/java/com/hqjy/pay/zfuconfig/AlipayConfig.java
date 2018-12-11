package com.hqjy.pay.zfuconfig;

public class AlipayConfig {

	public String getAppId() {
		return null;
	}

	public String getPrivateKey() {
		return null;
	}

	public String getPublicKey() {
		return null;
	}


	/**
	 * 学来学往
	 */
	public static class Account extends AlipayConfig {

		private static final Account account = new Account();

		private Account(){}

		public static Account getInstance(){
			return account;
		}

		// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
		// public static String app_id = "2017052707365647";
		public String appId = "2017073107974185";
		// 商户私钥，您的PKCS8格式RSA2私钥
		// public static String merchant_private_key
		// ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCbWT+bpC8oXTxX+Uw7TGmKH37sn5tEEjX9cpAOc1L57Ed0Yqr20Bty1Lm6+WBTJDGxd+AXX+X1TnBxiXgabohoyar4jv9KimlfxI412L4FssxqauScvZ+jn5dcbl6UV7fNjHEreS9ltaigDlBFHC8CR4jmfLJoN2KjXyynYKWEtheIHJ1WCHbkEyiP2K0mNyFW/IrXTSZOM9+2Ac2kh/I07gOz/kw5qgkseG9GMp1ufiYao2oFiS9tBDnyyOOCIxzGGAb25HItcB44r2jiMcN5jkLXTaUUz92do7ph7s+/OeKPqtALMYrGNJUBt0mrODhdwN75X4tzoeYabEt5BhDNAgMBAAECggEASplGCUiDeL+lDZ3idItSFwje22QdnEeec5HDCZ1pmChVe9cxwAnZVIBAnD/KeMJltpIZI0Bi3jSHnI3mBdNUX/WPy4Jnp3Tl+x4ZKFRnk7bzoJqqI3aPpXQqrDE368Zfti4d3kF4eEAOtKWK+FT7Yf3WCNCSn6cRD+DRQaMQeLR0rrYEtTgeXSIAfzyNxbR5OVdqjcQS7+YQQGYO3Ygrsrzv2s5O73HngLskKkskzt0Jhr8TE7piLbX4qqMIBehRzrx72H1qr9SZUtONqUEiQJhGxdrMdsqIBwUGB9hhGvMxfg6vscIw9nSt2YeIX7QfTp8YTl3vXIpqy8zD9GccQQKBgQDS3C47e+1wqY8D9jh+hj712bqzEyYTueU2L1c25snh5JmjvSGf7QvslNKn7O+IxCqG3VojdVQfHsY1NfvNO8et+ia+NjcNSx6SaZmh1N5M7iQKI6J37W1o/0BBCGWTIKQonFTC25Kjw700dd70kvqKMvGWYXqCFv/eem3ekDa2/QKBgQC8mtv+su5tKP/rjRT6EOHM99znK+LuuQjPbc4NkJRdUtaFiAlfw5f+t6jczctCVQvMslQJ+rdg8zVJgnBQ7MT4xeuu1RWm9Ve+S8/bl9mBxboGm9Jl8aX3CthXckL0kdwxE+306LzjaK/gb0xObj4graQr5CMa1LEAqxGE30iyEQKBgQCSPp/aJdPCxfcpGkaIind0cHRq6ycHm3D8Kc1liKAQikp9JBoEH5abmYSZb12bD+Mm8rMn0KJtixyRn0VrTMDrS4o/dr6r+6PSjGc/tLIRzUE1vw1Z/FRb9F0OPdKlPohcmkC3fS6xYnsBvZbLLn2SCE9Cq40aWPVRyrwVFxS7yQKBgQC2vWkIyD/hvobwqNq5TGAr48DCr7AKU3go2yB53isSnS/DY8KJag88OqE/h5o4ZASxydtJbAX/xsZLujmu/yn+TG6GEGoRQchJVlVIVEHnzjfnLVVXdyKyUWy6lbgwFZ9+Yev+jv5tbMJ0OAJ8P66spPHboQkudwQlPJsV3e4msQKBgBa19x++n37ZEiloWONZn4Pc6v/uyl7cEzqrjY/+qYUcM+OiQdXP4+jOBqBklx05Pz83G3biaslmNrBlIYrFsACnhLxsinmB/1OAidTXthiApuCAjEoTklfp59CrwC9G70Q/dmPNjD6LxmUQKwOouaeUObj1A4cAMpp72Ip5MvgR";
		public String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDsY3wrHFkGyT26j7c1HSB+r5ndgEukTrxrJuuIdsxIqKvAb0OVHS18KSR9uwmIg94bZoRt9sL4bQUecf8ji9r9Iww+xXOQY5Rj9DXQ3lsa7JHzM/pECTIOERG6Yjrtva95pr/WqKO++ApcME8IBGjCg3veT+5FoNx0WtWkF3/kAoQ2UyN7fzpiHwzpBWymbJk06eKFJRLBIWGZfSBJXTdr1rp5vMReUBkn3m55CVJQ4JC9hVvDqO7pex1kfxVVTUt/jV4pQI5lzYeJqvoAn9rDNv6kIOUGKqFLsW84NpRwEGjhXmg7iogxHqRdZIH/aTbKAippXiEBtMYhuc2BhDMnAgMBAAECggEAOeF2MtwjFF1MJxUvh+rU3RBEPY8/dkDtM9N40pY8GQJr45ea1bUqQP+V9s9IW0vmo5Drlj1YqhmPqk/BaEPDYD4nrmsMi+tGF19hmf4IEsSgTAuLGQOjZPxa+ec5hKLwh3oD+Ryb05PKfZ46i70ApbgqKVoq/pOPx1HChAZZA0v998Onihw/S8uajW2L1XGfbC7HNeOCncPn3L74G+Sfwe/CT1zH4dnH/ja9fJz2HjDLeqD1HdTCkHeCvbZfKe8SUPCqa8HRk4cLaO/sCMukbTVpYlpy4kNubDf2Wmxde1mVkOpt5Bi505dLRBGiGLfc0ad+n823udIM6sohBY/F8QKBgQD4gQo4FSzIdmjoIkvWMzkXGkJf38sW0I725V0IEf4pziCejUR/1oqkK3QnCFB3YK1Vmsf8CosQyGpvL/dVE5muPfsPgEjp9DNkFNbnMRA7e2B+Q/YOVTbIVnEPdxtosUdAJMABWXyElj3+kX/OBLKDe6cq8mC1OeOn3lhRcVuQ+QKBgQDzhOOYp47OIROLMeWEueoHuw9X0SnMMAZzLidS3LrWUEQ/YraHZVV36+RXXzLKEsm0zbPanQ6cNQmLzoIW8O1YPiA8j/sdZRnUEucbJJCDE+3QxGtlrwyQXGHh6Onouw07hYhblG7RppG60SCrEmectUvKRbSJ5DeOuCxIrtkNHwKBgGn8Jw6aQVpR3hY7Ilr9x7iJs4tOWECnvIGiexYJDnxn9FOK7o8TQlgYTFalXiQ78FFVTTu6h2sl/vbi3zrCvEWrG5uV3AMj6pMhrDI4I7MqwJG5jIS6ZT32WAv/gYoaKcg6pEUvFEM8DL648Wg9aqFXKInRi9tfEDA1TTeXADWJAoGBAMO2VrC2Yag6nxlstZjRTkx+Q1uBqAUJx/VD76jB64sYoXEL5cS1ZVZnCHJL+Wa8aM3d0KFvPnG+kniBNL45TZdw/rmNwfUi7vopYwHGhjpChwwVAHeNkxxmkuhcJ7m6s2zV6fwoNFRceZzaPFYhCZLBhjL/iJSWDSyvyi90Ds9jAoGAc18aCJvHD+Tx2GfkUhc+RiGydOjvcDfsbrI3fHbuLud9MFeHxoarKdwA3VjT4f8bxLteXtMSIxfWcLLfCdhpUK5ZrBDjjU+AfEHqOq9yZcTkWb5DB8Jgs+qC3+mf5jIL9jjbluHEpZgw66Z8v5G27cCfTv9VD+sJDmG2piYTaSY=";
		// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
		// 对应APPID下的支付宝公钥。
		// public static String alipay_public_key =
		// "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtvnlI8dhgGa6hvg95YTl4uUkwh/V8xqBzn9RnMZ0LVZUIReE9S/ZDE1gWkkMI01sZcgzqaBHse31Nh/t2hMZKmv5Netm/w9UUhETRp9P72LXWORrONRyJXygFgKE+T3MXZbDTOPjjqlmNC9P204aHL8kTgqniHhTL8KGXaRrzhkzFcNFTBezdpfrYSfL3dcnMY55hUv4xej94ekVKlSm1JMgYFW3gjimGFubGQoMSNHz43hwh47LONPMhpT7blTqDexMUNnPMu8lvdEvKo9A54WYmXgdEAdypp/MwnkstLyW4MCk3uLFIMgcy+bDIvlWpBRE0AW0eqx4azZBG5mRJQIDAQAB";
		public String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmbuVY6CPhcJxTkpdH0Sxrm3R19YB7lJtp15jQt4BasYfJEW4FUG/kf2oqQ2sX3ElMO9q6f2hIHGn+oPOq8Py5FYikuydzXjwKPQV988loYw6tpLkY76/KDcyWkqAIsfWCCctgYPq4qHTuvx6r43YXimunA6oxb6c1q2jW0m6ZKY9ozsmKVnRpu32N6KzYhKKqvqlgsSc8cboi+3oR+1kDRVWOVTT7iKZ43csevZ8wQW2lP4xRPoXoT+GMrXTr2b8ePWElXmw6Ol7HjV5bJe/zaQymgo8DzWl6W+vxmVw3Ixk90vgzPxx0eXm5njKv4nTNXGiDBO2uvT9NLzMv2KwrQIDAQAB";

		@Override
		public String getAppId() {
			return appId;
		}

		@Override
		public String getPrivateKey() {
			return privateKey;
		}

		@Override
		public String getPublicKey() {
			return publicKey;
		}
	}

	/**
	 * 自考
	 */
	public static class Exam extends AlipayConfig {

		private static final Exam exam = new Exam();

		private Exam(){}

		public static Exam getInstance(){
			return exam;
		}

		// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
		// public static String app_id = "2017052707365647";
		public String appId = "2018062160354772";
		// 商户私钥，您的PKCS8格式RSA2私钥
		// public static String merchant_private_key
		// ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCbWT+bpC8oXTxX+Uw7TGmKH37sn5tEEjX9cpAOc1L57Ed0Yqr20Bty1Lm6+WBTJDGxd+AXX+X1TnBxiXgabohoyar4jv9KimlfxI412L4FssxqauScvZ+jn5dcbl6UV7fNjHEreS9ltaigDlBFHC8CR4jmfLJoN2KjXyynYKWEtheIHJ1WCHbkEyiP2K0mNyFW/IrXTSZOM9+2Ac2kh/I07gOz/kw5qgkseG9GMp1ufiYao2oFiS9tBDnyyOOCIxzGGAb25HItcB44r2jiMcN5jkLXTaUUz92do7ph7s+/OeKPqtALMYrGNJUBt0mrODhdwN75X4tzoeYabEt5BhDNAgMBAAECggEASplGCUiDeL+lDZ3idItSFwje22QdnEeec5HDCZ1pmChVe9cxwAnZVIBAnD/KeMJltpIZI0Bi3jSHnI3mBdNUX/WPy4Jnp3Tl+x4ZKFRnk7bzoJqqI3aPpXQqrDE368Zfti4d3kF4eEAOtKWK+FT7Yf3WCNCSn6cRD+DRQaMQeLR0rrYEtTgeXSIAfzyNxbR5OVdqjcQS7+YQQGYO3Ygrsrzv2s5O73HngLskKkskzt0Jhr8TE7piLbX4qqMIBehRzrx72H1qr9SZUtONqUEiQJhGxdrMdsqIBwUGB9hhGvMxfg6vscIw9nSt2YeIX7QfTp8YTl3vXIpqy8zD9GccQQKBgQDS3C47e+1wqY8D9jh+hj712bqzEyYTueU2L1c25snh5JmjvSGf7QvslNKn7O+IxCqG3VojdVQfHsY1NfvNO8et+ia+NjcNSx6SaZmh1N5M7iQKI6J37W1o/0BBCGWTIKQonFTC25Kjw700dd70kvqKMvGWYXqCFv/eem3ekDa2/QKBgQC8mtv+su5tKP/rjRT6EOHM99znK+LuuQjPbc4NkJRdUtaFiAlfw5f+t6jczctCVQvMslQJ+rdg8zVJgnBQ7MT4xeuu1RWm9Ve+S8/bl9mBxboGm9Jl8aX3CthXckL0kdwxE+306LzjaK/gb0xObj4graQr5CMa1LEAqxGE30iyEQKBgQCSPp/aJdPCxfcpGkaIind0cHRq6ycHm3D8Kc1liKAQikp9JBoEH5abmYSZb12bD+Mm8rMn0KJtixyRn0VrTMDrS4o/dr6r+6PSjGc/tLIRzUE1vw1Z/FRb9F0OPdKlPohcmkC3fS6xYnsBvZbLLn2SCE9Cq40aWPVRyrwVFxS7yQKBgQC2vWkIyD/hvobwqNq5TGAr48DCr7AKU3go2yB53isSnS/DY8KJag88OqE/h5o4ZASxydtJbAX/xsZLujmu/yn+TG6GEGoRQchJVlVIVEHnzjfnLVVXdyKyUWy6lbgwFZ9+Yev+jv5tbMJ0OAJ8P66spPHboQkudwQlPJsV3e4msQKBgBa19x++n37ZEiloWONZn4Pc6v/uyl7cEzqrjY/+qYUcM+OiQdXP4+jOBqBklx05Pz83G3biaslmNrBlIYrFsACnhLxsinmB/1OAidTXthiApuCAjEoTklfp59CrwC9G70Q/dmPNjD6LxmUQKwOouaeUObj1A4cAMpp72Ip5MvgR";
		public String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCQ8n7Kxax0Z1lFLaMYbFXhpYiIYCehz5wqc7Mlb0h7UMK/Z3n1uPReWbFWiXLy0W6RyIRzKpgrVCDSGjhwi47YM7BooTNMQWaHrJgpBdaPfOF0AbcHTsqo0uNp7h6glH/jE/odsqiZPsidw0VPeVr/zY3nE9r+M5ZJ0iPHaplIylOP6TxzRXAkNT4BJLQtyfxIIiJstreM5MUtaqKyc4pUgYgMIRJ4Pd/uMtYR7ALYGcaPgZHEN54KLreCAmoiTqHzBU3Xf+YgYGXgLEhogxxTLuB9+3Sbe+3EVsALss+nVtRxgUfwUUiMuBmh6HE2RGV5Yp/EeUC4Je8M4BG9MtQ1AgMBAAECggEAU2uc2WrHA/9u+0d2tFm1W7Z/S627YX8w66XHFuZjGsWyL6KIKImX6q1ISPu2MlWigziaMgXzqgwzD3iTvwIy5e9pue38rgh9z9vffsyjXdWLk85g3k3UiTFGPPMUoWqV58TZkDoAmqHtqA54V3MKkmplHvbodKCQAFB20xBgw+j4NIc9YmksqMfNDQpQkIAmeRQ0lUJwpz8AVXkkmux27OfTcoqyEKlfkBf6lqzkPOBZksE93NfLEK/NjGD8Mr1j5RBU4GOlNXZIRM0PoLLfepl24jO7bVERYR3un6k/xUugzYIRzvrO7knQtsrKN6WKQ5Zc3KHWM4bJgyZuzGa+wQKBgQD8PRS1kZOr/8Mjh2HBCIxixb/lnbp/3y9jcyZtdfPq1cedA3l4PfxJ3wRhW0G6i+KwbSVz9RPbn+NZhv6HFAyVmaJP6qPDhWMtMpHeN4p1ViODp65TEHpCu9f7c3Vy4nYXFf7lewniDBm5yqsVwbJFv2EyQR7nitsnkvu9U1lBuQKBgQCTG9SU7vre6VN0GhDwARZLiwaqsnVbMP009LiwhM1dYNgGFcs0O9WrAhzHAsRTVdXggfxwkmIGoxIAAOqbgb0HNwc+J41aGo0qmBefUr22AV5D4EK/xdyh4f0+GiJaZ0XT5ZiR4UlQo+FDJ2ZmQfQT2tn2wd85AoamH8yByX+UXQKBgEA0pBLbrYU4o0ll6qIiNOnngENIdPKbswpTg1KVlbMPG7KB8eeDj7aWCB1oB0Ee3mMeRUxQ8+8RseNWE2/60IE8DC7fgTQBvg8BMlQoOxxHAt8+TYdjSFu4SO/+ocTOlFSoCqwLN6bMgbfjOM1e8qwCXRFXtjAmDkbyiRXWI4AxAoGAZ/bHKeoqydbD781sIvHhu8mpEIHGhof8qqw4yidQq6mBgettDKEsxApeJBdEgFCtMlkq+rO5K0+brPz40KXD3ZF6QOba8s7I7U1HhIWJJVOaNwYKplVDM/TucDEwDgvmQXaFeLNqiN8Il9kEeUgrIs0MNWOWSkgKuOTINwEcdNECgYBxWRVKH5juq5JZk7/2FHXnN2XHClWhBEp/wlJ+2RL+ENdoutxDxdtOoVK3hRy2u/9AtF3ayYvdn0XJ+e5iYrq5N6ruepAh/JSAYTjfnng6V5qiAepVfmSq3mW1VAmVHz8ii/C/DXuhRzRm8lOw2vm+qokuFbGOrBKgJXnOd60Qrw==";
		// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
		// 对应APPID下的支付宝公钥。
		// public static String alipay_public_key =
		// "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtvnlI8dhgGa6hvg95YTl4uUkwh/V8xqBzn9RnMZ0LVZUIReE9S/ZDE1gWkkMI01sZcgzqaBHse31Nh/t2hMZKmv5Netm/w9UUhETRp9P72LXWORrONRyJXygFgKE+T3MXZbDTOPjjqlmNC9P204aHL8kTgqniHhTL8KGXaRrzhkzFcNFTBezdpfrYSfL3dcnMY55hUv4xej94ekVKlSm1JMgYFW3gjimGFubGQoMSNHz43hwh47LONPMhpT7blTqDexMUNnPMu8lvdEvKo9A54WYmXgdEAdypp/MwnkstLyW4MCk3uLFIMgcy+bDIvlWpBRE0AW0eqx4azZBG5mRJQIDAQAB";
		public String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxOGveLcpjRzl9XmUf5gADrP3AIkJhczS7xfl/FJpL3CWxYj3uuCICymuGTg9r3KovA493YrWp3fzmqi5dldippM2ogNDvyJt1rYOe6Ouiwi9Iv3i63IovSsu7IOIpKhR3eumLpIvJeol/lW9vUG4ACfmNGw93glzuwvrjrIrJjZT/qJSgnFFj5baLneBFSyMJSRO1NCXHadtzR+x4v2tIpOoIxa0Kp5aS+w3D+fr/R+fY1wipqFGQ7gHnpH2DAM4R2djcS5nl/stcjJXKMSRTJ/aSoVvSOCqIQVsij4QOFYxcQsPGze90eCShAt0L7WJt352y3B/zrofM7jqpRPpDQIDAQAB";

		@Override
		public String getAppId() {
			return appId;
		}

		@Override
		public String getPrivateKey() {
			return privateKey;
		}

		@Override
		public String getPublicKey() {
			return publicKey;
		}

	}

	// 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	/*
	 * public static String notify_url =
	 * "http://pay.hqjy.com/alipay/callback/notify"; public static String
	 * return_url = "http://pay.hqjy.com/alipay/callback/return";
	 */

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	// public static String return_url =
	// "http://177.77.83.112:8080/school-webapp/alipay";

	public static String return_url = "http://test.pay.hqjy.com/alipay/callback/return";
	public static String notify_url = "http://test.pay.hqjy.com/alipay/callback/notify";
	// 签名方式
	public static String sign_type = "RSA2";

	// 字符编码格式
	public static String charset = "utf-8";

	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";

	// 支付宝网关
	public static String log_path = "C:\\";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑



	
}
