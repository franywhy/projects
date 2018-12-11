package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import io.renren.dao.TimetableDao;
import io.renren.entity.TimeTableDetailEntity;
import io.renren.entity.TimetableEntity;
import io.renren.pojo.timetable.TimeTableDetailPOJO;
import io.renren.pojo.timetable.TimeTablePOJO;
import io.renren.service.TimeTableDetailService;
import io.renren.service.TimetableService;
import io.renren.utils.BeanHelper;
import io.renren.utils.Constant;


@Transactional(readOnly = true)
@Service("timetableService")
public class TimetableServiceImpl implements TimetableService {
	@Autowired
	private TimetableDao timetableDao;
	@Autowired
	private TimeTableDetailService timeTableDetailService;
	@Override
	public TimetableEntity queryObject(Long number){
		return timetableDao.queryObject(number);
	}
	
	@Override
	public List<TimetableEntity> queryList(Map<String, Object> map){
		return timetableDao.queryList(map);
	}
	@Override
	public int queryTotal(Map<String, Object> map){
		return timetableDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(TimeTablePOJO timetable){
		//创建时间
		timetable.setCreateTime(new Date());
		//修改时间
		timetable.setUpdateTime(timetable.getCreateTime());
		//en
		TimetableEntity en = TimeTablePOJO.getEntity(timetable);
		//保存主表
		BeanHelper.beanAttributeValueTrim(en);
		timetableDao.save(en);
		
		//子表
		List<TimeTableDetailPOJO> detatilList = timetable.getDetailList();
		//子表保存
		if(null != detatilList && detatilList.size() >0){
			for(int i=0;i<detatilList.size();i++){
				//子表pojo
				TimeTableDetailPOJO tdp = detatilList.get(i);
				//子表entity
				TimeTableDetailEntity ttde = TimeTableDetailPOJO.getEntity(tdp);
				//pk
				ttde.setNumber(en.getNumber());
				//保存子表
				timeTableDetailService.save(ttde);
			}
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(TimeTablePOJO timetable){
		//修改时间
		timetable.setUpdateTime(new Date());
		//en
		TimetableEntity en = TimeTablePOJO.getEntity(timetable);
		//保存主表修改
		BeanHelper.beanAttributeValueTrim(en);
		timetableDao.update(en);
		//遍历子表
		List<TimeTableDetailPOJO> detatilList = timetable.getDetailList();
		
		//用于存放被保存或修改的子表id集合
		List<Long> delIds = new ArrayList<Long>();
		if(null != detatilList && detatilList.size() > 0){
			for(int i=0;i<detatilList.size();i++){
				//pojo
				TimeTableDetailPOJO ttdp = detatilList.get(i);
				//en
				TimeTableDetailEntity ttde = TimeTableDetailPOJO.getEntity(ttdp);
				//pk
				ttde.setNumber(en.getNumber());
				if(null == ttde.getId()){//保存
					timeTableDetailService.save(ttde);
				}else{//更新
					timeTableDetailService.update(ttde);
				}
				delIds.add(ttde.getId());
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ids", delIds);
			map.put("number", en.getNumber());
			timeTableDetailService.deleteBatchNotIn(map);
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void delete(Long number){
		timetableDao.delete(number);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Long[] numbers){
		timetableDao.deleteBatch(numbers);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void pause(Long[] numbers){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", numbers);
    	map.put("status", Constant.Status.PAUSE.getValue());
		timetableDao.updateBatch(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void resume(Long[] numbers){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", numbers);
    	map.put("status", Constant.Status.RESUME.getValue());
		timetableDao.updateBatch(map);
	}

	/**
	 * 上课时点列表
	 */
	@Override
	public List<TimetableEntity> findTimetableList(Map<String, Object> map) {
		return timetableDao.findTimetableList(map);
	}

	/**
	 * 查询信息
	 */
	@Override
	public TimeTablePOJO queryPojoObject(Long number) {
		return timetableDao.queryPojoObject(number);
	}

	/**
	 * 查询列表信息
	 */
	@Override
	public List<TimeTablePOJO> queryPojoList(Map<String, Object> map) {
		return timetableDao.queryPojoList(map);
	}

	@Override
	public List<TimeTablePOJO> queryPojoList1(Map<String, Object> map) {
		return this.timetableDao.queryPojoList1(map);
	}

	@Override
	public int queryTotal1(Map<String, Object> map) {
		return this.timetableDao.queryTotal1(map);
	}
}
