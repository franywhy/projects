package io.renren.service;

import io.renren.entity.UsersEntity;
import io.renren.entity.manage.Users;

import java.util.List;
import java.util.Map;

/**
 * 前台用户表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-27 10:26:24
 */
public interface UsersService {
	
	UsersEntity queryObject(Long userId);
	
	List<UsersEntity> queryList(Map<String, Object> map);
	//查询用户信息显示到浮沉中
	List<UsersEntity> queryLayList(Map<String,Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	Integer queryUserId(Map<String,Object> map);
	
	void save(UsersEntity users);
	
	void update(UsersEntity users);
	
	void delete(Long userId);
	
	void deleteBatch(Long[] userIds);
	
	void pause(Long[] userIds);
	
	void resume(Long[] userIds);
	
	/**
	 * 手机号码是否存在
	 * @param users
	 * @return
	 */
	boolean checkMobile(Map<String, Object> map);
	
	boolean mobileExist(Map<String, Object> map);
	
	long getUserIdByMobilePhoneNo(Map<String, Object> map);

	String getNickNameByMobilePhoneNo(Map<String, Object> map);

	int countUserIdByMobilePhoneNo(Map<String, Object> map);
	
	void restPsw(String psw , Long userId);

	/**
	 * 根据学员的手机号更新姓名
	 * @param phone
	 * @param user_name
	 */
	void updateNameByPhone(String phone, String user_name);

	int checkUser(String mobile);

	Users findByMobile(String mobile);
}
