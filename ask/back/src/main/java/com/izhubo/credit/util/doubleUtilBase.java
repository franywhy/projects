package com.izhubo.credit.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

//double类型-加减乘除高精度运算
public class doubleUtilBase {
	
	// 进行加法运算
	 public static  double add(double v1,double v2){  
	        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
	        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
	        return b1.add(b2).doubleValue();  
	    }  
	// 进行减法运算
	 public static double subtract(double v1,double v2){
	        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
	        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
	        return b1.subtract(b2).doubleValue();  
	    }
/**
 * 乘法
 * @param d1
 * @param d2
 * @return
 */
	    public static double mul(double d1, double d2){        // 进行乘法运算
	         BigDecimal b1 = new BigDecimal(d1);
	         BigDecimal b2 = new BigDecimal(d2);
	        return b1.multiply(b2).doubleValue();
	     }
	// 进行除法运算
	    public static double div(double d1,double d2,int len) {// 进行除法运算
	         BigDecimal b1 = new BigDecimal(d1);
	         BigDecimal b2 = new BigDecimal(d2);
	        return b1.divide(b2,len,BigDecimal.ROUND_HALF_UP).doubleValue();
	     }
	// 进行四舍五入操作
	    /**
	     * 4舍5入保留len位小数
	     * @param d
	     * @param len
	     * @return
	     */
	    public  static double round(double d,int len) {     // 进行四舍五入操作
	         BigDecimal b1 = new BigDecimal(d);
	         BigDecimal b2 = new BigDecimal(1);
	        // 任何一个数字除以1都是原数字
	        // ROUND_HALF_UP是BigDecimal的一个常量，表示进行四舍五入的操作
	        return b1.divide(b2, len,BigDecimal.ROUND_HALF_UP).doubleValue();
	     }
	    
	    
		public static String   setpercentString(double d){
			if (d==0){
				return "0.00%";
			}else {
				
				 
				
				 BigDecimal p = new BigDecimal(d);//保存4位小数
		         BigDecimal h = new BigDecimal(100);
		         

				
				
			DecimalFormat df=new DecimalFormat("#.00");
		   return   (df.format( p.multiply(h))+"%");
			}
			
		}
	    
	    
}
