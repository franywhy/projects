package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MsgContentCommonDao;
import io.renren.entity.MsgContentCommonEntity;
import io.renren.entity.UsersEntity;
import io.renren.service.MsgContentCommonService;
import io.renren.utils.Constant;



@Service("msgContentCommonService")
public class MsgContentCommonServiceImpl implements MsgContentCommonService {
	@Autowired
	private MsgContentCommonDao msgContentCommonDao;
	
	@Override
	public MsgContentCommonEntity queryObject(Map<String, Object> map){
		return msgContentCommonDao.queryObject(map);
	}
	
	@Override
	public List<MsgContentCommonEntity> queryList(Map<String, Object> map){
		return msgContentCommonDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return msgContentCommonDao.queryTotal(map);
	}
	
	@Override
	public void save(MsgContentCommonEntity msgContentCommon){
		msgContentCommonDao.save(msgContentCommon);
	}
	
	@Override
	public void update(MsgContentCommonEntity msgContentCommon){
		msgContentCommonDao.update(msgContentCommon);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		msgContentCommonDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		msgContentCommonDao.deleteBatch(map);
	}

	@Override
	public void saveBatch(List<Long> userIds, Long contentId, String schoolId) {
		if(null != userIds && !userIds.isEmpty()){
			for (Long userId : userIds) {
				MsgContentCommonEntity msgContentCommon = new MsgContentCommonEntity();
				msgContentCommon.setContentId(contentId);
				msgContentCommon.setSchoolId(schoolId);
				msgContentCommon.setUserId(userId);
				this.save(msgContentCommon);
			}
		}
	}

}
