package com.izhubo.alipay.util.details;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class DetailsUtils {
	
	public static void main(String[] args) {
//		// 指定参数的替换顺序
//        String string2 = "this %2$s for %1$s test";
//        System.out.println(String.format(string2, "AAA", "BBB"));
		
//		for(int i = 1 ; i < 10 ; i ++){
//			
//			String sql = "db.tiku_url.save({ '_id' :'" + UUID.randomUUID().toString() + "' , 'name' : '会计从业"+ i +"' , 'url' : 'SignLogin.aspx?openid=%1$s&openkey=%2$s&token=%3$s&tourl=catalog.aspx?gid=AA07FFA6-13F2-42BB-B4A0-7B905F88E3B3','order' : "+ i*100 +" , 'is_use' : true });";
//			System.out.println(sql);
//		}
		

		//时间
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DATE , -1);
//		System.out.println(cal.getTimeInMillis());
		
		/*List list = new ArrayList();
		
		for(int i = 1 ; i < 15 ; i++){
			list.add(i+"");
		}
		System.out.println(list.toString());
		int MAX = 12;
		for(int size0 = list.size() ; size0 > MAX ; size0 = list.size()){
			list.remove(size0-1);
		}
		System.out.println(list.toString());*/
		
		
//		System.out.println(sub(0.01, 0));
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = sdf.parse("2016-06-24 00:00:00");
//			Date date2 = sdf.parse("2016-06-25 00:00:00");
////			
//			System.out.println(date.getTime());
//			System.out.println(date2.getTime());
//			System.out.println(sdf.parse("2016-06-24 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-06-25 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-06-26 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-06-27 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-06-28 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-06-29 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-06-30 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-07-02 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-07-03 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-07-04 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-06-01 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-06-02 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-06-10 00:00:00").getTime());
//			System.out.println(sdf.parse("2016-06-20 00:00:00").getTime());
			System.out.println(sdf.parse("2016-10-23 00:00:00").getTime());
			System.out.println(sdf.parse("2016-11-01 00:00:00").getTime());
//			System.out.println(System.currentTimeMillis());
			
//			Long l1 = date.getTime();
//			Long l2 = date2.getTime();
			/*
			System.out.println(sdf.format(new Date(1474253943680L)));
			
//			List<Date> datas = new ArrayList();

//			for (int i = 1; i < 32; i++) {
//				r(i);
//			}
			
			String s = "bunus:time_out:0040c40b-594b-47b9-9ac6-0c5e8b25453a";
			System.out.println(s.split(":")[2]);*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println(System.currentTimeMillis());
		
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			
//			Date date = sdf.parse("2016-06-15 00:00:00");
//			System.out.println(date.getTime());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date(1474253943680L);
//		Date d2 = new Date(1465210719099L);
//		Date d3 = new Date(1465210719105L);
		System.out.println(sdf.format(d1));
//		System.out.println(sdf.format(d2));
//		System.out.println(sdf.format(d3));
		
//		System.out.println(dateBefore(0).getTime());
	}
	
	private static Date dateBefore(int d){
		Calendar c = Calendar.getInstance();  //得到当前日期和时间
		c.set(Calendar.HOUR_OF_DAY, 0);       //把当前时间小时变成０
		c.set(Calendar.MINUTE, 0);            //把当前时间分钟变成０
		c.set(Calendar.SECOND, 0);            //把当前时间秒数变成０
		c.set(Calendar.MILLISECOND, 0);       //把当前时间毫秒变成０
		c.add(Calendar.DATE, -d);			  //d天前
		return c.getTime();
	}

	
	public static void r(int d) {
//		// 指定参数的替换顺序
//        String string2 = "this %2$s for %1$s test";
//        System.out.println(String.format(string2, "AAA", "BBB"));
		
//		for(int i = 1 ; i < 10 ; i ++){
//			
//			String sql = "db.tiku_url.save({ '_id' :'" + UUID.randomUUID().toString() + "' , 'name' : '会计从业"+ i +"' , 'url' : 'SignLogin.aspx?openid=%1$s&openkey=%2$s&token=%3$s&tourl=catalog.aspx?gid=AA07FFA6-13F2-42BB-B4A0-7B905F88E3B3','order' : "+ i*100 +" , 'is_use' : true });";
//			System.out.println(sql);
//		}
		
		
		//时间
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DATE , -1);
//		System.out.println(cal.getTimeInMillis());
		
		/*List list = new ArrayList();
		
		for(int i = 1 ; i < 15 ; i++){
			list.add(i+"");
		}
		System.out.println(list.toString());
		int MAX = 12;
		for(int size0 = list.size() ; size0 > MAX ; size0 = list.size()){
			list.remove(size0-1);
		}
		System.out.println(list.toString());*/
		
		
//		System.out.println(sub(0.01, 0));
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = sdf.parse("2016-05-19 00:00:00");
//			Date date2 = sdf.parse("2016-07-18 00:12:00");
//			
//			System.out.println(date.getTime());
//			System.out.println(date2.getTime());
//			System.out.println(System.currentTimeMillis());
			
//			Long l1 = date.getTime();
//			Long l2 = date2.getTime();
			
			
			
//			List<Date> datas = new ArrayList();
			Calendar calendar = new GregorianCalendar(2016, 04, d,0,0,0);    
			
			Date d1 = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, +1);//取当前日期的后一天. 
			Date d2 = calendar.getTime();
			
