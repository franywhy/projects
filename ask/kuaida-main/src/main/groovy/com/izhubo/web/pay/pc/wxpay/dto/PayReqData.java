package com.izhubo.web.pay.pc.wxpay.dto;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.izhubo.web.pay.pc.wxpay.common.Configure;
import com.izhubo.web.pay.pc.wxpay.common.RandomStringGenerator;
import com.izhubo.web.pay.pc.wxpay.common.Signature;

/**
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 * 
 * @author Administrator
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement (name = "xml")  
public class PayReqData {

	/** 公众账号ID */
	@XmlElement
	private String appid;

	/** 商户号 */
	@XmlElement
	private String mch_id;

	/** 随机字符串 */
	@XmlElement
	private String nonce_str;

	/** 签名 */
	@XmlElement
	private String sign;

	/** 商品描述 */
	@XmlElement
	private String body;

	/** 商品详情 */
	@XmlElement
	private String detail;

	/** 附加数据 */
	@XmlElement
	private String attach;

	/** 商户订单号 */
	@XmlElement
	private String out_trade_no;

	/** 总金额 订单总金额，单位为分 */
	@XmlElement
	private String total_fee;

	/** 终端IP */
	@XmlElement
	private String spbill_create_ip;

	/** 通知地址 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。 */
	@XmlElement
	private String notify_url;

	/** 交易类型 取值如下：JSAPI，NATIVE，APP */
	@XmlElement
	private String trade_type = Configure.TRADE_TYPE;
	
	/** 用户标识 @see https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1 */
	@XmlElement
	private String openid ;

	
	
	
	public PayReqData() {
		super();
	}

	public PayReqData(String appid, String mch_id, String nonce_str,
			String sign, String body, String detail, String attach,
			String out_trade_no, String total_fee, String spbill_create_ip,
			String notify_url, String trade_type) {
		super();
		this.appid = appid;
		this.mch_id = mch_id;
		this.nonce_str = nonce_str;
		this.sign = sign;
		this.body = body;
		this.detail = detail;
		this.attach = attach;
		this.out_trade_no = out_trade_no;
		this.total_fee = total_fee;
		this.spbill_create_ip = spbill_create_ip;
		this.notify_url = notify_url;
		this.trade_type = trade_type;
	}

	/**
	 * 
	 * @param body
	 *            商品描述
	 * @param detail
	 *            商品详情
	 * @param attach
	 *            附加数据
	 * @param out_trade_no
	 *            商户订单号
	 * @param total_fee
	 *            总金额 订单总金额，单位为分
	 * @param spbill_create_ip
	 *            终端IP
	 * @param notify_url
	 *            通知地址 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	 * @param openid
	 *            用户标识	
	 */
	public PayReqData(String body, String detail,
			String attach, String out_trade_no, String total_fee,
			String spbill_create_ip, String notify_url , String openid) {
		super();
		this.appid = Configure.APPID;
		this.mch_id = Configure.MCHID;
		this.body = body;
		this.detail = detail;
		this.attach = attach;
		this.out_trade_no = out_trade_no;
		this.total_fee = total_fee;
		this.spbill_create_ip = spbill_create_ip;
		this.notify_url = notify_url;
		this.openid = openid;
		// 随机字符串，不长于32 位
		setNonce_str(RandomStringGenerator.getRandomStringByLength(32));

		// 根据API给的签名规则进行签名
		String sign = Signature.getSign(toMap());
		setSign(sign);// 把签名数据设置到Sign这个属性中
	}
	
	/**
	 * 
	 * @param body
	 *            商品描述
	 * @param detail
	 *            商品详情
	 * @param attach
	 *            附加数据
	 * @param out_trade_no
	 *            商户订单号
	 * @param total_fee
	 *            总金额 订单总金额，单位为分
	 * @param spbill_create_ip
	 *            终端IP
	 * @param notify_url
	 *            通知地址 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	 * @param openid
	 *            用户标识	
	 */
	public PayReqData(String body, String detail,
			String attach, String out_trade_no, String total_fee,
			String spbill_create_ip, String notify_url , String openid,String trade_type) {
		super();
		this.appid = Configure.APPID;
		this.mch_id = Configure.MCHID;
		this.body = body;
		this.detail = detail;
		this.attach = attach;
		this.out_trade_no = out_trade_no;
		this.total_fee = total_fee;
		this.spbill_create_ip = spbill_create_ip;
		this.notify_url = notify_url;
		this.openid = openid;
		this.trade_type = trade_type;
		// 随机字符串，不长于32 位
		setNonce_str(RandomStringGenerator.getRandomStringByLength(32));

		// 根据API给的签名规则进行签名
		String sign = Signature.getSign(toMap());
		setSign(sign);// 把签名数据设置到Sign这个属性中
	}
	

	// /** 交易起始时间
	// 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。 */
	// private String time_start;

	// /** 交易结束时间
	// 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则注意：最短失效时间间隔必须大于5分钟
	// */
	// private String time_expire;

	
	
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	
	

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					map.put(field.getName(), obj);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	@Override
	public String toString() {
		return "PayReqData [appid=" + appid + ", mch_id=" + mch_id
				+ ", nonce_str=" + nonce_str + ", sign=" + sign + ", body="
				+ body + ", detail=" + detail + ", attach=" + attach
				+ ", out_trade_no=" + out_trade_no + ", total_fee=" + total_fee
				+ ", spbill_create_ip=" + spbill_create_ip + ", notify_url="
				+ notify_url + ", trade_type=" + trade_type + ", openid="
				+ openid + "]";
	}

}
