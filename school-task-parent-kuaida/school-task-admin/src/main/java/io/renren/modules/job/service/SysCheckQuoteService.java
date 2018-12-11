package io.renren.modules.job.service;

import io.renren.modules.job.utils.SyncDateConstant;

import java.util.Map;
/**
 * 通用引用校验方法
 * @author xiechaojun
 *2017年5月18日
 */
public interface SysCheckQuoteService {
	

	String syncDate(Map<String, Object> map, SyncDateConstant syncDateConstant);
	
	void updateSyncTime(Map<String, Object> map, SyncDateConstant syncDateConstant);
	
}
