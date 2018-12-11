package io.renren.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.SysCheckQuoteDao;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.QuoteConstant;
import io.renren.utils.SyncDateConstant;
@Service("sysCheckQuoteService")
public class SysCheckQuoteServiceImpl implements SysCheckQuoteService {
	
	private static final String TABLENAME = "tableName";
	private static final String COLUMNNAME = "columnName";
	private static final String DBS = "dbs";
	@Autowired
	private SysCheckQuoteDao sysCheckQuoteDao;
	@Override
	public Boolean checkQuote(Map<String, Object> map, QuoteConstant quoteConstant) {
		map.put(TABLENAME, quoteConstant.getTableName());
		map.put(COLUMNNAME, quoteConstant.getColumnName());
		map.put(DBS, quoteConstant.isDbs());
		return sysCheckQuoteDao.checkQuote(map)>0;
	}
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
