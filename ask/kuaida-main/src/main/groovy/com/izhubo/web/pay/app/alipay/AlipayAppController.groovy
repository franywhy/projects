package com.izhubo.web.pay.app.alipay

import static com.izhubo.rest.common.util.WebUtils.$$

import java.text.SimpleDateFormat
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.AlipayClient
import com.alipay.api.DefaultAlipayClient
import com.izhubo.alipay.config.AlipayConfig
import com.izhubo.alipay.util.AlipaySubmit
import com.izhubo.model.DR
import com.izhubo.rest.anno.Rest
import com.izhubo.web.pay.PayBaseController
import com.mysqldb.model.Orders

/**
 * 支付宝支付
 * @author shihongjie
 *
 */
//@Controller
//@RequestMapping("/alipay")
@Rest
class AlipayAppController extends PayBaseController{
	
	@Resource
	private SessionFactory sessionFactory;
	
	@Value("#{application['notify.alipay.url']}")
	private String notify_url;

	
	private String APP_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMcwqY5inZC//Wt7khEu1pe0ENn65lOn6lm2toi7gn52V8i+hKk9hAZnzIZ5Zj9uc5/4SOoO0MLADvTmzUVf/7n6fHiZ4QgayQFicgSsykJ7kV2U+/B9xTedgEBV+/n7VadTUvyF0i3R62UNjxSRNq6+kvFh6g0kHd46K/nn40iLAgMBAAECgYAED4HlXS1JHiDUaBh6MTI+tXUCfJLlca7dVVQzRV9LpK31cazSpmZimwsYUrE66c2BnVYfL14iGjgz1eCpYWOdJbmObrSZH3se8YuXwv3ohWM7TnZcZW/SLGnWLj36JV9lfBBlLttqjZ2OojdmS1nC3LAYNShDFkQ8NTGfGqabwQJBAPSn1nonQtWhv/C0/k9CxNNFiefMWCi62x9q642eP2jpnVFqfDDm+YcLE61Nen2LfC3gnHOG0a658/koHMimCy0CQQDQbSFOqnlf7tbHGMAfIBxzdRVEoHwC/KO6od0y3/QufFqXAlJ8qQSS5ysYldC8R+Y3OEq6LYhjH/3ohVbwKBWXAkAaIbMu6PKijtxJgYuIO8F6L3tDXvgroYm2GV1/it+K/SjogU8yifktC2nWoOfscPGalNshG81ZtFLy0e4BIHshAkBXLl6xPgN5CjlSnFq4akEcVkwoIYzd3vlJdq1Eu4Ky8xcddT41oKkmHYXcScY/C0ATtvsi9yrGc7pMSYH9AHdPAkEA4sFVFbsZbkgYjCBNCYcRVV50XfQ4dY1BMr18Gpt3Z/dfPBSsrtHCEHYJPSSG43aj/7agIwgJd2QDB7ozNWx+Sg==";
	
	private String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	
	
	private String APP_ID = "2016040501268794";
	
	
	
	
	
	
	/**
	 * 构造支付订单参数信息
	 *
	 * @param map
	 * 支付订单参数
	 * @return
	 */
	public static String buildOrderParam(Map<String, String> map) {
		List<String> keys = new ArrayList<String>(map.keySet());

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			sb.append(buildKeyValue(key, value, true));
			sb.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		sb.append(buildKeyValue(tailKey, tailValue, true));

		return sb.toString();
	}
	
	/**
	 * 拼接键值对
	 *
	 * @param key
	 * @param value
	 * @param isEncode
	 * @return
	 */
	private static String buildKeyValue(String key, String value, boolean isEncode) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append("=");
		if (isEncode) {
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				sb.append(value);
			}
		} else {
			sb.append(value);
		}
		return sb.toString();
	}
	
	/**
	 * 对支付参数信息进行签名
	 *
	 * @param map
	 *            待签名授权信息
	 *
	 * @return
	 */
	public static String getSign(Map<String, String> map, String rsaKey) {
		List<String> keys = new ArrayList<String>(map.keySet());
		// key排序
		Collections.sort(keys);

		StringBuilder authInfo = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			authInfo.append(buildKeyValue(key, value, false));
			authInfo.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		authInfo.append(buildKeyValue(tailKey, tailValue, false));

		String oriSign = AlipaySignature.rsaSign(authInfo.toString(), rsaKey, "UTF-8")
		
		String encodedSign = "";

		try {
			encodedSign = URLEncoder.encode(oriSign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodedSign;
	}
	
	
	
	
	
	@Override
	public Object factoryPayHtml(Orders order) {
		//返回访问地址
		String sHtmlText = null;
		////////////////////////////////////请求参数//////////////////////////////////////
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = order.getOrderno();

		//订单名称，必填
		String subject = order.getOrderName();

		//付款金额，必填
		String total_fee = order.getPayMoney().toString();

		//商品描述，可空
		String body = order.getOrderDescribe();

		//////////////////////////////////////////////////////////////////////////////////
		
		//构造业务参数
		Map<String, String> bParaTemp = new HashMap<String, String>();
		bParaTemp.put("body", body);
		bParaTemp.put("subject", subject);
		bParaTemp.put("out_trade_no", out_trade_no);
		bParaTemp.put("timeout_express", "90m");
		bParaTemp.put("total_amount", total_fee);
		bParaTemp.put("product_code", "QUICK_MSECURITY_PAY");
		
				
		//构造公共参数
		Map<String, String> sParaTemp = new HashMap<String, String>();
		//信息确认
		sParaTemp.put("app_id", APP_ID);
		sParaTemp.put("method", "alipay.trade.app.pay");
		sParaTemp.put("format", "JSON");
		sParaTemp.put("charset", AlipayConfig.input_charset);
		sParaTemp.put("sign_type", "RSA");
		sParaTemp.put("sign", "");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		sParaTemp.put("timestamp", key);
		sParaTemp.put("version", "1.0");
	    sParaTemp.put("notify_url", notify_url);//后台接口
		sParaTemp.put("biz_content", buildOrderParam(bParaTemp));//后台接口
		sParaTemp.put("sign", getSign(sParaTemp,APP_PRIVATE_KEY));
		
		
		
		
		 
		
		return buildOrderParam(sParaTemp);
	}

	@Override
	public String payName() {
		return "alipay_app";
	}
	
}
