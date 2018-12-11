package com.izhubo.web.iosiap

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$
import static com.izhubo.web.api.Web.roomId

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory;



import org.json.JSONException
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

import com.izhubo.model.CurrencyGainType;
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.http.HttpClientUtil4_3
import com.izhubo.web.BaseController
import com.izhubo.web.currency.CurrencyController;
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mysqldb.model.Orders

/**
 * date: 13-2-28 下午4:04
 */
@Rest
class IapSecondaryController extends BaseController {

	Logger logger = LoggerFactory.getLogger(IapSecondaryController.class)


	
	@Resource
	private SessionFactory sessionFactory;

	@Resource
	CurrencyController currencyController;

	
	
	//所有的验证都进入这个方法
	public  Map getSecondaryVerify(HttpServletRequest request) throws Exception{
		
		
		Map<String, Object> verifyResultMap = new HashMap<>();
		
		String result = "";
		String returnresult="";
		String verifyStatus=null;
		
		String requestJson =request.getParameter("data").toString();
		String orderno =request.getParameter("or").toString();
		
		if(requestJson != "" || requestJson !=null){
			URL dataUrl = new URL("https://buy.itunes.apple.com/verifyReceipt");
			HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
			//设置请求头信息
			con.setRequestMethod("POST");
			con.setRequestProperty("content-type", "text/json");
			con.setRequestProperty("Proxy-Connection", "Keep-Alive");
			con.setDoOutput(true);
			con.setDoInput(true);
			OutputStreamWriter out =new OutputStreamWriter(con.getOutputStream());
			String str= String.format(Locale.CHINA,"{\"receipt-data\":\""+requestJson+"\"}");
		
			out.write(str);
			out.flush();
			out.close();
			InputStream is = con.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = reader.readLine()) != null){
				result+= line+"\r\n";
			}
			org.json.JSONObject j;
			try{
				j = new org.json.JSONObject(result);
				returnresult = j.get("status").toString();
				if(returnresult.equals("0")){
					
					
					//这里改成充值虚拟货币
					verifyResultMap.put("verfyStatus", 0);
					verifyResultMap.put("receipt", "");
					
					addCoin(orderno);
					
					
				
					
				}else if(returnresult.equals("21007")){
				  verifyResultMap =	sandboxUrl(requestJson,orderno);
				}else if(returnresult.equals("21002")){
					verifyResultMap.put("verfyStatus",-6L);
					verifyResultMap.put("receipt", "");
				}
				else{
					verifyResultMap.put("verfyStatus", Long.valueOf(returnresult));
					verifyResultMap.put("receipt", "");
				}
			}catch(Exception e){
				logger.info("接收返回类型:"+e.getMessage());
			}
		}
		return verifyResultMap;
	}
	public Map<String, Object> sandboxUrl(String requestJson,String orderno) {
		
		Map<String, Object> verifyResultMap = new HashMap<>();
		String result = "";
		String returnresult="";
		String verifyStatus=null;
		
		try {
			URL dataUrl = new URL("https://sandbox.itunes.apple.com/verifyReceipt");
			HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("content-type", "text/json");
			con.setRequestProperty("Proxy-Connection", "Keep-Alive");
			//con.setRequestProperty("receipt-data", receipt);
			con.setDoOutput(true);
			con.setDoInput(true);
			OutputStreamWriter out =new OutputStreamWriter(con.getOutputStream());
			String str2= String.format(Locale.CHINA,"{\"receipt-data\":\""+requestJson+"\"}");
			out.write(str2);
			out.flush();
			out.close();
			InputStream is = con.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = reader.readLine()) != null){
				result+= line+"\r\n";
			}
			org.json.JSONObject j;
			j = new org.json.JSONObject(result);
			returnresult = j.get("status").toString();
			if(returnresult.equals("0")){
				verifyResultMap.put("verfyStatus", 0);
				verifyResultMap.put("receipt", "");
				logger.info("iosiap 订单开始"+orderno);
			     addCoin(orderno);
			}else{
				verifyResultMap.put("verfyStatus", Long.valueOf(returnresult));
				verifyResultMap.put("receipt", null);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return verifyResultMap;
	}
	
	private Lock lock = new ReentrantLock();
	
	private void addCoin(String orderno)
	{

		
		lock.lock();
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Orders.class);
			c.add(Restrictions.eq(Orders.PROP_ORDERNO, orderno));
			Orders order = (Orders)c.uniqueResult();

			//这里加个特殊逻辑，如果订单不存在的情况下，则提交给题库
			if(order == null)
			{
				String URL =  "http://m.kjcity.com/wxpay/iphonepayret.ashx?no="
				HttpClientUtil4_3.get(URL+orderno, null);
				logger.info("iosiap 订单不存在"+URL+orderno);
			}
			else
			{
	
			if(order.payStatus!=com.izhubo.model.OrderPayStatus.支付成功.ordinal())
			{
				//tip：这里的逻辑如下，首先，异步回调的时候，只要mysql保存成功，不管请求会计城的虚拟货币接口是否成功，都应该保持成功的状态。（确保虚拟货币接口，只请求一次）
				order.setPayStatus(com.izhubo.model.OrderPayStatus.支付成功.ordinal());
				session.update(order);
				session.flush();
				try {
					currencyController.increaseCurrencyByFlow(orderno,order.getUserId(), order.getPayMoney(), CurrencyGainType.充值);
				
				}
				catch (Exception e) {
				
					logger.info("iosiap 订单处理失败"+e.toString());
				}
			}
			else

			{
				logger.info("iosiap "+orderno+" 订单重复请求，未处理");
			}
			}
		} catch (Exception e) {
			
		}finally{
			//显示释放锁
			lock.unlock();
		}
		
		
	}


}
