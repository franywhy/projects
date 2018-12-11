package com.hq.courses.pojo;
/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年12月1日
 */
public enum CsConfigKeyEnum {
	
	KNOWLEDGE_LEVEL("ADP_KNOWLEDGE_LEVEL" , "知识点难度"),
	ADP_KNOWLEDGE_QUESTION_TYPE("ADP_KNOWLEDGE_QUESTION_TYPE" , "知识点题型"),
	ADP_KNOWLEDGE_KEY_POINT("ADP_KNOWLEDGE_KEY_POINT" , "知识点考点"),
	;
	
	private String ckey;
	private String remark;


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private CsConfigKeyEnum(String ckey, String remark) {
		this.ckey = ckey;
		this.remark = remark;
	}

	public String getCkey() {
		return ckey;
	}

	public void setCkey(String ckey) {
		this.ckey = ckey;
	}
	
}
