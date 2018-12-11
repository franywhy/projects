package io.renren.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.entity.SysUserEntity;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:34:11
 */
public interface SysUserDao extends BaseDao<SysUserEntity> {
	
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
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);
	
	/**
	 * 班主任列表
	 * @param map
	 * @return
	 */
	List<SysUserEntity> findClassTeacherList(Map<String, Object> map);

	/**
	 * 老师列表
	 * @param classTeacher=1 班主任
	 * @param teacher=1 授课老师
	 * @return
	 */
	List<SysUserEntity> findTeacherList(@Param("nickName")String nickName,
			@Param("teacher")Long teacher ,@Param("classTeacher")Long classTeacher ,
			@Param("offset")Integer offset ,@Param("limit")Integer limit
			);
	/**
	 * 老师列表
	 * @param map
	 * @param classTeacher=1 班主任
	 * @param teacher=1 授课老师
	 * @return
	 */
	int findTeacherCount(@Param("nickName")String nickName,
			@Param("teacher")Long teacher ,@Param("classTeacher")Long classTeacher);

	/**
	 * 根据用户id查授课老师
	 * @param teacherMap
	 * @return
	 */
	SysUserEntity queryTeacherById(Map<String, Object> teacherMap);
	
	/**
	 * 查询授课老师总数
	 * @param map
	 * @return
	 */
	int queryTotalTeacher(Map<String, Object> map);
	
	int syncQueryTotal(Map<String, Object> map);
	
	/**
	 * 查询m_id
	 * @param user
	 */
	SysUserEntity queryMid(Map<String,Object> map);
	/**
	 * 查询m_id
	 * @param user
	 */
	SysUserEntity queryMobile(Map<String,Object> map);
	
	/**
	 * 批量更新状态
	 * @param map
	 * @return
	 */
	int updateBatch(Map<String, Object> map);

	Map<String, Object> queryObjectByClassId(@Param("classId")Long classId);

	/*
	 * 查询所有没有客服id的班主任
	 */
	List<SysUserEntity> queryAllAgentTeacher();
	
	/*
	 * 跟具名字模糊匹配所有userID
	 */
	List<Long> queryUserIdByName(String name);

	String queryMobileByUserId(@Param("userId")Long userId);
}
