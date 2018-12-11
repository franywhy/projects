package io.renren.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;

import io.renren.entity.NCResultCodeEntity;
import io.renren.utils.MD5Util;
import io.renren.utils.weixin.HttpClientUtil;
import net.sf.json.JSONObject;



public class Test {

	public static void main(String[] args) {
		/*double fee =8888.51*100;
		int i=(int) Math.rint(fee);
		System.out.println(i);
	*/
		/*double fee = 0.01* 100;
		String total_fee = String.valueOf(fee);
		int i = total_fee.indexOf(".");
		System.out.println(i);
		String ciphertext=MD5Util.string2MD5("HQ112013112900001561"+"2017-5-8"+0.01+"hengqijypay");
		System.out.println(ciphertext);*/
		/*Date d=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date=sdf.format(d);
		//采用md5加密保证双方支付安全性
		String ciphertext=MD5Util.string2MD5("HQ112013112900001561"+date+0.01+"SADFDSAff"+"hengqijypay");
		System.out.println("密文："+ciphertext);
		//同步支付成功给nc
	
		String httpUrl = "http://183.63.120.221:8899/servlet/~hq/nc.impl.hq.webservice.collectionbill.UpdateCollectionBillStatusServlet?";
		String param = "orderNo=" +"HQ112013112900001561" + "&orderTimestamp=" +date
				+ "&tradeMoney=" + 0.01 + "&tradeNo=" + "SADFDSAff" + "&code="+0+"&tradesys=zfb"+"&ciphertext="+ciphertext+"&test=暗室逢灯";
		String result = HttpClientUtil.getInstance().sendHttpPost(httpUrl, param);
		System.out.println(">>>>>"+result);
		Gson gson=new Gson();
		NCResultCodeEntity ncResultMsg=gson.fromJson(result,NCResultCodeEntity.class);
        System.out.println(ncResultMsg.getCode());*/
		/*String url="http://182.92.6.16:8081/hd-merchant-biz-app/gateway/queryOrder?";
		//String biz_content="phone_no=18701112701";
		 Map<String, String> m = new HashMap<String, String>();
        m.put("phone_no", "18701112701");
        m.put("order_status", "0");
        m.put("start_time", "2017-7-25 12:36:03");
        m.put("end_time", "2017-7-25 15:36:03");
        JSONObject bizcontentJson= JSONObject.fromObject(m);
        String s= bizcontentJson.toString();
		String param ="app_id=20170214001&biz_content="+bizcontentJson.toString();
		String result = HttpClientUtil.getInstance().sendHttpPost(url, param);
		System.out.println(result);*/
		/*Date d=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date=sdf.format(d);
		//采用md5加密保证双方支付安全性
		String ciphertext=MD5Util.string2MD5("HQ112013112900001561"+"2017-8-8"+0.01+"hengqijypay");
		System.out.println("密文："+ciphertext);*/
		/**
		 * 18bd48c7715090d8be33e8b3923c18e2  7000
		 * 475d3d6b3a92b198aceb6e512bbb3f1c   0
		 * b8004c13032062dec2c361652ab2572e  5000
		 */
		String ciphertext = MD5Util.string2MD5("null"+"null"+"5000"+ "hdBMGatewayQueryOrder");
		System.out.println(ciphertext);
	}

  
}
