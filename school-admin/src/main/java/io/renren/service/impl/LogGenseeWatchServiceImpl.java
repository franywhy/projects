package io.renren.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.utils.BeanHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.LogGenseeWatchDao;
import io.renren.entity.LogGenseeWatchEntity;
import io.renren.entity.StudyInfoEntity;
import io.renren.pojo.log.LogAttendWatchTimePOJO;
import io.renren.service.LogGenseeWatchService;



@Service("logGenseeWatchService")
public class LogGenseeWatchServiceImpl implements LogGenseeWatchService {
	@Autowired
	private LogGenseeWatchDao logGenseeWatchDao;
	
	@Override
	public LogGenseeWatchEntity queryObject(Map<String, Object> map){
		return logGenseeWatchDao.queryObject(map);
	}
	
	@Override
	public List<LogGenseeWatchEntity> queryList(Map<String, Object> map){
		return logGenseeWatchDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return logGenseeWatchDao.queryTotal(map);
	}
	
	@Override
	public void save(LogGenseeWatchEntity logGenseeWatch){
		logGenseeWatchDao.save(logGenseeWatch);
	}
	
	@Override
	public void update(LogGenseeWatchEntity logGenseeWatch){
		logGenseeWatchDao.update(logGenseeWatch);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		logGenseeWatchDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		logGenseeWatchDao.deleteBatch(map);
	}

	/**
	 * 新增或保存
	 * @param logGenseeWatch
	 */
	@Override
	public void saveOrUpdate(LogGenseeWatchEntity logGenseeWatch) {
		if(logGenseeWatch != null){
			//用户ID
			Long userId = logGenseeWatch.getUserId();
			//业务参数-排课子表-直播课课次ID
			String businessId = logGenseeWatch.getBusinessId();
			//业务参数非空
			if(null != logGenseeWatch && StringUtils.isNotBlank(businessId) && userId > 0){
				//查询条件
				Map<String,Object> queryMap = new HashMap<>();
				queryMap.put("businessId", businessId);
				queryMap.put("userId", userId);
				//查询是否有原来的记录
				LogGenseeWatchEntity oldLogEntity = queryObject(queryMap);
				
				if(null != oldLogEntity){//原来存在记录-修改
					//直播总时长
					oldLogEntity.setLiveDur(oldLogEntity.getLiveDur() + logGenseeWatch.getLiveDur());
					//录播总时长
					oldLogEntity.setVideoDur(oldLogEntity.getVideoDur()+logGenseeWatch.getVideoDur());
					//观看总时长
					oldLogEntity.setWatchDur(oldLogEntity.getWatchDur() + logGenseeWatch.getWatchDur());
					//出勤率
					oldLogEntity.setAttendPer(durPer(oldLogEntity.getFullDur(), oldLogEntity.getWatchDur()));
					//更新
					this.update(oldLogEntity);
				}else{//不存在记录-新增
					//出勤率
					logGenseeWatch.setAttendPer(durPer(logGenseeWatch.getFullDur(), logGenseeWatch.getWatchDur()));
					//创建时间
					logGenseeWatch.setCreateTime(new Date(System.currentTimeMillis()));
					//新增
					this.save(logGenseeWatch);
				}
			}
		}
	}
	
	/**
	 * 新增或保存(数据来源gensee日志记录)
	 * @param logGenseeWatch
	 */
	@Override
	public void saveOrUpdateFromLog(LogGenseeWatchEntity logGenseeWatch) {
		if(logGenseeWatch != null){
			//用户ID
			Long userId = logGenseeWatch.getUserId();
			//业务参数-排课子表-直播课课次ID
			String businessId = logGenseeWatch.getBusinessId();
			//业务参数非空
			if(null != logGenseeWatch && StringUtils.isNotBlank(businessId) && userId > 0){
				//查询条件
				Map<String,Object> queryMap = new HashMap<>();
				queryMap.put("businessId", businessId);
				queryMap.put("userId", userId);
				//查询是否有原来的记录
				LogGenseeWatchEntity oldLogEntity = queryObject(queryMap);
				
				if(null != oldLogEntity){//原来存在记录-修改(直播时长直接覆盖,不做累加)
					//直播总时长
					oldLogEntity.setLiveDur(logGenseeWatch.getLiveDur());
					//录播总时长
					oldLogEntity.setVideoDur(oldLogEntity.getVideoDur() + logGenseeWatch.getVideoDur());
					//观看总时长
					oldLogEntity.setWatchDur(oldLogEntity.getVideoDur() + logGenseeWatch.getLiveDur());//观看时长为原回放时长+新增回放时长+新增直播时长
					//出勤率
					oldLogEntity.setAttendPer(durPer(oldLogEntity.getFullDur(), oldLogEntity.getWatchDur()));
					//更新
					this.update(oldLogEntity);
				}else{//不存在记录-新增
					//出勤率
					logGenseeWatch.setAttendPer(durPer(logGenseeWatch.getFullDur(), logGenseeWatch.getWatchDur()));
					//创建时间
					logGenseeWatch.setCreateTime(new Date(System.currentTimeMillis()));
					//新增
					this.save(logGenseeWatch);
				}
			}
		}
	}
	
