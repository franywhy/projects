package com.hqonline.model;
/**
 * 文章类型
 * 0.企业动态 1.企业文化 2.总裁语录 3.2017年会
 * @author shihongjie
 *
 */
public enum ArticleType {
	企业动态,
	企业文化,
	总裁语录,
	年会2017
	;
	
	public static final String n(int t){
		String s = null;
		switch(t){
		case 0 :s = "企业动态";break;
		case 1 :s = "企业文化";break;
		case 2 :s = "总裁语录";break;
		case 3 :s = "2017年会";break;
			default : s = "";
		}
		return s;
	}
}
