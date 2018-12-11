package com.izhubo.web.pay.pc.bill99

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.model.DR
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.http.HttpClientUtil
import com.izhubo.web.pay.PayBaseController
import com.izhubo.web.pay.pc.bill99.util.Pkipair
import com.mysqldb.model.Orders

/**
 * 块钱
 * @author Administrator
 *
 */
@Rest
class Bill99SendController extends PayBaseController{
	
	@Resource
	private SessionFactory sessionFactory;
	
	//人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
	private static final String BILL_MERCHANTACCTID = "1001213884201";
	//编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
	private static final String BILL_INPUTCHARSET = "1";
	//网关版本，固定值：v2.0,该参数必填。
	private static final String BILL_VERSION =  "v2.0";
	//语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
	private static final String BILL_LANGUAGE =  "1";
	//签名类型,该值为4，代表PKI加密方式,该参数必填。
	private static final String BILL_SIGNTYPE =  "4";
	//订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
//	private static final String BILL_ORDERAMOUNT = "1";
	//支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10，必填。
	private static final String BILL_PAYTYPE = "00";
	//同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
	private static final String BILL_REDOFLAG = "0";
	//快钱合作伙伴的帐户号，即商户编号，可为空。
	private static final String BILL_PID = "";
//	private static final String BILL_PID = "pay@hengqijy.com";
	
	private static final String BILLURL = "https://sandbox.99bill.com/gateway/recvMerchantInfoAction.htm";
	
	@Value("#{application['notify.bill99.url']}")
	private String notify_url;
	@Value("#{application['pay.success.url']}")
	private String pay_success_url;
	@Value("#{application['return.alipay.url']}")
	private String return_url;
	
	
	def bdemo(HttpServletRequest request){
		Integer order_id = request["order_id"] as Integer;
		
		String html = super.createPayHtml(order_id);
		return getResultOK(html);
	}
	
	public String appendParam(String returns, String paramId, String paramValue) {
		if (returns != "") {
			if (paramValue != "") {
				returns += "&" + paramId + "=" + paramValue;
			}
		} else {
			if (paramValue != "") {
				returns = paramId + "=" + paramValue;
			}
		}
		return returns;
	}
	
