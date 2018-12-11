package com.hq.adaptive.pojo;
/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年12月9日
 */
public enum AdlKnowledgeExcelColumnEnum {
	/** 章名称**/
	zmc(1,"章名称"),
	/** 节名称**/
	jmc(2,"节名称"),
	/** 知识点名称**/
	zsdmc(3,"知识点名称"),
	/** 难度**/
	nd(4,"难度"),
	/**考点**/
	kd(5,"考点"),
	/** 题型(多个请用逗号隔开)**/
	tx(6,"题型(多个请用逗号隔开)"),
	/** 前续知识点(多个请用逗号隔开)**/
	qxzsd(7,"前续知识点(多个请用逗号隔开)"),
	/** 视频vid  **/
	vid(8,"视频vid"),
	;
	//第几列
	private int index;
	//列名称
	private String columnName;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	private AdlKnowledgeExcelColumnEnum(int index, String columnName) {
		this.index = index;
		this.columnName = columnName;
	}
	
	public int getExcelIndex() {
		return this.index - 1;
	}
	
}
