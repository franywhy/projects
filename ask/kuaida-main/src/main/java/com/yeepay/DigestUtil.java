package com.yeepay;

import com.izhubo.rest.common.util.http.HttpClientUtil;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class DigestUtil {

    private static Charset encodingCharset = HttpClientUtil.UTF8;

    public static String hmacSign(String aValue, String aKey) {
        byte k_ipad[] = new byte[64];
        byte k_opad[] = new byte[64];
        byte keyb[] = aKey.getBytes(encodingCharset);
        byte value[]= aValue.getBytes(encodingCharset);
        Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
        Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
        for (int i = 0; i < keyb.length; i++) {
            k_ipad[i] = (byte) (keyb[i] ^ 0x36);
            k_opad[i] = (byte) (keyb[i] ^ 0x5c);
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {

            return null;
        }
        md.update(k_ipad);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return toHex(dg);
    }

    public static String toHex(byte input[]) {
        if (input == null)
            return null;
        StringBuilder output = new StringBuilder(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            int current = input[i] & 0xff;
            if (current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }

        return output.toString();
    }

    /**
     *
     * @param args
     * @param key
     * @return
     */
    public static String getHmac(String[] args, String key) {
        if (args == null || args.length == 0) {
            return (null);
        }
        StringBuilder str = new StringBuilder(300);
        for (String arg  : args) {
            str.append(arg);
        }
//        System.out.println(
//                "hamcstr :" + str
//        );
//        System.out.println("Key :" + key);
        return (hmacSign(str.toString(), key));
    }

//	public static void main(String[] args) {
//		System.out.println(hmacSign("AnnulCard1000043252120080620160450.0http://localhost/SZXpro/callback.asp这4564868265473632445648682654736324511","8UPp0KE8sq73zVP370vko7C39403rtK1YwX40Td6irH216036H27Eb12792t"));
//	}
}
