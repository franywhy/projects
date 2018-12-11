package io.renren.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.MallExamScheduleDao;
import io.renren.entity.MallExamScheduleEntity;
import io.renren.pojo.examschedule.ExamSchedulePOJO;
import io.renren.service.MallExamScheduleService;
import io.renren.utils.Constant;
import io.renren.utils.OccupyException;



@Service("mallExamScheduleService")
public class MallExamScheduleServiceImpl implements MallExamScheduleService {
	@Autowired
	private MallExamScheduleDao mallExamScheduleDao;
	
	@Override
	public MallExamScheduleEntity queryObject(Long id){
		return mallExamScheduleDao.queryObject(id);
	}
	
	@Override
	public List<MallExamScheduleEntity> queryList(Map<String, Object> map){
		return mallExamScheduleDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallExamScheduleDao.queryTotal(map);
	}
	
	@Override
	public void save(MallExamScheduleEntity mallExamSchedule){
		mallExamSchedule.setDr(0);
		mallExamSchedule.setCreateTime(new Date());
		mallExamSchedule.setModifyTime(mallExamSchedule.getCreateTime());
		mallExamScheduleDao.save(mallExamSchedule);
	}
	
	@Override
	public void update(MallExamScheduleEntity mallExamSchedule){
		mallExamSchedule.setModifyTime(new Date());
		mallExamScheduleDao.update(mallExamSchedule);
	}
	
	@Override
	public void delete(Long id){
		mallExamScheduleDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids)throws OccupyException{
		//TODO 课时档案删除时被占用校验
		if(isUsed(ids)){
			throw new OccupyException("xxx");
		}
		mallExamScheduleDao.deleteBatch(ids);
	}
	@Override
	public void pause(Long[] ids){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.PAUSE.getValue());
		mallExamScheduleDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] ids){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.RESUME.getValue());
		mallExamScheduleDao.updateBatch(map);
	}

	@Override
	public List<ExamSchedulePOJO> queryPojoList(Map<String, Object> map) {
		return this.mallExamScheduleDao.queryPojoList(map);
	}

	@Override
	public ExamSchedulePOJO queryPojoObject(Map<String, Object> map) {
		return this.mallExamScheduleDao.queryPojoObject(map);
	}

	@Override
	public boolean isUsed(Long id) {
		return false;
	}

	@Override
	public boolean isUsed(Long[] ids) {
		return false;
	}

	@Override
	public List<ExamSchedulePOJO> queryPojoList1(Map<String, Object> map) {
		return this.mallExamScheduleDao.queryPojoList1(map);
	}
	
	@Override
	public MallExamScheduleEntity queryPojoByParam(Map<String, Object> map) {
		return this.mallExamScheduleDao.queryPojoByParam(map);
	}
}
