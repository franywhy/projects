package io.renren.service.impl;

import io.renren.dao.LogLabelBonusRelationRecordDao;
import io.renren.entity.LogLabelBonusRelationRecordEntity;
import io.renren.pojo.LogLabelBonusRelationRecordPOJO;
import io.renren.service.LogLabelBonusRelationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;




@Service("logLabelBonusRelationRecordService")
public class LogLabelBonusRelationRecordServiceImpl implements LogLabelBonusRelationRecordService {

	@Autowired
	private LogLabelBonusRelationRecordDao logLabelBonusRelationRecordDao;
	
	@Override
	public LogLabelBonusRelationRecordEntity queryObject(Long id){
		return logLabelBonusRelationRecordDao.queryObject(id);
	}
	
	@Override
	public List<LogLabelBonusRelationRecordEntity> queryList(Map<String, Object> map){
		return logLabelBonusRelationRecordDao.queryList(map);
	}

    @Override
    public List<LogLabelBonusRelationRecordPOJO> queryPOJOList(Map<String, Object> map) {
        return logLabelBonusRelationRecordDao.queryPOJOList(map);
    }

    @Override
    public int queryPOJOListTotal(Map<String, Object> map) {
        return logLabelBonusRelationRecordDao.queryPOJOListTotal(map);
    }

    @Override
	public int queryTotal(Map<String, Object> map){
		return logLabelBonusRelationRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(LogLabelBonusRelationRecordEntity logLabelBonusRelationRecord){
		logLabelBonusRelationRecordDao.save(logLabelBonusRelationRecord);
	}
	
	@Override
	public void update(LogLabelBonusRelationRecordEntity logLabelBonusRelationRecord){
		logLabelBonusRelationRecordDao.update(logLabelBonusRelationRecord);
	}
	
	@Override
	public void delete(Long id){
		logLabelBonusRelationRecordDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		logLabelBonusRelationRecordDao.deleteBatch(ids);
	}
	
}
