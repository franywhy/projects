package io.renren.dao;

import io.renren.entity.UsersEntity;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 前台用户表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-27 10:26:24
 */
public interface UsersDao extends BaseDao<UsersEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	/**
	 * 手机号码是否存在
	 * @param users
	 * @return
	 */
	int checkMobile(Map<String, Object> map);
	
	int mobileExist(Map<String, Object> map);
	
	long getUserIdByMobilePhoneNo(Map<String, Object> map);

	String getNickNameByMobilePhoneNo(Map<String, Object> map);

	int countUserIdByMobilePhoneNo(Map<String, Object> map);

	/**
	 * 根据学员的手机号更新学员姓名
	 * @param phone
	 * @param user_name
	 */
	void updateNameByPhone(@Param("phone")String phone, @Param("user_name")String user_name);
	/**
	 * 通过手机号码判断学员是否存在
	 * @param map
	 * @return
	 */
	int checkUser(String mobile);
	
}
