package io.renren.service;

import java.util.Map;

import io.renren.utils.QuoteConstant;
import io.renren.utils.SyncDateConstant;
/**
 * 通用引用校验方法
 * @author xiechaojun
 *2017年5月18日
 */
public interface SysCheckQuoteService {
	
	/**
	 * 校验quoteConstant中表的字段是否被引用
	 * @param map
	 * @param quoteConstant
	 * @return  true：被引用
	 */
	Boolean checkQuote(Map<String, Object> map , QuoteConstant quoteConstant);
	
	String syncDate(Map<String, Object> map , SyncDateConstant syncDateConstant);
	
	void updateSyncTime(Map<String, Object> map , SyncDateConstant syncDateConstant);
	
}
