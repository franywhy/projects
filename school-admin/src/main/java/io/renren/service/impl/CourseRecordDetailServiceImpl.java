package io.renren.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.CourseRecordDetailDao;
import io.renren.entity.CourseRecordDetailEntity;
import io.renren.pojo.CourseRecordDetailPOJO;
import io.renren.service.CourseRecordDetailService;
import io.renren.utils.DateUtils;
import io.renren.utils.RRException;



@Service("courseRecordDetailService")
public class CourseRecordDetailServiceImpl implements CourseRecordDetailService {
	@Autowired
	private CourseRecordDetailDao courseRecordDetailDao;
	
	@Override
	public CourseRecordDetailEntity queryObject(Map<String, Object> map){
		return courseRecordDetailDao.queryObject(map);
	}
	
	@Override
	public List<CourseRecordDetailEntity> queryList(Map<String, Object> map){
		return courseRecordDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseRecordDetailDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseRecordDetailEntity courseRecordDetail){
		if(StringUtils.isNotBlank(courseRecordDetail.getDuration())){
			//时长-毫秒
			courseRecordDetail.setDurationS(DateUtils.videDuration(courseRecordDetail.getDuration()));
		}
		courseRecordDetailDao.save(courseRecordDetail);
	}
	
	@Override
	public void update(CourseRecordDetailEntity courseRecordDetail){
		if(StringUtils.isNotBlank(courseRecordDetail.getDuration())){
			//时长-毫秒
			courseRecordDetail.setDurationS(DateUtils.videDuration(courseRecordDetail.getDuration()));
		}
		courseRecordDetailDao.update(courseRecordDetail);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		int count = this.courseRecordDetailDao.queryJCount(map);
//		if(true){
		if(count > 0){
			throw new RRException("删除失败，章下面存在节！");
		}
		courseRecordDetailDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseRecordDetailDao.deleteBatch(map);
	}

	@Override
	public CourseRecordDetailPOJO queryPOJO(Long recordId) {
		return this.courseRecordDetailDao.queryPOJO(recordId);
	}

	@Override
	public List<CourseRecordDetailPOJO> queryPOJOList(Long courseId) {
		return this.courseRecordDetailDao.queryPOJOList(courseId);
	}

}
