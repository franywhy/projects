package com.hq.courses.util;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.hq.adaptive.pojo.ZJEnum;

import io.renren.rest.persistent.KGS;

/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月23日
 */
public class CsCodeUtils {
	
//	@Resource
//	private static KGS adlChapterNoKGS;
//	@Resource
//	private static KGS adlSectionNoKGS;
//	@Resource
//	private static KGS adlKnowledgeNoKGS;
	
	/**
	 * 章编码
	 * @param head	课程code
	 * @param code
	 * @return
	 */
	public static String getChapterCode(String head, String code) {
		return getCode(head, ZJEnum.Z, code);
	}
	/**
	 * 节编码
	 * @param head	章code
	 * @param code
	 * @return
	 */
	public static String getSectionCode(String head, String code) {
		return getCode(head, ZJEnum.J, code);
//		return getCode(head, ZJEnum.Z, adlKnowledgeNoKGS.nextId()+"");
	}
	/**
	 * 课程编码
	 * @param head	节code
	 * @param code
	 * @return
	 */
	public static String getKnowledgeCode(String head, String code) {
		return getCode(head, ZJEnum.K, code);
//		return getCode(head, ZJEnum.K, adlSectionNoKGS.nextId()+"");
	}
	
	public static String getCode(String head, ZJEnum zj, String code) {
		return getCode(head, zj.getCodeSplit(), code);
	}
	
	private static String getCode(String head, String split, String code) {
		if (StringUtils.isNotBlank(head) && StringUtils.isNotBlank(split) && StringUtils.isNotBlank(code)) {
			return head + split + code;
		}
		return null;
	}
	
}
