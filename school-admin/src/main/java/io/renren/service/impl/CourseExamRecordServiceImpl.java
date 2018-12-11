package io.renren.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.CourseExamRecordDao;
import io.renren.entity.CourseExamRecordDetailEntity;
import io.renren.entity.CourseExamRecordEntity;
import io.renren.service.CourseExamRecordDetailService;
import io.renren.service.CourseExamRecordService;
import io.renren.utils.BeanHelper;



@Service("courseExamRecordService")
public class CourseExamRecordServiceImpl implements CourseExamRecordService {
	@Autowired
	private CourseExamRecordDao courseExamRecordDao;
	@Autowired
	private CourseExamRecordDetailService courseExamRecordDetailService;
	@Override
	public CourseExamRecordEntity queryObject(Map<String, Object> map){
		return courseExamRecordDao.queryObject(map);
	}
	
	@Override
	public List<CourseExamRecordEntity> queryList(Map<String, Object> map){
		return courseExamRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseExamRecordDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public void save(CourseExamRecordEntity courseExamRecord){
		//保存主表
		BeanHelper.beanAttributeValueTrim(courseExamRecord);
		courseExamRecordDao.save(courseExamRecord);
		//获取子表数据
		List<CourseExamRecordDetailEntity> detailList = courseExamRecord.getDetailList();
		if(null != detailList && detailList.size() > 0){
			for(int i=0;i<detailList.size();i++){
				//entity
				CourseExamRecordDetailEntity cerde = detailList.get(i);
				cerde.setExamRecordId(courseExamRecord.getExamRecordId());
				if (cerde.getExamareaId() == null || "".equals(cerde.getExamareaId()) ){
				    cerde.setExamareaId(cerde.getAreaId());
                }
				cerde.setDr(0);
				cerde.setSchoolId(courseExamRecord.getSchoolId());
				courseExamRecordDetailService.save(cerde);
			}
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(CourseExamRecordEntity courseExamRecord){
		//更新主表
		BeanHelper.beanAttributeValueTrim(courseExamRecord);
		this.courseExamRecordDao.update(courseExamRecord);
		
		List<CourseExamRecordDetailEntity> detailList = courseExamRecord.getDetailList();
		//用于存放被删除的子表id集合
		List<Long> delIds = new ArrayList<Long>();
		if(null != detailList && detailList.size() > 0){
			for(int i=0;i<detailList.size();i++){
				//entity
				CourseExamRecordDetailEntity detailentity = detailList.get(i);
                if (detailentity.getExamareaId() == null || "".equals(detailentity.getExamareaId()) ){
                    detailentity.setExamareaId(detailentity.getAreaId());
                }
				if(null == detailentity.getExamRecordDetailId()){//保存
					detailentity.setExamRecordId(courseExamRecord.getExamRecordId());
					detailentity.setSchoolId(courseExamRecord.getSchoolId());
					courseExamRecordDetailService.save(detailentity);
				}else{//更新
					courseExamRecordDetailService.update(detailentity);
				}
				delIds.add(detailentity.getExamRecordDetailId());
			}
		} else if(detailList.size() == 0) {
			Map<String , Object> map = new HashMap<String , Object>();
			List<Long> examRecordIds = new ArrayList<Long>();
			examRecordIds.add(courseExamRecord.getExamRecordId());
			map.put("schoolId", courseExamRecord.getSchoolId());
			map.put("ids", examRecordIds);
			courseExamRecordDetailService.deleteDetailBatch(map);
		}
		if(null != delIds && delIds.size()>0){
			Map<String , Object> map = new HashMap<String , Object>();
			map.put("schoolId", courseExamRecord.getSchoolId());
			map.put("courseExamRecordDetailIds", delIds);
			map.put("examRecordId", courseExamRecord.getExamRecordId());
			//map.put("courseExamRecordId", entity.getExamRecordId());
			courseExamRecordDetailService.deleteBatchNotIn(map);
		}
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseExamRecordDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseExamRecordDao.deleteBatch(map);
	}

	@Override
	public void accept(CourseExamRecordEntity courseExamRecord) {
		courseExamRecordDao.accept(courseExamRecord);
	}

	@Override
	public CourseExamRecordEntity getExamRecordIdByNo(Map<String, Object> map) {
		return courseExamRecordDao.getExamRecordIdByNo(map);
	}
	
	
}
