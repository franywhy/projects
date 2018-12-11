package com.hq.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpConnManager {

    private static final Logger TRACER = LoggerFactory.getLogger(HttpConnManager.class);

    private final static String DEFAULT_CHARSET = "UTF-8";

    private static CloseableHttpClient httpclient = null;

    public void init(Integer defaultConnTimeout,
                     Integer defaultConnRequestTimeout,
                     Integer defaultSocketTimeout,
                     Integer maxTotal,
                     Integer defaultMaxPerRoute,
                     Integer retryCount,
                     Boolean expectContinueEnabled) {


        RequestConfig params = RequestConfig.custom()
                .setConnectTimeout(defaultConnTimeout)
                .setConnectionRequestTimeout(defaultConnRequestTimeout)
                .setSocketTimeout(defaultSocketTimeout)
                .setExpectContinueEnabled(expectContinueEnabled).build();
        // initialize tcp pool
        PoolingHttpClientConnectionManager pccm = new PoolingHttpClientConnectionManager();
        pccm.setMaxTotal(maxTotal); // 连接池最大并发连接数
        pccm.setDefaultMaxPerRoute(defaultMaxPerRoute); // max concurrency count of a single route
        pccm.closeExpiredConnections();
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {

                if (executionCount > retryCount) {
                    return false;
                } else {
                    return true;
                }
            }
        };
        httpclient = HttpClients.custom().setConnectionManager(pccm).setDefaultRequestConfig(params).setRetryHandler(retryHandler)
                .build();
    }

    public HttpPlainResult invoke(HttpMethod method,
                                  Integer connTimeout,
                                  Integer connRequestTimeout,
                                  Integer socketTimeout,
                                  String url,
                                  Map<String, Object> map,
                                  String charset,
                                  String businessId) throws IOException, URISyntaxException {
        // Check Parameter
        if (httpclient == null) {
            throw new RuntimeException("httpclient is null.");
        }
        if (url == null || url.trim().length() == 0) {
            throw new RuntimeException("url is blank.");
        }
        if (charset == null || charset.trim().length() == 0) {
            throw new RuntimeException("character set is blank.");
        }
        if (method == null) {
            throw new RuntimeException("HttpMethod set is blank.");
        }
        HttpRequestBase request = getRequest(method, map, url, charset, businessId);
        if (connTimeout != null && connRequestTimeout != null && socketTimeout != null) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connTimeout)
                    .setConnectionRequestTimeout(connRequestTimeout).setSocketTimeout(socketTimeout).build();
            request.setConfig(requestConfig);
        }
        return remoteInvoke(request, charset);

    }

    public HttpPlainResult invoke(HttpMethod method,
                                  String url,
                                  Map<String, Object> map,
                                  String charset,
                                  String businessId) throws URISyntaxException, IOException {

        // Check Parameter
        if (httpclient == null) {
            throw new RuntimeException("httpclient is null.");
        }
        if (url == null || url.trim().length() == 0) {
            throw new RuntimeException("url is blank.");
        }
        if (charset == null || charset.trim().length() == 0) {
            throw new RuntimeException("character set is blank.");
        }
        if (method == null) {
            throw new RuntimeException("HttpMethod set is blank.");
        }
        HttpRequestBase request = getRequest(method, map, url, charset, businessId);
        return remoteInvoke(request, charset);
    }


    public HttpPlainResult invoke(HttpMethod method,
                                  String url,
                                  Map<String, Object> map, String businessId) throws IOException, URISyntaxException {
        return invoke(method, url, map, DEFAULT_CHARSET, businessId);
    }

    private HttpPlainResult remoteInvoke(HttpRequestBase request, String charset) {
        CloseableHttpResponse response = null;
        HttpPlainResult resultEntry = new HttpPlainResult();
        try {
            response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, charset);
                resultEntry.setCode(HttpResultType.OK);
                resultEntry.setResult(result);
                EntityUtils.consume(entity);
                return resultEntry;
            } else {
                resultEntry.setCode(HttpResultType.ERROR);
                resultEntry.setResult(null);
                return resultEntry;
            }
        } catch (IOException e) {
            TRACER.error("HttpMethod:" + request.getMethod() + " URI:" + request.getURI() + " is request exception", e);
            resultEntry.setCode(HttpResultType.TIMEOUT);
            resultEntry.setResult(null);
            return resultEntry;
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                TRACER.error("method:" + request.getMethod() + " URI:" + request.getURI() + " is closed exception", e);
            }
        }
    }

    private HttpRequestBase getRequest(HttpMethod method,
                                       Map<String, Object> map,
                                       String url,
                                       String charset,
                                       String businessId) throws UnsupportedEncodingException, URISyntaxException {
        HttpRequestBase request = null;
        switch (method) {
            case PUT:
                request = new HttpPut(url);
            case POST:
                if (request == null)
                    request = new HttpPost(url);
                List<NameValuePair> nameValueList = new ArrayList<>();
                if (map != null && !map.isEmpty()) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        nameValueList.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                    }
                }
                try {
                    ((HttpEntityEnclosingRequestBase) request).setEntity(new UrlEncodedFormEntity(nameValueList, charset));
                } catch (UnsupportedEncodingException e) {
                    TRACER.error(charset + " is not support");
                    throw e;
                }
                break;
            case DELETE:
                request = new HttpDelete();
            case GET:
                if (request == null)
                    request = new HttpGet();
                StringBuilder keyAndValue = new StringBuilder();
                if (map != null && !map.isEmpty()) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        keyAndValue.append(entry.getKey());
                        keyAndValue.append("=");
                        keyAndValue.append(entry.getValue());
                        keyAndValue.append("&");
                    }
                }
                StringBuilder reqUrlBuilder = new StringBuilder();
                reqUrlBuilder.append(url).append("?").append(keyAndValue);
                String reqUrl = reqUrlBuilder.substring(0, reqUrlBuilder.length() - 1);
                try {
                    request.setURI(new URI(reqUrl));
                } catch (URISyntaxException e) {
                    TRACER.error(reqUrl + " is not legal");
                    throw e;
                }
                break;
            default:
                throw new RuntimeException("HttpMethod is not supported");
        }
        request.addHeader("X-Forward-School", businessId);
        return request;
    }
}
