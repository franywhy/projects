package com.izhubo.web.pay.app.wxpay

import static com.izhubo.rest.common.util.WebUtils.$$

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory;
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.rest.anno.Rest
import com.izhubo.rest.persistent.KGS;
import com.izhubo.web.currency.CurrencyController
import com.izhubo.web.pay.PayNotityBaseController
import com.izhubo.web.pay.pc.wxpay.common.Signature
import com.izhubo.web.pay.pc.wxpay.common.XMLParser
import com.izhubo.web.pay.pc.wxpay.dto.NodityResData
import com.izhubo.model.CurrencyGainType
import com.mysqldb.model.Orders

/**
 * 微信支付 异步通知
 * @author shihongjie
 * @see https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
 */
@Rest
@RequestMapping("/wxappnotify")
class WXAppNotifyController extends PayNotityBaseController{




	@Resource
	private SessionFactory sessionFactory;

	@Resource
	CurrencyController currencyController;

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
				String trade_no = map.get("out_trade_no").toString();//订单号s
				float total_fee = Float.parseFloat(map.get("total_fee").toString());//订单金额
				String transaction_id = map.get("transaction_id").toString();//微信支付订单号

				//订单号
				super.out_trade_no = trade_no;
				//第三方交易号
				super.trade_no = transaction_id;
				//回调信息
				super.payCallBlackRemark = map.toString();

				Session session = sessionFactory.getCurrentSession();
				Criteria c = sessionFactory.getCurrentSession().createCriteria(Orders.class);
				c.add(Restrictions.eq(Orders.PROP_ORDERNO, trade_no));
				Orders order = (Orders)c.uniqueResult();

				if(order.payStatus!=com.izhubo.model.OrderPayStatus.支付成功.ordinal())
				{
					//tip：这里的逻辑如下，首先，异步回调的时候，只要mysql保存成功，不管请求会计城的虚拟货币接口是否成功，都应该保持成功的状态。（确保虚拟货币接口，只请求一次）
					order.setPayStatus(com.izhubo.model.OrderPayStatus.支付成功.ordinal());
					session.update(order);
					session.flush();
					try {
						currencyController.increaseCurrencyByFlow(trade_no,order.getUserId(), order.getPayMoney(), CurrencyGainType.充值);
					
						return true;
					}
					catch (Exception e) {
						return true;
					}
				}
				else
				{
					//如果订单已经被处理过了，则直接通知成功
					return true;
				}
				
			
			}else{
				//return entity;
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace()
			return false;
		}



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
		return "微信支付-异步回调";
	}



}
