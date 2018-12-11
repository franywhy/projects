package com.izhubo.web.pay.pc.wxpay

import static com.izhubo.rest.common.util.WebUtils.$$

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.rest.anno.Rest
import com.izhubo.web.pay.PayNotityBaseController
import com.izhubo.web.pay.pc.wxpay.common.Signature
import com.izhubo.web.pay.pc.wxpay.common.XMLParser
import com.izhubo.web.pay.pc.wxpay.dto.NodityResData

/**
 * 微信支付 异步通知
 * @author shihongjie
 * @see https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
 */
@Rest
@RequestMapping("/wxnotify")
class WXNotifyController extends PayNotityBaseController{


	
	@Override
	@RequestMapping(value = "/nodity")
	@ResponseBody
	public String nodity(HttpServletRequest request , HttpServletResponse response) {
		return super.nodity(request , response);
	}
	@Override
	public boolean nodityMethod(HttpServletRequest request , HttpServletResponse response) {
		//@see https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
		
		try {
			
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
//			@see http://blog.csdn.net/gbguanbo/article/details/50915718
//			System.out.println("~~~~~~~~~~~~~~~~微信付款成功~~~~~~~~~");
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			Map<String, Object> map = XMLParser.getMapFromXML(result);;
			
			Boolean check = Signature.checkIsSignValidFromResponseString(map);
//			println "===============check:" + check;
			
			if(!check){
				println "微信回调校验失败:" + result;
				return false;
			}
			
			if("SUCCESS".equals(map.get("result_code"))){
				Long trade_no = Long.parseLong(map.get("out_trade_no").toString());//订单号
				float total_fee = Float.parseFloat(map.get("total_fee").toString());//订单金额
				String transaction_id = map.get("transaction_id").toString();//微信支付订单号
				
				//订单号
				super.out_trade_no = trade_no;
				//第三方交易号
				super.trade_no = transaction_id;
				//回调信息
				super.payCallBlackRemark = map.toString();
				
				return true;
			}else{
				//return entity;
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace()
		}
		
		return false;
		
	}


	@Override
	public String successString() {
		return NodityResData.getSuccessXml();
	}


	@Override
	public String failString() {
		return NodityResData.getFailXml();
	}


	@Override
	public String nodityName() {
		return "微信支付-异步回调lock";
	}
	
	
	
}
