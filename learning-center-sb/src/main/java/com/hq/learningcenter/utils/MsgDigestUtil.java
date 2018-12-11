package com.hq.learningcenter.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 枚举一些消息摘要算法，简化摘要代码
 *
 * @author yangyang.cong
 */
public enum MsgDigestUtil {
    MD2("MD2"),
    MD5("MD5"),
    SHA("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

//    MessageDigest msgDigest;
//    Lock lock = new ReentrantLock();
    private static final char[] DIGITS_LOWER;
    private static final char[] DIGITS_UPPER;

    final String name;

    private MsgDigestUtil(String name) {
        this.name = name;
//        try {
//            this.msgDigest = MessageDigest.getInstance(name);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("无法获取摘要算法:" + name, e);
//        }
    }

    // static case thread use diff INSTANCE error .
    final ThreadLocal<MessageDigest> mdLocal = new ThreadLocal<MessageDigest>();


    public byte[] digest(byte[] targets) {
        MessageDigest md = mdLocal.get();

        if(md == null){
            try {
                md = MessageDigest.getInstance(name);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("无法获取摘要算法:" + name, e);
            }
            mdLocal.set(md);
        }else {
            md.reset();
        }
        return md.digest(targets);

//        this.lock.lock();
//        try {
//            this.msgDigest.reset();
//            return this.msgDigest.digest(targets);
//        } finally {
//            this.lock.unlock();
//        }
    }

    public String digest2HEX(byte[] targets, boolean toLowerCase) {
        return new String(encodeHex(digest(targets), toLowerCase));
    }

    public String digest2HEX(String targets, boolean toLowerCase) {
        try {
            return digest2HEX(targets.getBytes("UTF-8"), toLowerCase);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String digest2HEX(String targets) {
        return digest2HEX(targets, true);
    }

    public static final char[] encodeHex(byte[] data, boolean toLowerCase) {
        char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
        int l = data.length;
        char[] out = new char[l << 1];

        int i = 0;
        for (int j = 0; i < l; i++) {
            out[(j++)] = toDigits[((0xF0 & data[i]) >>> 4)];
            out[(j++)] = toDigits[(0xF & data[i])];
        }
        return out;
    }
    
    //特意为.NET准备的MD5算法，与会计城对接使用 byzhaokun 20150727
    public static String MD5ForDotNet(String str)
    {
    	        String reStr = null;  
    	        try {  
    	            MessageDigest md5 = MessageDigest.getInstance("MD5");  
    	            byte[] bytes = md5.digest(str.getBytes());  
    	            StringBuffer stringBuffer = new StringBuffer();  
    	            for (byte b : bytes){  
    	                int bt = b&0xff;  
    	                if (bt < 16){  
    	                    stringBuffer.append(0);  
    	                }   
    	                stringBuffer.append(Integer.toHexString(bt));  
    	            }  
    	            reStr = stringBuffer.toString();  
    	        } catch (NoSuchAlgorithmException e) {  
    	            e.printStackTrace();  
    	        }  
    	        return reStr;  
    	      
    }
    
    /** 
     * MD5加密 
     * @param message 要进行MD5加密的字符串 
     * @return 加密结果为32位字符串 
     */  
    public static String getMD5(String message) {  
        MessageDigest messageDigest = null;  
        StringBuffer md5StrBuff = new StringBuffer();  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
            messageDigest.reset();  
            messageDigest.update(message.getBytes("UTF-8"));  
              
            byte[] byteArray = messageDigest.digest();  
            for (int i = 0; i < byteArray.length; i++)   
            {  
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
                else  
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
            }  
        } catch (Exception e) {  
            throw new RuntimeException();  
        }  
        return md5StrBuff.toString().toUpperCase();//字母大写  
    }  
    
    /**
     * AES加密字符串
     * 
     * @param content
     *            需要被加密的字符串
     * @param password
     *            加密需要的密码
     * @return 密文
     */
    public static String aesEncrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者

            kgen.init(128, new SecureRandom(password.getBytes()));// 利用用户密码作为随机数初始化出 128位的key生产者
            
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行

            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥

            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
            
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥

            Cipher cipher = Cipher.getInstance("AES");// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return ParseSystemUtil.parseByte2HexStr(result);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * 解密AES加密过的字符串
     * 
     * @param content
     *            AES加密过过的内容
     * @param password
     *            加密时的密码
     * @return 明文
     */
    public static String aesDecrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(content);  
            return ParseSystemUtil.parseByte2HexStr(result); // 明文
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
//    public static void main(String[] args) {
//    	String s = getMD5("hX2pX21cLZaqBnH8Spxi4Q==oLNuVUoBPJ/LeQUNyFp4YQ==ap02061");
//    	System.out.println(s);
//	}
    
//    /** 
//     * MD5加密,加密结果采用Base64进行编码 
//     * @param message 要进行MD5加密的字符串 
//     * @return 
//     */  
//    public static String getMD5ByBase64(String message) {  
//        MessageDigest md= null;  
//        try {  
//            md= MessageDigest.getInstance("MD5");  
//            byte md5[]=md.digest(message.getBytes());  
//            BASE64Encoder base64=new BASE64Encoder();  
//            return base64.encode(md5);  
//        } catch (Exception e) {  
//            throw new RuntimeException();  
//        }  
//    }  

    static {
        DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }
}