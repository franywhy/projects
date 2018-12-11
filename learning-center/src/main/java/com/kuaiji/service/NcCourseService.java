package com.kuaiji.service;

import java.util.List;

import com.kuaiji.entity.NcCourse;

public interface NcCourseService {
	
	List<NcCourse> findByNcCode(String nccode);
}
