package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.SysCheckQuoteDao;
import io.renren.modules.job.service.SysCheckQuoteService;
import io.renren.modules.job.utils.SyncDateConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service("sysCheckQuoteService")
public class SysCheckQuoteServiceImpl implements SysCheckQuoteService {
	
	private static final String TABLENAME = "tableName";
	private static final String COLUMNNAME = "columnName";
	private static final String DBS = "dbs";
	@Autowired
	private SysCheckQuoteDao sysCheckQuoteDao;

	@Override
	public String syncDate(Map<String, Object> map, SyncDateConstant syncDateConstant) {
		map.put("value", syncDateConstant.getTableName());
		return sysCheckQuoteDao.syncDate(map);
	}
	@Override
	public void updateSyncTime(Map<String, Object> map, SyncDateConstant syncDateConstant) {
		map.put("value", syncDateConstant.getTableName());
		sysCheckQuoteDao.updateSyncTime(map);
	}

}
