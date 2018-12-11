package com.hq.courses.pojo.query;

/**
 * 静态变量表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public class CsConfigQuery extends BaseQuery {
	
	public String ckey;
	public String cname;
	public String cvalue;
	
	public String getCvalue() {
		return cvalue;
	}
	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}
	public String getCkey() {
		return ckey;
	}
	public void setCkey(String ckey) {
		this.ckey = ckey;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	@Override
	public String toString() {
		return "AdlConfigQuery [ckey=" + ckey + ", cname=" + cname + ", cvalue=" + cvalue + "]";
	}
	
	
	
}
