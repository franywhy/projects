package io.renren.utils.http;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import com.alibaba.fastjson.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * date: 2012.10.22
 *
 * @author: wubinjie@ak.cc
 */
//@CompileStatic
public abstract class HttpClientUtil4_3 {

    static final Logger log = LoggerFactory.getLogger(HttpClientUtil4_3.class);

    public static final Charset UTF8 =Charset.forName("UTF-8");

    public static final Charset GB18030 =  Charset.forName("GB18030");

    static final int  TIME_OUT  = Integer.getInteger("http.timeout", 40000);

    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.4 (KHTML, like Gecko) Safari/537.4";

    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000).build();

    static HttpClient HTTP_CLIENT = bulidHttpClient();
    static HttpClient  bulidHttpClient(){
//        SchemeRegistry registry = new SchemeRegistry();
//        registry.register(new Scheme("http",  80, PlainSocketFactory.getSocketFactory()));
//        registry.register(new Scheme("https",  443, SSLSocketFactory.getSocketFactory()));
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(800);
        cm.setDefaultMaxPerRoute(200);

        cm.setMaxPerRoute(new HttpRoute(new HttpHost("localhost")),500);
        cm.setMaxPerRoute(new HttpRoute(new HttpHost("127.0.0.1")),500);
//        cm.setMaxPerRoute(new HttpRoute(new HttpHost("us.izhubo.com")), 500);
        cm.setDefaultConnectionConfig(
                ConnectionConfig.custom()
                        .setBufferSize(4096)
                        .setCharset(UTF8)
                        .build()
        );


        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(TIME_OUT)
                .setConnectTimeout(TIME_OUT)//CoreConnectionPNames.CONNECTION_TIMEOUT
                .setSocketTimeout(TIME_OUT)//CoreConnectionPNames.SO_TIMEOUT
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();

        return HttpClientBuilder.create()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent(USER_AGENT)
                .addInterceptorFirst(new RequestAcceptEncoding())
                .addInterceptorFirst(new ResponseContentEncoding())
                .build();

    }

    /**
     * 发送 post请求
     * @param httpUrl 地址
     * @param maps 参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> maps) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送Post请求
     * @param httpPost
     * @return
     */
    private static String sendHttpPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            //httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            // 执行请求
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    public static String get(String url,Map<String,String> HEADERS)throws IOException{
        HttpGet get = new HttpGet(url);
        return execute(get,HEADERS,null);
    }


    public static String get(String url,Map<String,String> HEADERS,Charset forceCharset)throws IOException{
        HttpGet get = new HttpGet(url);
        return execute(get,HEADERS,forceCharset);
    }


    public static String post(String url,Map<String,String> params,Map<String,String> headers) throws IOException{
        HttpPost post = new HttpPost(url);
        if(params !=null &&  ! params.isEmpty()){
            List<NameValuePair> ps = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String,String> kv : params.entrySet()){
                ps.add(new BasicNameValuePair(kv.getKey(), kv.getValue()));
            }
            post.setEntity(new UrlEncodedFormEntity(ps));
        }
        return execute(post,headers,null);
    }
    
    public static String postStr(String url,String json,Map<String,String> headers) throws IOException{
        HttpPost post = new HttpPost(url);
        StringEntity postingString = new StringEntity(json,"UTF-8");// json传递  
        post.setEntity(postingString);  
        return execute(post,headers,null);
    }
    
    public static String putStr(String url,String json,Map<String,String> headers) throws IOException{
    	HttpPut put = new HttpPut(url);
    	StringEntity putingString = new StringEntity(json,"UTF-8");// json传递  
    	put.setEntity(putingString);  
    	return execute(put,headers,null);
    }
    
    public static void httpDelete(String url,Map<String,String> headers){
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();   
        HttpDelete httpdelete = new HttpDelete(url);
        CloseableHttpResponse httpResponse = null;
        try {
			httpResponse = closeableHttpClient.execute(httpdelete);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }


    public static <T> T  http(HttpClient  client,HttpRequestBase request,Map<String,String> headers,HttpEntityHandler<T> handler)
            throws IOException {
        if(headers !=null &&  ! headers.isEmpty()){
            for (Map.Entry<String,String> kv : headers.entrySet()){
                request.addHeader(kv.getKey(),kv.getValue());
            }
        }
        long begin = System.currentTimeMillis();
        try{
            return client.execute(request,handler,null);
//            entity = response.getEntity();
//            int code = response.getStatusLine().getStatusCode();
//            if(code != HttpStatus.SC_OK){
//                throw new HttpStatusException(code,request.getURI().toString());
//            }
//
//
//            return callBack.handle(entity);
        }catch (ConnectTimeoutException e){
                log.error(" catch ConnectTimeoutException ,closeExpiredConnections &  closeIdleConnections for 30 s. ");
                client.getConnectionManager().closeExpiredConnections();
                client.getConnectionManager().closeIdleConnections(30, TimeUnit.SECONDS);
                throw  e;
        }finally {
            // netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}'
            // CLOSE_WAIT =  DefaultMaxPerRoute
            // HttpClient4使用 InputStream.close() 来确认连接关闭
            // CLOST_WAIT 僵死连接数 （占用一个路由的连接）
            //EntityUtils.consumeQuietly(entity);
            // 被动关闭连接 (目标服务器发生异常主动关闭了链接) 之后自己并没有释放连接，那就会造成CLOSE_WAIT的状态
            log.info(handler.getName() + "  {},cost {} ms",request.getURI(),System.currentTimeMillis() - begin);
        }
    }


	private static String execute(final HttpRequestBase request, Map<String, String> headers, final Charset forceCharset) throws IOException {
		return http(HTTP_CLIENT, request, headers, new HttpEntityHandler<String>() {
			@Override
			public String handle(HttpEntity entity) throws IOException {
				if (entity == null) {
					return null;
				}
				byte[] content = EntityUtils.toByteArray(entity);
				if (forceCharset != null) {
					return new String(content, forceCharset);
				}
				String html;
				Charset charset = null;
				ContentType contentType = ContentType.get(entity);
				if (contentType != null) {
					charset = contentType.getCharset();
				}
				if (charset == null) {
					charset = GB18030;
				}
				html = new String(content, charset);
				charset = checkMetaCharset(html, charset);
				if (charset != null) {
					html = new String(content, charset);
				}
				return html;
			}

			public String getName() {
				return request.getMethod();
			}
		});
	}


    private static Charset checkMetaCharset(String html,Charset use){
		String magic = "charset=";
		int index = html.indexOf(magic);
		if (index > 0 && index < 1000) {
			index += magic.length();
			int end = html.indexOf('"', index);
			if (end > index) {
				try {

					String charSetString = html.substring(index, end).toLowerCase();

					if (charSetString.length() > 10) {
						return null;
					}
					// GBK GB2312 --> GB18030
					if (charSetString.startsWith("gb")) {
						return GB18030.equals(use) ? null : GB18030;
					}
					Charset curr = Charset.forName(charSetString);
					if (!curr.equals(use)) {
						return curr;
					}
				} catch (Exception e) {
					log.error("Get MetaCharset error", e);
				}
			}
		}

		return null;
	}

    public static void main(String[] args) throws Exception{


//        Map map = new HashMap();
//        map.put("Cookie","BDUSS=JTbUhoeWhST3V5TTVoMXlvZXcyeUUwNHI1eS1Xc3BvNnFnU340MjhlMTE3TDVSQVFBQUFBJCQAAAAAAAAAAApBLRAPsZEvAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAYIArMAAAALD2RHMAAAAA6p5DAAAAAAAxMC4zOC4yOHWe0VB1ntFQN3");
//
//        System.out.println(HttpClientUtil4_3.get("http://music.baidu.com/song/13859395/download", map));
//    	http://w.kjcity.com/ajax/TrainingCourse.ashx?openid=18620523707
    	
//        Map map = new HashMap();
//        map.put("openid","18620523707");
//        
//        System.out.println(HttpClientUtil.get("http://w.kjcity.com/ajax/TrainingCourse.ashx?openid=18620523707", map));
    }

    public static JSONObject doPatch(String url,JSONObject jsonParam, String token){
        JSONObject resultObj = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPatch httpPatch = new HttpPatch(url);
        httpPatch.setHeader("Content-type", "application/json");
        httpPatch.setHeader("Charset", HTTP.UTF_8);
        httpPatch.setHeader("token",token);
        try {
            if (jsonParam != null){
                StringEntity entity = new StringEntity(jsonParam.toString(),HTTP.UTF_8);
                httpPatch.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPatch);
            String result = EntityUtils.toString(response.getEntity());// 返回json格式：
            resultObj =  JSONObject.fromObject(result);
        } catch (ParseException | JSONException | IOException   e) {
            e.printStackTrace();
        }
        return resultObj;
    }
    public static JSONObject doPost(String url,JSONObject jsonParam, String token){
        JSONObject resultObj = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Charset", HTTP.UTF_8);
        if(StringUtils.isNotBlank(token)){
            httpPost.setHeader("token",token);
        }
        try {
            if (jsonParam != null){
                StringEntity entity = new StringEntity(jsonParam.toString(),HTTP.UTF_8);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());// 返回json格式：
            resultObj =  JSONObject.fromObject(result);
            Header[] headers =  response.getHeaders("token");
            if(StringUtils.isBlank(token) && headers.length > 0){
                resultObj.put("token",headers[0].getValue());
            }
        } catch (ParseException | JSONException | IOException   e) {
            e.printStackTrace();
        }
        return resultObj;
    }
}
