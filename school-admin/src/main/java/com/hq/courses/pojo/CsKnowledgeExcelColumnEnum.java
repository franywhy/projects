package com.hq.courses.pojo;
/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年12月9日
 */
public enum CsKnowledgeExcelColumnEnum {
	/** 章编码**/
	zbm(1,"章编码"),
	/** 章名称**/
	zmc(2,"章名称"),
	/** 节编码**/
	jbm(3,"节编码"),
	/** 节名称**/
	jmc(4,"节名称"),
	/** 知识点编码**/
	zsdbm(5,"知识点编码"),
	/** 知识点名称**/
	zsdmc(6,"知识点名称"),
	/** 难度**/
	nd(7,"难度"),
	/**考点**/
	kd(8,"考点"),
	/** 题型(多个请用|隔开)**/
	tx(9,"题型(多个请用|隔开)"),
	/** 前续知识点(多个请用|隔开)**/
	qxzsd(10,"前续知识点(多个请用|隔开)"),
	vid(11,"视频id");
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
	private CsKnowledgeExcelColumnEnum(int index, String columnName) {
		this.index = index;
		this.columnName = columnName;
	}
	
	public int getExcelIndex() {
		return this.index - 1;
	}
	
}
