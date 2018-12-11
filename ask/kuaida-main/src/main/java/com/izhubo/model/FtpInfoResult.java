package com.izhubo.model;

import java.util.List;

public class FtpInfoResult implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3185059841804852134L;

	private List<FtpInfo> data;


	public List<FtpInfo> getData() {
		return data;
	}

	public void setData(List<FtpInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "FtpInfoResult [data=" + data + "]";
	}

}
