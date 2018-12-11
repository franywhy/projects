package io.renren.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Demo {
	private static final String MD5_KEY = "%^\\$AF>.12*******zK";
	public static void main(String[] args) throws ParseException {
		
		Map<String , Object> map = new HashMap<>();
		map.put("i1", 1);
		map.put("s1", "v2");
		System.out.println(map.getOrDefault("i1", 1));
		System.out.println(map.getOrDefault("i2", 2));
		System.out.println(map.getOrDefault("s1", "s1"));
		System.out.println(map.getOrDefault("s2", "s2"));
		
		/*for (int i = 0; i < 500; i++) {
			System.out.print(i + ",");
			if(i > 50 && i%50 ==0){
				
				System.out.println();
			}
		}*/
//		for(int i = 0 ; i < 5 ; i++){
//			if(3==i){
//				continue;
//			}
//			System.out.println(i);
//			
//		}
		
//		sf1();
//		d1();
//		d2();
//		d3();
//		d4();
//		d5();
//		System.out.println(new Sha256Hash("123456").toHex());
//		System.out.println(System.currentTimeMillis());
		
//		byte[] b = null;
//		String str64 = null;
//		try {
//			b = "hqzk123456".getBytes("utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		str64 = new BASE64Encoder().encode(b);
//		String md5Hex = EncryptionUtils.md5Hex(str64 + MD5_KEY);
//		System.out.println(md5Hex);
	}
	
	public static final void sf1(){
		String string = "/zk-message.html?contentId=%d&token=%s";
		String string2 = String.format(string, 1 ,"%s");
		System.out.println(string2);
		System.out.println(String.format(string2, "xxx"));
	}
	
	static void d1(){
		String string = "2017-04-01 00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println(string + "=" + sdf.parse(string).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void d2(){
		String string = "2017-05-01 00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println(string + "=" + sdf.parse(string).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	static void d3(){
		String string = "2017-03-31 00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println(string + "=" + sdf.parse(string).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void d4(){
		String string = "2017-03-01 00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println(string + "=" + sdf.parse(string).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void d5(){
		String string = "2017-04-01 00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println(string + "=" + sdf.parse(string).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
