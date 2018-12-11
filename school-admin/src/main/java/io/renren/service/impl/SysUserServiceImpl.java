package io.renren.service.impl;

import io.renren.dao.SysUserDao;
import io.renren.entity.SysUserEntity;
import io.renren.service.SysUserRoleService;
import io.renren.service.SysUserService;
import io.renren.utils.Constant;
import io.renren.utils.SendUdeskUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Override
	public List<String> queryAllPerms(Long userId) {
		return sysUserDao.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return sysUserDao.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return sysUserDao.queryByUserName(username);
	}
	
	@Override
	public SysUserEntity queryObject(Long userId) {
		return sysUserDao.queryObject(userId);
	}

	@Override
	public List<SysUserEntity> queryList(Map<String, Object> map){
		return sysUserDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysUserDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		user.setTs(new Date());
		//sha256加密
		user.setPassword(new Sha256Hash(user.getPassword()).toHex());
		
		//如果是班主任
		if(1 == user.getClassTeacher()){
			//发送Udesk创建客服
			if(StringUtils.isNotBlank(user.getMobile())){
				Integer ownerId = SendUdeskUtil.creatUdeskAgent(user.getMobile(), user.getNickName());
				user.setOwnerId(ownerId);
			}else{
				Integer ownerId = SendUdeskUtil.creatUdeskAgent(user.getUsername(), user.getNickName());
				user.setOwnerId(ownerId);
			}
		}
		
		sysUserDao.save(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}
	
	/**
	 * 同步服务-批量保存
	 * @param user
	 */
	@Override
	@Transactional
	public void saveList(List<SysUserEntity> userList){
		if(null != userList && 0 < userList.size()){
			for (SysUserEntity sysUserEntity : userList) {
				Map<String , Object> map = new HashMap<>();
				map.put("username", sysUserEntity.getUsername());
				map.put("mId", sysUserEntity.getmId());
				if(0 == sysUserDao.syncQueryTotal(map)){
					save(sysUserEntity);
				}
			}
		}
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		user.setTs(new Date());
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword()).toHex());
		}
		sysUserDao.update(user);
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] userId) {
		sysUserDao.deleteBatch(userId);
	}

	@Override
	public int updatePassword(Long userId, String password, String newPassword) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		map.put("newPassword", newPassword);
		return sysUserDao.updatePassword(map);
	}
	
	@Override
	public List<SysUserEntity> findClassTeacherList(Map<String, Object> map) {
		return sysUserDao.findClassTeacherList(map);
	}

	@Override
	public List<SysUserEntity> findTeacherList(String nickName,Long teacher ,Long classTeacher ,
			Integer offset ,Integer limit) {
		if(null == teacher){
			teacher = 0l;
		}
		if(null == classTeacher){
			classTeacher = 0l;
		}
		return sysUserDao.findTeacherList(nickName,teacher, classTeacher, offset, limit);
	}
	/**
	 * 授课老师列表
	 * @param classTeacher=1 班主任
	 * @param teacher=1 授课老师
	 * @return
	 */
	@Override
	public int findTeacherCount(String nickName,Long teacher ,Long classTeacher){
		if(null == teacher){
			teacher = 0l;
		}
		if(null == classTeacher){
			classTeacher = 0l;
		}
		return this.sysUserDao.findTeacherCount(nickName,teacher, classTeacher);
	}

	@Override
	public SysUserEntity queryTeacherById(Map<String, Object> teacherMap) {
		return sysUserDao.queryTeacherById(teacherMap);
	}

	@Override
	public int queryTotalTeacher(Map<String, Object> map) {
		return this.sysUserDao.queryTotalTeacher(map);
	}

	@Override
	public SysUserEntity queryMid(Map<String, Object> map) {
		return this.sysUserDao.queryMid(map);
	}

	@Override
	public void pause(Long[] userIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", userIds);
		map.put("status", Constant.Status.PAUSE.getValue());
		this.sysUserDao.updateBatch(map);
	}

	@Override
	public void resume(Long[] userIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", userIds);
		map.put("status", Constant.Status.RESUME.getValue());
		this.sysUserDao.updateBatch(map);
	}

	@Override
	public SysUserEntity queryMobile(Map<String, Object> map) {
		return this.sysUserDao.queryMobile(map);
	}

	@Override
	public Map<String, Object> queryObjectByClassId(Long classId) {
		return this.sysUserDao.queryObjectByClassId(classId);
	}

	@Override
	public List<SysUserEntity> queryAllAgentTeacher() {
		return this.sysUserDao.queryAllAgentTeacher();
	}

	@Override
	public String queryMobileByUserId(Long userId) {
		return sysUserDao.queryMobileByUserId(userId);
	}
}
