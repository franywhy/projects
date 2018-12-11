package com.school.utils;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密&解密工具类
 * 
 * @author lihaifei
 *
 */
public class EncryptionUtils {

	private static final String AES_KEY_ALGORITHM = "AES";
	private static final String AES_DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";// 默认的加密算法

	/**
	 * 对字符串做MD5加密，返回加密后的字符串。
	 * 
	 * @param text
	 *            待加密的字符串。
	 * @return 加密后的字符串。
	 */
	public static String md5Hex(String text) {
		return DigestUtils.md5Hex(text);
	}

	public static Boolean md5Check(String src, String dst) {
		String digest = DigestUtils.md5Hex(src);
		return digest.equals(dst);
	}

	/**
	 * 对字符串做SHA-1加密，返回加密后的字符串。
	 * 
	 * @param text
	 *            待加密的字符串。
	 * @return 加密后的字符串。
	 */
	public static String shaHex(String text) {
		return DigestUtils.sha1Hex(text);
	}

	/**
	 * 对字符串做SHA-1加密，然后截取前面20个字符（遗留OVP系统的密码验证方式）。
	 * 
	 * @param text
	 *            待加密的字符串。
	 * @return 加密后的前20个字符。
	 */
	public static String getLittleSHA1(String text) {
		String encryptedStr = DigestUtils.sha1Hex(text).toUpperCase();
		return encryptedStr.substring(0, 20);
	}

	/**
	 * base 64 编码
	 * 
	 * @param bytes
	 *            待编码的text
	 * @return 编码后的base 64 code
	 */
	public static String base64Encode(String text) {
		try {
			return Base64.encodeBase64String(text.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return text;
	}

	/**
	 * base 64 解码
	 * 
	 * @param base64Code
	 *            待解码的base 64 code
	 * @return 解码后的text
	 */
	public static String base64Decode(String base64Code) {
		try {
			return new String(Base64.decodeBase64(base64Code), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return base64Code;
	}

	/**
	 * AES 加密操作
	 * 
	 * @param content
	 *            待加密内容
	 * @param password
	 *            加密密码
	 * @return 返回Base64转码后的加密数据
	 */
	public static String AESencrypt(String content, String password) {
		try {
			byte[] passwordByte = password.getBytes("UTF-8");
	        SecretKeySpec skeySpec = new SecretKeySpec(passwordByte, AES_KEY_ALGORITHM);
	        Cipher cipher = Cipher.getInstance(AES_DEFAULT_CIPHER_ALGORITHM);//"算法/模式/补码方式"
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	        byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
			return EncryptionUtils.byte2hex(encrypted);
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
			byte[] passwordByte = password.getBytes("UTF-8");
	        SecretKeySpec skeySpec = new SecretKeySpec(passwordByte, AES_KEY_ALGORITHM);
	        Cipher cipher = Cipher.getInstance(AES_DEFAULT_CIPHER_ALGORITHM);//"算法/模式/补码方式"
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] result = cipher.doFinal(EncryptionUtils.hex2byte(content));
			return new String(result,"UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 二进制byte数组转十六进制byte数组 byte array to hex
	 * 
	 * @param b
	 *            byte array
	 * @return hex string
	 */
	public static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int i = 0; i < b.length; i++) {
			stmp = Integer.toHexString(b[i] & 0xFF);
			if (stmp.length() == 1) {
				hs.append("0").append(stmp);
			} else {
				hs.append(stmp);
			}
		}
		return hs.toString();
	}

	/**
	 * 十六进制byte数组转二进制byte数组 hex to byte array
	 * @param hex
	 *            hex string
	 * @return byte array
	 */
	public static byte[] hex2byte(String hex) throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException("invalid hex string");
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}

	public static void main(String[] args) {
		//String s = EncryptionUtils.AESencrypt("13824429749", SxConstant.Product_PracticeLearn_PASSWORD);
		
		//System.out.println(s);

		//System.out.println(EncryptionUtils.AESdecrypt("689d9e87218e058eaa9fe95034032ca8", SxConstant.Product_PracticeLearn_PASSWORD));

	}
}