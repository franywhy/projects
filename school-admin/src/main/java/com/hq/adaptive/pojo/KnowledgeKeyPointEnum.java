package com.hq.adaptive.pojo;
/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年12月4日
 */
public enum KnowledgeKeyPointEnum {
	
	normal("正常",1),
	kp("考点",2);
	
	private String text;
	
	private Integer value;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	private KnowledgeKeyPointEnum(String text, Integer value) {
		this.text = text;
		this.value = value;
	}
	
	
}
