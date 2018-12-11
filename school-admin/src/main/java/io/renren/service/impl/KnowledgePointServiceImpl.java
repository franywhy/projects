package io.renren.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.KnowledgePointDao;
import io.renren.dao.KnowledgePointMaterialDao;
import io.renren.entity.KnowledgePointEntity;
import io.renren.entity.KnowledgePointMaterialEntity;
import io.renren.mongo.dao.ICourseDao;
import io.renren.mongo.entity.MongoPagination;
import io.renren.service.KnowledgePointService;



//@Service("knowledgePointService")
public class KnowledgePointServiceImpl implements KnowledgePointService {
	@Autowired
	private KnowledgePointDao knowledgePointDao;
	
	@Autowired
	private KnowledgePointMaterialDao knowledgePointMaterialDao;
	
	@Resource
	private ICourseDao courseDao;
	
	@Override
	public KnowledgePointEntity queryObject(Map<String, Object> map){
		return knowledgePointDao.queryObject(map);
	}
	
	@Override
	public List<KnowledgePointEntity> queryList(Map<String, Object> map){
		return knowledgePointDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return knowledgePointDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(KnowledgePointEntity knowledgePoint){
		List ids = knowledgePoint.getIds();
		knowledgePointDao.save(knowledgePoint);
		KnowledgePointMaterialEntity kpm = null;
		for (int i = 0; i < ids.size(); i++) {
			kpm = new KnowledgePointMaterialEntity();
			kpm.setKnowledgePointId(knowledgePoint.getId());
			kpm.setMaterialId(Integer.valueOf(ids.get(i).toString()));
			knowledgePointMaterialDao.save(kpm);
		}
		
	}
	
	@Override
	@Transactional
	public void update(KnowledgePointEntity knowledgePoint){
		Map map = new HashMap();
		map.put("point_id", knowledgePoint.getId());
		List list = this.knowledgePointMaterialDao.findByPointId(map);
		List ids = knowledgePoint.getIds();
		Map m =null;
		Integer id = null;
		Integer idx = null;
		boolean isExisted ;
		if (null!=list && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				m = (Map) list.get(i);
				id = null==m || null==m.get("id")?0:Integer.valueOf(m.get("id").toString());
				isExisted = false;
				
				//
				for (int j = 0; j < ids.size(); j++) {
					idx = Integer.valueOf(ids.get(j).toString());
					if (id == idx) {
						isExisted = true;
						ids.remove(j);
					}
				}
				
				//不存在就删除
				if (!isExisted) {
					this.knowledgePointMaterialDao.deleteByMaterialId(id);
				}
				
				
			}
			
			//
			KnowledgePointMaterialEntity kpm = null;
			for (int i = 0; i < ids.size(); i++) {
				kpm = new KnowledgePointMaterialEntity();
				kpm.setKnowledgePointId(knowledgePoint.getId());
				kpm.setMaterialId(Integer.valueOf(ids.get(i).toString()));
				knowledgePointMaterialDao.save(kpm);
			}
		}
		
		knowledgePointDao.update(knowledgePoint);
	}
	
	@Override
	@Transactional
	public void delete(Map<String, Object> map){
		knowledgePointDao.delete(map);
		this.knowledgePointMaterialDao.deleteBatch(map);
		
	}
	
	@Override
	@Transactional
	public void deleteBatch(Map<String, Object> map){
		knowledgePointDao.deleteBatch(map);
		this.knowledgePointMaterialDao.deleteBatch(map);
	}

	@Override
	public MongoPagination findAllCourse(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return this.courseDao.findAllCourse(pageNo, pageSize);
	}

	@Override
	public MongoPagination findCourseByName(int pageNo, int pageSize,String name) {
		// TODO Auto-generated method stub
		return this.courseDao.findCourseByName( pageNo,pageSize,name);
	}

	@Override
	public List queryListForDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.knowledgePointDao.queryListForDetail(map);
	}

	@Override
	public int queryTotalForDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.knowledgePointDao.queryTotalForDetail(map);
	}

	@Override
	public List queryList1(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.knowledgePointDao.queryList1(map);
	}
	
	
}