//			for (int i = 0; i < 32; i++) {
//				datas.add(calendar.getTime());
//				calendar.add(Calendar.DAY_OF_MONTH, +1);//取当前日期的后一天. 
//			}
			
			StringBuffer sbf = new StringBuffer();
			
			sbf.append("/** 时间范围:" + sdf.format(d1) + "~" + sdf.format(d2) + "*/  \n");
			sbf.append(" var query = { $match : { \"timestamp\" : { $gt : " + d1.getTime() + ", $lte :  " + d2.getTime() + " } } };  \n");
			
//			sbf.append(" var query = { $match : { \"timestamp\" : { $gt : " + l1 + ", $lte :  " + l2 + " } } };  \n");
			sbf.append("   \n");
			sbf.append(" var group =  \n");
			sbf.append(" {  \n");
			sbf.append("   \"$group\":{ \n");
			sbf.append("                \"_id\":{\"author_id\":\"$author_id\"}, \n");
			sbf.append("                \"count\":{\"$sum\" : 1} \n");
			sbf.append("               } \n");
			sbf.append(" };  \n");
			sbf.append("   \n");
			sbf.append(" var match =  \n");
			sbf.append("  { \n");
			sbf.append("    \"$match\":{ \n");
			sbf.append("    			\"count\" : { \n");
			sbf.append("                	\"$gt\" : 0 \n");
			sbf.append("                } \n");
			sbf.append("              } \n");
			sbf.append("  };  \n");
//			sbf.append("   \n");
			sbf.append(" var sort =  \n");
			sbf.append(" {  \n");
			sbf.append("  \"$sort\":{  \n");
			sbf.append("      			\"count\" : -1  \n");
			sbf.append("  			}  \n");
			sbf.append("  };  \n");
			sbf.append("    \n");
			
			sbf.append(" /** 时间范围:" + sdf.format(d1) + "~" + sdf.format(d2) + "*/  \n");
//			sbf.append(" print('"+ sdf.format(d1) +" ')  \n");
			sbf.append(" var data = db.topics.aggregate(query, group, match,sort);  \n ");
			sbf.append("  for(var i = 0 ; i < data.result.length ; i ++){  \n");
			sbf.append("     if(i==0){print('"+ sdf.format(d1) +" ');print('学员id,提问数量');} \n");
			sbf.append("     var item = data.result[i]; \n");
			sbf.append("     print(item._id.author_id  + \",\" +item.count); \n");
			sbf.append("     db.topic_demo.save({'time' :'" + sdf.format(d1) + "' ,'user_id' : item._id.author_id , 'count' : item.count }) \n");
			sbf.append("   } \n");
			sbf.append("    \n");
			
			
			System.out.println(sbf.toString());
			
			
			
			writeTxtFile(sbf.toString() , "C:/Users/Administrator/Desktop/mjs/"+sdf.format(d1)+".js");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static boolean writeTxtFile(String content,String  fileName)throws Exception{  
		  RandomAccessFile mm=null;  
		  boolean flag=false;  
		  FileOutputStream o=null;  
		  try {  
		File f = new File(fileName);
		createFile(f);
		   o = new FileOutputStream(f);  
		      o.write(content.getBytes("GBK"));  
		      o.close();  
		//   mm=new RandomAccessFile(fileName,"rw");  
		//   mm.writeBytes(content);  
		   flag=true;  
		  } catch (Exception e) {  
		   // TODO: handle exception  
		   e.printStackTrace();  
		  }finally{  
		   if(mm!=null){  
		    mm.close();  
		   }  
		  }  
		  return flag;  
		 }  
	 /** 
	  * 创建文件 
	  * @param fileName 
	  * @return 
	  */  
	 public static boolean createFile(File fileName)throws Exception{  
	  boolean flag=false;  
	  try{  
	   if(!fileName.exists()){  
	    fileName.createNewFile();  
	    flag=true;  
	   }  
	  }catch(Exception e){  
	   e.printStackTrace();  
	  }  
	  return true;  
	 }   
	     
	
	/**
	  * 提供精确的减法运算。
	  * 
	  * @param v1
	  *            被减数
	  * @param v2
	  *            减数
	  * @return 两个参数的差
	  */
	 public static double sub(double v1, double v2) {
	  BigDecimal b1 = new BigDecimal(Double.toString(v1));
	  BigDecimal b2 = new BigDecimal(Double.toString(v2));
	  return b1.subtract(b2).doubleValue();
	 }
	
	
	/**
	 * 成功和失败信息转换成VO
	 * @Description: 成功和失败信息转换成VO
	 * @date 2015年8月20日 上午11:04:10 
	 * @param @param details 格式为：流水号^收款方账号^收款账号姓名^付款金额^成功标识(S)^成功原因(null)^支付宝内部流水号^完成时间。 每条记录以“|”间隔。
	 * @param @return  List<DetailVo> list 
	 * @throws
	 */
	public static List<DetailVo> strToDetailVo(String details) {
		List<DetailVo> list = new ArrayList<DetailVo>();
		if (StringUtils.isNotBlank(details)) {
			String detailItem[] = details.split(DetailConfing.SR2);
			if (detailItem != null && detailItem.length > 0) {
				for (int i = 0 , len =detailItem.length ; i <len ; i ++) {
					String string = detailItem[i];
					String atts[] = string.split(DetailConfing.SR1);
					list.add(new DetailVo(atts[0], atts[1], atts[2], atts[3], atts[4], atts[5], atts[6], atts[7]));
				}
			}
		}
		return list;
	}
}
