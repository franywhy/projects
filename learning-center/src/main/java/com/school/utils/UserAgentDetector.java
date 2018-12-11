package com.school.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测用户的终端设备。
 * @author XingNing OU
 */
public abstract class UserAgentDetector {

    private static final String MOBILE_EXCLUDE_IPAD = "android|blackberry|benq|cell|htc|iphone|ipod|motorola|nokia|phone|symbian|zte"; // 不包括iPad
    private static final String WEIXIN = ".+MicroMessenger/([0-9\\.]+).*";
    private static final String IPAD = "ipad"; // iPad

    // 匹配模式
    private static final Pattern PATTERN_MOBILE_EXCLUDE_IPAD = Pattern.compile(MOBILE_EXCLUDE_IPAD,
            Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_IPAD = Pattern.compile(IPAD, Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_WEIXIN = Pattern.compile(WEIXIN, Pattern.CASE_INSENSITIVE);

    /**
     * 检测User-Agent是否属于移动终端设备。
     * @param userAgent User-Agent内容
     * @return 如果是移动设备则返回true，否则返回false
     */
    public static boolean isMobile(String userAgent) {
        if (null != userAgent) {
            Matcher m = PATTERN_MOBILE_EXCLUDE_IPAD.matcher(userAgent);
            return m.find();
        }
        return false;
    }

    /**
     * 检测User-Agent是否属于移动终端设备。
     * @param userAgent User-Agent内容
     * @return 如果是移动设备则返回true，否则返回false
     */
    public static boolean isWeixin(String userAgent) {
        if (null != userAgent) {
            Matcher m = PATTERN_WEIXIN.matcher(userAgent);
            return m.find();
        }
        return false;
    }

    /**
     * 解析User-Agent，获取微信的版本号，如6.3.16 。
     * @param userAgent User-Agent内容
     * @return 微信的版本号，如果非微信终端则返回空字符串
     */
    public static String getWeixinVersion(String userAgent) {
        if (null != userAgent) {
            Matcher m = PATTERN_WEIXIN.matcher(userAgent);
            if (m.find()) {
                String version = m.group(1);
                if (null != version) {
                    return version;
                }
            }
        }
        return "";
    }

    /**
     * 检测User-Agent是否属于iPad设备。
     * @param userAgent User-Agent内容
     * @return 如果是iPad设备则返回true，否则返回false
     */
    public static boolean isIpad(String userAgent) {
        Matcher m = PATTERN_IPAD.matcher(userAgent);
        return m.find();
    }

    public static void main(String[] args) {
        System.out.println(isMobile(
                "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0 like Mac OS X; en-us) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53"));
        System.out.println(getWeixinVersion(
                "Mozilla/5.0(iphone;CPU iphone OS 5_1_1 like Mac OS X) AppleWebKit/534.46(KHTML,like Geocko) Mobile/9B206 MicroMessenger/5.0"));

        System.out.println(getWeixinVersion(
                "Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13E238 MicroMessenger/6.3.16 NetType/WIFI Language/zh_CN"));

        String version = getWeixinVersion(
                "Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13E238 MicroMessenger/5.0.16 NetType/WIFI Language/zh_CN");

        int v = -1;
        int dotIndex = version.indexOf(".");
        if (dotIndex > 0) {
            v = Integer.parseInt(version.substring(0, dotIndex));
        } else {
            v = Integer.parseInt(version);
        }
        System.out.println(v);

        System.out.println((version.compareTo("5.0") >= 0));

    }
}
