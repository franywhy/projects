package com.izhubo.rest.common.util;

import groovy.transform.CompileStatic;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 枚举一些消息摘要算法，简化摘要代码
 *
 * @author yangyang.cong
 */
@CompileStatic
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