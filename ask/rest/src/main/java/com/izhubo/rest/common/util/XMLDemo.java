package com.izhubo.rest.common.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement (name = "xml")   
//@XmlType(propOrder = { "s1", "s2", "s3" })
public class XMLDemo {

	@XmlElement(name = "ss1", required = true) 
	private String s1 = "a";
	@XmlElement(name = "s2", required = true) 
	private String s2 = "b";
	@XmlElement  
	private String s3 = "c";

	public String getS1() {
		return s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}

	public String getS3() {
		return s3;
	}

	public void setS3(String s3) {
		this.s3 = s3;
	}

	@Override
	public String toString() {
		return "XMLDemo [s1=" + s1 + ", s2=" + s2 + ", s3=" + s3 + "]";
	}

	public XMLDemo() {
		super();
	}

}
