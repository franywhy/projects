package com.hq.learningcenter.school.service.impl;

import com.hq.learningcenter.school.service.RecordService;
import com.hq.learningcenter.school.dao.CourseRecordDetailDao;
import com.hq.learningcenter.school.pojo.CourseRecordDetailPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("recordService")
public class RecordServiceImpl implements RecordService {
	@Autowired
	private CourseRecordDetailDao courseRecordDetailDao;
	
	@Override
	public CourseRecordDetailPOJO getRecordInfo(Long recordId) {
		return this.courseRecordDetailDao.getRecordInfo(recordId);
	}

}
