package com.hqjy.pay.weixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





/**
 * httpclient的操作实现类
 * 
 * @author alexydz
 */
public class HttpClientUtil {
	public static final Charset UTF8 =Charset.forName("UTF-8");
	 static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
   public static final Charset GB18030 =  Charset.forName("GB18030");

   static final int  TIME_OUT  = Integer.getInteger("http.timeout", 40000);

   static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.4 (KHTML, like Gecko) Safari/537.4";

	  static HttpClient HTTP_CLIENT = bulidHttpClient();
	    static HttpClient  bulidHttpClient(){
//	        SchemeRegistry registry = new SchemeRegistry();
//	        registry.register(new Scheme("http",  80, PlainSocketFactory.getSocketFactory()));
//	        registry.register(new Scheme("https",  443, SSLSocketFactory.getSocketFactory()));
	        PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
	        cm.setMaxTotal(800);
	        cm.setDefaultMaxPerRoute(200);

	        cm.setMaxPerRoute(new HttpRoute(new HttpHost("localhost")),500);
	        cm.setMaxPerRoute(new HttpRoute(new HttpHost("127.0.0.1")),500);
	        cm.setMaxPerRoute(new HttpRoute(new HttpHost("us.izhubo.com")),500);
	        HttpParams defaultParams = new  BasicHttpParams();

	        defaultParams.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, TIME_OUT);
	        defaultParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);//连接超时
	        defaultParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);//读取超时

	        defaultParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
	        defaultParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,UTF8.name());
	        //defaultParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,"HTTP/1.1");
	        defaultParams.setParameter(CoreProtocolPNames.USER_AGENT,USER_AGENT);


//	        CacheConfig cacheConfig = new CacheConfig();
//	        cacheConfig.setMaxCacheEntries(5000);
//	        cacheConfig.setMaxObjectSize(8192 * 4);

	        HttpClient client = new DefaultHttpClient(cm,defaultParams);
	        // 500 错误 重试一次 bw, also retry by seeds..
