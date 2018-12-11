package tim;

import org.json.JSONObject;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;

// org.json 第三方库请自行下载编译，或者在以下链接下载使用 jdk 1.7 的版本
// http://share.weiyun.com/630a8c65e9fd497f3687b3546d0b839e

public class SmsSingleSender {
	int appid;
	String appkey;
    String url = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms";
    
    //初始化ascClient需要的几个参数
    private static final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
    private static final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
    //初始化AK
    private static final String accessKeyId = "LTAIdrAX6n4YdV2a";
    private static final String accessKeySecret = "nPXMmznNFYDZeT3DuDnGnedNnH0U4g";
	
	SmsSenderUtil util = new SmsSenderUtil();

	public SmsSingleSender(int appid, String appkey) throws Exception {
		this.appid = appid;
		this.appkey = appkey;
	}

	/**
	 * 普通单发短信接口，明确指定内容，如果有多个签名，请在内容中以【】的方式添加到信息内容中，否则系统将使用默认签名
	 * @param type 短信类型，0 为普通短信，1 营销短信
	 * @param nationCode 国家码，如 86 为中国
	 * @param phoneNumber 不带国家码的手机号
	 * @param msg 信息内容，必须与申请的模板格式一致，否则将返回错误
	 * @param extend 扩展码，可填空
	 * @param ext 服务端原样返回的参数，可填空
	 * @return {@link}SmsSingleSenderResult
	 * @throws Exception
	 */
	public SmsSingleSenderResult send(
			int type,
			String nationCode,
			String phoneNumber,
			String msg,
			String extend,
			String ext) throws Exception {
/*
请求包体
{
    "tel": {
        "nationcode": "86", 
        "mobile": "13788888888"
    },
    "type": 0, 
    "msg": "你的验证码是1234", 
    "sig": "fdba654e05bc0d15796713a1a1a2318c", 
    "time": 1479888540,
    "extend": "",
    "ext": ""
}
应答包体
{
    "result": 0,
    "errmsg": "OK", 
    "ext": "", 
    "sid": "xxxxxxx", 
    "fee": 1
}
*/
		// 校验 type 类型
		if (0 != type && 1 != type) {
			throw new Exception("type " + type + " error");
		}
		if (null == extend) {
			extend = "";
		}		
		if (null == ext) {
			ext = "";
		}

		// 按照协议组织 post 请求包体
        long random = util.getRandom();
        long curTime = System.currentTimeMillis()/1000;

		JSONObject data = new JSONObject();

        JSONObject tel = new JSONObject();
        tel.put("nationcode", nationCode);
        tel.put("mobile", phoneNumber);

        data.put("type", type);
        data.put("msg", msg);
        data.put("sig", util.strToHash(String.format(
        		"appkey=%s&random=%d&time=%d&mobile=%s",
        		appkey, random, curTime, phoneNumber)));
        data.put("tel", tel);
        data.put("time", curTime);
        data.put("extend", extend);
        data.put("ext", ext);

        // 与上面的 random 必须一致
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid, random);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(data.toString());
        wr.flush();
        
        System.out.println(data.toString());

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        SmsSingleSenderResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsSingleSenderResult(json);
        } else {
        	result = new SmsSingleSenderResult();
        	result.result = httpRspCode;
        	result.errMsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }
        conn.disconnect();
        return result;
	}
	
/* *短信发送切换为使用阿里云（2018-04-21）Vince */
	/**
	 * 指定模板单发
	 * @param nationCode 国家码，如 86 为中国
	 * @param phoneNumber 不带国家码的手机号
	 * @param templId 信息内容
	 * @param params 模板参数列表，如模板 {1}...{2}...{3}，那么需要带三个参数
	 * @param sign 签名，如果填空，系统会使用默认签名
	 * @param extend 扩展码，可填空
	 * @param ext 服务端原样返回的参数，可填空
	 * @return {@link}SmsSingleSenderResult
	 * @throws Exception
	 */
	/*public SmsSingleSenderResult sendWithParam1(
			String nationCode,
			String phoneNumber,
			int templId,
			ArrayList<String> params,
			String sign,
			String extend,
			String ext) throws Exception {*/
