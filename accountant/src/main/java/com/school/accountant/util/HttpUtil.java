package com.school.accountant.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	/**
	 * @Description:使用HttpClient发送post请求
	 */
	public static String httpFormPost(String urlParam, Map<String, Object> params) {
		StringBuffer resultBuffer = null;
		BufferedReader br = null;

		CloseableHttpClient httpClient = HttpClients.createDefault();// HttpClient4.3版本之后官方推荐的方式
		CloseableHttpResponse response = null;// HttpClient4.3版本之后官方推荐CloseableHttpResponse
		// 构建请求参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> elem = iterator.next();
			list.add(new BasicNameValuePair(elem.getKey(), String.valueOf(elem.getValue())));
		}
		try {
			HttpPost httpPost = new HttpPost(urlParam);
			// 设置http请求头Header，请求头具体内容详见org.apache.http.HttpHeaders
			/**
			 * 常见的几种Content-Type application/xml ： XML数据格式 application/json ：
			 * JSON数据格式 application/x-www-form-urlencoded ：
			 * <form encType=””>中默认的encType，form表单数据被编码为key/value格式发送到服务器（
			 * 表单默认的提交数据的格式）
			 */
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			// httpPost.setHeader("Content-Type", "text/html;charset=UTF-8");
			// httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
			httpPost.setHeader("Content-Encoding", "UTF-8");
			if (list.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
				httpPost.setEntity(entity);
			}
			response = httpClient.execute(httpPost);
			// 读取服务器响应数据
			resultBuffer = new StringBuffer();
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
				String temp;
				while ((temp = br.readLine()) != null) {
					resultBuffer.append(temp);
				}
				// 关闭资源
				EntityUtils.consume(httpEntity);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					response = null;
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					httpClient = null;
					e.printStackTrace();
				}
			}
		}
		return resultBuffer.toString();
	}

	/**
	 * JSON方式提交POST请求
	 * 
	 * @param url
	 * @param json
	 *            JSONObject params = new JSONObject(); 
	 *            params.put("reqId","4201030000574040000000001");
	 * @return
	 */
	public static String doPost4Json(String url, String json) {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;// HttpClient4.3版本之后官方推荐CloseableHttpResponse
		String result = null;
		try {
			httpClient = HttpClients.createDefault();// HttpClient4.3版本之后官方推荐的方式
			HttpPost httpPost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(json, "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json;charset=UTF-8");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, "UTF-8");
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					response = null;
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					httpClient = null;
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 请求SSO接口
	 * @param url
	 * @return
	 */
	public static String doPostWithJson4SSO(String url) {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;// HttpClient4.3版本之后官方推荐CloseableHttpResponse
		String result = null;
		try {
			httpClient = HttpClients.createDefault();// HttpClient4.3版本之后官方推荐的方式
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Encoding", "UTF-8");
			httpPost.setHeader("Content-Type", "text/html;charset=utf-8");
			httpPost.setHeader("X-Forward-School","kuaiji");
			response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, "UTF-8");
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					response = null;
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					httpClient = null;
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static String doGet4Json(String url) {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;// HttpClient4.3版本之后官方推荐CloseableHttpResponse
		String result = null;
		try {
			httpClient = HttpClients.createDefault();// HttpClient4.3版本之后官方推荐的方式
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Content-Type", "text/html;charset=utf-8");
			httpGet.setHeader("Content-Encoding", "UTF-8");
			response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, "UTF-8");
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					response = null;
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					httpClient = null;
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		/*Map<String, Object> params = new HashMap<String, Object>();
		String result = HttpUtil.httpFormPost("http://127.0.0.1:8080/common/user/ajax.jhtml", params);
		System.out.println(result);*/
	}
}
