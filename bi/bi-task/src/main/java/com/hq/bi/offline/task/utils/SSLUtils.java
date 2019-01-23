package com.hq.bi.offline.task.utils;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @author zhouyibin
 * @date 2019/1/10
 * @desc 绕过SSL检测
 */
@Slf4j
public class SSLUtils {

    static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType){ }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType){ }

        @Override
        public X509Certificate[] getAcceptedIssuers() { return null; }
    } };

    public class NullHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String arg0, SSLSession arg1) { return true; }
    }

    public static void turnOffSSLChecking() throws NoSuchAlgorithmException, KeyManagementException {
        HttpsURLConnection.setDefaultHostnameVerifier(new SSLUtils().new NullHostNameVerifier());
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}
