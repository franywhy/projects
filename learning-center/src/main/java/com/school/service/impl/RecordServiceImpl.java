package com.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.dao.CourseRecordDetailDao;
import com.school.pojo.CourseRecordDetailPOJO;
import com.school.service.RecordService;

@Service("recordService")
public class RecordServiceImpl implements RecordService {
	@Autowired
	private CourseRecordDetailDao courseRecordDetailDao;
	
	@Override
	public CourseRecordDetailPOJO getRecordInfo(Long recordId) {
		return this.courseRecordDetailDao.getRecordInfo(recordId);
	}

}
