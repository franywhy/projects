package io.renren.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.renren.entity.HDDataEntity;
import io.renren.entity.HDJsonEntity;
import io.renren.test.domain.Info;
import io.renren.test.domain.SSOEntity;
import io.renren.test.domain.SSOJsonEntity;
import io.renren.utils.weixin.HttpClientUtil;

public class SSO {
	public static void main(String[] args) {
		String password = new String(Base64.encodeBase64("123456".getBytes()));
		System.out.println(password);
		// 获取token
		String httpUrl = "http://177.77.83.248:8084/inner/login?";
		String param = "mobileNo=18620523707&passWord=" + password + "&clientType=web&versionCode=1";
		String result = HttpClientUtil.getInstance().sendHttpPost(httpUrl, param);
		Gson gson = new Gson();
		SSOJsonEntity SSOJson = gson.fromJson(result, SSOJsonEntity.class);
		SSOEntity listData = SSOJson.getData();
		String token = listData.getToken();

		String url = "http://177.77.83.248:8084/inner/userInfo?";
		List<NameValuePair> values = new ArrayList<NameValuePair>();

		values.add(new BasicNameValuePair("token", token));

		values.add(new BasicNameValuePair("nickName", ""));
		try {
			String re = doPut(url, values);
			System.out.println(re);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * httpUrl =
		 * "http://177.77.83.248:8084/inner/userMobileNo?token="+token;
		 * //param="token="+token; result =
		 * HttpClientUtil.getInstance().sendHttpGet(httpUrl);
		 */

		/*
		 * HttpClient httpClient = new HttpClient(); PutMethod httpPut = new
		 * PutMethod(httpUrl); httpPut.setQueryString(param);
		 * httpPut.getParams().setParameter(HttpMethodParams.
		 * HTTP_CONTENT_CHARSET, "UTF-8"); try {
		 * httpClient.executeMethod(httpPut); String res =
		 * httpPut.getResponseBodyAsString(); System.out.println(res); } catch
		 * (HttpException e) { e.printStackTrace(); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */

	}

	/**
	 * @description 向指定的URL发起一个put请求
	 * @param uri
	 * @param values
	 * @return
	 * @throws IOException
	 */
	public static String doPut(String url, List<NameValuePair> values) throws IOException {
		HttpPut request = new HttpPut(url);

		if (values != null) {
			request.setEntity(new UrlEncodedFormEntity(values, "utf-8"));
		}
		return sendRequest(request);
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
				InputStreamReader isr = new InputStreamReader(entity.getContent(), HTTP.UTF_8);
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

}
