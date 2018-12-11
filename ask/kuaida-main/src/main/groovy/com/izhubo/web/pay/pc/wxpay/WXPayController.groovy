package com.izhubo.web.pay.pc.wxpay

import static com.izhubo.rest.common.util.WebUtils.$$

import java.text.SimpleDateFormat

import javax.annotation.Resource

import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Value

import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.Snippet
import com.izhubo.rest.common.util.http.HttpClientUtil
import com.izhubo.web.pay.PayBaseController
import com.izhubo.web.pay.pc.wxpay.common.Configure
import com.izhubo.web.pay.pc.wxpay.common.RandomStringGenerator
import com.izhubo.web.pay.pc.wxpay.common.Signature
import com.izhubo.web.pay.pc.wxpay.common.Util
import com.izhubo.web.pay.pc.wxpay.dto.PayReqData
import com.izhubo.web.pay.pc.wxpay.dto.PayResData
import com.mysqldb.model.Orders

/**
 * 微信支付
 * @author shihongjie
 * @see https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 */
@Rest
class WXPayController extends PayBaseController{
	
	@Resource
	private SessionFactory sessionFactory;
	
	@Value("#{application['notify.wx.url']}")
	private String notify_url;

	
	//TODO 
	@Override
	public Object factoryPayHtml(Orders order) {
		
		//这个是扫码终端设备从用户手机上扫取到的支付授权号，这个号是跟用户用来支付的银行卡绑定的，有效期是1分钟
		//调试的时候可以在微信上打开“钱包”里面的“刷卡”，将扫码页面里的那一串14位的数字输入到这里来，进行提交验证
		//记住out_trade_no这个订单号可以将这一笔支付进行退款
		String authCode = REF;
		
		//要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
		String body = order.getOrderName();
		
		//商品详情
		String detail = order.getOrderDescribe();
		
		//支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回，有助于商户自己可以注明该笔消费的具体内容，方便后续的运营和记录
		String attach = "hqjy";
		
		//商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
		String outTradeNo = order.getOrderno();
		
		//订单总金额，单位为“分”，只能整数
		String totalFee = (order.getPayMoney() * 100).intValue().toString();
		
		//商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
		String deviceInfo = REF;
		
		//订单生成的机器IP
		String spBillCreateIP = order.getPayip();
		
		//订单生成时间， 格式为yyyyMMddHHmmss，如2009年12 月25 日9 点10 分10 秒表示为20091225091010。时区为GMT+8 beijing。该时间取自商户服务器
		String timeStart = getStringStratDate();
		
		//订单失效时间，格式同上
		String timeExpire = getStringEndDate();
		
		//商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
		String goodsTag = REF;
		
		//用户唯一标识
		String opend_id = order.getWxopenId();
		
		//支付data
		PayReqData data = new PayReqData(body, detail, attach, outTradeNo, totalFee, spBillCreateIP, notify_url , opend_id);
		
		println "=============支付申请生成的sign:" + data.getSign();
		println "=============支付申请DATA:" + data;
		//xml
		String xml = Snippet.convertToXml(data);
		//头部
		Map<String, String> headers = new HashMap<String, String>();
		//提交订单 返回结果xml
		String result = HttpClientUtil.postXML(Configure.CREATE_ORDER_URL, headers, xml)
//		println "===============checkIsSignValidFromResponseString:" + Signature.checkIsSignValidFromResponseString(result);
		
		//返回结果格式化
		result =  new String(result.getBytes("gbk"),"utf-8");
		//返回结果的结构体
		PayResData resData = Snippet.converyToJavaBean(result, PayResData.class);
		println "resData?.getReturn_msg():" + resData?.getReturn_msg();
		Map map = new HashMap();
		
		//商户注册具有支付权限的公众号成功后即可获得
		map["appId"] = Configure.APPID;
		//当前的时间，其他详见 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
		map["timeStamp"] = Util.create_timestamp();
		//随机字符串，不长于32位。推荐随机数生成算法
		String noceStr = RandomStringGenerator.getRandomStringByLength(16);
		map["nonceStr"] = noceStr;
		//签名算法，暂支持MD5
		map["signType"] = Configure.SIGNTYPE;
		//统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
		String prepay_id = resData.getPrepay_id();
		map["package"] = "prepay_id=" + prepay_id;
//		println "---------------------:" + map;
		//签名
		map["paySign"] = Signature.getSign(map);
		
//		println "++++++++++++++++++:" + resData.getSign();
		return map;
		
	}
	
	public static String getStringStratDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	 }
	public static String getStringEndDate() {
		Calendar c = Calendar.getInstance();
		//TODO 分钟
		c.add(Calendar.MINUTE, 5); // 目前時間加 分钟
		Date currentTime = c.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	

	@Override
	public String payName() {
		return "wxpay_PC";
	}
	
}
