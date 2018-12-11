package io.renren.service;

import io.renren.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:43:39
 */
public interface SysUserService {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);
	
	/**
	 * 根据用户ID，查询用户
	 * @param userId
	 * @return
	 */
	SysUserEntity queryObject(Long userId);
	
	/**
	 * 查询用户列表
	 */
	List<SysUserEntity> queryList(Map<String, Object> map);
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);
	
	/**
	 * 保存用户
	 */
	void save(SysUserEntity user);
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);
	
	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds);
	
	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	int updatePassword(Long userId, String password, String newPassword);
	
	/**
	 * 班主任列表
	 * @param map
	 * @return
	 */
	List<SysUserEntity> findClassTeacherList(Map<String, Object> map);
	
	/**
	 * 授课老师列表
	 * @param classTeacher=1 班主任
	 * @param teacher=1 授课老师
	 * @return
	 */
	List<SysUserEntity> findTeacherList(String nickName,Long teacher ,Long classTeacher ,
			Integer offset ,Integer limit);
	/**
	 * 授课老师列表
	 * @param classTeacher=1 班主任
	 * @param teacher=1 授课老师
	 * @return
	 */
	int findTeacherCount(String nickName,Long teacher ,Long classTeacher);

	/**
	 * 根据用户id查授课教师
	 * @param teacherMap
	 * @return
	 */
	SysUserEntity queryTeacherById(Map<String, Object> teacherMap);
	
	/**
	 * 查询授课老师总数
	 */
	int queryTotalTeacher(Map<String, Object> map);
	
	/**
	 * 同步服务-批量保存
	 * @param user
	 */
	public void saveList(List<SysUserEntity> userList);
	

	/**
	 * 查询m_id
	 * @param user
	 */
	public SysUserEntity queryMid(Map<String,Object> map);
	/**
	 * 查询mobile
	 * @param user
	 */
	public SysUserEntity queryMobile(Map<String,Object> map);
	/**
	 * 禁用
	 * @param userIds 管理员ids
	 */
	void pause(Long[] userIds);
	/**
	 * 启用
	 * @param userIds 管理员ids
	 */
	void resume(Long[] userIds);

	Map<String, Object> queryObjectByClassId(Long classId);

	/*
	 * 查询所有没有客服id的班主任
	 */
	List<SysUserEntity> queryAllAgentTeacher();

	String queryMobileByUserId(Long userId);
} 
