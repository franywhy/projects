package io.renren.service.impl;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.dao.NcCourseClassplanDao;
import io.renren.entity.NcCourseClassplanEntity;
import io.renren.service.NcCourseClassplanService;


@Service("ncCourseClassplanService")
public class NcCourseClassplanServiceImpl implements NcCourseClassplanService {
	@Autowired
	private NcCourseClassplanDao ncCourseClassplanDao;
	
	@Override
	public NcCourseClassplanEntity queryObject(Map<String, Object> map){
		return ncCourseClassplanDao.queryObject(map);
	}
	
	@Override
	public List<NcCourseClassplanEntity> queryList(Map<String, Object> map){
		return ncCourseClassplanDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return ncCourseClassplanDao.queryTotal(map);
	}
	
	@Override
	public void save(NcCourseClassplanEntity ncCourseClassplan){
		ncCourseClassplanDao.save(ncCourseClassplan);
	}
	
	@Override
	public void update(NcCourseClassplanEntity ncCourseClassplan){
		ncCourseClassplanDao.update(ncCourseClassplan);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		ncCourseClassplanDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		ncCourseClassplanDao.deleteBatch(map);
	}

    @Override
    public List<NcCourseClassplanEntity> queryDataByTs(Map<String, Object> map) {
        return ncCourseClassplanDao.queryDataByTs(map);
    }

    @Override
    public Integer queryTotalNotSuccess() {
        return ncCourseClassplanDao.queryTotalNotSuccess();
    }

    @Override
    public void updateIsSuccess(Long id) {
        Map<String,Object> map = new HashedMap();
        map.put("id",id);
        map.put("isSuccess",1);
        ncCourseClassplanDao.updateIsSuccess(map);
    }

    @Override
    public void updateIsSuccessByTime(String mobile, String classplanId, String ncModifiedTime) {
        Map<String,Object> map = new HashedMap();
        map.put("mobile",mobile);
        map.put("classplanId",classplanId);
        map.put("ncModifiedTime",ncModifiedTime);
        map.put("isSuccess",1);
        ncCourseClassplanDao.updateIsSuccessByTime(map);
    }


}
