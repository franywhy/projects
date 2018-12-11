package com.izhubo.web.pay.pc.bill99.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class Pkipair {
	
	
	public String signMsg( String signMsg) {

		String base64 = "";
		try {
			// 密钥仓库
			KeyStore ks = KeyStore.getInstance("PKCS12");

			// 读取密钥仓库
//			FileInputStream ksfis = new FileInputStream("D:/DEV2/Dev/Ask/api/main/src/main/groovy/com/izhubo/web/pay/pc/bill99/util/tester-rsa.pfx");
			
			// 读取密钥仓库（相对路径）
			URL aa = Pkipair.class.getClassLoader().getResource("unionpay/tester-rsa.pfx");
//			Resource resource = ApplicationContextFactory.getApplicationContext().getResource("classpath:com/springdemo/resource/test.txt");
//			ApplicationContextFactory.getApplicationContext().getResource("classpath:com/springdemo/resource/test.txt");
//			URL aa = Pkipair.class.getResource("unionpay/tester-rsa.pfx");
//			URL aa = Pkipair.class.getResource("bill99/pc/tester-rsa.pfx");
//			URL aa = Pkipair.class.getResource("tester-rsa.pfx");
//			System.out.println(aa);
//			String file = Pkipair.class.getResource("tester-rsa.pfx").getPath().replaceAll("%20", " ");
//			System.out.println(file);
			
			FileInputStream ksfis = new FileInputStream(aa.getPath());
			
			BufferedInputStream ksbufin = new BufferedInputStream(ksfis);

			char[] keyPwd = "123456".toCharArray();
			//char[] keyPwd = "YaoJiaNiLOVE999Year".toCharArray();
			ks.load(ksbufin, keyPwd);
			// 从密钥仓库得到私钥
			PrivateKey priK = (PrivateKey) ks.getKey("test-alias", keyPwd);
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(priK);
			signature.update(signMsg.getBytes("utf-8"));
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			base64 = encoder.encode(signature.sign());
			
		} catch(FileNotFoundException e){
			System.out.println("文件找不到");
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("test = "+base64);
		return base64;
	}
	public boolean enCodeByCer( String val, String msg) {
		boolean flag = false;
		try {
			//获得文件(绝对路径)
//			InputStream inStream = new FileInputStream("D:/DEV2/Dev/Ask/api/main/src/main/groovy/com/izhubo/web/pay/pc/bill99/util/99bill.cert.rsa.20340630test.cer");
//			
			//获得文件(相对路径)
			String file = Pkipair.class.getClassLoader().getResource("unionpay/99bill.cert.rsa.20340630test.cer").toURI().getPath();
//			String file = Pkipair.class.getResource("99bill[1].cert.rsa.20140803.cer").toURI().getPath();
//			System.out.println(file);
			FileInputStream inStream = new FileInputStream(file);
			
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
			//获得公钥
			PublicKey pk = cert.getPublicKey();
			//签名
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(pk);
			signature.update(val.getBytes());
			//解码
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			System.out.println(new String(decoder.decodeBuffer(msg)));
			flag = signature.verify(decoder.decodeBuffer(msg));
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("no");
		} 
		return flag;
	}
}
