package com.hq.learningcenter.school.dao;


/**
 * 订单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
public interface SysUserDao{
	
	String queryWxCode(Long userId);
	
	String queryOwnerId(Long userId);
	
	Long querUserIdByMobile(Long mobileNo);
}
