package io.renren.test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.DateUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.renren.utils.bmutils.ApiException;
import io.renren.utils.bmutils.HttpUtil;
import io.renren.utils.bmutils.SignatureUtils;
import io.renren.utils.bmutils.model.HdBizContent;
import io.renren.utils.bmutils.model.HdBizLoanInfo;
import io.renren.utils.bmutils.model.ReturnObj;


/****
 * author pan
 * 2017-05-06
 * */
public class App {
	
	/**********************************************************/
	/**
	 * 仅作简单验签
	 * 商户自行生成公钥私钥 并将公钥提供给互动
	 * 
	 * **/
	/**********************************************************/
	
	
	private static String appId = "20170214001";
	private static String hd_public_key ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlUSbTpT7iI+jlF0dNRsLhr5fHFoyjhbliSDVee1TZjGrYdSNJVHRSzLvNhTjhmX05wA0wCaMaHAwwxUN3sz4MKwy3eYWsQ0lA3TXz466G6Iqdzqsm3VPXeMYmHDLuV3grkc3W1wcAPL6XXD1ROxowdQhsHhCxkurFBIauZNCdrGTzi/am73nb2cQNVD4ixxLrxD0GpEYcbZnVZy511MBHrPPgGliAFF5dLF0v9+IE6UWTGHC0pFFhOuECMCoiwsI1ov4Rm4QL2DoCBJMVSLLMsGK+lvnSJnoY6pAo45+lg8aGN2NXlQVVxOz6vKMJsknr3Ty6fojiuQm2DYmYjbGKwIDAQAB";
	//private static String biz_hq_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAup3SjympUPTIbcF7wX+A/xLsViGRaxVaYz2bD1hOkDOkm4WWLH0D5cFy2xP0k7uMr9jCNbZYU7yKJ/Gar5mER6KSx0RgI9BflkKhQ9pp0DzJVnypgB1E61vsXvYFmyZTOziImvgR8IXTfx3CEtqA7BMavl2knmOMjey3w+Zhhs2EdNqYAyd4RlCE8c2PBsabfNaU3BFTJTPZ6CoMKMLlFe4qdqeTgACJsbEoNr/ApPU+fEBeu2n16XrwbrsQz2KDO85DT0fOQcwUJHWEZ4XCDQICMxP15VXabkvT8bEkJIyVXwiqQyPdXOIReWZl3AT1FEt1aFHOT2A9giZQ68nbtwIDAQAB";
	private static String biz_hq_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC6ndKPKalQ9MhtwXvBf4D/EuxWIZFrFVpjPZsPWE6QM6SbhZYsfQPlwXLbE/STu4yv2MI1tlhTvIon8ZqvmYRHopLHRGAj0F+WQqFD2mnQPMlWfKmAHUTrW+xe9gWbJlM7OIia+BHwhdN/HcIS2oDsExq+XaSeY4yN7LfD5mGGzYR02pgDJ3hGUITxzY8Gxpt81pTcEVMlM9noKgwowuUV7ip2p5OAAImxsSg2v8Ck9T58QF67afXpevBuuxDPYoM7zkNPR85BzBQkdYRnhcINAgIzE/XlVdpuS9PxsSQkjJVfCKpDI91c4hF5ZmXcBPUUS3VoUc5PYD2CJlDrydu3AgMBAAECggEAE7Or0i98z644JtZBgYk7jjJqPyGmH58OfuG3KCSTjsfU/hJZa3L7YVtOC/EMZjhrvvjscA1DP/vsvhDU2usjOwjf5CfE2qMaJFv+eJJrj2+LdhUNNBhi+VIoH1Jr/xhJuzvZuDquci0+yfHTVng5GjNeJHDwe0RZR3aRZB5Dd562ZNY67theguRZu4tgerNbU4fhAgRKKDXcgzPa0Xhr98XR8MHHz4JF2u+DUC89cPqnwAGrlWEWAVsar88yMGUKJGfJlI5DnO6drSUv/ltaD/IA/FLQ4ngzwdFmvfgU1nzFJNQ5KHOV6jUVdPspniHXxQzxZBUUjzoCjyuTXNTYeQKBgQD6Rp45UvOz+Ln2QDMJgUfKHPo4EznOBkXgcIUyLABYmJtmxSiUw/0zAzFYtJrdIzBSKb9O9AJXdah2XyESgVEPEtAukfr88WcguAnfQKmUjLIWwW8XAZ3DrLqNd45P9xF+O7ujwSWLvJmtcxlWs5mM/RKN1HhaKlWf9DfKLTDmNQKBgQC+4nmAbdnOw6iqANpFXIGEkUjcu588uy2WROdpt/gIsJzPqOwo5zm26Ohvp1hecsfko439We9uHVmid7j/w1ktEUdERW1e+mYcwshS6yq/0tGbEZm54SA7+hjSLHsBhKEaQrynUedKpA6JK3Zwh6Ovw0fo+4qx4reYqe9393VHuwKBgFPqVab2z0uqu/97KyGmvUFhUex3Vcxkgg1s7NwTsNID6SceOCsZQ85U5bjOZZZ78/m4kbKRUi+s/Z0i7F3eCiW5sx/KRpOFBYUM8BJxBesO+a9zkCHwoss3IR3J1meteaoPsSyI2GPwCr8Ddp2BfpytRglUEj55L1iAdCxP1rN5AoGAIJDo48VaDJGK+/4LPxwqKKDUynvkDettOp7k0D+HvIvNJQyPxea1DQStayckx3BTtClHNzsZwzKUsUg1ssT0OOEixH4Hy9VyiGOe466la3afNhKJpJHI+xKPG4RlM/wZtC5Am1c4254jsLlOiqJJhdoOMayAnBqu8gfECDaatrMCgYBlOOr5mPQxd/hUyB8ZhxwcU6AKmutsrmaxMVn4sPUxNgLQmc9F0MdqzCXxgMkMYZou4FwMRt59ML0Nzg5jEB/laApjTcD+g5z05deBcrh1+REpgZ5h6fUJ3Aa9MrpUwAP0MnolivReXDULeZRqw/bqbW6jAQVUSi+ckeJQm4H89g==";
    public static void main( String[] args ) throws ApiException, IOException{
    	String timestamp = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    	//1.组装业务参数
    	HdBizContent bizContent = new HdBizContent(); 
    	bizContent.setOrderStatus("7000");
     	/*bizContent.setStartTime("2017-08-04 00:00:00");
    	bizContent.setEndTime("2017-08-04 23:59:59");*/
    	//bizContent.setEndTime("2017-06-15 16:22:18");
    	/*bizContent.setOrderStatus("7000");
    	bizContent.setStartTime("2017-06-21 16:39:35");
    	bizContent.setEndTime("2017-06-21 16:42:29");*/
    	/*bizContent.setOrderStatus("0");
    	bizContent.setStartTime("2017-06-14 18:27:56");
    	bizContent.setEndTime("2017-06-14 18:27:56");*/
		/**
		 * 0 待审批 5000 审批通过 7000 已放款
		 */
    	//2.序列化业务参数 首字母排序 
    	String biz_content =JSON.toJSONString(bizContent);

    	//3.生成签名
    	String preSignStr = "app_id=" + appId + "&biz_content=" + biz_content + "&sign_type=RSA2&timestamp=" + timestamp;
    	System.out.println("待签名数据：="+preSignStr);
    	
    	String sign = SignatureUtils.rsaSign(preSignStr, biz_hq_private_key, "UTF-8", "RSA2");
    	//4.组装请求结果
    	Map<String,String> params = new HashMap<String,String>();
    	params.put("app_id", appId);
    	params.put("biz_content",biz_content);
    	params.put("sign_type", "RSA2");
    	params.put("timestamp", timestamp);
    	params.put("sign", sign);
    	//5.发送请求并接收结果
    	//String result = HttpUtil.postFormResponse("http://182.92.6.16:8081/hd-merchant-biz-app/gateway/queryOrder", params);
    	String result = HttpUtil.postFormResponse("https://ib.hdfex.com/hd-merchant-biz-app/gateway/queryOrder", params);
    	System.out.println(result);
    	ReturnObj returnObj = JSON.parseObject(result,ReturnObj.class);
    	//6.验证签名
    	String returnObjStr = "code=" + returnObj.getCode() +"&message="+ returnObj.getMessage();
    	if (StringUtils.isEmpty(returnObj.getData())){
    		returnObjStr += "&data=";
    	} else {
    		List<HdBizLoanInfo> loanInfos = JSON.parseArray(returnObj.getData(),HdBizLoanInfo.class);
    		returnObjStr += "&data="+JSON.toJSONString(loanInfos);
    	}
    	System.out.println("verfiy=====验证签名结果:"+returnObjStr);
    	boolean verfiy = SignatureUtils.rsaCheck(returnObjStr,returnObj.getSign(), hd_public_key, "UTF-8", "RSA2");
        System.out.println("verfiy=====验证签名结果:"+verfiy);
        if (verfiy) {
        	//7.处理业务数据 略
        	
        }
    }
}
