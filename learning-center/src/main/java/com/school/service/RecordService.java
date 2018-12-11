package com.school.service;

import com.school.pojo.CourseRecordDetailPOJO;

public interface RecordService {

	/**
	 * 根据录播课id获取录播课信息
	 * @param recordId
	 * @return
	 */
	CourseRecordDetailPOJO getRecordInfo(Long recordId);

}
