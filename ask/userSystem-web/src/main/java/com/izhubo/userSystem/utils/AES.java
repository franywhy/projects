package com.izhubo.userSystem.utils;



import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.utils.URLEncodedUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/*******************************************************************************
 * AES加解密算法
 * 
 * @author arix04
 * 
 */

public class AES {
	
	
	///加密
	public static String aesEncrypt(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
        return new BASE64Encoder().encode(bytes);
    }
	
	//解密
	public static String aesDecrypt(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = new BASE64Decoder().decodeBuffer(str);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, "utf-8");
    }

   
	//加密
	public static String aesEncrypt_hex(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
        return AES.byte2hex(bytes);
    }
	
	//解密
	public static String aesDecrypt_hex(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = AES.hex2byte(str);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, "utf-8");
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

/*public static void main(String[] args) {
	//String s = new String(Base64.getUrlDecoder().decode("YTYwZTFmNmU0NTMxN2RkMWJkNzJiYTgzNjUxM2NlNTQ="));
	try {
		//System.out.println(AES.aesDecrypt("YTYwZTFmNmU0NTMxN2RkMWJkNzJiYTgzNjUxM2NlNTQ=", "06k9bCEpUAKeL9I3"));
		String s1 = AES.aesEncrypt_hex("15895838212", "06k9bCEpUAKeL9I3");
		//a60e1f6e45317dd1bd72ba836513ce54
		//a60e1f6e45317dd1bd72ba836513ce54
		System.out.println("---"+s1);
		String s2 = URLEncoder.encode(s1, "utf-8");
		System.out.println(s2);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}*/
  
}