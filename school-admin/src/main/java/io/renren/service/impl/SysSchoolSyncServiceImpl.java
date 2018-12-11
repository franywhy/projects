package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.SysSchoolSyncDao;
import io.renren.entity.SysSchoolSyncEntity;
import io.renren.service.SysSchoolSyncService;
import io.renren.utils.Constant;



@Service("sysSchoolSyncService")
public class SysSchoolSyncServiceImpl implements SysSchoolSyncService {
	@Autowired
	private SysSchoolSyncDao sysSchoolSyncDao;
	
	@Override
	public void save(SysSchoolSyncEntity sysSchoolSync){
		sysSchoolSyncDao.save(sysSchoolSync);
	}
	
}
