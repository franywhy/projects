package com.izhubo.service;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;


public class SingletonSmsService {
	private static SmsService smsService=null;
	private SingletonSmsService(){
	}
	public synchronized static SmsService getSmsService(String softwareSerialNo,String key){
		if(smsService==null){
			try {
				smsService = new SmsService(softwareSerialNo,key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return smsService;
	}
	public synchronized static SmsService getSmsService(){
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		System.out.println(bundle.getString("softwareSerialNo"));
		if(smsService==null){
			try {
				smsService=new SmsService(bundle.getString("softwareSerialNo"),bundle.getString("key"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return smsService;
	}
}
