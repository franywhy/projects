package io.renren.service.impl;

import io.renren.dao.UserOldclassLogDao;
import io.renren.entity.UserOldclassLogEntity;
import io.renren.service.UserOldclassLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;




@Service("userOldclassLogService")
public class UserOldclassLogServiceImpl implements UserOldclassLogService {
	@Autowired
	private UserOldclassLogDao userOldclassLogDao;
	
	@Override
	public UserOldclassLogEntity queryObject(Long id){
		return userOldclassLogDao.queryObject(id);
	}
	
	@Override
	public List<UserOldclassLogEntity> queryList(Map<String, Object> map){
		return userOldclassLogDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return userOldclassLogDao.queryTotal(map);
	}
	
	@Override
	public void save(UserOldclassLogEntity userOldclassLog){
		userOldclassLogDao.save(userOldclassLog);
	}
	
	@Override
	public void update(UserOldclassLogEntity userOldclassLog){
		userOldclassLogDao.update(userOldclassLog);
	}
	
	@Override
	public void delete(Long id){
		userOldclassLogDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		userOldclassLogDao.deleteBatch(ids);
	}

    @Override
    public List<Map<String, Object>> queryMessage(String millisecond) {
        return userOldclassLogDao.queryMessage(millisecond);
    }

}
