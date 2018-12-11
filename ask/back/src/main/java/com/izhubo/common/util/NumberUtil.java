package com.izhubo.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 将程序中的double值精确到小数点后两位。可以四舍五入，也可以直接截断。
 * @author shihongjie
 * 2015-01-23
 */
public class NumberUtil {


    
    /**
     * The BigDecimal class provides operations for arithmetic, scale manipulation, rounding, comparison, hashing, and format conversion.
     * @param d
     * @return
     */
    public static double formatDouble2(double d) {
        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        
        return bg.doubleValue();
    }

    /**
     * NumberFormat is the abstract base class for all number formats. 
     * This class provides the interface for formatting and parsing numbers.
     * @param d
     * @return
     */
    public static String formatDouble3(Double d , int len) {
    	if(d == null){
    		return "0";
    	}
        NumberFormat nf = NumberFormat.getNumberInstance();
        
        // 保留两位小数
        nf.setMaximumFractionDigits(len); 
        
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.UP);
        
        return nf.format(d);
    }

    /**
     * 这个方法挺简单的。
     * DecimalFormat is a concrete subclass of NumberFormat that formats decimal numbers. 
     * @param d
     * @return
     */
    public static String formatDouble4(double d) {
        DecimalFormat df = new DecimalFormat("#.00");

        
        return df.format(d);
    }

    
    /**
     * 如果只是用于程序中的格式化数值然后输出，那么这个方法还是挺方便的。
     * 应该是这样使用：System.out.println(String.format("%.2f", d));
     * @param d
     * @return
     */
    public static String formatDouble5(Double d , int len) {
    	if(d == null){
    		return "0";
    	}
        return String.format("%."+len+"f", d);
    }

//    public static void main(String[] args) {
//        double d = 12345.67890;
//        //TODO 暂未使用 如果需要用到请重构
//        System.out.println(formatDouble2(d));
//        System.out.println(formatDouble4(d));
//        //已用到
//        System.out.println(formatDouble3(d ,1));
//        System.out.println(formatDouble5(d,1));
//    12345.68
//    12345.68
//    12,346
//    12345.7
//    }

}
