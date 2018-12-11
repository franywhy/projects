package io.renren.dao;

import java.util.Map;

import io.renren.utils.SyncDateConstant;

/**
 * 基础Dao(还需在XML文件里，有对应的SQL语句)
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:31:36
 */
public interface SysCheckQuoteDao {
	// 判断是否有班型的引用
	int checkQuote(Map<String, Object> map);
	
    String syncDate(Map<String, Object> map);
    
    void updateSyncTime(Map<String, Object> map);
}
