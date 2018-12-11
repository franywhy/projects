package com.hqjy.pay;

import lombok.Data;

public class PayConfig {
	private int id;
	private String ncUrl;
	private String borrowMoneyUrl;
	private String bwUrl;
	private String zfbCallBackUrl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNcUrl() {
		return ncUrl;
	}
	public void setNcUrl(String ncUrl) {
		this.ncUrl = ncUrl;
	}
	public String getBorrowMoneyUrl() {
		return borrowMoneyUrl;
	}
	public void setBorrowMoneyUrl(String borrowMoneyUrl) {
		this.borrowMoneyUrl = borrowMoneyUrl;
	}

	public String getBwUrl() {
		return bwUrl;
	}

	public void setBwUrl(String bwUrl) {
		this.bwUrl = bwUrl;
	}

	public String getZfbCallBackUrl() {
		return zfbCallBackUrl;
	}

	public void setZfbCallBackUrl(String zfbCallBackUrl) {
		this.zfbCallBackUrl = zfbCallBackUrl;
	}
}
