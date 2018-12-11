package com.hqjy.msg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by baobao on 2017/12/15 0015.
 * 网络判断类
 */
public class WebPingUtils {
    private static Logger logger = LoggerFactory.getLogger(WebPingUtils.class);
    public static void main(String[] args) {

     /*   String url = "jdbc:mysql://10.0.33.224:3309/test?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&useSSL=true";
        URI uri = getIP(URI.create(getIP(url)));

        System.out.println(getIP(url));
        System.out.println(uri.getHost()+"#"+uri.getPort());*/
        //System.out.println(connectWithSocket("10.0.98.39",6379));

    }

    public static boolean testMysqlConn(String url){

        URI uri = getIP(URI.create(getIP(url)));


        return connectWithSocket(uri.getHost(),uri.getPort());
    }

    private static boolean connectWithSocket(String host,int port){
        boolean isConned = false;
        try {
            InetAddress addr;
            //Socket sock = new Socket("10.0.33.224", 3308);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 100);//设置连接请求超时时间10 s(1000)

            addr = socket.getInetAddress();
            //System.out.println("连接到 " + addr);
            logger.info("连接到 " + addr+":"+port);
            isConned = true;
            socket.close();
        } catch (java.io.IOException e) {
            //System.out.println("无法连接 " + args[0]);
            logger.info(e+"无法连接 ip[" + host+":"+port+"]");
        }
        return isConned;

    }

    private static URI getIP(URI uri) {
        URI effectiveURI = null;

        try {
            // URI(String scheme, String userInfo, String host, int port, String
            // path, String query,String fragment)
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (Throwable var4) {
            effectiveURI = null;
        }

        return effectiveURI;
    }

    public static String getIP(String url) {
        url = url.replaceAll("jdbc:mysql","http");
        //使用正则表达式过滤，
        String re = "((http|ftp|https|jdbc:mysql)://)(([a-zA-Z0-9._-]+)|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(([a-zA-Z]{2,6})|(:[0-9]{1,4})?)";
        String str = "";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(re);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        //若url==http://127.0.0.1:9040或www.baidu.com的，正则表达式表示匹配
        if (matcher.matches()) {
            str = url;
        } else {
            String[] split2 = url.split(re);
            if (split2.length > 1) {
                String substring = url.substring(0, url.length() - split2[1].length());
                str = substring;
            } else {
                str = split2[0];
            }
        }
        return str;
    }
}
