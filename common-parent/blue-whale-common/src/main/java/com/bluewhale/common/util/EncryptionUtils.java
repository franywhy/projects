package com.bluewhale.common.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密工具类。
 * @author XingNing OU
 */
public class EncryptionUtils {

    /**
     * 对字符串做MD5加密，返回加密后的字符串。
     * @param text 待加密的字符串。
     * @return 加密后的字符串。
     */

    public static String md5Hex(String text) {
        return DigestUtils.md5Hex(text);
    }

    public static Boolean check(String md5,byte[] content){
        String hexResult = DigestUtils.md5Hex(content);
        return hexResult.equals(md5);
    }

    public static Boolean md5Check(String src,String dst) {
        String digest =  DigestUtils.md5Hex(src);
        return digest.equals(dst);
    }

    /**
     * 对字符串做SHA-1加密，返回加密后的字符串。
     * @param text 待加密的字符串。
     * @return 加密后的字符串。
     */
    public static String shaHex(String text) {
        return DigestUtils.sha1Hex(text);
    }

    /**
     * 对字符串做SHA-1加密，然后截取前面20个字符（遗留OVP系统的密码验证方式）。
     * @param text 待加密的字符串。
     * @return 加密后的前20个字符。
     */
    public static String getLittleSHA1(String text) {
        String encryptedStr = DigestUtils.sha1Hex(text).toUpperCase();
        return encryptedStr.substring(0, 20);
    }
}
