package com.hq.learningcenter.kuaiji.service;

import java.util.List;

import com.hq.learningcenter.kuaiji.entity.NcCourse;

public interface NcCourseService {
	
	List<NcCourse> findByNcCode(String nccode);
}