	/**
	 * 计算出勤率
	 * 观看总时长watchDur/应观看总时长fullDur
	 * @return
	 */
	private static final BigDecimal durPer(Long fullDur , Long watchDur){
		BigDecimal per = null;
		if(null == fullDur || null == watchDur || 0 == fullDur){
			per = new BigDecimal(0);
		}else if(fullDur <= watchDur){
			per = new BigDecimal(1);
		}else{
			//保留四位小数
			per = new BigDecimal(watchDur).divide(new BigDecimal(fullDur) , 4 , RoundingMode.HALF_UP);
		}
		return per;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void saveOrUpdateList(List<LogGenseeWatchEntity> logGenseeWatchList) {
		for(LogGenseeWatchEntity entity : logGenseeWatchList){
			saveOrUpdate(entity);
		}
	}

	@Override
	public void saveOrUpdateBatch(List<LogGenseeWatchEntity> logGenseeWatchList) {
		for(LogGenseeWatchEntity logGenseeWatch : logGenseeWatchList){
			if(logGenseeWatch != null){
				//用户ID
				Long userId = logGenseeWatch.getUserId();
				//业务参数-排课子表-直播课课次ID
				String businessId = logGenseeWatch.getBusinessId();
				//业务参数非空
				if(null != logGenseeWatch && StringUtils.isNotBlank(businessId) && userId > 0){
					//查询条件
					Map<String,Object> queryMap = new HashMap<>();
					queryMap.put("businessId", businessId);
					queryMap.put("userId", userId);
					//查询是否有原来的记录
					LogGenseeWatchEntity oldLogEntity = queryObject(queryMap);
					
					if(null != oldLogEntity){//原来存在记录-修改
						//直播总时长
						oldLogEntity.setLiveDur(oldLogEntity.getLiveDur() + logGenseeWatch.getLiveDur());
						//录播总时长
						oldLogEntity.setVideoDur(oldLogEntity.getVideoDur()+logGenseeWatch.getVideoDur());
						//观看总时长
						oldLogEntity.setWatchDur(oldLogEntity.getWatchDur() + logGenseeWatch.getWatchDur());
						//出勤率
						oldLogEntity.setAttendPer(logGenseeWatch.getAttendPer());
						//更新
						this.update(oldLogEntity);
					}else{//不存在记录-新增
						//出勤率
						logGenseeWatch.setAttendPer(logGenseeWatch.getAttendPer());
						//创建时间
						logGenseeWatch.setCreateTime(new Date(System.currentTimeMillis()));
						//加入去除前后空格操作
						BeanHelper.beanAttributeValueTrim(logGenseeWatch);
						//新增
						this.save(logGenseeWatch);
					}
					
				}
			}
		}
	}
	
	@Override
	public LogAttendWatchTimePOJO sumWatchTime(Long userId, List<String> classplanId, String classTypeId, String startTime, String endTime) {
		return logGenseeWatchDao.sumWatchTime(userId, classplanId, classTypeId, startTime, endTime);
	}

    @Override
    public LogAttendWatchTimePOJO sumWatchTimeByClassPlanLives(Map<String, Object> map) {
        return logGenseeWatchDao.sumWatchTimeByClassPlanLives(map);
    }

	@Override
	public List<StudyInfoEntity> queryVideoWatch(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return logGenseeWatchDao.queryVideoWatch(map);
	}

	@Override
	public BigDecimal getVideoWatchPersents(Map<String, Object> map) {
		// TODO Auto-generated method stub
		//获取基础表
		List<StudyInfoEntity> entityList = queryVideoWatch(map);
		if(entityList.size() == 0){
			return BigDecimal.ZERO;
		}
		StudyInfoEntity info = entityList.get(0);
		
		return info.getStudyPersents() == null ? BigDecimal.ZERO : info.getStudyPersents();
	}
	
	
}