//	        client = new AutoRetryHttpClient(client,new ServiceUnavailableRetryStrategy() {
//	            @Override
//	            public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
//	                return executionCount <= 2 &&
//	                        response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR;
//	            }
//	            @Override
//	            public long getRetryInterval() {
//	                return 1500;
//	            }
//	        });

	        client = new DecompressingHttpClient(client);

	        return client;
	        //return new CachingHttpClient(client, cacheConfig);
	    }
    private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000).build();

    private static HttpClientUtil instance = null;
    public static String get(String url,Map<String,String> HEADERS,Charset forceCharset)throws IOException{
        HttpGet get = new HttpGet(url);
        return execute(get,HEADERS,forceCharset);
    }
    private HttpClientUtil() {
    }

    public static HttpClientUtil getInstance() {
        if (instance == null) {
            instance = new HttpClientUtil();
        }
        return instance;
    }
    public static String postXML(String url , Map<String,String> headers , String xmlString) throws IOException{
    	HttpPost post = new HttpPost(url);
    	
    	StringEntity postEntity = new StringEntity(xmlString, "UTF-8");
    	post.addHeader("Content-Type", "text/xml");
    	post.setEntity(postEntity);

    	return execute(post,headers,null);
    }
    private static String execute(final HttpRequestBase request,Map<String,String> headers,final Charset forceCharset)throws IOException{
        return http(HTTP_CLIENT,request,headers,new HttpEntityHandler<String>() {
            @Override
            public String handle(HttpEntity entity) throws IOException{
                if (entity == null) {
                    return null;
                }
//                        if(log.isDebugEnabled()){
//                            CacheResponseStatus responseStatus = (CacheResponseStatus) localContext.getAttribute(
//                                CachingHttpClient.CACHE_RESPONSE_STATUS);
//
//                            if(CacheResponseStatus.CACHE_HIT == responseStatus){
//                                log.debug("A response hit cache with no requests :{}",request.getURI());
//                            }
//                        }
                byte[] content = EntityUtils.toByteArray(entity);
                if(forceCharset != null){
                    return new String(content,forceCharset) ;
                }
                String html;
                Charset charset =null;
                ContentType contentType = ContentType.get(entity);
                if(contentType !=null){
                    charset = contentType.getCharset();
                }
                if(charset ==null){
                    charset =GB18030;
                }
                html = new String(content,charset) ;
                charset = checkMetaCharset(html,charset);
                if(charset!=null){
                    html = new String(content,charset);
                }
                return html;
            }
            public String getName() {
                return request.getMethod();
            }
        });
    }
    private static Charset checkMetaCharset(String html,Charset use){
        String magic ="charset=";
        int index = html.indexOf(magic);
        if(index >0 && index < 1000){
            index+=magic.length();
            int end = html.indexOf('"',index);
            if(end > index){
                try{

                    String charSetString = html.substring(index,end).toLowerCase();

                    if(charSetString.length() > 10){
                        return null;
                    }
                    //GBK GB2312 --> GB18030
                    if(charSetString.startsWith("gb")){
                        return GB18030.equals(use) ? null : GB18030;
                    }
                    Charset curr = Charset.forName(charSetString);
                    if(!curr.equals(use)){
                        return curr;
                    }
                }catch (Exception e){
                   log.error("Get MetaCharset error",e);
                }
            }
        }

        return null;
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
    
    /**
     * 发送 post请求
     * @param httpUrl 地址
     */
    public String sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     * @param httpUrl 地址
     * @param params 参数(格式:key1=value1&key2=value2)
     */
    public String sendHttpPost(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }
    /** 
     * @description 发送Http请求 
     * @param request 
     * @return 
     */  
    private static String sendRequest(HttpUriRequest request) {  
  
        HttpClient client = new DefaultHttpClient();  
        String line = null;  
        StringBuffer sb = new StringBuffer();  
        try {  
            HttpResponse res = client.execute(request);  
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                HttpEntity entity = res.getEntity();  
                InputStreamReader isr = new InputStreamReader(  
                        entity.getContent(),HTTP.UTF_8);  
                BufferedReader bufr = new BufferedReader(isr);// 缓冲  
                while ((line = bufr.readLine()) != null) {  
                    sb.append(line);  
                }  
                isr.close();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } finally {  
            // 关闭连接 ,释放资源  
            client.getConnectionManager().shutdown();  
        }  
        return sb.toString();  
    }  
    /** 
     * @description 向指定的URL发起一个put请求 
     * @param uri 
     * @param values 
     * @return 
     * @throws IOException 
     */  
    public static String doPut(String url, List<NameValuePair> values)  
            throws IOException {  
        HttpPut request = new HttpPut(url);  
   
        if (values != null) {  
            request.setEntity(new UrlEncodedFormEntity(values, "utf-8"));
        }  
        return sendRequest(request);  
    }  
   
    /**
     * 发送 post请求
     * @param httpUrl 地址
     * @param maps 参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps) {
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
     * 发送 post请求（带文件）
     * @param httpUrl 地址
     * @param maps 参数
     * @param fileLists 附件
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
        for (String key : maps.keySet()) {
            meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
        }
        for (File file : fileLists) {
            FileBody fileBody = new FileBody(file);
            meBuilder.addPart("files", fileBody);
        }
        HttpEntity reqEntity = meBuilder.build();
        httpPost.setEntity(reqEntity);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送Post请求
     * @param httpPost
     * @return
     */
    private String sendHttpPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
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

    /**
     * 发送 get请求
     * @param httpUrl
     */
    public String sendHttpGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpGet(httpGet);
    }

    /**
     * 发送 get请求Https
     * @param httpUrl
     */
    public String sendHttpsGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpsGet(httpGet);
    }

    /**
     * 发送Get请求
     * @param httpPost
     * @return
     */
    private String sendHttpGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
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

    /**
     * 发送Get请求Https
     * @param httpPost
     * @return
     */
    private String sendHttpsGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader
                    .load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
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
    public static void main(String[] args) {
    	String url = "http://177.77.83.248:8084/inner/userInfo?";
		List<NameValuePair> values = new ArrayList<NameValuePair>();

		values.add(new BasicNameValuePair("token", ""));

		values.add(new BasicNameValuePair("nickName", "史宏杰"));
		try {
			String re = doPut(url, values);
			System.out.println(re);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
