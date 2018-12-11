package io.renren.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.dao.manage.UsersMapper;
import io.renren.entity.manage.Users;
import io.renren.entity.manage.UsersExample;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.UsersDao;
import io.renren.entity.UsersEntity;
import io.renren.service.UsersService;
import io.renren.utils.Constant;



@Service("usersService")
public class UsersServiceImpl implements UsersService {
	
	
	@Autowired
	private UsersDao usersDao;

	@Autowired
	private UsersMapper usersMapper;

	@Override
	public UsersEntity queryObject(Long userId){
		return usersDao.queryObject(userId);
	}
	
	@Override
	public List<UsersEntity> queryList(Map<String, Object> map){
		return usersDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return usersDao.queryTotal(map);
	}
	
	@Override
	public void save(UsersEntity users){
		users.setDr(0);
		users.setCreationTime(new Date());
		users.setModifiedTime(users.getCreationTime());
		usersDao.save(users);
	}
	
	@Override
	public void update(UsersEntity users){
		if(StringUtils.isBlank(users.getNickName())){
			users.setPassword(null);
		}
		users.setModifiedTime(new Date());
		usersDao.update(users);
	}
	
	@Override
	public void delete(Long userId){
		usersDao.delete(userId);
	}
	
	@Override
	public void deleteBatch(Long[] userIds){
		usersDao.deleteBatch(userIds);
	}
	@Override
	public void pause(Long[] userIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", userIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
		usersDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] userIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", userIds);
    	map.put("status", Constant.Status.RESUME.getValue());
		usersDao.updateBatch(map);
	}
	@Override
	public boolean checkMobile(Map<String, Object> map) {
		return usersDao.checkMobile(map)>0;
	}
	@Override
	public boolean mobileExist(Map<String, Object> map) {
		return this.usersDao.mobileExist(map) > 0;
	}
	@Override
	public long getUserIdByMobilePhoneNo(Map<String, Object> map) {
		return this.usersDao.getUserIdByMobilePhoneNo(map);
	}

	@Override
	public String getNickNameByMobilePhoneNo(Map<String, Object> map) {
		return this.usersDao.getNickNameByMobilePhoneNo(map);
	}

	@Override
	public int countUserIdByMobilePhoneNo(Map<String, Object> map) {
		return this.usersDao.countUserIdByMobilePhoneNo(map);
	}


	@Override
	public List<UsersEntity> queryLayList(Map<String, Object> map) {
		return usersDao.queryLayList(map);
	}


	@Override
	public Integer queryUserId(Map<String, Object> map) {
		return usersDao.queryUserId(map);
	}

	@Override
	public void restPsw(String psw , Long userId) {
		UsersEntity usersEntity = this.queryObject(userId);
		if(null != usersEntity){
			usersEntity.setPassword(psw);
			this.update(usersEntity);
		}
	}

	@Override
	public void updateNameByPhone(String phone, String user_name) {
		this.usersDao.updateNameByPhone(phone, user_name);
	}

	@Override
	public int checkUser(String mobile) {		
		return this.usersDao.checkUser(mobile);
	}

	@Override
	public Users findByMobile(String mobile) {
		UsersExample example = new UsersExample();
		example.createCriteria().andMobileEqualTo(mobile).andDrEqualTo((byte)0);
		return usersMapper.selectByExampleFetchOne(example);
	}
}
