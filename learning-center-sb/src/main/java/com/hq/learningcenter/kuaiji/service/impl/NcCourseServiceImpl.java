package com.hq.learningcenter.kuaiji.service.impl;

import java.util.List;

import com.hq.learningcenter.kuaiji.entity.NcCourse;
import com.hq.learningcenter.kuaiji.entity.NcCourseExample;
import com.hq.learningcenter.kuaiji.service.NcCourseService;
import com.hq.learningcenter.kuaiji.dao.NcCourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
