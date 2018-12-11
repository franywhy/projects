package com.school.accountant.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密&解密工具类
 * @author lihaifei
 *
 */
public class EncryptionUtils {
    
	
	private static final String AES_KEY_ALGORITHM = "AES";
    private static final String AES_DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
	
	
	/**
     * 对字符串做MD5加密，返回加密后的字符串。
     * @param text 待加密的字符串。
     * @return 加密后的字符串。
    */
    public static String md5Hex(String text) {
        return DigestUtils.md5Hex(text);
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
    
    /** 
     * base 64 编码 
     * @param bytes 待编码的text
     * @return 编码后的base 64 code 
     * @throws UnsupportedEncodingException 
     */  
    public static String base64Encode(String text){
        try {
			return Base64.encodeBase64String(text.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
      
    /** 
     * base 64 解码 
     * @param base64Code 待解码的base 64 code 
     * @return 解码后的text
     * @throws Exception 
     */  
    public static String base64Decode(String base64Code) throws Exception{
    	return new String(Base64.decodeBase64(base64Code),"UTF-8");
    }
    
    /**
     * AES 加密操作
     * @param content 待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String AESencrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(AES_DEFAULT_CIPHER_ALGORITHM);// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception ex) {
            Logger.getLogger(EncryptionUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * AES 解密操作
     * @param content
     * @param password
     * @return
     */
    public static String AESdecrypt(String content, String password) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(AES_DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            Logger.getLogger(EncryptionUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    /**
     * AES生成加密秘钥
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(AES_KEY_ALGORITHM);
            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), AES_KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}