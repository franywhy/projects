package com.hqjy.pay.bmutils.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;


/**  
 * @Description: app 接口返回结果 vo
 * @author xushenpeng
 * @date 2016年3月30日
 */
public class ReturnObj implements Serializable{

    private static final long serialVersionUID = 2122571529078856666L;
    private int code;
	private String message;
	private String data;
	private String sign;
	//private static ObjectMapper objectMapper = new ObjectMapper();
	
	public ReturnObj(){
		this.code = 0;
		this.message = "request success!";
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		String string = null;
		try {
			//string = objectMapper.writeValueAsString(this);
            string = JSON.toJSONString(this);
		} catch (Exception e) {
			string = "ReturnObj JOSN 转换异常 {code:"+this.code+", message:"+this.message+", result:"+this.data+"}";
		}
		return string;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
