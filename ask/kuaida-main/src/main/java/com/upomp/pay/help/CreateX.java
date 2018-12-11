package com.upomp.pay.help;

import java.text.SimpleDateFormat;
import java.util.Random;

public class CreateX {
    /*
     * 随机生成8位订单号（订单号：8到32位）
     */
    public static String CreateMerchantOrderId(int code_len) {
        int count = 0;
        char str[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < code_len) {
            int i = Math.abs(r.nextInt(10));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    public static String CreateMerchantOrderTime(){

        SimpleDateFormat data = new SimpleDateFormat("yyyyMMddHHmmss");

        String time = data.format(new java.util.Date());

        return time;
    }
}
