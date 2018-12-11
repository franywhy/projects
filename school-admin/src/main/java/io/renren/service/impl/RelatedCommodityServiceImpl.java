package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import io.renren.dao.RelatedCommodityDao;
import io.renren.entity.RelatedCommodityDetailEntity;
import io.renren.entity.RelatedCommodityEntity;
import io.renren.pojo.relatedCommodity.RelatedCommodityDetailPOJO;
import io.renren.pojo.relatedCommodity.RelatedCommodityPOJO;
import io.renren.service.RelatedCommodityDetailService;
import io.renren.service.RelatedCommodityService;
import io.renren.utils.Constant;


@Transactional(readOnly = true)
@Service("relatedCommodityService")
public class RelatedCommodityServiceImpl implements RelatedCommodityService {
	@Autowired
	private RelatedCommodityDao relatedCommodityDao;
	@Autowired
	private RelatedCommodityDetailService relatedCommodityDetailService;
	
	@Override
	public RelatedCommodityEntity queryObject(Long relatedCommodityId){
		return relatedCommodityDao.queryObject(relatedCommodityId);
	}
	
	@Override
	public List<RelatedCommodityEntity> queryList(Map<String, Object> map){
		return relatedCommodityDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return relatedCommodityDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(RelatedCommodityPOJO relatedCommodity){
		//dr
		relatedCommodity.setDr(0);
		//创建时间
		relatedCommodity.setCreationTime(new Date());
		//修改时间
		relatedCommodity.setModifiedTime(relatedCommodity.getCreationTime());
		//en
		RelatedCommodityEntity en = RelatedCommodityPOJO.getEntity(relatedCommodity);
		//保存主表
		relatedCommodityDao.save(en);
		//子表
		List<RelatedCommodityDetailPOJO> detatilList = relatedCommodity.getDetailList();
		//子表保存
		if(null != detatilList && detatilList.size() >0){
			for(int i = 0 ; i < detatilList.size() ; i++){
				//子表pojo
				RelatedCommodityDetailPOJO rcdp = detatilList.get(i);
				//子表entity
				RelatedCommodityDetailEntity rcde = RelatedCommodityDetailPOJO.getEntity(rcdp);
				//pk
				rcde.setRelatedCommodityId(en.getRelatedCommodityId());
				//排序
				rcde.setOrderNum(i);
				//保存子表
				relatedCommodityDetailService.save(rcde);
			}
			
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(RelatedCommodityPOJO relatedCommodity){
		//修改时间
		relatedCommodity.setModifiedTime(new Date());
		//en
		RelatedCommodityEntity en = RelatedCommodityPOJO.getEntity(relatedCommodity);
		//保存主表修改
		relatedCommodityDao.update(en);
		//遍历子表
		List<RelatedCommodityDetailPOJO> detatilList = relatedCommodity.getDetailList();
		
		//用于存放被保存或修改的子表id集合
		List<Long> delIds = new ArrayList<Long>();
		if(null != detatilList && detatilList.size() > 0){
			for(int i=0;i<detatilList.size();i++){
				//pojo
				RelatedCommodityDetailPOJO rcdp = detatilList.get(i);
				//entity
				RelatedCommodityDetailEntity rcde = RelatedCommodityDetailPOJO.getEntity(rcdp);
				//pk
				rcde.setRelatedCommodityId(en.getRelatedCommodityId());
				//排序
				rcde.setOrderNum(i);
				if(null == rcde.getId()){//保存
					relatedCommodityDetailService.save(rcde);
				}else{//更新
					relatedCommodityDetailService.update(rcde);
				}
				delIds.add(rcde.getId());
			}
		}
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("ids", delIds);
		map.put("relatedCommodityId", en.getRelatedCommodityId());
		relatedCommodityDetailService.deleteBatchNotIn(map);
	}
	
	@Override
	public void delete(Long relatedCommodityId){
		relatedCommodityDao.delete(relatedCommodityId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Long[] relatedCommodityIds){
		relatedCommodityDao.deleteBatch(relatedCommodityIds);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void pause(Long[] relatedCommodityIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", relatedCommodityIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
		relatedCommodityDao.updateBatch(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void resume(Long[] relatedCommodityIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", relatedCommodityIds);
    	map.put("status", Constant.Status.RESUME.getValue());
		relatedCommodityDao.updateBatch(map);
	}
}
