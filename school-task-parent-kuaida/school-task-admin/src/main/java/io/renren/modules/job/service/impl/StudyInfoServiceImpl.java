package io.renren.modules.job.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.modules.job.dao.StudyInfoDao;
import io.renren.modules.job.entity.StudyInfoEntity;
import io.renren.modules.job.service.StudyInfoService;

@Service("studyInfoService")
public class StudyInfoServiceImpl implements StudyInfoService {
	
	@Autowired
	StudyInfoDao studyInfoDao;

	@Override
	public String queryMaxDate() {
		// TODO Auto-generated method stub
		return studyInfoDao.queryMaxDate();
	}

	@Override
	public List<StudyInfoEntity> queryVideoWatch(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return studyInfoDao.queryVideoWatch(map);
	}

	@Override
	public List<StudyInfoEntity> queryStudyInfos(Set<Long> set) {
		// TODO Auto-generated method stub
		return studyInfoDao.queryStudyInfos(set);
	}

	@Override
	public int update(List<StudyInfoEntity> list) {
		// TODO Auto-generated method stub
		return studyInfoDao.update(list);
	}

	@Override
	public int insert(List<StudyInfoEntity> list) {
		// TODO Auto-generated method stub
		return studyInfoDao.insert(list);
	}

	@Override
	public int queryVideoWatchTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return studyInfoDao.queryVideoWatchTotal(map);
	}

}
