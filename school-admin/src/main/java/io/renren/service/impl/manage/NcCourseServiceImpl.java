package io.renren.service.impl.manage;

import io.renren.dao.manage.NcCourseMapper;
import io.renren.entity.manage.NcCourse;
import io.renren.entity.manage.NcCourseExample;
import io.renren.service.manage.NcCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NcCourseServiceImpl implements NcCourseService {

	@Autowired
	private NcCourseMapper ncCourseMapper;

	@Override
	public List<NcCourse> findByNcCode(String nccode) {
		NcCourseExample example = new NcCourseExample();
		example.createCriteria().andNccodeEqualTo(nccode).andDrEqualTo(0);
		return ncCourseMapper.selectByExample(example);
	}

}
