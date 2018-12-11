package io.renren.service.manage;


import io.renren.entity.manage.NcCourse;

import java.util.List;

public interface NcCourseService {
	
	List<NcCourse> findByNcCode(String nccode);
}
