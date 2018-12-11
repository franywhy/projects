package io.renren.modules.job.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.renren.modules.job.entity.StudyInfoEntity;
 
public interface StudyInfoDao {
	
	String queryMaxDate();
	
	List<StudyInfoEntity> queryVideoWatch(Map<String,Object> map);
	
	int queryVideoWatchTotal(Map<String,Object> map);
	
	List<StudyInfoEntity> queryStudyInfos(Set<Long> set);
	
	int update(List<StudyInfoEntity> list);
	
	int insert(List<StudyInfoEntity> list);
}
