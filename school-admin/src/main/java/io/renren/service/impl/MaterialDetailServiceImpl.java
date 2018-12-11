package io.renren.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import io.renren.dao.MaterialDetailDao;
import io.renren.dao.ScheduleJobDao;
import io.renren.entity.MaterialDetailEntity;
import io.renren.entity.ScheduleJobEntity;
import io.renren.service.MaterialDetailService;
import io.renren.utils.Constant;
import io.renren.utils.DateUtils;


/**
 * 
 * @author Yuanjp
 *
 */
@Service("materialDetailService")
public class MaterialDetailServiceImpl implements MaterialDetailService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MaterialDetailDao materialDetailDao;
	
	@Autowired
	private ScheduleJobDao schedulerJobDao;
	
	@Override
	public MaterialDetailEntity queryObject(Map<String, Object> map){
		MaterialDetailEntity materialDetail=null;
		if(materialDetailDao.queryCount(map)>0){
			materialDetail=materialDetailDao.queryObjectIsRe(map);
			if(materialDetail!=null) materialDetail.setIsRelevance("是");  //防止报空指针  金融学那个报错
		}else{
			materialDetail=materialDetailDao.queryObject(map);
		}
		return materialDetail;
	}
	
	@Override
	public List<MaterialDetailEntity> queryList(Map<String, Object> map){
		return materialDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return materialDetailDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(MaterialDetailEntity materialDetail){
		materialDetailDao.save(materialDetail);
		
		//保存定时任务
		if (null != materialDetail.getPushContent() &&
				!materialDetail.getPushContent().trim().equals("")
				&& null!=materialDetail.getPushTime() 
				&& null!=materialDetail.getPushType()) {
			ScheduleJobEntity scheduleJob = new ScheduleJobEntity();
			scheduleJob.setBeanName(Constant.SCHEDULE_BEAN_NAME);
			scheduleJob.setMethodName(Constant.SCHEDULE_METHOD_NAME);
			scheduleJob.setCreateTime(new Date());
			scheduleJob.setCronExpression(DateUtils.getCronByDate(materialDetail.getPushTime(), materialDetail.getPushType()));
			scheduleJob.setExitId(materialDetail.getId());
			scheduleJob.setParams("content : "+materialDetail.getPushContent());
			scheduleJob.setRemark("有参数测试");
			scheduleJob.setStatus(0);
			scheduleJob.setGroupName(Constant.ScheduleGroup.MATERIAL_GROUP_NAME.getText());
			schedulerJobDao.save(scheduleJob);
		}
		
		
	}
	
	@Override
	@Transactional
	public void update(MaterialDetailEntity materialDetail){
		materialDetailDao.update(materialDetail);
		if (null != materialDetail.getPushContent() && !materialDetail.getPushContent().trim().equals("") && null != materialDetail.getPushTime()
				&& null != materialDetail.getPushType()) {
			Map map = new HashMap();
			map.put("exitId", materialDetail.getId());
			List results = this.schedulerJobDao.queryListByExitId(map);
			if (null != results && results.size() > 0) {

				ScheduleJobEntity scheduleJob = (ScheduleJobEntity) results.get(0);
				scheduleJob.setCronExpression(
						DateUtils.getCronByDate(materialDetail.getPushTime(), materialDetail.getPushType()));
				scheduleJob.setParams("content : " + materialDetail.getPushContent());
				this.schedulerJobDao.update(scheduleJob);
			}
		}
		
		
	}
	
	@Override
	public void delete(Map<String, Object> map){
		materialDetailDao.delete(map);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Map<String, Object> map){
 		int num1=schedulerJobDao.deleteBatchByExitId(map);
		int num2=materialDetailDao.deleteBatch(map);
 		logger.info("定时表中删除"+num1+"条记录!");
		logger.info("资料表中删除"+num2+"条记录!");
	}

	@Override
	public List<MaterialDetailEntity> queryListForLay(Map<String, Object> map) {
		return materialDetailDao.queryListForLay(map);
	}

	@Override
	public int queryTotalForLay(Map<String, Object> map) {
		return materialDetailDao.queryTotalForLay(map);
	}


	@Override
	public List<MaterialDetailEntity> queryDetailEntities(List<MaterialDetailEntity> materialDetails) {
		List<MaterialDetailEntity> materialDetaillist=new ArrayList<MaterialDetailEntity>();
		for(MaterialDetailEntity materialDetailEntity:materialDetails){
			if(materialDetailDao.queryCount(materialDetailEntity)>0){
				materialDetailEntity.setIsRelevance("是");
			} 
			materialDetaillist.add(materialDetailEntity);
		}
		
		return materialDetaillist;
	}

	@Override
	public List<MaterialDetailEntity> queryMaterialDetailYesList(Map<String, Object> map) {
		
		//求出已经关联的资料ID
		map.put("flag", true); // 查询已关联 分页标识      未关联的不做分页处理
		List<Integer> materialIds=materialDetailDao.queryMaterialDetailYesIds(map);  //分页查询 已关联资料ID
		 
		map.put("materialIds", materialIds);
		List<MaterialDetailEntity> materialDetails=materialDetailDao.querymateriDetailYesList(map);
		List<MaterialDetailEntity> materialDetaillist=new ArrayList<MaterialDetailEntity>();
		for(MaterialDetailEntity materialDetailEntity:materialDetails){
			if(materialDetailDao.queryCount(materialDetailEntity)>0){ //添加 是标志
				materialDetailEntity.setIsRelevance("是"); 
			} 
			materialDetaillist.add(materialDetailEntity);
		}
		return materialDetaillist;  //
	}

	@Override
	public int queryYesTotal(Map<String, Object> map) {
		 
		return materialDetailDao.queryYesTotal(map);
	}

	@Override
	public List<MaterialDetailEntity> queryMaterialDetailNoList(Map<String, Object> map) {
		
		//求出已经关联的资料ID
		map.put("flag", false);
		List<Integer> materialIds=materialDetailDao.queryMaterialDetailYesIds(map);
		map.put("materialIds", materialIds);
		return materialDetailDao.querymateriDetailNoList(map);
	}

	@Override
	public int queryNoTotal(Map<String, Object> map) {
		return materialDetailDao.queryTotal(map)-materialDetailDao.queryYesTotal(map);
	}

	@Override
	public MaterialDetailEntity queryObjectIsRe(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return materialDetailDao.queryObjectIsRe(map);
	}
	
	/**
	 * 判断删除的id是否已被关联
	 */
	@Override
	public int judgeIds(Map<String, Object> map) {
		return materialDetailDao.judgeIds(map);
	}

	@Override
	public List<Integer> queryMaterialDetailYesIds(Map<String, Object> map) {
		return materialDetailDao.queryMaterialDetailYesIds(map);
	}

}
