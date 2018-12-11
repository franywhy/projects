package io.renren.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptographUtil {
	public static String AESEncrypt(String text, String iv, String password) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// setup key
		byte[] keyBytes = new byte[16];
		byte[] b = password.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length) len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);

		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));

		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] results = cipher.doFinal(text.getBytes("UTF-8"));

		return Base64.encode(results);
	}

	public static String AESDecrypt(String text, String iv, String password) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// setup key
		byte[] keyBytes = new byte[16];
		byte[] b = password.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length) len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		// IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		byte[] results = cipher.doFinal(Base64.decode(text));
		return new String(results, "UTF-8");
	}

}
