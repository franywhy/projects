package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MsgContentTopDao;
import io.renren.entity.MsgContentTopEntity;
import io.renren.service.MsgContentTopService;
import io.renren.utils.Constant;



@Service("msgContentTopService")
public class MsgContentTopServiceImpl implements MsgContentTopService {
	@Autowired
	private MsgContentTopDao msgContentTopDao;
	
	@Override
	public MsgContentTopEntity queryObject(Map<String, Object> map){
		return msgContentTopDao.queryObject(map);
	}
	
	@Override
	public List<MsgContentTopEntity> queryList(Map<String, Object> map){
		return msgContentTopDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return msgContentTopDao.queryTotal(map);
	}
	
	@Override
	public void save(MsgContentTopEntity msgContentTop){
		msgContentTopDao.save(msgContentTop);
	}
	
	@Override
	public void update(MsgContentTopEntity msgContentTop){
		msgContentTopDao.update(msgContentTop);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		msgContentTopDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		msgContentTopDao.deleteBatch(map);
	}

	@Override
	public void saveBatch(List<Long> userIds, Long contentId, String schoolId) {
		if(null != userIds && !userIds.isEmpty()){
			for (Long userId : userIds) {
				MsgContentTopEntity msgContentTop = new MsgContentTopEntity();
				msgContentTop.setContentId(contentId);
				msgContentTop.setSchoolId(schoolId);
				msgContentTop.setUserId(userId);
				//初始化弹出类型
				msgContentTop.setType(0);
				this.save(msgContentTop);
			}
		}
	}
	
	
}