/*
请求包体
{
    "tel": {
        "nationcode": "86", 
        "mobile": "13788888888"
    }, 
    "sign": "腾讯云", 
    "tpl_id": 19, 
    "params": [
        "验证码", 
        "1234", 
        "4"
    ], 
    "sig": "fdba654e05bc0d15796713a1a1a2318c",
    "time": 1479888540,
    "extend": "", 
    "ext": ""
}
应答包体
{
    "result": 0,
    "errmsg": "OK", 
    "ext": "", 
    "sid": "xxxxxxx", 
    "fee": 1
}
*/
		/*if (null == nationCode || 0 == nationCode.length()) {
			nationCode = "86";
		}
		if (null == params) {
			params = new ArrayList<>();
		}
		if (null == sign) {
			sign = "";
		}
		if (null == extend) {
			extend = "";
		}		
		if (null == ext) {
			ext = "";
		}
		
		long random = util.getRandom();
		long curTime = System.currentTimeMillis()/1000;

		JSONObject data = new JSONObject();

        JSONObject tel = new JSONObject();
        tel.put("nationcode", nationCode);
        tel.put("mobile", phoneNumber);

        data.put("tel", tel);
        data.put("sig", util.calculateSigForTempl(appkey, random, curTime, phoneNumber));
        data.put("tpl_id", templId);
        data.put("params", util.smsParamsToJSONArray(params));
        data.put("sign", sign);
        data.put("time", curTime);
        data.put("extend", extend);
        data.put("ext", ext);
        
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid, random);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);
        
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(data.toString());
        wr.flush();

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        SmsSingleSenderResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsSingleSenderResult(json);
        } else {
        	result = new SmsSingleSenderResult();
        	result.result = httpRspCode;
        	result.errMsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }
		conn.disconnect();
        return result;
	}*/
	
	
	
	/**
	 * 指定模板单发
	 * @param nationCode 国家码，如 86 为中国
	 * @param phoneNumber 不带国家码的手机号
	 * @param templId 信息内容
	 * @param params 模板参数列表，如模板 {1}...{2}...{3}，那么需要带三个参数
	 * @param sign 签名，如果填空，系统会使用默认签名
	 * @param extend 扩展码，可填空
	 * @param ext 服务端原样返回的参数，可填空
	 * @return {@link}SmsSingleSenderResult
	 * @throws Exception
	 */
	public SmsSingleSenderResult sendWithParam(
			String nationCode,
			String phoneNumber,
			int templId,
			ArrayList<String> params,
			String sign,
			String extend,
			String ext) throws Exception {
        SmsSingleSenderResult result =null;
        result = this.sendAlism(phoneNumber, params);
        
        return result;
	}
	/**
	 * 
	 * @return
	 */
	public SmsSingleSenderResult sendAlism(String phoneNumber, ArrayList<String> params)throws Exception{
		
		/*阿里云短信发送*/
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
           SendSmsRequest request = new SendSmsRequest();
           //使用post提交
          request.setMethod(MethodType.POST);
		  //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
          request.setPhoneNumbers(phoneNumber);
		  //必填:短信签名-可在短信控制台中找到
		  request.setSignName("恒企教育");
		  //必填:短信模板-可在短信控制台中找到
		  request.setTemplateCode("SMS_113385014");
		  //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		  //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		  String otp1=params.get(0);
		  String template="{code"+":"+otp1+"}";
		  request.setTemplateParam(template);
//		  request.setTemplateParam("{\"code\":\"123\"}");
		  //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		  //request.setSmsUpExtendCode("90997");
		  //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//		  request.setOutId("yourOutId");
	     //请求失败这里会抛ClientException异常
		 SmsSingleSenderResult result;
	     SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			// 请求成功
			result = new SmsSingleSenderResult();
			result.result = 0;
			result.errMsg = "OK";
//			System.out.println("请求成功:" + sendSmsResponse.toString());
		} else {
			result = new SmsSingleSenderResult();
			result.result = 400;
			result.errMsg = "http error";
		}
	     /*阿里云短信发送*/
	     return result;
	}
}