	private String appendForm(Map<String , String> params){
		StringBuffer sbf = new StringBuffer();
		sbf.append("<form name=\"kqPay\" id=\"kqPay\" action=\"https://sandbox.99bill.com/gateway/recvMerchantInfoAction.htm\" method=\"post\">");
		
		sbf.append(" <input type=\"hidden\" name=\"inputCharset\" value=\"" + params["inputCharset"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"pageUrl\" value=\"" + params["pageUrl"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"bgUrl\" value=\"" + params["bgUrl"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"version\" value=\"" + params["version"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"language\" value=\"" + params["language"] + "\" />");
		
		sbf.append(" <input type=\"hidden\" name=\"signType\" value=\"" + params["signType"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"signMsg\" value=\"" + params["signMsg"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"merchantAcctId\" value=\"" + params["merchantAcctId"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"payerName\" value=\"" + params["payerName"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"payerContactType\" value=\"" + params["payerContactType"] + "\" />");
		
		sbf.append(" <input type=\"hidden\" name=\"payerContact\" value=\"" + params["payerContact"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"orderId\" value=\"" + params["orderId"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"orderAmount\" value=\"" + params["orderAmount"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"orderTime\" value=\"" + params["orderTime"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"productName\" value=\"" + params["productName"] + "\" />");
		
		sbf.append(" <input type=\"hidden\" name=\"productNum\" value=\"" + params["productNum"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"productId\" value=\"" + params["productId"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"productDesc\" value=\"" + params["productDesc"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"ext1\" value=\"" + params["ext1"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"ext2\" value=\"" + params["ext2"] + "\" />");
		
		sbf.append(" <input type=\"hidden\" name=\"payType\" value=\"" + params["payType"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"bankId\" value=\"" + params["bankId"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"redoFlag\" value=\"" + params["redoFlag"] + "\" />");
		sbf.append(" <input type=\"hidden\" name=\"pid\" value=\"" + params["pid"] + "\" />");
		
		sbf.append(" <input type=\"submit\" name=\"submit\" value=\"提交到快钱\" />");
//		sbf.append(" <input type=\"submit\" name=\"submit\" value=\"提交到快钱\"  style=\"display:none;\"/>");
		sbf.append("</form>");
		sbf.append("<script>document.forms[\"kqPay\"].submit();</script>");
		
		
		return sbf.toString();
	}

	@Override
	public String factoryPayHtml(Orders order) {
		String formString = null;
		//人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
		String merchantAcctId = BILL_MERCHANTACCTID;
		//编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
		String inputCharset = BILL_INPUTCHARSET;
		//接收支付结果的页面地址，该参数一般置为空即可。
		//TODO 支付成功后的跳转url
		String pageUrl = return_url;
//			String pageUrl = pay_success_url + "&order_no="+order.getOrderno();
		//服务器接收支付结果的后台地址，该参数务必填写，不能为空。
		String bgUrl = notify_url;
		//网关版本，固定值：v2.0,该参数必填。
		String version =  BILL_VERSION;
		//语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
		String language =  BILL_LANGUAGE;
		//签名类型,该值为4，代表PKI加密方式,该参数必填。
		String signType =  BILL_SIGNTYPE;
		//支付人姓名,可以为空。
		String payerName = "";
		//支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
		String payerContactType =  "";
		//支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
		String payerContact =  "";
		//商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
		String orderId = order.getOrderno();
		//订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
		String orderAmount = order.getPayMoney() * 100;
		//订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
		String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
		//商品名称，可以为空。
		String productName= order.getOrderName();
		//商品数量，可以为空。
		String productNum = "1";
		//商品代码，可以为空。
		String productId = "";
		//商品描述，可以为空。
		String productDesc = order.getOrderDescribe();
		//扩展字段1，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
		String ext1 = "ext11";
		//扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
		String ext2 = "ext22";
		//支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10，必填。
		String payType = BILL_PAYTYPE;
		//银行代码，如果payType为00，该值可以为空；如果payType为10，该值必须填写，具体请参考银行列表。
		String bankId = "";
		//同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
		String redoFlag = BILL_REDOFLAG;
		//TODO 快钱合作伙伴的帐户号，即商户编号，可为空。
		String pid = BILL_PID;
		
		// signMsg 签名字符串 不可空，生成加密签名串
		String signMsgVal = "";
		signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset);
		signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl);
		signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl);
		signMsgVal = appendParam(signMsgVal, "version", version);
		signMsgVal = appendParam(signMsgVal, "language", language);
		signMsgVal = appendParam(signMsgVal, "signType", signType);
		signMsgVal = appendParam(signMsgVal, "merchantAcctId",merchantAcctId);
		signMsgVal = appendParam(signMsgVal, "payerName", payerName);
		signMsgVal = appendParam(signMsgVal, "payerContactType",payerContactType);
		signMsgVal = appendParam(signMsgVal, "payerContact", payerContact);
		signMsgVal = appendParam(signMsgVal, "orderId", orderId);
		signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount);
		signMsgVal = appendParam(signMsgVal, "orderTime", orderTime);
		signMsgVal = appendParam(signMsgVal, "productName", productName);
		signMsgVal = appendParam(signMsgVal, "productNum", productNum);
		signMsgVal = appendParam(signMsgVal, "productId", productId);
		signMsgVal = appendParam(signMsgVal, "productDesc", productDesc);
		signMsgVal = appendParam(signMsgVal, "ext1", ext1);
		signMsgVal = appendParam(signMsgVal, "ext2", ext2);
		signMsgVal = appendParam(signMsgVal, "payType", payType);
		signMsgVal = appendParam(signMsgVal, "bankId", bankId);
		signMsgVal = appendParam(signMsgVal, "redoFlag", redoFlag);
		signMsgVal = appendParam(signMsgVal, "pid", pid);
		
		Pkipair pki = new Pkipair();
		String signMsg = pki.signMsg(signMsgVal);
		System.out.println(signMsg);
		
		Map<String , String> params = new HashMap<String , String>();
		
		params.put("bgUrl", bgUrl );
		params.put("version", version );
		params.put("language", language );
		params.put("signType", signType );
		params.put("signMsg", signMsg );
		params.put("merchantAcctId", merchantAcctId );
		params.put("payerName", payerName );
		params.put("payerContactType", payerContactType );
		params.put("payerContact", payerContact );
		params.put("orderId", orderId );
		params.put("orderAmount", orderAmount );
		params.put("orderTime", orderTime );
		params.put("productName", productName );
		params.put("productNum", productNum );
		params.put("productId", productId );
		params.put("productDesc", productDesc );
		params.put("ext1", ext1 );
		params.put("ext2", ext2 );
		params.put("payType", payType );
		params.put("bankId", bankId );
		params.put("redoFlag", redoFlag );
		params.put("pid", pid );
		
		params.put("inputCharset", inputCharset );
		params.put("pageUrl", pageUrl );
		
		Map<String, String> headers = new HashMap<String, String>();
		
		String pString = HttpClientUtil.post(BILLURL, params, headers);
		savePayLogs("Bill99Send", ["PostBillReturn" , params.toString() ,pString]);
		
		formString = appendForm(params);
		
		savePayLogs("Bill99Send", ["formString" , formString]);
		
		return formString;
	}

	@Override
	public String payName() {
		return "Bill99_PC";
	}
	
	
}
