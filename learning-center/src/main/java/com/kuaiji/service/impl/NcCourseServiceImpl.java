package com.kuaiji.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuaiji.dao.NcCourseMapper;
import com.kuaiji.entity.NcCourse;
import com.kuaiji.entity.NcCourseExample;
import com.kuaiji.service.NcCourseService;

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
